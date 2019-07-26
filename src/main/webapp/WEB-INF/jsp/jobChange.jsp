<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page">
	<s:form action="jobChange" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="jobTask.id" value="%{jobTask.id}" />
		<s:hidden name="jobTask.employee_id" value="%{jobTask.employee_id}" />		
		<s:hidden name="jobTask.group_id" value="%{jobTask.group_id}" />		
		<h1>Change Employee Job <s:property value="%{jobTask.employee}" /></h1>
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="%{errors}" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>
		Note: To change employee's job to another job
		<ul>
			<li> If the employee switched department, you need to change the employee
				department first, then come here and change the job and group.</li>
			<li> If the department is changed or no need to change the department continue</li>
			<li> A new job will be created to replace the selected job </li>
			<li> Pick the pay period where the pay period start date
				will be the employee new job start date. </li>
			<li> The day before the picked pay period start date will mark the expire date for the old job </li> 
			<li>It is better to pick a future pay period to avoid conflicts</li>
			<li>Pick the employee group from the list </li>
			<li>Fill the rest of fields to create the new job and click on 'Change'</li>
		</ul>
		<div class="width-one-half">
			<div class="form-group">
				<label>Position</label>
				<s:select name="jobTask.position_id" value="%{jobTask.position_id}" list="positions" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Position" required="true" />
			</div>
			<div class="form-group">
				<label>Salary Group</label>
				<s:select name="jobTask.salary_group_id" value="%{jobTask.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Salary Group" required="true" />
			</div>
			<div class="form-group">
				<label>Department</label>
				<s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Dept" id="department_id_change" />
			</div>			
			<div class="form-group">
				<label>Group</label>
				<select name="jobTask.new_group_id" value="" id="group_id_set"  disabled="disabled" >
						<option value="-1">All</option>
				</select><br />				
			</div>
      <div class="form-group">
        <label>New Job Effective Start Pay Period</label>
				<div class="date-range-picker">
					<div>					
						<s:select name="jobTask.pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Effective start pay period" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Primary Job?</label>
				<s:checkbox name="jobTask.primary_flag" value="%{jobTask.primary_flag}" fieldValue="true" />Yes
			</div>
			<div class="form-group">
				<label>Clock Time Required?</label>
				<s:checkbox name="jobTask.clock_time_required" value="%{jobTask.clock_time_required}" fieldValue="true" />Yes (Employees who are required to use punch clock)
			</div>
			<div class="form-group">
				<label>Weekly Reg Hours </label>
				<s:textfield name="jobTask.weekly_regular_hours" value="%{jobTask.weekly_regular_hours}" size="2" maxlength="2" required="true" />(normally 40)
			</div>
			<div class="form-group">
				<label>Comp Time Weekly Hours </label>
				<s:textfield name="jobTask.comp_time_weekly_hours" value="%{jobTask.comp_time_weekly_hours}" size="2" maxlength="2" required="true" />(normally 40 for non-exempt and 45 for exempt)
			</div>
			<div class="form-group">
				<label>Comp Time Multiple Factor </label>
				<s:textfield name="jobTask.comp_time_factor" value="%{jobTask.comp_time_factor}" size="3" maxlength="3" required="true" />(normally 1.5 for non-exempt and 1 for exempt)
			</div>
			<div class="form-group">
				<label>Holiday Comp Multiple Factor </label>
				<s:textfield name="jobTask.holiday_comp_factor" value="%{jobTask.holiday_comp_factor}" size="3" maxlength="3" required="true" />(normally 1.5 for non-exempt and 1 for  exempt)
			</div>			
			<div class="button-group">	
				<s:submit name="action" type="button" value="Change Job"/>		
			</div>
		</div>
	</s:form>		
</div>

<%@  include file="footer.jsp" %>


