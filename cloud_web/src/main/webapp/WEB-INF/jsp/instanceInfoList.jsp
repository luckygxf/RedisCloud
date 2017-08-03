<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="head/head.jsp"%>
<head>
    <title>redis实例信息</title>
    <script type="text/javascript">
        function startRedisInstance(instanceInfoId) {
            $.post(
                '/instanceInfo/startRedisInstance',
                {
                    'instanceInfoId':instanceInfoId
                },
                function (data) {
                    if(data == 1){
                        alert('启动成功');
                    }else{
                        alert('启动失败');
                    }
                    location.reload();
                }
            );
        }
        function shutdownRedisInstance(instanceInfoId) {
            $.post(
                '/instanceInfo/shutdownRedisInstance',
                {
                    'instanceInfoId':instanceInfoId
                },
                function (data) {
                    if(data == 1){
                        alert('下线成功');
                    }else{
                        alert('下线失败');
                    }
                    location.reload();
                }
            );
        }
    </script>
</head>
<body>
<h1>redis实例信息</h1>
    <table class="table table-bordered">
        <thead>
            <th>host</th>
            <th>端口</th>
            <th>密码</th>
            <th>状态</th>
            <th>操作</th>
        </thead>
        <tbody>
            <c:forEach items="${infoList}" var="instanceInfo">
                <tr>
                    <td>${instanceInfo.host}</td>
                    <td>${instanceInfo.port}</td>
                    <td>${instanceInfo.password}</td>
                    <td>
                        <c:if test="${instanceInfo.status == 1}">
                            运行中
                        </c:if>
                        <c:if test="${instanceInfo.status == 2}">
                            已下线
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${instanceInfo.status == 1}">
                            <input type="button" value="下线实例" class="btn btn-warning" onclick="shutdownRedisInstance(${instanceInfo.id})"/>
                        </c:if>
                        <c:if test="${instanceInfo.status == 2}">
                            <input type="button" value="启动实例" class="btn btn-success"  onclick="startRedisInstance(${instanceInfo.id})"/>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
