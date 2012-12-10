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
                alert("<spring:message code='edmexport.search.no_submit' />");
                return false;
            }
        }
        //-->
        </script>
        <h2><spring:message code="edmexport.search.title" /></h2>
        <form:form action="home.htm" method="post" name="search" commandName="search" onsubmit="return valid_search(this);" >
            <input type="hidden" name="pageAction" value="searchItems" />
        <div id="div_search" class="div_search">
            <div id="div_search_text" class="div_search_text">
                <label for="term"><spring:message code="edmexport.search.term" /></label>
                <form:input path="term" size="50" required="required" />
            </div>
            <div id="div_search_options" class="div_search_options">
                <label for="option"><spring:message code="edmexport.search.options" /></label>
                <form:select path="option">
                    <option value=""><spring:message code="edmexport.search.option0" /></option>
                    <option value="title"><spring:message code="edmexport.search.option.title" /></option>
                    <option value="author"><spring:message code="edmexport.search.option.author" /></option>
                    <option value="subject"><spring:message code="edmexport.search.option.subject" /></option>
                </form:select>
            </div>
            <div id="div_search_submit" class="div_search_submit">
                <input  type="submit" name="go_search" value="<spring:message code="edmexport.search.go" />" />
            </div>
        </div>
        <div id="div_search_error" class="div_search_error">
            <c:if test="${!empty error}">
                <spring:message code="edmexport.search.no_items" />
            </c:if>
        </div>
        </form:form>
