<%@ include file="header.jsp" %>
<div class="internal-page">
<s:form action="jobTask" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h1>Edit Job: <s:property value="%{jobTask.name}" /></h1>
	<s:hidden id="jobTask.id" name="jobTask.id" value="%{jobTask.id}" />
	<s:hidden name="jobTask.hourlyRate" value="%{jobTask.hourlyRate}" />
	<s:hidden name="jobTask.group_id" value="%{jobTask.group_id}" />
	<s:hidden id="jobTask.employee_id" name="jobTask.employee_id" value="%{jobTask.employee_id}" />		
	<s:if test="hasMessages()">
		<s:set var="messages" value="messages" />		
		<%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
		<s:set var="errors" value="errors" />		
		<%@ include file="errors.jsp" %>
	</s:elseif>
	<div class="width-one-half">
		<div class="form-group">
			<label>ID</label>
			<s:property value="jobTask.id" />
		</div>
		<div class="form-group">
			<label>Employee</label>
			<a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='%{jobTask.employee_id}' />"> <s:property value="%{jobTask.employee}" /></a>
		</div>
		<div class="form-group">
			<label>Position</label>
			<s:select name="jobTask.position_id" value="%{jobTask.position_id}" list="positions" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Position" required="true" />
		</div>
		<div class="form-group">
			<label>Salary Group</label>
			<s:select name="jobTask.salary_group_id" value="%{jobTask.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Salary Group" required="true" id="job_salary_group_change" />
		</div>
		<div class="form-group">
			<label>Group</label>
			<s:if test="jobTask.hasGroup()">
				<s:property value="jobTask.group" />
			</s:if>
			<s:elseif test="jobTask.hasAllGroups()">
				<s:select name="jobTask.group_id" value="%{jobTask.group_id}" list="jobTask.allGroups"  listKey="id" listValue="name" headerKey="-1" headerValue="Pick a group" />
			</s:elseif>
		</div>
		<div class="form-group">
			<label>Effective Date</label>
			<div class="date-range-picker">
				<div>
					<s:textfield name="jobTask.effective_date" value="%{jobTask.effective_date}" size="10" cssClass="date" /> 
				</div>
			</div>
		</div>
		<div class="form-group">
			<label>Expire Date</label>
			<div class="date-range-picker">
				<div>
					<s:if test="jobTask.hasExpireDate()">
						<s:textfield name="jobTask.expire_date" value="%{jobTask.expire_date}" size="10" cssClass="date" />
					</s:if>
					<s:else>
						<s:select name="jobTask.expire_date" value="" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick Expire Date" /> (End pay period date)	
					</s:else>
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
	<div class="form-group">
		<label>Added Date </label>
		<s:property value="jobTask.added_date" />
	</div>
	<div class="form-group">
		<label>Inactive?</label>
		<s:checkbox name="jobTask.inactive" value="%{jobTask.inactive}" fieldValue="true" />Yes
	</div>
	<div class="button-group">	
		<s:submit name="action" type="button" value="Save Changes" class="button"/>
		<a href="<s:property value='#application.url' />jobTask.action?add_employee_id=<s:property value='jobTask.employee_id' />&effective_date=<s:property value='jobTask.effective_date' />" class="button">Add Another Job</a>
	</div>
</div>
</s:form>

<%@ include file="footer.jsp" %>
