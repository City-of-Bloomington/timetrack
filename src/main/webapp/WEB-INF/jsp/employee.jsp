<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="employee" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="emp_id == ''">
			<h1>New Employee</h1>
		</s:if>
		<s:else>
			<h1>Employee <s:property value="emp.full_name" /></h1>
			<s:hidden name="emp.id" value="%{emp.id}" />
			<s:if test="!user.isAdmin()">
				<s:hidden name="emp.rolesText" value="%{emp.rolesText}" />
			</s:if>
		</s:else>
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:if test="hasErrors()">
			<s:set var="errors" value="%{errors}" />
			<%@ include file="errors.jsp" %>
		</s:if>		
	  <div class="width-one-half">
			<s:if test="emp.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="%{emp.id}" />
				</div>
			</s:if>
			<s:if test="emp.id == ''">
				<div class="form-group">
					<label>Search by Full Name or ID Code</label>
					<s:textfield name="emp.full_name" value="" size="30" maxlength="70" id="emp_name" /><br /> Start typing employee last name or ID code then pick from the list
				</div>
			</s:if>
			<div class="form-group">
				<label>Username</label>
				<s:textfield name="emp.username" size="10" value="%{emp.username}" required="true" id="username_id" />
			</div>

			<div class="form-group">
				<label>First Name </label>
				<s:textfield name="emp.first_name" value="%{emp.first_name}" size="30" maxlength="70" required="true" id="first_name_id" />
			</div>

			<div class="form-group">
				<label>Last Name </label>
				<s:textfield name="emp.last_name" value="%{emp.last_name}" size="30" maxlength="70" required="true" id="last_name_id" />
			</div>

			<div class="form-group">
				<label>ID Code # </label>
				<s:textfield name="emp.id_code" value="%{emp.id_code}" size="10" maxlength="10" id="id_code_id" /><br />(The number on City ID)
			</div>

			<div class="form-group">
				<label>Employee # </label>
				<s:textfield name="emp.employee_number" value="%{emp.employee_number}" size="15" maxlength="15" id="employee_number_id" /><br />(from new world)
			</div>

			<div class="form-group">
				<label>Email</label>
				<s:textfield name="emp.email" size="30" value="%{emp.email}" id="email_id" />
			</div>
			<s:if test="emp.id == ''">
				<div class="form-group">
					<label>Effective Date</label>
					<div class="date-range-picker">
						<div>									
							<s:textfield name="emp.effective_date" size="10" value="" cssClass="date"  required="true" />
						</div>
					</div>
				</div>
			</s:if>
			<s:if test="emp.id == ''">
				<div class="form-group">
					<label>Department</label>
					<s:select name="emp.department_id" value="" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" id="department_id_change" />
				</div>
				<div class="form-group">
					<label>Group</label>
					<select name="emp.group_id" value="" id="group_id_set"  disabled="disabled"/>
					<option value="-1">Pick a group</option>
				</select>(To pick a group you need to pick a department first)					
				</div>
			</s:if>				
			<s:else>
				<div class="form-group">
					<label>Added Date</label>
					<s:property value="emp.added_date" />
				</div>
			</s:else>
			<div class="form-group">
				<label>Roles</label>
				<s:if test="user.isAdmin()">
					<s:checkboxlist key="emp.roles" list="roles" />
				</s:if>
				<s:else>
					<s:property value="emp.rolesText" />
				</s:else>
			</div>
			<div class="form-group">
				<label>Inactive ?</label>
				<s:checkbox name="emp.inactive" value="%{emp.inactive}" /> Yes (check to disable)
			</div>

			<s:if test="emp.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>

			<s:else>
				<div class="button-group">
					<s:submit name="action" type="button" value="Save Changes" class="button"/>					
					<a href="<s:property value='#application.url' />employee.action" class="button">New Employee</a>
					<s:if test="!emp.hasDepartments()">
						<a href="<s:property value='#application.url' />departmentEmployee.action?emp_id=<s:property value='emp.id' />" class="button">Add Employee to Department</a>
					</s:if>
					<s:if test="!emp.hasGroupEmployees()">
						<a href="<s:property value='#application.url' />groupEmployee.action?emp_id=<s:property value='emp.id' />&department_id=<s:property value='emp.department_id' />" class="button"> Add Employee to a Group</a>
					</s:if>
					<s:else>
						<a href="<s:property value='#application.url' />groupEmployee.action?emp_id=<s:property value='emp.id' />&department_id=<s:property value='emp.department_id' />" class="button"> Add Employee to Another Group</a>
					</s:else>
					<s:if test="emp.hasNoJob()">
						<a href="<s:property value='#application.url' />jobTask.action?add_employee_id=<s:property value='emp.id' />" class="button"> Add A Job</a>
					</s:if>
					<s:else>
						<a href="<s:property value='#application.url' />jobTask.action?add_employee_id=<s:property value='emp.id' />" class="button"> Add Another Job</a>
						<s:if test="emp.hasOneJobOnly()">
							<a href="<s:property value='#application.url' />jobChange.action?related_employee_id=<s:property value='emp.id' />&id=<s:property value='emp.job.id' />" class="button">Change Job</a>
						</s:if>
						<s:else>
							<a href="<s:property value='#application.url' />jobChange.action?related_employee_id=<s:property value='emp.id' />" class="button">Change Job</a>
							<a href="<s:property value='#application.url' />jobTimeChange.action?related_employee_id=<s:property value='emp.id' />" class="button">Transfer Job Times</a>							
						</s:else>
					</s:else>
				</div>
			</s:else>
		</div>
	</s:form>
	<s:if test="emp.id != ''">
		<s:if test="emp.hasDepartments()">
			<s:set var="departmentEmployees" value="%{emp.departmentEmployees}" />
			<s:set var="departmentEmployeesTitle" value="'Employee Department'" />
			<%@ include file="departmentEmployees.jsp" %>
			<s:if test="emp.hasGroupEmployees()">
				<s:set var="groupEmployees" value="%{emp.groupEmployees}" />
				<s:set var="groupEmployeesTitle" value="'Employee Group'" />
				<%@ include file="groupEmployees.jsp" %>
			</s:if>
			<s:if test="emp.hasAllJobs()">
				<s:set var="jobTasks" value="%{emp.allJobs}" />
				<s:set var="jobTasksTitle" value="'Employee Jobs'" />
				<%@ include file="jobTasks.jsp" %>
			</s:if>
			<s:if test="emp.canPayrollProcess()">
				<s:set var="groupManagers" value="%{emp.processors}" />
				<s:set var="groupManagersTitle" value="'Payroll Approver of Groups '" />
				<%@ include file="groupManagers.jsp" %>
			</s:if>			
			<s:if test="emp.canApprove()">
				<s:set var="groupManagers" value="%{emp.approvers}" />
				<s:set var="groupManagersTitle" value="'Approver of Groups '" />
				<%@ include file="groupManagers.jsp" %>
			</s:if>
			<s:if test="emp.canReview()">
				<s:set var="groupManagers" value="%{emp.reviewers}" />
				<s:set var="groupManagersTitle" value="'Review of Groups'" />
				<%@ include file="groupManagers.jsp" %>
			</s:if>
			<s:if test="emp.canMaintain()">
				<s:set var="groupManagers" value="%{emp.enterors}" />
				<s:set var="groupManagersTitle" value="'Maintain of Groups'" />
				<%@ include file="groupManagers.jsp" %>
			</s:if>			
		</s:if>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
