<%@ page contentType="text/html;charset=UTF-8"%>
<link rel="stylesheet" href="/resources/bootstrap/css/bootstrap.css">
<script src="/resources/jquery/jquery.js" type="text/javascript"></script>
<script src="/resources/bootstrap/js/bootstrap.js" type="text/javascript"></script>

<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Redis_Cloud</a>
        </div>
        <div>
            <ul class="nav navbar-nav">
                <%--<li class="active"><a href="/manage/app/initAppDeploy">部署Redis</a></li>--%>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        部署Redis
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/manage/app/initAppDeploy">Redis sentinel模式</a></li>
                        <li><a href="/manage/app/initDeployCluster">Redis cluster模式</a></li>
                    </ul>
                </li>
                <li><a href="/machinestatics/list">机器统计信息</a></li>
                <li><a href="/instanceInfo/list">redis实例信息</a></li>
                <li><a href="/instancestatics/list">redis实例统计信息</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        待扩展
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="#">jmeter</a></li>
                        <li><a href="#">EJB</a></li>
                        <li><a href="#">Jasper Report</a></li>
                        <li class="divider"></li>
                        <li><a href="#">分离的链接</a></li>
                        <li class="divider"></li>
                        <li><a href="#">另一个分离的链接</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>