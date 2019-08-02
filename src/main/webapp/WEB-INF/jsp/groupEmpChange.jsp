<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page">
	
<s:form action="groupEmpChange" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="groupEmployee.employee_id" value="%{groupEmployee.employee_id}" />
	<s:hidden id="groupEmployee.id" name="groupEmployee.id" value="%{groupEmployee.id}" />
	<s:hidden id="groupEmployee.group_id" name="groupEmployee.group_id" value="%{groupEmployee.group_id}" />			
	<h1>Change Employee Group </h1>
	<s:if test="hasMessages()">
		<s:set var="messages" value="%{messages}" />			
		<%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
		<s:set var="errors" value="%{errors}" />			
		<%@ include file="errors.jsp" %>
	</s:elseif>		
	<ul>
		<li>If you want to change employee group to another one in the same department only</li>
		<li>If the employee is changing department then do not use this page.  Add the employee to the new department, click on department employee link to add to a new group. Then expire the old group(s) and job(s).</li>
		<li>When an employee group is changed to another one,
			the old group will be set to expire on the day before the effective date 
			of the new group.</li>
		<li>All group related jobs will be expired as well to the same expire group date above.</li>
		<li>Then you may need to add new job(s) to the new group</li>
	</ul>
	<div class="width-one-half">
		<div class="form-group">
		<label>Employee</label>
		<div><a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='groupEmployee.employee_id' />" /><s:property value="%{groupEmployee.employee}" /></a></div>
	</div>			
		<div class="form-group">
		<label>Old Group</label>
		<div><s:property value="groupEmployee.group" /></div>
	</div>
	<div class="form-group">
		<label>New Group</label>	
			<div><s:select name="groupEmployee.new_group_id" value="%{groupEmployee.new_group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick A Group" /></div>
	</div>
	<div class="form-group">
		<label>New Group Start Date</label>
		<div><s:select name="groupEmployee.effective_date" value="" list="payPeriods" listKey="startDate" listValue="startDate" headerKey="-1" headerValue="Effective start date" />(Start of a pey period date)
    </div>
	</div>
	<div class="button-group">	
		<s:submit name="action" type="button" value="Change Group" class="button"/>
	</div>
</s:form>
<%@  include file="footer.jsp" %>


