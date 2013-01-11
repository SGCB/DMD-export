<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <script>
        <!--
        
        var posSearchArray = new Array();
        
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
            		posSearchArray.length = 0;
            		jQuery("#li_viewxml_next").remove();
            		jQuery("#li_viewxml_prev").remove();
            		var element = jQuery("#listElementsFilled").val();
            		var pos = searchTermElement(term, element, 'EDMXml', 0);
            		if (pos < 0) {
            			alert("<spring:message code='edmexport.viewxml.label.search.noresults' />");
            		}
            		renderNext(pos, term, 'EDMXml', element);
            	}
            });
        });
        
        
        function searchTermElement(term, element, id, offset)
        {
        	var pos = -1;
        	var content = jQuery('#' + id).val();
            if (element != "") {
            	var pos_element = -1;
            	do {
            	   pos_element = content.indexOf("<" + element, offset);
            	   if (pos_element >= 0) {
	            	   var pos_begin = content.indexOf(">", pos_element);
	            	   var pos_end = content.indexOf("</" + element + ">", pos_begin);
	            	   if (pos_begin > 0 && pos_end > 0) {
	            		    var str = content.substring(pos_begin + 1, pos_end - 1);
	            		    if ((pos = str.indexOf(term)) >= 0) {
	            		    	pos += pos_begin + 1;
	            		    	break;
	            		    }
	            		    offset = pos_end + element.length + 3;
	            	   } else break;
            	   }
            	} while (pos_element >= 0);
            } else {
            	pos = content.indexOf(term, offset);
            }
        	return pos;
        }
        
        function renderNext(pos, term, id, element)
        {
        	if (pos < 0) return;
            selectText(pos, term, id);
        	if (posSearchArray.indexOf(pos) < 0) posSearchArray.push(pos);
        	var index_prev = 0;
        	if (posSearchArray.length > 0 && (index_prev = posSearchArray.indexOf(pos)) > 0) {
        		var pos_prev = posSearchArray[index_prev - 1];
        		var li_prev = jQuery("#li_viewxml_prev");
        		var prev_str = "<spring:message code='edmexport.viewxml.label.search.prev' />";
        		if (li_prev.html() == undefined) {
                    var btn = jQuery("#li_viewxml_btn");
                    li_prev = jQuery("<li id='li_viewxml_prev'><a href='#' onclick='renderNext(" + pos_prev + ", \"" + term + "\", \"" + id + "\", \"" + element + "\")'>" + prev_str + " &lt;&lt;</a></li>");
                    btn.after(li_prev);
                } else {
                	li_prev.html("<a href='#' onclick='renderNext(" + pos_prev + ", \"" + term + "\", \"" + id + "\", \"" + element + "\")'>" + prev_str + " &lt;&lt;</a>");
                }
        	}
        	var pos_next = pos + term.length;
        	var li = jQuery("#li_viewxml_next");
        	if (pos_next > jQuery('#' + id).val().length) pos_next = 0;
            if (pos_next >= 0) {
                pos = searchTermElement(term, element, id, pos_next);
                if (pos < 0 && posSearchArray.length > 1) {
                	pos_next = 0;
                	pos = searchTermElement(term, element, id, pos_next);
                }
                if (pos >= 0) {
                	var next_str = "<spring:message code='edmexport.viewxml.label.search.next' />";
                    if (li.html() == undefined) {
                        var btn = jQuery("#li_viewxml_btn");
                        li = jQuery("<li id='li_viewxml_next'><a href='#' onclick='renderNext(" + pos + ", \"" + term + "\", \"" + id + "\", \"" + element + "\")'>" + next_str + " &gt;&gt;</a></li>");
                        if (jQuery("#li_viewxml_prev").html() != undefined) jQuery("#li_viewxml_prev").after(li);
                        else btn.after(li);
                    } else {
                    	li.html("<a href='#' onclick='renderNext(" + pos + ", \"" + term + "\", \"" + id + "\", \"" + element + "\")'>" + next_str + " &gt;&gt;</a>");
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
                <form action="getFile.htm" method="post" name="form_edm_data">
                    <input type="hidden" name="pageAction" id="pageAction" value="exportView" />
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