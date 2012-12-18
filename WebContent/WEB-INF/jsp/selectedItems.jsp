<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <div id="div_selecteditems" class="div_selecteditems">
            <div id="div_selecteditems_list" class="div_selecteditems_list">
	            <span id="selecteditems_title" class="selecteditems_title"><spring:message code="edmexport.selecteditems.title" /></span>
	            ${selectedItemsCount} <spring:message code="edmexport.selecteditems.title" />
	            <c:if test="${!empty listCollections}">
	                <span><spring:message code="edmexport.selecteditems.listcollections" />: ${listCollectionsCount}</span>
	                <ul id="ul_selecteditems_listCollections" class="ul_selecteditems_listCollections">
	                <c:forEach items="${listCollections}" var="coll">
	                    <li>${coll}</li>
	                </c:forEach>
	                </ul>
	            </c:if>
            </div>
            <div id="div_selecteditems_form" class="div_selecteditems_form">
                <form:form action="home.htm" method="post" name="form_edm_data" commandName="FormEDMData">
                    <ul id="ul_selecteditems_form" class="ul_selecteditems_form">
                        <li>
                            <form:input path="currentLocation" value="${FormEDMData.currentLocation}" />
                        </li>
	                    <li>
	                    <c:if test="${!empty FormEDMData.listTypes}">
	                       <c:forEach items="${FormEDMData.listTypes}" var="type" varStatus="typeStatus">
		                    <div id="div_selecteditems_text" class="div_selecteditems_text">
		                        <label for="term"><spring:message code="edmexport.selecteditems.types" /> ${type}</label>
		                        <form:input path="listTypes[${typeStatus.index}]" size="80" required="required" />
		                        <form:errors path="listTypes[${typeStatus.index}]" cssClass="error" />
		                    </div>
	                       </c:forEach>
	                    </c:if>
	                    </li>
                    </ul>
                </form:form>
            </div>
            <div id="div_selecteditems_actions" class="div_selecteditems_actions">
                <ul>
                    <li><a href="home.htm?referer=selectedItems&action=visualize"><spring:message code="edmexport.selecteditems.visualize" /></a></li>
                    <li><a href="home.htm?referer=selectedItems&action=export"><spring:message code="edmexport.selecteditems.exportxml" /></a></li>
                </ul>
            </div>
        </div>