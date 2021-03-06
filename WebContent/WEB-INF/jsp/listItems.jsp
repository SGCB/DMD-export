<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <script type="text/javascript">
        <!--
        
        jQuery.ajaxSettings.traditional = true;
        
        jQuery(window).load(function() {
            jQuery('#loading-image').hide();
        });
        
        jQuery(document).ready(function() {
	        jQuery("#chk_all").click(function() {
	        	if (jQuery(this).is(':checked')) {
	        		jQuery("#un_chk_all").attr("checked", false);
	        		jQuery("#list_items input[type=checkbox]").attr("checked", true);
	        	}
	        });
	        
	        jQuery("#un_chk_all").click(function() {
	            if (jQuery(this).is(':checked')) {
	                jQuery("#chk_all").attr("checked", false);
	                jQuery("#list_items input[type=checkbox]").attr("checked", false);
	            }
	        });
	        
	        jQuery("#next_page").attr('href', '#');
	        jQuery("#prev_page").attr('href', '#');
	        jQuery("#show_all").attr('href', '#');
	        jQuery("#page_1").attr('href', '#');
	        
	        jQuery("#next_page").click(function() {
	        	var arr_checked = new Array();
	        	var arr_nochecked = new Array();
	        	jQuery("#list_items input[type=checkbox]").each(function() {
	        		if (jQuery(this).is(':checked')) {
	        			arr_checked.push(jQuery(this).val());
	        		} else {
	        			arr_nochecked.push(jQuery(this).val());
	        		}
	        	});
	        	jQuery.ajax({
                    url: 'home.htm',
                    contentType: 'application/json; charset=UTF-8',
                    dataType: 'json',
                    async:false,
                    data: { referer: jQuery("#referer").val(), checked: JSON.stringify(arr_checked), nochecked: JSON.stringify(arr_nochecked) },
                    traditional: true,
                    success: function(data) {
                        if (parseInt(data, 10) >= 0) window.location = "home.htm?referer=${referer}&page=${next_page}";
                        else {
                        	try {
                        	    jQuery.spro.jpopit("<spring:message code='edmexport.listItems.json.error' htmlEscape='false' javaScriptEscape='true' />"
                                , {fadeInTime: 200, fadeOutTime: 1000, delay: 10000}).css({"background-color":"#FFFFFF", "color":"#000000"});
                        	} catch (e) {
                        		alert("<spring:message code='edmexport.listItems.json.error' htmlEscape='false' javaScriptEscape='true' />");
                        	}
                        }
                    }
                });
	        });
	        		
       		jQuery("#prev_page").click(function() {
                var arr_checked = new Array();
                var arr_nochecked = new Array();
                jQuery("#list_items input[type=checkbox]").each(function() {
                    if (jQuery(this).is(':checked')) {
                        arr_checked.push(jQuery(this).val());
                    } else {
                        arr_nochecked.push(jQuery(this).val());
                    }
                });
                jQuery.ajax({
                    url: 'home.htm',
                    contentType: 'application/json; charset=UTF-8',
                    dataType: 'json',
                    async:false,
                    data: { referer: jQuery("#referer").val(), checked: JSON.stringify(arr_checked), nochecked: JSON.stringify(arr_nochecked) },
                    traditional: true,
                    success: function(data) {
                        if (parseInt(data, 10) >= 0) window.location = "home.htm?referer=${referer}&page=${prev_page}";
                        else {
                            try {
                                jQuery.spro.jpopit("<spring:message code='edmexport.listItems.json.error' htmlEscape='false' javaScriptEscape='true' />"
                                , {fadeInTime: 200, fadeOutTime: 1000, delay: 10000}).css({"background-color":"#FFFFFF", "color":"#000000"});
                            } catch (e) {
                                alert("<spring:message code='edmexport.listItems.json.error' htmlEscape='false' javaScriptEscape='true' />");
                            }
                        }
                    }
                });
            });
       		
       		jQuery("#show_all").click(function() {
                var arr_checked = new Array();
                var arr_nochecked = new Array();
                jQuery("#list_items input[type=checkbox]").each(function() {
                    if (jQuery(this).is(':checked')) {
                        arr_checked.push(jQuery(this).val());
                    } else {
                        arr_nochecked.push(jQuery(this).val());
                    }
                });
                jQuery.ajax({
                	type: "POST",
                    url: 'home.htm',
                    /* only for GET method
                    contentType: 'application/json; charset=UTF-8',
                    dataType: 'json',
                    */
                    async:false,
                    data: { referer: jQuery("#referer").val(), checked: JSON.stringify(arr_checked), nochecked: JSON.stringify(arr_nochecked) },
                    traditional: true,
                    success: function(data) {
                        if (parseInt(data, 10) >= 0) window.location = "home.htm?referer=${referer}";
                        else {
                            try {
                                jQuery.spro.jpopit("<spring:message code='edmexport.listItems.json.error' htmlEscape='false' javaScriptEscape='true' />"
                                , {fadeInTime: 200, fadeOutTime: 1000, delay: 10000}).css({"background-color":"#FFFFFF", "color":"#000000"});
                            } catch (e) {
                                alert("<spring:message code='edmexport.listItems.json.error' htmlEscape='false' javaScriptEscape='true' />");
                            }
                        }
                    }
                });
            });
       		
       		jQuery("#page_1").click(function() {
                var arr_checked = new Array();
                var arr_nochecked = new Array();
                jQuery("#list_items input[type=checkbox]").each(function() {
                    if (jQuery(this).is(':checked')) {
                        arr_checked.push(jQuery(this).val());
                    } else {
                        arr_nochecked.push(jQuery(this).val());
                    }
                });
                jQuery.ajax({
                	type: "POST",
                    url: 'home.htm',
                    /* only for GET method
                    contentType: 'application/json; charset=UTF-8',
                    dataType: 'json',
                    */
                    async:false,
                    data: { referer: jQuery("#referer").val(), checked: JSON.stringify(arr_checked), nochecked: JSON.stringify(arr_nochecked) },
                    traditional: true,
                    success: function(data) {
                        if (parseInt(data, 10) >= 0) window.location = "home.htm?referer=${referer}&page=1";
                        else {
                            try {
                                jQuery.spro.jpopit("<spring:message code='edmexport.listItems.json.error' htmlEscape='false' javaScriptEscape='true' />"
                                , {fadeInTime: 200, fadeOutTime: 1000, delay: 10000}).css({"background-color":"#FFFFFF", "color":"#000000"});
                            } catch (e) {
                                alert("<spring:message code='edmexport.listItems.json.error' htmlEscape='false' javaScriptEscape='true' />");
                            }
                        }
                    }
                });
            });
       		
       		jQuery("#list_items input[type=checkbox]").each(function() {
                var prefix = jQuery(this).attr("name").substring(0, jQuery(this).attr("name").indexOf("."));
                if (!jQuery(this).is(':checked')) {
                    jQuery("#list_items input[name^='" + prefix + "']").each(function() {
                        if (jQuery(this)[0].disabled) jQuery(this).removeAttr('disabled');
                    });
                }
       		});

        });
        
        
        function valid_list_items(form)
        {
        	var arr_checked = new Array();
            var arr_nochecked = new Array();
            jQuery("#list_items input[type=checkbox]").each(function() {
                if (jQuery(this).is(':checked')) {
                    arr_checked.push(jQuery(this).val());
                } else {
                    arr_nochecked.push(jQuery(this).val());
                }
            });
            var result = 0;
            jQuery.ajax({
            	type: "POST",
                url: 'home.htm',
                /* only for GET method
                contentType: 'application/json; charset=UTF-8',
                dataType: 'json',
                */
                async:false,
                data: { referer: jQuery("#referer").val(), checked: JSON.stringify(arr_checked), nochecked: JSON.stringify(arr_nochecked) },
                traditional: true,
                success: function(data) {
                	result = parseInt(data, 10);
                }
            });
            if (result > 0) {
            	var i = 0;
            	jQuery("#list_items input[type=checkbox]").each(function() {
            		var prefix = jQuery(this).attr("name").substring(0, jQuery(this).attr("name").indexOf("."));
                    if (!jQuery(this).is(':checked')) {
                    	jQuery("#list_items input[name^='" + prefix + "']").each(function() {
                    		jQuery(this).attr('disabled','disabled');
                    	});
                    } else {
                    	//console.debug(i + " ; " + jQuery(this).attr('id'));
                        if (jQuery(this).attr('id').indexOf("listItems" + i) < 0) {
                            jQuery("#list_items input[name^='" + prefix + "']").each(function() {
                                //console.debug(jQuery(this).attr('id'));
                                jQuery(this).attr('id', 
                                    jQuery(this).attr('id').replace(/^listItems\d+\./, "listItems" + i + ".")
                                );
                                //console.debug(jQuery(this).attr('id'));
                                //console.debug(jQuery(this).attr('name'));
                                jQuery(this).attr('name',
                                    jQuery(this).attr('name').replace(/^listItems\[\d+\]\./, "listItems[" + i + "].")
                                );
                                //console.debug(jQuery(this).attr('name'));
                            });
                        }
                        i++;
                    }
            	});
            	return true;
            } else {
            	try {
                    jQuery.spro.jpopit("<spring:message code='edmexport.listItems.json.submit.error' htmlEscape='false' javaScriptEscape='true' />"
                    , {fadeInTime: 200, fadeOutTime: 1000, delay: 10000}).css({"background-color":"#FFFFFF", "color":"#000000"});
                } catch (e) {
                    alert("<spring:message code='edmexport.listItems.json.submit.error' htmlEscape='false' javaScriptEscape='true' />");
                }
                return false;
            }
        }
        
        //-->
        </script>
        <span id="edmexport_main_help" class="edmexport_main_help"><p><spring:message code="edmexport.listItems.help" htmlEscape='false' /></p></span>
        <h2><spring:message code="edmexport.listItems.title" htmlEscape='false' /></h2>
        <c:choose>
            <c:when test="${!empty listItemsBO && !empty listItemsBO.listItems}">
            <div id="loading-image">
                <spring:url value="/img/lb-loading.gif" var="edmexport_loading_url" htmlEscape="true" />
                 <img id="loading-image-img" src="${edmexport_loading_url}" alt="Loading..." />
            </div>
            <div id="div_list_items_check" class="div_list_items_check">
                <form name="form_chk" action="selectedItems.htm">
                <table>
                    <tr>
                        <td>
                            <label for="chk_all"><spring:message code="edmexport.listItems.chk_all.label" htmlEscape='false' /></label>
                        </td>
                        <td>
                            <input type="checkbox" id="chk_all" name="chk_all" value="chk_all" />
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            <label for="un_chk_all"><spring:message code="edmexport.listItems.un_chk_all.label" htmlEscape='false' /></label>
                        </td>
                        <td>
                            <input type="checkbox" id="un_chk_all" name="un_chk_all" value="un_chk_all" />
                        </td>
                    </tr>
                </table>
                </form>
            </div>
            <form:form action="selectedItems.htm" method="post" name="list_items" id="list_items" commandName="listItemsBO" onsubmit="return valid_list_items(this);" >
            <input type="hidden" name="referer" id="referer" value="${referer}" />
            <input type="hidden" name="numItemsChecked" value="${numItemsChecked}" />
            <div id="div_list_items" class="div_list_items">
                <div id="div_list_items_header" class="div_list_items_header">
                    <spring:message code="edmexport.listItems.header.item.label" htmlEscape='false' /> ${listItemsPage} / ${hitCount}.
                     <c:if test="${!empty page}">Pag: ${page}/${pageTotal}</c:if>
                     <c:choose>
                        <c:when test="${listItemsPage < hitCount}">
                            <a href="home.htm?referer=${referer}" id="show_all"><spring:message code="edmexport.listItems.show_all.label" /></a>
                        </c:when>
                        <c:otherwise>
                            <a href="home.htm?referer=${referer}&amp;page=1" id="page_1" class="page_1">Pag 1</a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div id="div_list_items_body" class="div_list_items_body">
                    <ul id="ul_list_items">
	                    <c:set var="i" value="${offset}"/>
	                    <c:forEach items="${listItemsBO.listItems}" var="item" varStatus="itemStatus">
	                        <li id="li_${i + 1}">
	                           <ul id="ul_${i + 1}" class="ul_list_items_item">
	                               <li>
	                                   <c:set var="i">${i + 1 + offset}</c:set><c:out value="${i}" />
	                               </li>
	                               <li>
	                                   <spring:message code="edmexport.listItems.title.label" htmlEscape='false' />: <c:out value="${item.title}" /> (<c:out value="${item.handle}" />)
	                                   <spring:bind path="listItems[${itemStatus.index}].id">
	                                       <input type="checkbox" name="listItems[${itemStatus.index}].id" id="listItems${itemStatus.index}.id" value="${item.id}" <c:if test="${item.checked}" >checked="checked"</c:if> />
	                                   </spring:bind>
                                    </li>
                                    <li>
                                        <spring:message code="edmexport.listItems.author.label" htmlEscape='false' />:
                                        <c:if test="${!empty item.author && !empty item.author.listAuthors}">
                                            <ul class="ul_list_items_item_author">
                                            <c:forEach items="${item.author.listAuthors}" var="authorVar" varStatus="authorStatus">
                                                <li>
                                                    <c:out value="${authorVar}" />
                                                    <form:hidden path="listItems[${itemStatus.index}].author.listAuthors[${authorStatus.index}]" />
                                                </li>
                                            </c:forEach>
                                            </ul>
                                        </c:if>
	                               </li>
	                               <li>
                                        <spring:message code="edmexport.listItems.collection.label" htmlEscape='false' />:
                                        <c:if test="${!empty item.listCollections && !empty item.listCollections.listCollections}">
                                            <ul class="ul_list_items_item_collections">
                                            <c:forEach items="${item.listCollections.listCollections}" var="coll" varStatus="collStatus">
                                                <li>
                                                    <c:out value="${coll.name}" />  (<c:out value="${coll.handle}" />)
                                                    <form:hidden path="listItems[${itemStatus.index}].listCollections.listCollections[${collStatus.index}].name" />
                                                    <form:hidden path="listItems[${itemStatus.index}].listCollections.listCollections[${collStatus.index}].handle" />
                                                    <form:hidden path="listItems[${itemStatus.index}].listCollections.listCollections[${collStatus.index}].id" />
                                                    <form:hidden path="listItems[${itemStatus.index}].listCollections.listCollections[${collStatus.index}].index" />
                                                </li>
                                            </c:forEach>
                                            </ul>
                                        </c:if>
                                   </li>
	                               <li>
                                        <spring:message code="edmexport.listItems.subject.label" htmlEscape='false' />:
                                        <c:if test="${!empty item.subject && !empty item.subject.listSubjects}">
                                            <ul class="ul_list_items_item_subject">
                                            <c:forEach items="${item.subject.listSubjects}" var="subject" varStatus="subjectStatus">
                                                <li>
                                                    <c:out value="${subject}" />
                                                    <form:hidden path="listItems[${itemStatus.index}].subject.listSubjects[${subjectStatus.index}]" />
                                                </li>
                                            </c:forEach>
                                            </ul>
                                        </c:if>
                                   </li>
                                   <li>
                                        <spring:message code="edmexport.listItems.type.label" htmlEscape='false' />:
                                        <c:if test="${!empty item.type && !empty item.type.listTypes}">
                                            <ul class="ul_list_items_item_type">
                                            <c:forEach items="${item.type.listTypes}" var="type" varStatus="typeStatus">
                                                <li>
                                                    <c:out value="${type}" />
                                                    <form:hidden path="listItems[${itemStatus.index}].type.listTypes[${typeStatus.index}]" />
                                                </li>
                                            </c:forEach>
                                            </ul>
                                        </c:if>
                                   </li>
	                            </ul>
	                        <li class="li_linefeed">&nbsp;</li>
	                    </c:forEach>
                    </ul>
                </div>
                <div id="div_list_items_submit" class="div_list_items_submit">
                    <input  type="submit" name="go_list_items" value="<spring:message code="edmexport.listItems.save_reg" htmlEscape='false' />" />
                </div>
                
                <div id="div_list_items_footer" class="div_list_items_footer">
                    <c:if test="${!empty prev_page}"><a href="home.htm?referer=${referer}&page=${prev_page}" id="prev_page" class="prev_page"><spring:message code="edmexport.listItems.previous_page.label" htmlEscape='false' /></a></c:if>
                    <c:if test="${!empty next_page}"><a href="home.htm?referer=${referer}&page=${next_page}" id="next_page" class="next_page"><spring:message code="edmexport.listItems.next_page.label" htmlEscape='false' /></a></c:if>
                </div>
            </div>
            </form:form>
            </c:when>
            <c:otherwise>
                <h3><spring:message code="edmexport.listItems.void" htmlEscape='false' /></h3>
            </c:otherwise>
        </c:choose>