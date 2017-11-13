<%@ taglib uri="/struts-tags" prefix="s" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
String str = (String)request.getAttribute("csvFileName");
response.setHeader("Content-Disposition","inline; filename="+str);
response.setContentType("application/csv");
%>
<s:set var="line" value="',,,,'" /><s:set var="line2" value="',,,,,\n'" /><s:iterator var="one" value="processes"><s:if test="hasHandHash()"><s:iterator var="codes" value="twoWeekHandHash"><s:set var="codeKey" value="#codes.key" /><s:set var="hoursValue" value="#codes.value" /><s:property value="employee.employee_number" />,<s:property value="hoursValue" />,<s:property value="#codeKey.nw_code" />,<s:property value="payPeriod.end_date" />,<s:property value="#line" />,<s:property value="#codeKey.gl_value" />,<s:property value="#line2" /></s:iterator></s:if></s:iterator>

