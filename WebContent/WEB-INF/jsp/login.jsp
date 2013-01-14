<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <h1><spring:message code="edmexport.login.title" htmlEscape='false' /></h1>
        <c:if test="${not empty error}">
        <div id="login-error" class="login-error"><spring:message code="edmexport.login.error" /></div>
        </c:if>
        <form action="../j_spring_security_check" method="post" >
            <label for="j_username"><spring:message code="edmexport.login.username" htmlEscape='false' /></label>
            <input id="j_username" name="j_username" type="text" />
            <label for="j_password"><spring:message code="edmexport.login.password" htmlEscape='false' /></label>
            <input id="j_password" name="j_password" type="password" />
            <br /><input  type="submit" value="Login"/>
        </form>