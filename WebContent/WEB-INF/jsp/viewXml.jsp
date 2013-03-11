<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <script type="text/javascript">
        <!--
        
        var posSearchArray = new Array();
        
        jQuery(document).ready(function() {
        	
        	var ul = jQuery("#ul_viewxml_form");
        	
        	var li = jQuery("<li id='li_viewxml_btn'><input type='button' id='btn_viewxml_search' name='btn_viewxml_search' value=\"<spring:message code='edmexport.viewxml.label.search.btn' htmlEscape='false' javaScriptEscape='true' />\" \/><\/li>");
            ul.prepend(li);
        	
        	li = jQuery("<li \/>");
        	var label = jQuery("<label for='listElementsFilled'><spring:message code='edmexport.viewxml.label.select' htmlEscape='false' javaScriptEscape='true' /><\/label>");
        	li.append(label);
        	var select = jQuery("<select name='listElementsFilled' id='listElementsFilled' \/>");
        	var option = jQuery("<option value=''><\/option>");
        	select.append(option);
        	<c:if test="${!empty listElementsFilled}">
        	   <c:forEach items="${listElementsFilled}" var="element" varStatus="elementStatus">
        	    option = jQuery("<option value='${element}'>${element}<\/option>");
        	    select.append(option);
        	   </c:forEach>
        	</c:if>
        	li.append(select);
        	ul.prepend(li);
        	
        	li = jQuery("<li \/>");
        	label = jQuery("<label for='term'><spring:message code='edmexport.viewxml.label.search.term' htmlEscape='false' javaScriptEscape='true' /><\/label>");
            li.append(label);
        	var input = jQuery("<input type='text' name='term' id='term' size='60' \/>");
        	li.append(input);
        	ul.prepend(li);
        	
        	li = jQuery("<li><h3><spring:message code='edmexport.viewxml.label.search' htmlEscape='false' javaScriptEscape='true' /><\/h3><\/li>");
            ul.prepend(li);
            
            jQuery("#btn_viewxml_search").click(function() {
            	var term = jQuery("#term").val();
            	if (term == "") {
            		try {
            			jQuery.spro.jpopit("<spring:message code='edmexport.viewxml.label.search.term.error' htmlEscape='false' javaScriptEscape='true' />"
                            , {fadeInTime: 200, fadeOutTime: 1000, delay: 10000}).css({"background-color":"#FFFFFF", "color":"#000000"});
            		} catch (e) {
            			alert("<spring:message code='edmexport.viewxml.label.search.term.error' htmlEscape='false' javaScriptEscape='true' />");
            		}
            	} else {
            		posSearchArray.length = 0;
            		jQuery("#li_viewxml_next").remove();
            		jQuery("#li_viewxml_prev").remove();
            		var element = jQuery("#listElementsFilled").val();
            		var pos = searchTermElement(term, element, 'EDMXml', 0);
            		if (pos < 0) {
            			try {
            			    jQuery.spro.jpopit("<spring:message code='edmexport.viewxml.label.search.noresults' htmlEscape='false' javaScriptEscape='true' />"
                                , {fadeInTime: 200, fadeOutTime: 1000, delay: 10000}).css({"background-color":"#FFFFFF", "color":"#000000"});
            			} catch (e) {
            			    alert("<spring:message code='edmexport.viewxml.label.search.noresults' htmlEscape='false' javaScriptEscape='true' />");
            			}
            		}
            		renderNext(pos, term, 'EDMXml', element);
            	}
            });
            
            var xml_size = jQuery('#EDMXml').val().length;
            var units = "B";
            if (xml_size > 1048576) {
            	xml_size /= 1048576;
            	xml_size = xml_size.toFixed(2);
            	units = "MB";
            } else if (xml_size > 1024) {
            	xml_size /= 1024;
            	xml_size = xml_size.toFixed(2);
            	units = "KB";
            }
            jQuery('#xml_size').html(" " + xml_size + " " + units);
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
	            		    var str = content.substring(pos_begin + 1, pos_end);
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
        		var prev_str = "<spring:message code='edmexport.viewxml.label.search.prev' htmlEscape='false' javaScriptEscape='true' />";
        		if (li_prev.html() == undefined) {
                    var btn = jQuery("#li_viewxml_btn");
                    li_prev = jQuery("<li id='li_viewxml_prev'><a href='#' onclick='renderNext(" + pos_prev + ", \"" + term + "\", \"" + id + "\", \"" + element + "\")'>" + prev_str + " &lt;&lt;<\/a><\/li>");
                    btn.after(li_prev);
                } else {
                	li_prev.html("<a href='#' onclick='renderNext(" + pos_prev + ", \"" + term + "\", \"" + id + "\", \"" + element + "\")'>" + prev_str + " &lt;&lt;<\/a>");
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
                	var next_str = "<spring:message code='edmexport.viewxml.label.search.next' htmlEscape='false' javaScriptEscape='true' />";
                    if (li.html() == undefined) {
                        var btn = jQuery("#li_viewxml_btn");
                        li = jQuery("<li id='li_viewxml_next'><a href='#' onclick='renderNext(" + pos + ", \"" + term + "\", \"" + id + "\", \"" + element + "\")'>" + next_str + " &gt;&gt;<\/a><\/li>");
                        if (jQuery("#li_viewxml_prev").html() != undefined) jQuery("#li_viewxml_prev").after(li);
                        else btn.after(li);
                    } else {
                    	li.html("<a href='#' onclick='renderNext(" + pos + ", \"" + term + "\", \"" + id + "\", \"" + element + "\")'>" + next_str + " &gt;&gt;<\/a>");
                    }
                } else {
                	li.remove();
                }
            }
        }
        
        function valid_edXML(form)
        {
        	if (form.edmXMLEncoded.value != "") {
        		form.EDMXml.value = "";
        	} else if (form.EDMXml.value == "") {
        		form.EDMXml.value = document.getElementById("EDMXml").value;
        	}
        	return true;
        }
        
        //--> 
        </script>

        <span id="edmexport_main_help" class="edmexport_main_help"><p><spring:message code="edmexport.viewxml.help" htmlEscape='false' /></p></span>
        <div id="div_viewxml" class="div_viewxml">
            <div id="div_viewxml_title" class="div_viewxml_title">
                <spring:message code="edmexport.viewxml.title" htmlEscape='false' />
            </div>
            <div id="div_viewxml_form" class="div_viewxml_form">
                <form action="getFile.htm" method="post" name="form_edm_data">
                    <ul id="ul_viewxml_form" class="ul_viewxml_form">
                        <li>
                            <h3><label for="EDMXml" id="EDMXml_label"><spring:message code="edmexport.viewxml.label" htmlEscape='false' /></label></h3>
                        </li>
                        <li>
                            <textarea name="EDMXml" id="EDMXml" cols="120" rows="30" required="required" readonly="readonly"><c:out value="${edmXML}" escapeXml="true" /></textarea>
                        </li>
                        <li><spring:message code="edmexport.viewxml.tamanyo" htmlEscape='false' /><span id="xml_size"></span></li>
                    </ul>
                </form>
                <form action="getFile.htm" method="post" name="form_edm_data_submit" onsubmit="return valid_edXML(this)">
                    <input type="hidden" name="pageAction" id="pageAction" value="exportView" />
                    <textarea name="EDMXml" cols="120" rows="30" required="required" readonly="readonly" style="display: none;"></textarea>
                    <textarea name="edmXMLEncoded" id="edmXMLEncoded" style="display: none;" readonly="readonly"><c:out value="${edmXMLEncoded}" /></textarea>
                    <div id="div_viewxml_actions" class="div_viewxml_actions">
                        <ul>
                            <li><input type="submit" title="<spring:message code="edmexport.selecteditems.exportxml.help" htmlEscape='false' />" value="<spring:message code="edmexport.viewxml.export" htmlEscape='false' />" /></li>
                        </ul>
                    </div>
                </form>
            </div>
        </div>