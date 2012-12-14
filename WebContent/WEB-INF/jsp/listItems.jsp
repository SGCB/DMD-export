<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <script>
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
                        if (data == "1") window.location = "home.htm?referer=${referer}&page=${next_page}";
                        else alert("<spring:message code='edmexport.listItems.json.error' />");
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
                        if (data == "1") window.location = "home.htm?referer=${referer}&page=${prev_page}";
                        else alert("<spring:message code='edmexport.listItems.json.error' />");
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
                    url: 'home.htm',
                    contentType: 'application/json; charset=UTF-8',
                    dataType: 'json',
                    async:false,
                    data: { referer: jQuery("#referer").val(), checked: JSON.stringify(arr_checked), nochecked: JSON.stringify(arr_nochecked) },
                    traditional: true,
                    success: function(data) {
                        if (data == "1") window.location = "home.htm?referer=${referer}";
                        else alert("<spring:message code='edmexport.listItems.json.error' />");
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
                    url: 'home.htm',
                    contentType: 'application/json; charset=UTF-8',
                    dataType: 'json',
                    async:false,
                    data: { referer: jQuery("#referer").val(), checked: JSON.stringify(arr_checked), nochecked: JSON.stringify(arr_nochecked) },
                    traditional: true,
                    success: function(data) {
                        if (data == "1") window.location = "home.htm?referer=${referer}&page=1";
                        else alert("<spring:message code='edmexport.listItems.json.error' />");
                    }
                });
            });

        });
        //-->
        </script>
        <h2><spring:message code="edmexport.listItems.title" /></h2>
        <c:choose>
            <c:when test="${!empty listItems && !empty listItems.listItems}">
            <div id="loading-image">
                <spring:url value="/img/lb-loading.gif" var="edmexport_loading_url" htmlEscape="true" />
                 <img id="loading-image-img" src="${edmexport_loading_url}" alt="Loading..." />
            </div>
            <div id="div_list_items_check" class="div_list_items_check">
                <form name="form_chk">
                <table>
                    <tr>
                        <td>
                            <label for="chk_all"><spring:message code="edmexport.listItems.chk_all.label" /></label>
                        </td>
                        <td>
                            <input type="checkbox" id="chk_all" name="chk_all" value="chk_all" />
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            <label for="un_chk_all"><spring:message code="edmexport.listItems.un_chk_all.label" /></label>
                        </td>
                        <td>
                            <input type="checkbox" id="un_chk_all" name="un_chk_all" value="un_chk_all" />
                        </td>
                    </tr>
                </table>
                </form>
            </div>
            <form:form action="listItems.htm" method="post" name="list_items" id="list_items" commandName="listItems" onsubmit="return valid_list_items(this);" >
            <input type="hidden" name="referer" id="referer" value="${referer}" />
            <div id="div_list_items" class="div_list_items">
                <div id="div_list_items_header" class="div_list_items_header">
                    <spring:message code="edmexport.listItems.header.item.label" /> ${listItemsPage} / ${hitCount}.
                     <c:if test="${!empty page}">Pag: ${page}/${pageTotal}</c:if>
                     <c:choose>
                        <c:when test="${listItemsPage < hitCount}">
                            <a href="home.htm?referer=${referer}" id="show_all"><spring:message code="edmexport.listItems.show_all.label" /></a>
                        </c:when>
                        <c:otherwise>
                            <a href="home.htm?referer=${referer}&page=1" id="page_1" class="page_1">Pag 1</a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div id="div_list_items_body" class="div_list_items_body">
                    <ul id="ul_list_items">
	                    <c:set var="i" value="${offset}"/>
	                    <c:set var="j" value="0"/>
	                    <c:forEach items="${listItems.listItems}" var="item">
	                        <li id="li_${i + 1}">
	                           <ul id="ul_${i + 1}" class="ul_list_items_item">
	                               <li>
	                                   <c:set var="i">${i + 1 + offset}</c:set><c:out value="${i}" />
	                               </li>
	                               <li>
	                                   <spring:message code="edmexport.listItems.title.label" />: <c:out value="${item.title}" /> (<c:out value="${item.handle}" />)
	                                   <spring:bind path="listItems[${j}].id">
	                                       <input type="checkbox" name="listItems[${j}].id" id="listItems${j}.id" value="${item.id}" <c:if test="${item.checked}" >checked="checked"</c:if> />
	                                   </spring:bind>
                                    </li>
                                    <li>
                                        <spring:message code="edmexport.listItems.author.label" />:
                                        <c:if test="${!empty item.author}">
                                            <ul class="ul_list_items_item_author">
                                            <c:set var="auth_i" value="0"/>
                                            <c:forEach items="${item.author}" var="author">
                                                <li>
                                                    <c:out value="${author}" />
                                                    <form:hidden path="listItems[${j}].author[${auth_i}]" value="${author}" />
                                                    <c:set var="auth_i">${auth_i + 1}</c:set>
                                                </li>
                                            </c:forEach>
                                            </ul>
                                        </c:if>
	                               </li>
	                               <li>
                                        <spring:message code="edmexport.listItems.collection.label" />:
                                        <c:if test="${!empty item.listCollections && !empty item.listCollections.listCollections}">
                                            <ul class="ul_list_items_item_collections">
                                            <c:set var="coll_i" value="0"/>
                                            <c:forEach items="${item.listCollections.listCollections}" var="coll">
                                                <li>
                                                    <c:out value="${coll.name}" />  (<c:out value="${coll.handle}" />)
                                                    <form:hidden path="listItems[${j}].listCollections.listCollections[${coll_i}].name" value="${coll.name}" />
                                                    <form:hidden path="listItems[${j}].listCollections.listCollections[${coll_i}].handle" value="${coll.handle}" />
                                                    <form:hidden path="listItems[${j}].listCollections.listCollections[${coll_i}].id" value="${coll.id}" />
                                                    <form:hidden path="listItems[${j}].listCollections.listCollections[${coll_i}].index" value="${coll_i}" />
                                                    <c:set var="coll_i">${coll_i + 1}</c:set>
                                                </li>
                                            </c:forEach>
                                            </ul>
                                        </c:if>
                                   </li>
	                               <li>
                                        <spring:message code="edmexport.listItems.subject.label" />:
                                        <c:if test="${!empty item.subject}">
                                            <ul class="ul_list_items_item_subject">
                                            <c:set var="subject_i" value="0"/>
                                            <c:forEach items="${item.subject}" var="subject">
                                                <li>
                                                    <c:out value="${subject}" />
                                                    <form:hidden path="listItems[${j}].subject[${subject_i}]" value="${subject}" />
                                                    <c:set var="subject_i">${subject_i + 1}</c:set>
                                                </li>
                                            </c:forEach>
                                            </ul>
                                        </c:if>
                                   </li>
                                   <li>
                                        <spring:message code="edmexport.listItems.type.label" />:
                                        <c:if test="${!empty item.type}">
                                            <ul class="ul_list_items_item_type">
                                            <c:set var="type_i" value="0"/>
                                            <c:forEach items="${item.type}" var="type">
                                                <li>
                                                    <c:out value="${type}" />
                                                    <form:hidden path="listItems[${j}].type[${type_i}]" value="${type}" />
                                                    <c:set var="type_i">${type_i + 1}</c:set>
                                                </li>
                                            </c:forEach>
                                            </ul>
                                        </c:if>
                                   </li>
	                            </ul>
	                        <li>&nbsp;</li>
	                        <c:set var="j">${j + 1}</c:set>
	                    </c:forEach>
                    </ul>
                </div>
                <div id="div_list_items_submit" class="div_list_items_submit">
                    <input  type="submit" name="go_list_items" value="<spring:message code="edmexport.listItems.save_reg" />" />
                </div>
                
                <div id="div_list_items_footer" class="div_list_items_footer">
                    <c:if test="${!empty prev_page}"><a href="home.htm?referer=${referer}&page=${prev_page}" id="prev_page" class="prev_page"><spring:message code="edmexport.listItems.previous_page.label" /></a></c:if>
                    <c:if test="${!empty next_page}"><a href="home.htm?referer=${referer}&page=${next_page}" id="next_page" class="next_page"><spring:message code="edmexport.listItems.next_page.label" /></a></c:if>
                </div>
            </div>
            </form:form>
            </c:when>
            <c:otherwise>
                <h3><spring:message code="edmexport.listItems.void" /></h3>
            </c:otherwise>
        </c:choose>