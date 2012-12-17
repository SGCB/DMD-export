<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<body>
    <a href="home.htm">EDMExport</a>
    <h2>${exception.message}</h2>
    <ul></ul>
    <c:forEach items="${exception.stackTrace}" var="element">
        <li><c:out value="${element}" /></li>
    </c:forEach>
    </ul>
</body>
</html>