<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>部署Redis实例</title>
    <%--<%@include file="head/head.jsp"%>--%>
    <link rel="stylesheet" href="/resources/bootstrap/css/bootstrap.css">
    <script src="/resources/jquery/jquery.js" type="text/javascript"></script>
    <script src="/resources/bootstrap/js/bootstrap.js" type="text/javascript"></script>
</head>
<body>
<h1>部署redis实例</h1>
    <form action="/manage/app/deployInstance" method="post">
        redis IP<input type="text" placeholder="redis ip"/>
        redis port<input type="text" placeholder="redis 端口"/>
        <button class="btn btn-default">部署</button>
    </form>
</body>
