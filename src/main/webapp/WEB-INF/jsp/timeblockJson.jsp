<%@ taglib uri="/struts-tags" prefix="s" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
response.setContentType("application/json");
%>
<s:if test="hasErrors()">{"errors":[
	<s:iterator value="errors" status="row">
		"<s:property />"<s:if test="!%{#row.last}">,</s:if>
	</s:iterator>]}</s:if>
	<s:else>{"sucess":"success"}</s:else>

