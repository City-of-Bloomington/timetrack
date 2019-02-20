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
Employee,Job Title,Salary Group,Group,ID card #,Employee NW #,Change,Effective Date
<s:iterator var="row" value="jobs">"<s:property value='employee.nameLastFirst' />",<s:property value="position" />,<s:property value="salaryGroup" />,<s:property value="group" />,<s:property value="employee.id_code" />,<s:property value="employee.employee_number" />,,
</s:iterator>
	

