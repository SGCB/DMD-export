<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <title><tiles:insertAttribute name="title" ignore="true" /></title>
        
        <spring:url value="/css/edmexport.css" var="edmexport_css_url" htmlEscape="true" />
        <link rel='stylesheet' id='edmexport-css'  href='${edmexport_css_url}' type='text/css' media='all' />
        
        <spring:url value="/js/jquery-1.8.3.js" var="jquery_url" htmlEscape="true" />
        <script type="text/javascript" src="${jquery_url}"> </script>
        <spring:url value="/js/edmexport.js" var="edmexport_url" htmlEscape="true" />
        <script type="text/javascript" src="${edmexport_url}"> </script>
    </head>
    <body>
        <tiles:insertAttribute name="header"/>
        <h1><spring:message code="edmexport.title" /></h1>
        <tiles:insertAttribute name="tabs" />
        <tiles:insertAttribute name="body" />
        <tiles:insertAttribute name="footer"/>
    </body>
</html>