<%@ taglib uri="/struts-tags" prefix="s" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
String str = (String)request.getAttribute("fileName");
response.setHeader("Content-Disposition","inline; filename="+str);
response.setContentType("application/csv");
%>
<s:property value="jobsTitle" />,,,,
Job ID,Employee,Job Title,Salary Group,Group
<s:iterator var="row" value="jobs"><s:property value="id" />,<s:property value="employee" />,<s:property value="position" />,<s:property value="salaryGroup" />,<s:property value="group" />
</s:iterator>
	

