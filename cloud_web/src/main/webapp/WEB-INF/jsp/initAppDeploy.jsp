<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>部署Redis实例</title>
    <%@include file="head/head.jsp"%>
</head>
<body>
<h1>部署redis实例</h1>
    <form action="/manage/app/deployInstance" method="post" class="form-horizontal" role="form">
        <div class="form-group">
            <label for="host" class="col-sm-1 control-label">Redis IP</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="host" name="host" placeholder="请输入IP" value="192.168.211.131"/>
            </div>
        </div>
        <div class="form-group">
            <label for="port" class="col-sm-1 control-label">Redis Port</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="port" name="port" placeholder="Redis Port" value="6340"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-1 col-sm-10">
                <button type="submit" class="btn btn-default">部署</button>
            </div>
        </div>
    </form>
</body>
