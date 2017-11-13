<%@ taglib uri="/struts-tags" prefix="s" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
String str = (String)request.getAttribute("fileName");
response.setHeader("Content-Disposition","inline; filename="+str);
response.setContentType("application/csv");
%>
<s:property value="#reportTitle" />,,,,
Employee,Hour Code, Hours, Hourly Rate($), Amount($)
<s:iterator var="row" value="#hoursSums"><s:set var="code" value="#row.key" /><s:set var="hours" value="#row.value" /><s:iterator var="row2" value="#mapEntries"><s:set var="code2" value="#row2.key" /><s:set var="entries" value="#row2.value" /><s:if test="#code == #code2"><s:iterator var="entry" value="#entries"><s:property value="#entry.fullname" />,<s:property value="#entry.code" />,<s:property value="#entry.hoursStr" />,$<s:property value="#entry.hourlyRate" />,$<s:property value="#entry.amountStr" />
</s:iterator></s:if></s:iterator>,Sub Total,<s:property value="#hours" />,,<s:iterator var="row3" value="#amountsSums"><s:set var="code3" value="#row3.key" /><s:set var="amount" value="#row3.value" /><s:if test="#code == #code3">$<s:property value="#amount" />
</s:if></s:iterator></s:iterator>,Total,<s:property value="#totalHours" />,,$<s:property value="#totalAmount" />

