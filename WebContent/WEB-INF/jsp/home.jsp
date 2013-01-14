<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


    <c:if test="${!empty listCollections && !empty listCollections.listCollections}">
        <script type="text/javascript">
        <!--
        var objListCollectionsJS;
        document.documentElement.style.overflow = document.body.style.overflow = 'hidden';
        
        jQuery(document).ready(function () {
        	objListCollectionsJS = new ListCollectionsJS();
        	<c:if test="${!empty ItemsPage}">
        	objListCollectionsJS.setItemsPage(${ItemsPage});
        	</c:if>
        	<c:if test="${!empty PageCount}">
            objListCollectionsJS.setPageCount(${PageCount});
            </c:if>
            objListCollectionsJS.setCaptionStrong("<spring:message code='edmexport.home.list.caption.strong' htmlEscape='false' />");
            objListCollectionsJS.setCaptionSummary("<spring:message code='edmexport.home.list.caption.summary' htmlEscape='false' />");
            objListCollectionsJS.setCaptionParagraph("<spring:message code='edmexport.home.list.caption.paragraph' htmlEscape='false' />");
            objListCollectionsJS.setThIndex("<spring:message code='edmexport.home.list.index' htmlEscape='false' />");
            objListCollectionsJS.setThName("<spring:message code='edmexport.home.list.name' htmlEscape='false' />");
            objListCollectionsJS.setThHandle("<spring:message code='edmexport.home.list.handle' htmlEscape='false' />");
            objListCollectionsJS.setThSelect("<spring:message code='edmexport.home.list.select' htmlEscape='false' />");
        	objListCollectionsJS.init();
        });
        
        jQuery(window).load(function() {
            jQuery('#loading-image').hide();
            document.documentElement.style.overflow = document.body.style.overflow = 'auto';
        });
        
        jQuery(window).unload(function() {
        });
        
        function valid_list_coll(form)
        {
        	objListCollectionsJS.submit("<spring:message code='edmexport.home.list.recov_items' htmlEscape='false' />");
        	if (objListCollectionsJS.list_collections_submit.length() > 0) {
        		return true;
        	} else {
        		alert("<spring:message code='edmexport.home.list.no_check_coll' htmlEscape='false' />");
        		return false;
        	}
        }
        //-->
        </script>
    </c:if>
        <h2><spring:message code="edmexport.home.list.title" htmlEscape='false' /></h2>
        <c:choose>
            <c:when test="${!empty listCollections && !empty listCollections.listCollections}">
            <div id="loading-image">
                <spring:url value="/img/lb-loading.gif" var="edmexport_loading_url" htmlEscape="true" />
                 <img id="loading-image-img" src="${edmexport_loading_url}" alt="Loading..." />
            </div>
            <form:form action="home.htm" method="post" name="listCollections" commandName="listCollections" onsubmit="return valid_list_coll(this);" >
            <input type="hidden" name="pageAction" value="listColls" />
            <div id="div_list_collec" class="div_list_collec">
                <div id="div_list_collec_dict" class="div_list_collec_dict">
                </div>
                <div id="div_list_collec_header" class="div_list_collec_header">
                </div>
                <div id="div_list_collec_body" class="div_list_collec_body">
                </div>
                <div id="div_list_collec_submit" class="div_list_collec_submit">
                    <input  type="submit" name="go_list_collections" value="<spring:message code="edmexport.home.list.save_reg" htmlEscape='false' />" />
                </div>
                
                <div id="div_list_collec_footer" class="div_list_collec_footer">
                </div>
            </div>
            <div id="div_list_collec_nosc" class="div_list_collec_nosc">
                <section>
	            <table id="table_list_collec_nosc">
	               <caption>
	                   <strong><spring:message code="edmexport.home.list.caption.strong" htmlEscape='false' /></strong><br />
                       <details>
						   <summary><em><spring:message code="edmexport.home.list.caption.summary" htmlEscape='false' /></em></summary>
						   <p><em><spring:message code="edmexport.home.list.caption.paragraph" htmlEscape='false' /></em></p>
					   </details>
	               </caption>
	               <thead>
	                   <tr>
	                       <th><spring:message code="edmexport.home.list.index" htmlEscape='false' /></th>
	                       <th><spring:message code="edmexport.home.list.name" htmlEscape='false' /></th>
	                       <th><spring:message code="edmexport.home.list.handle" htmlEscape='false' /></th>
	                       <th><spring:message code="edmexport.home.list.select" htmlEscape='false' /></th>
	                   </tr>
	               </thead>
	               <tbody>
		            <c:forEach items="${listCollections.listCollections}" var="collection" varStatus="collectionStatus">
		                <tr id="tr_${collectionStatus.index}_nosc">
		                    <td>
		                      <c:out value="${collectionStatus.index}" />
		                    </td>
		                    <td>
		                      <c:out value="${collection.name}" />
		                    <form:hidden path="listCollections[${collection.index}].name" />
		                    </td>
		                    <td>
		                      <c:out value="${collection.handle}" />
		                    <form:hidden path="listCollections[${collection.index}].handle" />
		                    </td>
		                    <td>
		                      <form:checkbox path="listCollections[${collection.index}].id" value="${collection.id}" />
		                    </td>
		                </tr>
		            </c:forEach>
		            </tbody>
		            <tfoot>
			            <tr>
			              <td colspan="4">
			                  <input  type="submit" name="go_list_collections" value="<spring:message code="edmexport.home.list.save_reg" htmlEscape='false' />" />
		                  </td>
			            </tr>
		            </tfoot>
	            </table>
	            </section>
            </div>
            </form:form>
            </c:when>
            <c:otherwise>
                <h3><spring:message code="edmexport.home.list.void" /></h3>
            </c:otherwise>
        </c:choose>