<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <script>
        <!--
        jQuery(document).ready(function() {
        	
        	var ul = jQuery("#ul_viewxml_form");
        	
        	var li = jQuery("<li id='li_viewxml_btn'><input type='button' id='btn_viewxml_search' name='btn_viewxml_search' value=\"<spring:message code='edmexport.viewxml.label.search.btn' />\" /></li>");
            ul.prepend(li);
        	
        	li = jQuery("<li />");
        	var label = jQuery("<label for='listElementsFilled'><spring:message code='edmexport.viewxml.label.select' /></label>");
        	li.append(label);
        	var select = jQuery("<select name='listElementsFilled' id='listElementsFilled' />");
        	var option = jQuery("<option value=''></option>");
        	select.append(option);
        	<c:if test="${!empty listElementsFilled}">
        	   <c:forEach items="${listElementsFilled}" var="element" varStatus="elementStatus">
        	    option = jQuery("<option value='${element}'>${element}</option>");
        	    select.append(option);
        	   </c:forEach>
        	</c:if>
        	li.append(select);
        	ul.prepend(li);
        	
        	li = jQuery("<li />");
        	label = jQuery("<label for='term'><spring:message code='edmexport.viewxml.label.search.term' /></label>");
            li.append(label);
        	var input = jQuery("<input type='text' name='term' id='term' size='60' />");
        	li.append(input);
        	ul.prepend(li);
        	
        	li = jQuery("<li><h3><spring:message code='edmexport.viewxml.label.search' /></h3></li>");
            ul.prepend(li);
            
            jQuery("#btn_viewxml_search").click(function() {
            	var term = jQuery("#term").val();
            	if (term == "") {
            		alert("<spring:message code='edmexport.viewxml.label.search.term.error' />");
            	} else {
            		jQuery("#li_viewxml_next").remove();
            		var pos = 0;
            		var element = jQuery("#listElementsFilled").val();
            		if (element != "") {
            	
                    } else {
                    	pos = jQuery('#EDMXml').val().indexOf(term);
                 		renderNext(pos, term, 'EDMXml');
                    }
            	}
            });
        });
        
        function renderNext(pos, term, id)
        {
        	if (pos < 0) return;
        	selectText(pos, term, 'EDMXml');
        	var pos_next = pos + term.length;
        	var li = jQuery("#li_viewxml_next");
        	if (pos_next > jQuery('#EDMXml').val().length) pos_next = 0;
            if (pos_next >= 0) {
                pos = jQuery('#EDMXml').val().indexOf(term, pos_next);
                if (pos < 0 && li.html() != undefined) {
                	pos_next = 0;
                	pos = jQuery('#EDMXml').val().indexOf(term, pos_next);
                }
                if (pos >= 0) {
                    if (li.html() == undefined) {
                        var btn = jQuery("#li_viewxml_btn");
                        li = jQuery("<li id='li_viewxml_next'><a href='#' onclick='renderNext(" + pos + ", \"" + term + "\", \"" + id + "\")'>&gt;&gt;</a></li>");
                        btn.after(li);
                    } else {
                    	li.html("<a href='#' onclick='renderNext(" + pos + ", \"" + term + "\", \"" + id + "\")'>&gt;&gt;</a>");
                    }
                } else {
                	li.remove();
                }
            }
        }
        
        //--> 
        </script>

        <div id="div_viewxml" class="div_viewxml">
            <div id="div_viewxml_title" class="div_viewxml_title">
                <spring:message code="edmexport.viewxml.title" />
            </div>
            <div id="div_viewxml_form" class="div_viewxml_form">
                <form action="viewXml.htm" method="post" name="form_edm_data">
                    <input type="hidden" name="pageAction" id="pageAction" value="export" />
                    <ul id="ul_viewxml_form" class="ul_viewxml_form">
                        <li>
                            <h3><label for="EDMXml" id="EDMXml_label"><spring:message code="edmexport.viewxml.label" /></label></h3>
                        </li>
                        <li>
                            <textarea name="EDMXml" id="EDMXml" cols="120" rows="30" required="required" readonly="readonly"><c:out value="${edmXML}" /></textarea>
                        </li>
                    </ul>
                    <div id="div_viewxml_actions" class="div_viewxml_actions">
                        <ul>
                            <li><input type="submit" value="<spring:message code="edmexport.viewxml.export" />" /></li>
                        </ul>
                    </div>
                </form>
            </div>
        </div>