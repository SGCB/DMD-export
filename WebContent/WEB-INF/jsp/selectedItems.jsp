<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <div id="div_selecteditems" class="div_selecteditems">
            <div id="div_selecteditems_list" class="div_selecteditems_list">
	            <span id="selecteditems_title" class="selecteditems_title"><spring:message code="edmexport.selecteditems.title" /></span>
	            <b>${selectedItemsCount}</b> <spring:message code="edmexport.selecteditems.title" />
	            <c:if test="${!empty listCollections}">
	                <span><spring:message code="edmexport.selecteditems.listcollections" />: <b>${listCollectionsCount}</b></span>
	                <ul id="ul_selecteditems_listCollections" class="ul_selecteditems_listCollections">
	                <c:forEach items="${listCollections}" var="coll">
	                    <li>${coll}</li>
	                </c:forEach>
	                </ul>
	            </c:if>
            </div>
            <div id="div_selecteditems_form" class="div_selecteditems_form">
                <form:form action="home.htm" method="post" name="form_edm_data" commandName="FormEDMData">
                    <input type="hidden" name="referer" value="xml" /> 
                    <ul id="ul_selecteditems_form" class="ul_selecteditems_form">
                        <li>
                            <label for="currentLocation"><spring:message code="edmexport.selecteditems.currentLocation" /></label>
                            <form:input path="currentLocation" size="80" required="required" value="${FormEDMData.currentLocation}" />
                            <form:errors path="currentLocation" cssClass="error" />
                        </li>
	                    <li>
	                    <c:if test="${!empty FormEDMData.listTypes}">
	                       <div id="div_selecteditems_types" class="div_selecteditems_types">
	                       <h4><spring:message code="edmexport.selecteditems.types" /></h4>
	                       <c:forEach items="${FormEDMData.listTypes}" var="type" varStatus="typeStatus">
		                        <ul id="ul_selecteditems_text" class="ul_selecteditems_text">
		                          <li>
		                              <label for="listTypes${typeStatus.index}" title='<spring:message code="edmexport.selecteditems.types.title" />'> ${type}</label>
		                              <form:textarea path="listTypes[${typeStatus.index}]" cols="80" rows="3" required="required" />
		                              <form:errors path="listTypes[${typeStatus.index}]" cssClass="error" />
		                          </li>
		                        </ul>
	                       </c:forEach>
	                       </div>
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