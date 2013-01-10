<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="edmexport_hometabs" class="edmexport_hometabs">
<table border="0" cellspacing="0" width="100%"> 
    <tr> 
        <td align="center">
            <c:choose>
            <c:when test="${tab == 'list'}">
            <spring:message code="edmexport.tab.list_collections" />
            </c:when>
            <c:otherwise>
            <a href="home.htm?tab=list"><spring:message code="edmexport.tab.list_collections" /></a>
            </c:otherwise>
            </c:choose>
        </td>
            
        <td align="center">
            <c:choose>
            <c:when test="${tab == 'search'}">
            <spring:message code="edmexport.tab.search" />
            </c:when>
            <c:otherwise>
            <a href="home.htm?tab=search"><spring:message code="edmexport.tab.search" /></a>
            </c:otherwise>
            </c:choose>
        </td> 
    </tr> 
</table></div>