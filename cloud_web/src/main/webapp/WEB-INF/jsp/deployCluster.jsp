<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>部署Redis集群</title>
    <%@include file="head/head.jsp"%>
</head>
<body>
<h3>部署redis集群</h3>
<form action="/manage/app/deployCluster" method="post" class="form-horizontal" role="form">
    <h4>master IP</h4>
    <div class="form-group">
        <label for="masterHost" class="col-sm-1 control-label">Redis IP</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="masterHost" name="masterHost" placeholder="请输入master IP" value="192.168.211.131"/>
        </div>
    </div>
    <div class="form-group">
        <label for="masterPort" class="col-sm-1 control-label">Redis Port</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="masterPort" name="masterPort" placeholder="Redis Port" value="7340, 7341, 7342"/>
        </div>
    </div>
    <h4>slave IP</h4>
    <div class="form-group">
        <label for="slaveHost" class="col-sm-1 control-label">Redis IP</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="slaveHost" name="slaveHost" placeholder="slave IP" value="192.168.211.132"/>
        </div>
    </div>
    <div class="form-group">
        <label for="slavePort" class="col-sm-1 control-label">Redis Port</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="slavePort" name="slavePort" placeholder="Redis Port" value="7340, 7341, 7342"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-1 col-sm-10">
            <button type="submit" class="btn btn-default">部署集群模式</button>
        </div>
    </div>
</form>
</body>
