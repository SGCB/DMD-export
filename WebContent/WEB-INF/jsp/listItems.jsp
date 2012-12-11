<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

        <script>
        <!--
        
        jQuery(window).load(function() {
            jQuery('#loading-image').hide();
        });
        
        $(document).ready(function() {
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
            <div id="div_list_items" class="div_list_items">
                <div id="div_list_items_header" class="div_list_items_header">
                    <spring:message code="edmexport.listItems.header.item.label" /> ${hitCount}
                </div>
                <div id="div_list_items_body" class="div_list_items_body">
                    <ul id="ul_list_items">
	                    <c:set var="i" value="${offset}"/>
	                    <c:forEach items="${listItems.listItems}" var="item">
	                        <li id="li_${i + 1}">
	                           <ul id="ul_${i + 1}" class="ul_list_items_item">
	                               <li>
	                                   <c:set var="i">${i + 1 + offset}</c:set><c:out value="${i}" />
	                               </li>
	                               <li>
	                                   <spring:message code="edmexport.listItems.title.label" />: <c:out value="${item.title}" /> (<c:out value="${item.handle}" />)
	                                   <form:checkbox path="listItems[${item.index}].id" value="${item.id}" />
                                    </li>
                                    <li>
                                        <spring:message code="edmexport.listItems.author.label" />:
                                        <c:if test="${!empty item.author}">
                                            <ul class="ul_list_items_item_author">
                                            <c:set var="auth_i" value="0"/>
                                            <c:forEach items="${item.author}" var="author">
                                                <li>
                                                    <c:out value="${author}" />
                                                    <form:hidden path="listItems[${item.index}].author[${auth_i}]" value="${author}" />
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
                                                    <form:hidden path="listItems[${item.index}].listCollections.listCollections[${coll_i}].name" value="${coll.name}" />
                                                    <form:hidden path="listItems[${item.index}].listCollections.listCollections[${coll_i}].handle" value="${coll.handle}" />
                                                    <form:hidden path="listItems[${item.index}].listCollections.listCollections[${coll_i}].id" value="${coll.id}" />
                                                    <form:hidden path="listItems[${item.index}].listCollections.listCollections[${coll_i}].index" value="${coll_i}" />
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
                                                    <form:hidden path="listItems[${item.index}].subject[${subject_i}]" value="${subject}" />
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
                                                    <form:hidden path="listItems[${item.index}].type[${type_i}]" value="${type}" />
                                                    <c:set var="type_i">${type_i + 1}</c:set>
                                                </li>
                                            </c:forEach>
                                            </ul>
                                        </c:if>
                                   </li>
	                            </ul>
	                        <li>&nbsp;</li>
	                    </c:forEach>
                    </ul>
                </div>
                <div id="div_list_items_submit" class="div_list_items_submit">
                    <input  type="submit" name="go_list_items" value="<spring:message code="edmexport.listItems.save_reg" />" />
                </div>
                
                <div id="div_list_items_footer" class="div_list_items_footer">
                </div>
            </div>
            </form:form>
            </c:when>
            <c:otherwise>
                <h3><spring:message code="edmexport.listItems.void" /></h3>
            </c:otherwise>
        </c:choose>