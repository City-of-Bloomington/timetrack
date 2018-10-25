<%@ taglib uri="/struts-tags" prefix="s" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
String str = (String)request.getAttribute("csvFileName");
response.setHeader("Content-Disposition","inline; filename="+str);
response.setContentType("application/csv");
%>
<s:set var="line" value="',,,,,,,,,,'" /><s:set var="line2" value="',,,,,,,,,,'"/><s:iterator var="one" value="processes"><s:property value="employee.employee_number" />,<s:property value="twoWeekNetRegular" />,<s:property value="regCode" />,<s:property value="payPeriod.end_date" />,<s:if test="hasMultipleJobs()"><s:property value="#line2" /><s:property value="job_name" /></s:if><s:else><s:property value="#line" /></s:else>
<s:iterator var="code" value="all2"><s:iterator var="valst" value="#code.value"><s:property value="employee.employee_number" />,<s:property />,<s:property value="#code.key" />,<s:property value="payPeriod.end_date" />,<s:if test="hasMultipleJobs()"><s:property value="#line2" /><s:property value="job_name"/></s:if><s:else><s:property value="#line" /></s:else>
</s:iterator></s:iterator></s:iterator>

