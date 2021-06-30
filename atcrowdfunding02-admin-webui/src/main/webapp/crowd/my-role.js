// 执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {
    // 1.获取分页结果
    var pageInfo = getPageInfoRemote();

    // 2.填充表格
    fillTableBody(pageInfo);
}

// 远程访问服务器获取分页数据pageInfo
function getPageInfoRemote() {
    var ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        // 关闭异步方式
        "async": false,
        // 返回的数据类型
        "dataType": "json"
    });
    console.log(ajaxResult);
    var statusCode = ajaxResult.status;
    if (statusCode != 200) {
        layer.msg("出错了！状态码为:" + statusCode + "错误信息:" + ajaxResult.statusText)
        return null;
    }
    var responseJSON = ajaxResult.responseJSON;
    var result = responseJSON.result;
    if (result == "FAILED") {
        layer.msg(responseJSON.message);
        return null;
    }
    var pageInfo = responseJSON.data;
    return pageInfo;
}

// 填充表格数据
function fillTableBody(pageInfo) {
    // 清除tbody中的数据
    $("#bodyTd").empty();

    // 这里是为了让没有搜索结果时不显示页码导航条
    $("#Pagination").empty()

    if (pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        $("#bodyTd").append("<tr><td colspan='4' align='center'>抱歉没有查询到您搜索的数据！</td></tr>");
        return;
    }

    for (var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];

        var roleId = role.id;

        var numberTd = "<td>" + (i + 1) + "</td>";
        var checkTd = "<td width='30'><input id='" + roleId + "' type='checkbox' class='itemBox'></td>";
        var roleName = role.name;
        var roleNameTd = "<td>" + roleName + "</td>";

        var checkBtn = "<button id='" + roleId + "' type='button' class='btn btn-success btn-xs checkBtn'><i class='glyphicon glyphicon-check'></i></button>";
        var pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'><i class='glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button id='" + roleId + "' type='button' class='btn btn-danger btn-xs removeBtn'><i class='glyphicon glyphicon-remove'></i></button>";

        var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";

        var tr = "<tr>" + numberTd + checkTd + roleNameTd + buttonTd + "</tr>";
        $("#bodyTd").append(tr);
    }
    generateNavigator(pageInfo);
}

// 生成分页导航条
function generateNavigator(pageInfo) {

    // 获取总记录数
    var totalRecord = pageInfo.total;
    // 定义一个JSON 设置属性

    var properties = {
        "num_edge_entries": 3,                  // 定义边缘页
        "num_display_entries": 5,               // 定义主题页数
        "callback": paginationCallBack,         // 回调函数
        "items_per_page": pageInfo.pageSize,    // 每页的大小
        "current_page": pageInfo.pageNum - 1,    // 当前页
        "prev_text": "上一页",
        "next_text": "下一页"
    }
    $("#Pagination").pagination(totalRecord, properties);

}

// 翻页时的回调函数
function paginationCallBack(pageIndex, jQuery) {
    window.pageNum = pageIndex + 1;
    generatePage();
    return false;
}

function showConfirmModal(roleArray) {
    // 打开模态框
    $("#confirmModal").modal("show");

    // 清除旧的数据
    $("#roleNameDiv").empty();

    // 在全局变量范围创建数组用来存放角色id
    window.roleIdArray = [];

    // 遍历roleArray数组
    for (var i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];

        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName + "<br/>");

        var roleId = role.roleId;
        // 调用数组对象的push方法存入新元素
        window.roleIdArray.push(roleId);
    }
}

// 声明专门的函数用来在分配Auth 的模态框中显示Auth 的树形结构数据
function fillAuthTree() {
// 1.发送Ajax 请求查询Auth 数据
    var ajaxReturn = $.ajax({
        "url": "assign/get/all/auth.json",
        "type": "post",
        "dataType": "json",
        "async": false
    });
    if (ajaxReturn.status != 200) {
        layer.msg(" 请求处理出错！ 响应状态码是： " + ajaxReturn.status + " 说明是：" + ajaxReturn.statusText);
        return;
    }
// 2.从响应结果中获取Auth 的JSON 数据
// 从服务器端查询到的list 不需要组装成树形结构，这里我们交给zTree 去组装
    var authList = ajaxReturn.responseJSON.data;
// 3.准备对zTree 进行设置的JSON 对象
    var setting = {
        "data": {
            "simpleData": {
// 开启简单JSON 功能
                "enable": true,
// 使用categoryId 属性关联父节点，不用默认的pId 了
                "pIdKey": "categoryId"
            },
            "key": {
// 使用title 属性显示节点名称，不用默认的name 作为属性名了
                "name": "title"
            }
        },
        "check": {
            "enable": true
        }
    };
// 4.生成树形结构
// <ul id="authTreeDemo" class="ztree"></ul>
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);
// 获取zTreeObj 对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
// 调用zTreeObj 对象的方法，把节点展开
    zTreeObj.expandAll(true);
// 5.查询已分配的Auth 的id 组成的数组
    var ajaxReturn = $.ajax({
        "url": "assign/get/assigned/auth/id/by/role/id.json",
        "type": "post",
        "data": {
            "roleId": window.roleId
        },
        "dataType": "json",
        "async": false
    });
    if (ajaxReturn.status != 200) {
        layer.msg(" 请求处理出错！ 响应状态码是： " + ajaxReturn.status + " 说明是：" + ajaxReturn.statusText);
        return;
    }
// 从响应结果中获取authIdArray
    var responseJSON = ajaxReturn.responseJSON
    var authIdArray = responseJSON.data;
// 6.根据authIdArray 把树形结构中对应的节点勾选上
// ①遍历authIdArray
    for (var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];
// ②根据id 查询树形结构中对应的节点
        var treeNode = zTreeObj.getNodeByParam("id", authId);
// ③将treeNode 设置为被勾选
// checked 设置为true 表示节点勾选
        var checked = true;
// checkTypeFlag 设置为false，表示不“联动”，不联动是为了避免把不该勾选的勾选上
        var checkTypeFlag = false;
// 执行
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }
}