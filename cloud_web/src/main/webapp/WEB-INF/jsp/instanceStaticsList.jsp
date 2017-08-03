<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="head/head.jsp"%>
<head>
    <title>redis实例统计信息</title>
</head>
<body>
<h1>redis实例信息</h1>
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
                    <td>
                        <c:if test="${instanceStatics.role == 1}">
                            master
                        </c:if>
                        <c:if test="${instanceStatics.role == 2}">
                            slave
                        </c:if>
                    </td>
                    <td>
                        <fmt:formatNumber type="number" maxFractionDigits="2" value="${instanceStatics.maxMemory / 1024 / 1024}" />MB
                    </td>
                    <td>
                        <fmt:formatNumber type="number" maxFractionDigits="2" value="${instanceStatics.usedMemory / 1024 / 1024}" />MB
                    </td>
                    <td>${instanceStatics.currItems}</td>
                    <td>${instanceStatics.currConnections}</td>
                    <td>${instanceStatics.misses}</td>
                    <td>${instanceStatics.hits}</td>
                    <td>
                        <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${instanceStatics.modifyTime}" />
                    </td>
                    <td>
                        <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${instanceStatics.createTime}" />
                    </td>
                    <td>
                        <c:if test="${instanceStatics.isRun == 1}">
                            运行中
                        </c:if>
                        <c:if test="${instanceStatics.isRun == 2}">
                            已下线
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
