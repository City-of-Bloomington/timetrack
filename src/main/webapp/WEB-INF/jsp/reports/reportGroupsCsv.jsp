
<%@ taglib uri="/struts-tags" prefix="s" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
String str = (String)request.getAttribute("fileName");
response.setHeader("Content-Disposition","inline; filename="+str);
response.setContentType("application/csv");
%>
Group, Employee, Job, Salary Group, Weekly hrs, Comp Weekly Hrs\n
<s:iterator var="one" value="#entries"><s:iterator var="row" value="#one" status="status">"<s:property  />"<s:if test="!#status.last">,</s:if></s:iterator>
</s:iterator>
Group,Approvers,Payroll Approvers\n
<s:iterator var="one" value="#entries2"><s:iterator var="row" value="#one" status="status2">"<s:property  />"<s:if test="!#status2.last">,</s:if></s:iterator>
</s:iterator>





