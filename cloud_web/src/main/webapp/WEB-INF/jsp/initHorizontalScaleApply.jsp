<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>Redis集群扩容</title>
    <%@include file="head/head.jsp"%>
</head>
<body>
<h3>Redis集群扩容</h3>
<form action="#" method="post" class="form-horizontal" role="form">
    <div class="form-group">
        <label for="masterHost" class="col-sm-1 control-label">主从分片配置</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="masterHost" name="masterHost" placeholder="masterIP:masterPort:slaveIP:slavePort" />
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-1 col-sm-10">
            <button type="submit" class="btn btn-default">添加分片</button>
        </div>
    </div>
</form>
</body>
