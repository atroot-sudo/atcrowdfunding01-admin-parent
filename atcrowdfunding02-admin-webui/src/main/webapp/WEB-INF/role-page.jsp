<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>
<link charset="UTF-8" rel="stylesheet" href="css/pagination.css">
<link rel="stylesheet" href="ztree/zTreeStyle.css">
<body>

<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">


                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger"
                            style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button id="showAddModalBtn" type="button" class="btn btn-primary" style="float:right;"><i
                            class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="bodyTd">

                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/modal-role-confirm.jsp" %>
<%@include file="/WEB-INF/modal-role-add.jsp" %>
<%@include file="/WEB-INF/modal-role-edit.jsp" %>
<%@include file="/WEB-INF/modal-role-assign-auth.jsp" %>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" charset="UTF-8">
    $(function () {
        // 初始化
        window.pageNum = 1;
        window.size = 5;
        window.keyword = "";
        // 调用执行分页的函数，显示分页的效果
        generatePage();
    });

    $("#searchBtn").click(function () {
        window.keyword = $("#keywordInput").val();
        generatePage();
    });

    $("#showAddModalBtn").click(function () {
        $("#addModal").modal("show");
    })

    $("#saveRoleBtn").click(function () {
        var roleName = $.trim($("#addModal [name=roleName]").val())

        $.ajax({
            "url": "role/save.json",
            "type": "post",
            "data": {
                "name": roleName
            },
            "dataType": "json",
            "success": function (response) {
                var result = response.result;

                if (result == "SUCCESS") {
                    layer.msg("操作成功！");
                    // 向用户展示成功信息的页面
                    window.pageNum = 999999;
                    // 重新加载分页数据
                    generatePage();
                }
                if (result == "FAILED") {
                    layer.msg("操作失败！" + response.message);
                }
            },
            "error": function (response) {
                layer.msg(response.status + " " + response.statusText);
            }
        });
        // 关闭模态框
        $("#addModal").modal("hide");
        // 清理模态框
        $("#addModal [name=roleName]").val("");
    });

    $("#bodyTd").on("click", ".pencilBtn", function () {
        // 打开模态框
        $("#editModal").modal("show");
        // 获取当前表格中当前行中的角色名称
        var roleName = $(this).parent().prev().text();

        // 获取当前角色id(我们在前边按钮上设置了id)
        window.roleId = this.id;

        // 使用roleName的值设置模态框中的文本框
        $("#editModal [name=roleName]").val(roleName);
    });
    $("#updateRoleBtn").click(function () {
        var roleName = $("#editModal [name=roleName]").val();
        $.ajax({
            "url": "role/update.json",
            "type": "post",
            "data": {
                "id": window.roleId,
                "name": roleName
            },
            "dataType": "json",
            "success": function (response) {
                var result = response.result;
                if (result == "SUCCESS") {
                    layer.msg("操作成功！")
                    generatePage();
                }
                if (result == "FAILED") {
                    layer.msg("操作失败！" + response.message);
                }

            },
            "error": function (response) {
                layer.msg(response.status + " " + response.statusText);
            }
        });
        $("#editModal").modal("hide");


    });

    $("#removeRoleBtn").click(function () {
        // 从全局变量范围中获取roleArray，转换为诶Json字符串
        var requestBody = JSON.stringify(window.roleIdArray);

        $.ajax({
            "url": "role/remove/by/role/id/array.json",
            "type": "post",
            "data": requestBody,
            "contentType": "application/json;charset=UTF-8",
            "dataType": "json",
            "success": function (response) {
                var result = response.result;
                if (result == "SUCCESS") {
                    layer.msg("操作成功！");
                    generatePage();
                }
                if (result == "FAILED") {
                    layer.msg("操作失败！" + response.message)
                }
            },
            "error": function (response) {
                layer.msg(response.status + " " + response.statusText);
            }
        });
        $("#confirmModal").modal("hide");
    });
    $("#summaryBox").click(function () {
        // 1、获取当前多选框的状态
        var currentStatus = this.checked;

        // 2、用当前复选框的状态设置其他多选框
        $(".itemBox").prop("checked", currentStatus);
    })

    $("#bodyTd").on("click", ".itemBox", function () {
        // 获取当前已选中的itemBox的数量
        var checkedBoxCount = $(".itemBox:checked").length;

        // 获取全部的checkBox的数量
        var totalBoxCount = $(".itemBox").length;

        // 使用二者的比较结果设置总的checkBox
        $("#summaryBox").prop("checked", checkedBoxCount == totalBoxCount);
    });
    // 单条删除
    $("#bodyTd").on("click", ".removeBtn", function () {

        // 从当前按钮出发获取角色名称
        var roleName = $(this).parent().prev().text();

        // 创建role对象存入数组
        var roleArray = [{
            roleId: this.id,
            roleName: roleName
        }];

        // 调用专门的函数打开模态框
        showConfirmModal(roleArray);

    });

    $("#batchRemoveBtn").click(function () {
        // 创建一个数组对象用来存放后面获取到的角色对象
        var roleArray = [];

        // 使用this引用当前遍历得到的多选框
        $(".itemBox:checked").each(function () {

            // 使用this引用当前遍历得到的多选框
            var roleId = this.id;
            // 通过DOM操作获取角色名称
            var roleName = $(this).parent().next().text();
            roleArray.push({
                "roleId": roleId,
                "roleName": roleName
            });
        });
        if (roleArray.length == 0) {
            layer.msg("请至少选择一个执行删除！")
            return;
        }
        // 调用函数打开模态框
        showConfirmModal(roleArray);
    });

    // 给分配权限按钮绑定单击响应函数
    $("#bodyTd").on("click", ".checkBtn", function () {
        window.roleId = this.id;
        // 打开模态框
        $("#assignModal").modal("show");
        // 在模态框中装载树Auth 的形结构数据
        fillAuthTree();

    });
    // 14.给分配权限模态框中的“分配”按钮绑定单击响应函数
    $("#assignBtn").click(function(){
// ①收集树形结构的各个节点中被勾选的节点
// [1]声明一个专门的数组存放id
        var authIdArray = [];
// [2]获取zTreeObj 对象
        var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
// [3]获取全部被勾选的节点
        var checkedNodes = zTreeObj.getCheckedNodes();
        // [4]遍历checkedNodes
        for(var i = 0; i < checkedNodes.length; i++) {
            var checkedNode = checkedNodes[i];
            var authId = checkedNode.id;
            authIdArray.push(authId);
        }
// ②发送请求执行分配
        var requestBody = {
            "authIdArray":authIdArray,
// 为了服务器端handler 方法能够统一使用List<Integer>方式接收数据，roleId 也存入数组
            "roleId":[window.roleId]
        };
        requestBody = JSON.stringify(requestBody);
        $.ajax({
            "url":"assign/do/role/assign/auth.json",
            "type":"post",
            "data":requestBody,
            "contentType":"application/json;charset=UTF-8",
            "dataType":"json",
            "success":function(response){
                var result = response.result;
                if(result == "SUCCESS") {
                    layer.msg("操作成功！");
                }
                if(result == "FAILED") {
                    layer.msg("操作失败！"+response.message);
                }
            },
            "error":function(response) {
                layer.msg("出错了！" + response.status+" "+response.statusText);
            }
        });
        $("#assignModal").modal("hide");
    });
</script>
<script type="text/javascript" src="crowd/my-role.js"></script>
</body>
</html>