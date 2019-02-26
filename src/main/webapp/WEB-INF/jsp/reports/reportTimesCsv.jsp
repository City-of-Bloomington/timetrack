
<%@ taglib uri="/struts-tags" prefix="s" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
String str = (String)request.getAttribute("fileName");
response.setHeader("Content-Disposition","inline; filename="+str);
response.setContentType("application/csv");
%>
<s:iterator var="one" value="arrAll"><s:iterator var="two" value="#one" status="status">"<s:property  />"<s:if test="!#status.last">,</s:if></s:iterator>
</s:iterator>





