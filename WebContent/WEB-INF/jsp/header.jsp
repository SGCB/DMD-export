<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="edmexport_header" class="edmexport_header">
    <h2><a href="home.htm"><spring:message code="edmexport.title" /></a></h2>
</div>
<c:if test="${!empty edmExportBOUser}">
<div id="edmexport_user" class="edmexport_user">
    <spring:message code="edmexport.dspace_user" /> <strong><c:out value="${edmExportBOUser.username}" /></strong>
</div>
</c:if>