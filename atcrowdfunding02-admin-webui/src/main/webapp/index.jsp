<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<html>
<base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<head>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#btn1").click(function () {
                var array = [5, 7, 6];
                var arrayStr = JSON.stringify(array);
                $.ajax({
                    "url": "test/ajax.html",
                    "type": "post",
                    "data": arrayStr,
                    "contentType": "application/json;charset=UTF-8",
                    "dataType": "text/json",
                    "success": function (response) {
                        alert(response);
                    },
                    "error": function (response) {
                        alert(response);
                    }
                });
            });

            $("#btn2").click(function () {
                layer.msg("layer的弹框测试！")
            });
        });
    </script>
</head>

<body>
<h2>Hello World!</h2>


<a href="test/hello.html">测试1</a>
<br>
<button id="btn1">测试ajax</button>
<br>
<button id="btn2">layer弹框测试</button>
<br>
<a href="admin/to/login/page.html">去首页</a>
</body>
</html>
