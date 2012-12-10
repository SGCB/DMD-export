<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <script>
        <!--
        var objListCollectionsJS;
        document.body.style.overflow = 'hidden';
        
        jQuery(document).ready(function () {
        	objListCollectionsJS = new ListCollectionsJS();
        	<c:if test="${!empty ItemsPage}">
        	objListCollectionsJS.setItemsPage(${ItemsPage});
        	</c:if>
        	<c:if test="${!empty PageCount}">
            objListCollectionsJS.setPageCount(${PageCount});
            </c:if>
        	objListCollectionsJS.init();
        });
        
        jQuery(window).load(function() {
            jQuery('#loading-image').hide();
            document.body.style.overflow = 'show';
        });
        
        jQuery(window).unload(function() {
        });
        
        function valid_list_coll(form)
        {
        		objListCollectionsJS.submit();
        	if (objListCollectionsJS.list_collections_submit.length() > 0) {
        		return true;
        	} else {
        		alert("<spring:message code='edmexport.home.list.no_check_coll' />");
        		return false;
        	}
        }
        //-->
        </script>
        <h2><spring:message code="edmexport.home.list.title" /></h2>
        <c:choose>
            <c:when test="${!empty listCollections && !empty listCollections.listCollections}">
            <div id="loading-image">
                <spring:url value="/img/lb-loading.gif" var="edmexport_loading_url" htmlEscape="true" />
                 <img id="loading-image-img" src="${edmexport_loading_url}" alt="Loading..." />
            </div>
            <form:form action="home.htm" method="post" name="list_collections" commandName="listCollections" onsubmit="return valid_list_coll(this);" >
            <input type="hidden" name="pageAction" value="listColls" />
            <div id="div_list_collec" class="div_list_collec">
                <div id="div_list_collec_dict" class="div_list_collec_dict">
                </div>
                <div id="div_list_collec_header" class="div_list_collec_header">
                </div>
                <div id="div_list_collec_body" class="div_list_collec_body">
                </div>
                <div id="div_list_collec_submit" class="div_list_collec_submit">
                    <input  type="submit" name="go_list_collections" value="<spring:message code="edmexport.home.list.save_reg" />" />
                </div>
                
                <div id="div_list_collec_footer" class="div_list_collec_footer">
                </div>
            </div>
            <div id="div_list_collec_nosc" class="div_list_collec_nosc">
	            <ul id="ul_list_collec_nosc">
		            <c:set var="i" value="0"/>
		            <c:forEach items="${listCollections.listCollections}" var="collection">
		                <li id="li_${i + 1}_nosc">
		                    <c:set var="i">${i + 1}</c:set><c:out value="${i}" />: <c:out value="${collection.name}" /> (<c:out value="${collection.handle}" />)
		                    <form:checkbox path="listCollections[${collection.index}].id" value="${collection.id}" />
		                    <form:hidden path="listCollections[${collection.index}].name" value="${collection.name}" />
		                    <form:hidden path="listCollections[${collection.index}].handle" value="${collection.handle}" />
		                </li>
		            </c:forEach>
		            <li><input  type="submit" name="go_list_collections" value="<spring:message code="edmexport.home.list.save_reg" />" /></li>
	            </ul>
            </div>
            </form:form>
            </c:when>
            <c:otherwise>
                <h3><spring:message code="edmexport.home.list.void" /></h3>
            </c:otherwise>
        </c:choose>