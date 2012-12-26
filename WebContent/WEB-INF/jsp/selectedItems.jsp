<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <script>
        <!--
                
        jQuery(document).ready(function() {
        	
        	jQuery("#btn_export").click(function () {
        		var message = valid_selectedItems2(document.forms["form_edm_data"]);
        		if (message == "") {
        			  jQuery("#FormEDMData").attr("action", "getFile.htm");
        			  jQuery("#FormEDMData").submit();
        			  jQuery("#FormEDMData").attr("action", "selectedItems.htm");
        		} else alert(message);
        	});
        	
        });
        
        
        function valid_selectedItems(form)
        {
        	var message = valid_selectedItems2(form);
        	if (message == "") {
        		jQuery("#pageAction").val("xml");
        		return true;
        	}
        	alert(message);
            return false;
        }
        
        
        function valid_selectedItems2(form)
        {
        	var message = "";
        	
        	if (form.title.value == "") {
                message += "<spring:message code='NotEmpty.FormEDMData.title' />\n";
            }
            
            if (form.urlBase.value == "") {
                message += "<spring:message code='URL.FormEDMData.urlBase' />\n";
            }
            
            if (form.edmRights.value == "") {
                message += "<spring:message code='URL.FormEDMData.edmRights' />\n";
            }
            
            jQuery("textarea[id^='listTypes']").each(function() {
                if (jQuery(this).val() == "") {
                    message += jQuery("#label_" + jQuery(this).attr("id")).html() + "<spring:message code='edmexport.selecteditems.type.error' />\n";
                }
            });
            
            return message;
        }
        
        //-->
        </script>

        <div id="div_selecteditems" class="div_selecteditems">
            <div id="div_selecteditems_list" class="div_selecteditems_list">
	            <span id="selecteditems_title" class="selecteditems_title"><spring:message code="edmexport.selecteditems.title" /></span>
	            <b>${selectedItemsCount}</b> <spring:message code="edmexport.selecteditems.title" />
	            <c:if test="${!empty listCollections}">
	                <span id="selecteditems_listCollections" class="selecteditems_listCollections"><spring:message code="edmexport.selecteditems.listcollections" />: <b>${listCollectionsCount}</b></span>
	                <ul id="ul_selecteditems_listCollections" class="ul_selecteditems_listCollections">
	                <c:forEach items="${listCollections}" var="coll">
	                    <li>${coll}</li>
	                </c:forEach>
	                </ul>
	            </c:if>
            </div>
            <div id="div_selecteditems_form" class="div_selecteditems_form">
                <form:form action="selectedItems.htm" method="post" name="form_edm_data" commandName="FormEDMData" onsubmit="return valid_selectedItems(this);">
                    <form:errors path="*" cssClass="errorblock" element="div" />
                    <form:hidden path="pageAction" /> 
                    <ul id="ul_selecteditems_form" class="ul_selecteditems_form">
                        <li><h3><spring:message code="edmexport.selecteditems.form.title" /></h3></li>
                        <li>
                            <form:label path="title"><spring:message code="edmexport.selecteditems.title" /></form:label>

                            <form:input path="title" size="80" required="required" />
                            <form:errors path="title" cssClass="error" />
                        </li>
	                    <li>
	                    <c:if test="${!empty FormEDMData.listTypes}">
	                       <div id="div_selecteditems_types" class="div_selecteditems_types">
	                       <spring:message code="edmexport.selecteditems.types" />
	                       <c:forEach items="${FormEDMData.listTypes}" var="type" varStatus="typeStatus">
		                        <ul id="ul_selecteditems_text" class="ul_selecteditems_text">
		                          <li>
		                              <label for="listTypes${typeStatus.index}" id="label_listTypes${typeStatus.index}" title='<spring:message code="edmexport.selecteditems.types.title" />'> ${type}</label>
		                              <form:textarea path="listTypes[${typeStatus.index}]" cols="80" rows="3" required="required" />
		                              <form:errors path="listTypes[${typeStatus.index}]" cssClass="error" />
		                          </li>
		                        </ul>
	                       </c:forEach>
	                       </div>
	                    </c:if>
	                    </li>
	                    <li>
                            <form:label path="urlBase"><spring:message code="edmexport.selecteditems.urlbase" /></form:label>

                            <form:input path="urlBase" size="80" required="required" />
                            <form:errors path="urlBase" cssClass="error" />
                        </li>
                        <li>
                            <form:label path="edmRights"><spring:message code="edmexport.selecteditems.edm_rights" /></form:label>

                            <form:input path="edmRights" size="80" required="required" />
                            <form:errors path="edmRights" cssClass="error" />
                        </li>
                    </ul>
                    <div id="div_selecteditems_actions" class="div_selecteditems_actions">
                        <ul>
		                    <li><input type="submit" value="<spring:message code="edmexport.selecteditems.visualize" />" /></li>
		                    <li><input type="button" name="btn_export" id="btn_export" value="<spring:message code="edmexport.selecteditems.exportxml" />" /></li>
		                </ul>
		            </div>
                </form:form>
            </div>
        </div>