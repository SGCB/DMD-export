<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <script>
        <!--
        
        function valid_search(form)
        {
            if (form.term.value != "") {
                return true;
            } else {
                alert("<spring:message code='edmexport.search.no_submit' htmlEscape='false' javaScriptEscape='true' />");
                return false;
            }
        }
        //-->
        </script>
        <h2><spring:message code="edmexport.search.title" htmlEscape='false' /></h2>
        <form:form action="home.htm" method="post" name="search" commandName="search" onsubmit="return valid_search(this);" >
            <form:errors path="*" cssClass="errorblock" element="div" htmlEscape='false' />
            <input type="hidden" name="pageAction" value="searchItems" />
        <div id="div_search" class="div_search">
            <div id="div_search_text" class="div_search_text">
                <label for="term"><spring:message code="edmexport.search.term" htmlEscape='false' /></label>
                <form:input path="term" size="50" required="required" />
                <form:errors path="term" cssClass="error" htmlEscape='false' />
            </div>
            <div id="div_search_options" class="div_search_options">
                <label for="option"><spring:message code="edmexport.search.options" htmlEscape='false' /></label>
                <form:select path="option">
                    <option value=""><spring:message code="edmexport.search.option0" htmlEscape='false' /></option>
                    <option value="title"><spring:message code="edmexport.search.option.title" htmlEscape='false' /></option>
                    <option value="author"><spring:message code="edmexport.search.option.author" htmlEscape='false' /></option>
                    <option value="subject"><spring:message code="edmexport.search.option.subject" htmlEscape='false' /></option>
                </form:select>
            </div>
            <div id="div_search_submit" class="div_search_submit">
                <input  type="submit" name="go_search" value="<spring:message code="edmexport.search.go" htmlEscape='false' />" />
            </div>
        </div>
        <div id="div_search_error" class="div_search_error">
            <c:if test="${!empty error}">
                <spring:message code="edmexport.search.no_items" htmlEscape='false' />
            </c:if>
        </div>
        </form:form>
