<%@ taglib uri="/struts-tags" prefix="s" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
String str = (String)request.getAttribute("fileName");
response.setHeader("Content-Disposition","inline; filename="+str);
response.setContentType("application/csv");
%><s:set var="mapEntries" value="report.mapEntries" /><s:set var="hoursSums" value="report.hoursSums" /><s:set var="totalHours" value="report.totalHours" /><s:set var="dailyEntries" value="report.dailyEntries" />		
<s:property value="reportTitle" />,,,,
"Hours Classified by Employeee, Date, Earn Codes",,,,
Employee,Employee Number,Date,Earn Code,Hours
<s:iterator var="row" value="#dailyEntries"><s:property value="#row.fullname" /> ,<s:property value="#row.empNum" />,<s:property value="#row.date" />,<s:property value="#row.code" />,<s:property value="#row.hoursStr" />
</s:iterator>
	

