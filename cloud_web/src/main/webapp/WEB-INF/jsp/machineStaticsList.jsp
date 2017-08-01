<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head/head.jsp"%>
<head>
    <title>机器统计信息</title>
</head>

</head>
<body>
<h1>机器统计信息</h1>
    <table class="table table-bordered">
        <thead>
            <th>IP</th>
            <th>CPU usage</th>
            <th>负载</th>
            <th>内存使用率</th>
            <th>空闲内存</th>
            <th>总内存</th>
            <th>创建时间</th>
            <th>修改时间</th>
        </thead>
        <tbody>
            <c:forEach items="${listOfMachineStatics}" var="machineStatics">
                <tr>
                    <td>${machineStatics.ip}</td>
                    <td>${machineStatics.cpuUsage}</td>
                    <td>${machineStatics.load}</td>
                    <td>${machineStatics.memoryUsageRatio}</td>
                    <td>${machineStatics.memoryFree / 1024}MB</td>
                    <td>${machineStatics.memoryTotal / 1024}MB</td>
                    <td>${machineStatics.createTime}</td>
                    <td>${machineStatics.modifyTime}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
