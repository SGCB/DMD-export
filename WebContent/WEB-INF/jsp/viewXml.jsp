<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


        <div id="div_viewxml" class="div_viewxml">
            <div id="div_viewxml_title" class="div_viewxml_title">
                <spring:message code="edmexport.viewxml.title" />
            </div>
            <div id="div_viewxml_form" class="div_viewxml_form">
                <form action="viewXml.htm" method="post" name="form_edm_data">
                    <input type="hidden" name="pageAction" id="pageAction" value="export" /> 
                    <ul id="ul_viewxml_form" class="ul_viewxml_form">
                        <li>
                            <label for="EDMXml" id="EDMXml"><spring:message code="edmexport.viewxml.label" /></label>
                            <textarea name="EDMXml" cols="120" rows="30" required="required" readonly="readonly"><c:out value="${edmXML}" /></textarea>
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