<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="head/head.jsp"%>
<head>
    <title>redis实例统计信息</title>
</head>

</head>
<body>
<h1>机器统计信息</h1>
    <table class="table table-bordered">
        <thead>
            <th>IP</th>
            <th>port</th>
            <th>role</th>
            <th>max_memory</th>
            <th>used_memory</th>
            <th>curr_items</th>
            <th>curr_connections</th>
            <th>misses</th>
            <th>hits</th>
            <th>modify_time</th>
            <th>create_time</th>
            <th>is_run</th>
        </thead>
        <tbody>
            <c:forEach items="${list}" var="instanceStatics">
                <tr>
                    <td>${instanceStatics.ip}</td>
                    <td>${instanceStatics.port}</td>
                    <td>${instanceStatics.role}</td>
                    <td>${instanceStatics.maxMemory}</td>
                    <td>${instanceStatics.usedMemory}</td>
                    <td>${instanceStatics.currItems}</td>
                    <td>${instanceStatics.currConnections}</td>
                    <td>${instanceStatics.misses}</td>
                    <td>${instanceStatics.hits}</td>
                    <td>${instanceStatics.modifyTime}</td>
                    <td>${instanceStatics.createTime}</td>
                    <td>${instanceStatics.isRun}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
