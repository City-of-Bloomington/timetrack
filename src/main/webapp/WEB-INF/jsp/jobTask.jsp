<%@ include file="header.jsp" %>
<div class="internal-page">
<s:form action="jobTask" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="jobTask.id == ''">
		<s:if test="jobTask.employee_id != ''">
			<s:hidden id="jobTask.employee_id" name="jobTask.employee_id" value="%{jobTask.employee_id}" />			
		</s:if>
		<h1>New job</h1>
		<s:if test="jobTask.employee_id == ''">
			<p>Employee Info is needed to create a job, start from employee page</p>
		</s:if>
	</s:if>
	<s:else>
		<h1>Edit Job: <s:property value="%{jobTask.name}" /></h1>
		<s:hidden id="jobTask.id" name="jobTask.id" value="%{jobTask.id}" />
		<s:hidden name="jobTask.hourlyRate" value="%{jobTask.hourlyRate}" />
		<s:hidden name="jobTask.group_id" value="%{jobTask.group_id}" />
		<s:hidden id="jobTask.employee_id" name="jobTask.employee_id" value="%{jobTask.employee_id}" />		
	</s:else>
	<s:if test="hasMessages()">
		<s:set var="messages" value="messages" />		
		<%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
		<s:set var="errors" value="errors" />		
		<%@ include file="errors.jsp" %>
	</s:elseif>
	<div class="width-one-half">
		<s:if test="jobTask.id != ''">
			<div class="form-group">
				<label>ID</label>
				<s:property value="jobTask.id" />
			</div>
		</s:if>
		<div class="form-group">
			<label>Employee</label>
			<s:if test="jobTask.employee_id == ''">
				<s:textfield name="jobTask.name" value="" size="30" id="employee_name" />Employee ID <s:textfield name="jobTask.employee_id" value="%{jobTask.employee_id}" size="10" id="employee_id" />
			</s:if>
			<s:else>
				<a href="<s:property value='#application.url' />employee.action?id=<s:property value='%{jobTask.employee_id}' />"> <s:property value="%{jobTask.employee}" /></a>
			</s:else>
		</div>
		<div class="form-group">
			<label>Position</label>
			<s:select name="jobTask.position_id" value="%{jobTask.position_id}" list="positions" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Position" required="true" />
		</div>

		<div class="form-group">
			<label>Salary Group</label>
			<s:select name="jobTask.salary_group_id" value="%{jobTask.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Salary Group" required="true" id="job_salary_group_change" />
		</div>
		<s:if test="jobTask.employee_id == ''">
			<div class="form-group">
				<label>Department</label>
				<s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Dept" id="department_id_change" />
			</div>
			<div class="form-group">
				<label>Group</label>
				<select name="jobTask.group_id" value="" id="group_id_set"  disabled="disabled" >
						<option value="-1">All</option>
					</select><br />
			</div>
		</s:if>
		<s:else>
			<div class="form-group">
				<label>Group</label>
				<s:if test="jobTask.hasGroup()">
					<s:property value="jobTask.group" />
				</s:if>
				<s:elseif test="jobTask.hasAllGroups()">
					<s:select name="jobTask.group_id" value="%{jobTask.group_id}" list="jobTask.allGroups"  listKey="id" listValue="name" headerKey="-1" headerValue="Pick a group" />
				</s:elseif>
			</div>
		</s:else>
		<div class="form-group">
			<label>Effective Date</label>
			<s:textfield name="jobTask.effective_date" value="%{jobTask.effective_date}" size="10" cssClass="date" />
		</div>

		<div class="form-group">
			<label>Expire Date</label>
			<s:textfield name="jobTask.expire_date" value="%{jobTask.expire_date}" size="10" cssClass="date" />
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
	<s:if test="jobTask.id != ''">
		<div class="form-group">
			<label>Added Date </label>
			<s:property value="jobTask.added_date" />
		</div>
		<div class="form-group">
			<label>Inactive?</label>
			<s:checkbox name="jobTask.inactive" value="%{jobTask.inactive}" fieldValue="true" />Yes
		</div>
	</s:if>
	<s:if test="jobTask.id == ''">
		<s:submit name="action" type="button" value="Save" class="button"/>
	</s:if>
		<s:else>
			<div class="button-group">
				<a href="<s:property value='#application.url' />jobTask.action" class="button">New Job</a>
				<s:submit name="action" type="button" value="Save Changes" class="button"/>
			</div>
		</s:else>
		<a href="<s:property value='#application.url' />searchJobs.action" class="button">Search Jobs</a>
	</div>
</s:form>

<%@ include file="footer.jsp" %>
