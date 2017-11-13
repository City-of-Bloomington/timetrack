<%@ taglib uri="/struts-tags" prefix="s" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
String str = (String)request.getAttribute("csvFileName");
response.setHeader("Content-Disposition","inline; filename="+str);
response.setContentType("application/csv");
%>
<s:set var="line" value="',,,,,,,,,,\n'" /><s:iterator var="one" value="processes"><s:property value="employee.employee_number" />,<s:property value="twoWeekNetRegular" />,<s:property value="regCode" />,<s:property value="payPeriod.end_date" />,<s:property value="#line" /><s:iterator var="code" value="all"><s:property value="employee.employee_number" />,<s:property value="#code.value" />,<s:property value="#code.key" />,<s:property value="payPeriod.end_date" />,<s:property value="#line" /></s:iterator></s:iterator>

