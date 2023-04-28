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
		Note: To change employee job to another job, such as promoted, moved to another department, etc.
		<ul>
			<li> A new job will be created to replace the selected job </li>
			<li> Pick the department (if different from existing one) </li>
			<li> Pick the group (if different from existing one) </li>			
			<li> Pick the new job start date (pay period start date)</li>
			<li> The old job will be set to expire to the preceding payperiod end date. </li> 
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
        <label>New Job Start Date</label>
				<div class="date-range-picker">
					<div>					
						<s:select name="jobTask.effective_date" value="" list="payPeriods" listKey="startDate" listValue="startDate" headerKey="-1" headerValue="Pick Start Date" /> (Start of pay period)
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Primary Job?</label>
				<s:checkbox name="jobTask.primary_flag" value="%{jobTask.primary_flag}" fieldValue="true" />Yes
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
			<div class="form-group">
				<label>Irregular Work Days?</label>
				<s:checkbox name="jobTask.irregularWorkDays" value="%{jobTask.irregularWorkDays}" fieldValue="true" id="irregular_work_days" />Yes (Employee may required to work on Saturday and/or Sunday)
			</div>
			<div class="button-group">	
				<s:submit name="action" type="button" value="Change Job"/>		
			</div>
		</div>
	</s:form>		
</div>

<%@  include file="footer.jsp" %>


