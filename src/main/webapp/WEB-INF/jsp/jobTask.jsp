<%@ include file="header.jsp" %>
<div class="internal-page">
<s:form action="jobTask" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="employee_number" id="employee_number" value="%{employee_number}" />
	<s:hidden name="effective_date" value="%{effective_date}" />	
	<s:if test="isDeptSpecified()">
		<s:hidden id="jobTask.department_id" name="jobTask.department_id" value="%{dept_id}" />
	</s:if>
	<h1>New job</h1>
	<s:if test="hasMessages()">
		<s:set var="messages" value="messages" />		
		<%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
		<s:set var="errors" value="errors" />		
		<%@ include file="errors.jsp" %>
	</s:elseif>
	<s:if test="jobTask.employee_id == ''">
		<p>Employee Info is needed to create a job, start from employee page</p>
	</s:if>
	<s:else>
		<s:hidden id="jobTask.employee_id" name="jobTask.employee_id" value="%{jobTask.employee_id}" />
		<div class="width-one-half">
			<div class="form-group">
				<label>Employee</label>
				<a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='%{jobTask.employee_id}' />"> <s:property value="%{jobTask.employee}" /></a>
			</div>
			<s:if test="hasShortListPositions()">
				<div class="form-group">
					<label>Position</label>
					<s:select name="jobTask.position_id" value="" list="shortListPositions" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Position" /> (list of positions from NW) if the needed position is not in the list use 'Alt Position' to pick from instead
				</div>
				<div class="form-group">
					<label>Alt Position</label>
					<s:select name="jobTask.position_id_alt" value="" list="positions" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Position" /> 
				</div>
			</s:if>
			<s:else>
				<div class="form-group">
					<label>Position</label>
					<s:select name="jobTask.position_id" value="%{jobTask.position_id}" list="positions" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Position" required="true" />
				</div>
			</s:else>			
			<div class="form-group">
				<label>Salary Group</label>
				<s:select name="jobTask.salary_group_id" value="%{jobTask.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Salary Group" required="true" id="job_salary_group_change" />
			</div>
			<s:if test="isDeptSpecified()">
				<div class="form-group">
					<label>Group</label>
					<s:if test="hasGroups()">
						<s:select name="jobTask.group_id" value="%{jobTask.group_id}" list="groups"  listKey="id" listValue="name" headerKey="-1" headerValue="Pick a group" />
					</s:if>
				</div>
			</s:if>
			<s:else>
				<div class="form-group">
					<label>Department</label>
					<s:select name="jobTask.department_id" value="" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" id="department_id_change" />
				</div>
				<div class="form-group">
					<label>Group</label>
					<select name="jobTask.group_id" value="" id="group_id_set"  disabled="disabled"/>
					<option value="-1">Pick a group</option>
				   </select>(To pick a group you need to pick a department first)
				</div>
			</s:else>
			<div class="form-group">
				<label>Effective Date</label>
				<div class="date-range-picker">
					<div>
						<s:select name="jobTask.effective_date" value="%{effective_date}" list="payPeriods" listKey="startDate" listValue="startDate" headerKey="-1" headerValue="Pick Start Date" /> (Start pay period date)
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Expire Date</label>
				<div class="date-range-picker">
					<div>
						<s:select name="jobTask.expire_date" value="" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick Expire Date" /> (End pay period date)	
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Primary Job?</label>
				<s:checkbox name="jobTask.primary_flag" value="%{jobTask.primary_flag}" fieldValue="true" />Yes
			</div>
			<div class="form-group">
				<label>Clock Time Required?</label>
				<s:checkbox name="jobTask.clock_time_required" value="%{jobTask.clock_time_required}" fieldValue="true" id="clock_required_id" />Yes (Employees who are required to use punch clock)
			</div>
			<div class="form-group">
				<label>Weekly Reg Hours </label>
				<s:textfield name="jobTask.weekly_regular_hours" value="%{jobTask.weekly_regular_hours}" size="3" maxlength="3" required="true" id="weekly_hrs_id" />(normally 40)
			</div>
			<div class="form-group">
				<label>Comp Time Weekly Hours </label>
				<s:textfield name="jobTask.comp_time_weekly_hours" value="%{jobTask.comp_time_weekly_hours}" size="3" maxlength="3" required="true" />(normally 40 for non-exempt)
			</div>
			<div class="form-group">
				<label>Comp Time Multiple Factor </label>
				<s:textfield name="jobTask.comp_time_factor" value="%{jobTask.comp_time_factor}" size="3" maxlength="3" required="true" id="comp_factor_id" />(normally 1.5 for non-exempt and 1 for exempt)
			</div>
			<div class="form-group">
				<label>Holiday Comp Multiple Factor </label>
				<s:textfield name="jobTask.holiday_comp_factor" value="%{jobTask.holiday_comp_factor}" size="3" maxlength="3" required="true" id="holiday_factor_id" />(normally 1.5 for non-exempt and 1 for  exempt)
			</div>
		</div>
		<div class="button-group">
			<s:if test="hasEmployeeNumber() && !isEmployeeFound()">
				<s:submit name="action" type="button" value="Find Employee Info" class="button"/>
			</s:if>
			<s:submit name="action" type="button" value="Save" class="button"/>
		</div>
	</s:else>
</div>
</s:form>
<s:if test="hasJobs()">
	<s:set var="jobTasks" value="jobs" />
	<s:set var="jobTasksTitle" value="'Employee Active Jobs'" />
	<%@ include file="jobTasks.jsp" %>	
</s:if>
<%@ include file="footer.jsp" %>
