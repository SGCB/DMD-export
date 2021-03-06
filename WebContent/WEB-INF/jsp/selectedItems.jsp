<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <script type="text/javascript">
        <!--
                
        jQuery(document).ready(function() {
        	
        	jQuery("#btn_export").click(function () {
        		var message = valid_selectedItems2(document.forms["FormEDMData"]);
        		if (message == "") {
        			  jQuery("#FormEDMData").attr("action", "getFile.htm");
        			  jQuery("#FormEDMData").submit();
        			  jQuery("#FormEDMData").attr("action", "selectedItems.htm");
        		} else {
        			try {
        			    jQuery.spro.jpopit(message
        			        , {fadeInTime: 200, fadeOutTime: 1000, delay: 10000}).css({"background-color":"#FFFFFF", "color":"#000000"});
        			} catch (e) {
        				alert(messsage);
        			}
        		}
        	});
        	
        	changeBtnLiterals();
            switch(jQuery("#xmlFormat option:selected").val()) {
                case "EDM":
                case "ESE":
                    if (jQuery("#ul_selecteditems_form_edm_ese").css('display') == 'none') jQuery("#ul_selecteditems_form_edm_ese").show();
                    break;
                default:
                    if (jQuery("#ul_selecteditems_form_edm_ese").css('display') != 'none') jQuery("#ul_selecteditems_form_edm_ese").hide();
            }
            
            if (jQuery("#edmRights").val() === "") {
            	jQuery("#edmRights").val("http://creativecommons.org/publicdomain/mark/1.0/");
            }
            
            jQuery("#xmlFormat").change(function() {
                var value = jQuery("#xmlFormat option:selected").val();
                changeBtnLiterals();
                switch(value) {
                    case "EDM":
                    case "ESE":
                        if (jQuery("#ul_selecteditems_form_edm_ese").css('display') == 'none') jQuery("#ul_selecteditems_form_edm_ese").show('slow');
                        break;
                    default:
                        if (jQuery("#ul_selecteditems_form_edm_ese").css('display') != 'none') jQuery("#ul_selecteditems_form_edm_ese").hide('slow');
                }
            });
        	
        });
        
        
        function changeBtnLiterals()
        {
        	var value = jQuery("#xmlFormat option:selected").val();
            var btn_submit_value = jQuery("#btn_submit").val();
            var btn_submit_title = jQuery("#btn_submit").attr("title");
            var btn_export_value = jQuery("#btn_export").val();
            var btn_export_title = jQuery("#btn_export").attr("title");
            jQuery("#xmlFormat option").each(function() {
                var value_option = jQuery(this).val();
                if (btn_submit_value.indexOf(" " + value_option + " ") >= 0) {
                    jQuery("#btn_submit").val(btn_submit_value.replace(value_option, value));
                }
                if (btn_submit_title.indexOf(" " + value_option + " ") >= 0) {
                    jQuery("#btn_submit").attr("title", btn_submit_title.replace(value_option, value));
                }
                if (btn_export_value.indexOf(" " + value_option + " ") >= 0) {
                    jQuery("#btn_export").val(btn_export_value.replace(value_option, value));
                }
                if (btn_export_title.indexOf(" " + value_option + " ") >= 0) {
                    jQuery("#btn_export").attr("title", btn_export_title.replace(value_option, value));
                }
            });
        }
        
        
        function valid_selectedItems(form)
        {
        	var message = valid_selectedItems2(form);
        	if (message == "") {
        		jQuery("#pageAction").val("xml");
        		return true;
        	}
        	try {
        		jQuery.spro.jpopit(message
                    , {fadeInTime: 200, fadeOutTime: 1000, delay: 10000}).css({"background-color":"#FFFFFF", "color":"#000000"});
        	} catch (e) {
                alert(messsage);
            }
            return false;
        }
        
        
        function valid_selectedItems2(form)
        {
        	var message = "";
        	
        	if (form.title.value == "") {
                message += "<spring:message code='NotEmpty.FormEDMData.title' htmlEscape='false' javaScriptEscape='true' />\n";
            }
            
            if (form.urlBase.value == "") {
                message += "<spring:message code='URL.FormEDMData.urlBase' htmlEscape='false' javaScriptEscape='true' />\n";
            }
            
            if (form.edmRights.value == "") {
                message += "<spring:message code='URL.FormEDMData.edmRights' htmlEscape='false' javaScriptEscape='true' />\n";
            }
            
            jQuery("textarea[id^='listTypes']").each(function() {
                if (jQuery(this).val() == "") {
                    message += jQuery("#label_" + jQuery(this).attr("id")).html() + "<spring:message code='edmexport.selecteditems.type.error' htmlEscape='false' javaScriptEscape='true' />\n";
                }
            });
            
            return message;
        }
        
        
        //-->
        </script>

        <span id="edmexport_main_help" class="edmexport_main_help"><p><spring:message code="edmexport.selecteditems.help" htmlEscape='false' /></p></span>
        <div id="div_selecteditems" class="div_selecteditems">
            <div id="div_selecteditems_list" class="div_selecteditems_list">
	            <span id="selecteditems_title" class="selecteditems_title"><spring:message code="edmexport.selecteditems.title.main" htmlEscape='false' /></span>
	            <b>${selectedItemsCount}</b>
	            <c:if test="${!empty listCollections}">
	                <span id="selecteditems_listCollections" class="selecteditems_listCollections"><spring:message code="edmexport.selecteditems.listcollections" htmlEscape='false' /> <b>${listCollectionsCount}</b> <spring:message code="edmexport.selecteditems.collections" htmlEscape='false' /></span>
	                <ul id="ul_selecteditems_listCollections" class="ul_selecteditems_listCollections">
	                <c:forEach items="${listCollections}" var="coll">
	                    <li>${coll}</li>
	                </c:forEach>
	                </ul>
	            </c:if>
            </div>
            <div id="div_selecteditems_form" class="div_selecteditems_form">
                <form:form action="selectedItems.htm" method="post" name="FormEDMData" commandName="FormEDMData" onsubmit="return valid_selectedItems(this);">
                    <form:errors path="*" cssClass="errorblock" element="div" htmlEscape='false' />
                    <form:hidden path="pageAction" /> 
                    <ul id="ul_selecteditems_form" class="ul_selecteditems_form">
                        <li>&nbsp;</li>
                        <li id="li_selecteditems_help">
                           <spring:message code="edmexport.selecteditems.xmlFormats.title" htmlEscape='false' />
                        </li>
                        <li>
                        <c:if test="${!empty FormEDMData.listXmlFormats}">
                            <form:hidden path="listXmlFormats" /> 
                           <div id="div_selecteditems_xmlFormats" class="div_selecteditems_xmlFormats">
                           <spring:message code="edmexport.selecteditems.xmlFormats" htmlEscape='false' />
                           <form:select path="xmlFormat">
                           <c:forEach items="${FormEDMData.listXmlFormats}" var="xmlFormat">
                                <option value="${xmlFormat}">${xmlFormat}</option>
                           </c:forEach>
                           </form:select>
                           </div>
                        </c:if>
                        </li>
                    </ul>
                    <ul id="ul_selecteditems_form_edm_ese" class="ul_selecteditems_form" style="display: none;">
                        <li>&nbsp;</li>
                        <li>&nbsp;</li>
                        <li>&nbsp;</li>
                        <li><h3><spring:message code="edmexport.selecteditems.form.title" htmlEscape='false' /> EDM/ESE</h3></li>
                        <li>&nbsp;</li>
                        <li id="li_selecteditems_help">
                           <spring:message code="edmexport.selecteditems.title.help" htmlEscape='false' />
                        </li>
                        <li>
                            <form:label path="title"><span title="<spring:message code='edmexport.selecteditems.title' htmlEscape='false' />">
                                <spring:message code="edmexport.selecteditems.title" htmlEscape='false' /></span></form:label>

                            <form:input path="title" size="80" required="required" />
                            <form:errors path="title" cssClass="error" htmlEscape='false' />
                        </li>
                        <li>&nbsp;</li>
                        <li id="li_selecteditems_help">
                           <spring:message code="edmexport.selecteditems.types.title" htmlEscape='false' />
                        </li>
	                    <li>
	                    <c:if test="${!empty FormEDMData.listTypes}">
	                       <div id="div_selecteditems_types" class="div_selecteditems_types">
	                       <spring:message code="edmexport.selecteditems.types" htmlEscape='false' />
	                       <c:forEach items="${FormEDMData.listTypes}" var="type" varStatus="typeStatus">
		                        <ul id="ul_selecteditems_text_${typeStatus.index}" class="ul_selecteditems_text">
		                          <li>
		                              <label for="listTypes${typeStatus.index}" id="label_listTypes${typeStatus.index}" title='${type}'> ${type}</label>
		                              <form:textarea path="listTypes[${typeStatus.index}]" cols="80" rows="3" required="required" />
		                              <form:errors path="listTypes[${typeStatus.index}]" cssClass="error" htmlEscape='false' />
		                          </li>
		                        </ul>
	                       </c:forEach>
	                       </div>
	                    </c:if>
	                    </li>
	                    <li>&nbsp;</li>
	                    <li id="li_selecteditems_help">
                            <spring:message code="edmexport.selecteditems.urlbase.help" htmlEscape='false' />
                        </li>
	                    <li>
                            <form:label path="urlBase"><span title="<spring:message code='edmexport.selecteditems.urlbase' htmlEscape='false' />">
                                <spring:message code="edmexport.selecteditems.urlbase" htmlEscape='false' /></span></form:label>

                            <form:input path="urlBase" size="80" required="required" />
                            <form:errors path="urlBase" cssClass="error" htmlEscape='false' />
                        </li>
                        <li>&nbsp;</li>
                        <li id="li_selecteditems_help">
                            <spring:message code="edmexport.selecteditems.edm_rights.help" htmlEscape='false' />
                        </li>
                        <li>
                            <form:label path="edmRights"><span title="<spring:message code='edmexport.selecteditems.edm_rights' htmlEscape='false' />">
                                <spring:message code="edmexport.selecteditems.edm_rights" htmlEscape='false' /></span></form:label>

                            <form:input path="edmRights" size="80" required="required" />
                            <form:errors path="edmRights" cssClass="error" htmlEscape='false' />
                        </li>
                        <li>&nbsp;</li>
                        <li id="li_selecteditems_help">
                            <spring:message code="edmexport.selecteditems.edmUgc.help" htmlEscape='false' />
                        </li>
                        <li>
                             <form:label path="edmUgc"><span title="<spring:message code='edmexport.selecteditems.edmUgc' htmlEscape='false' />">
                                <spring:message code="edmexport.selecteditems.edmUgc" htmlEscape='false' /></span></form:label>
                            
                            <form:checkbox path="edmUgc" />
                        </li>
                    </ul>
                    <div id="div_selecteditems_actions" class="div_selecteditems_actions">
                        <ul>
		                    <li><input type="submit" title="<spring:message code='edmexport.selecteditems.visualize.help' htmlEscape='false' />" id="btn_submit" value="<spring:message code='edmexport.selecteditems.visualize' htmlEscape='false' />" /></li>
		                    <li><input type="button" title="<spring:message code='edmexport.selecteditems.exportxml.help' htmlEscape='false' />" name="btn_export" id="btn_export" value="<spring:message code='edmexport.selecteditems.exportxml' htmlEscape='false' />" /></li>
		                </ul>
		            </div>
                </form:form>
            </div>
        </div>