<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="employee" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="employee.id == ''">
			<h1>New Employee</h1>
			<s:hidden name="employee.group_id" value="" id="group_id" />
		</s:if>
		<s:else>
			<h1>Employee <s:property value="employee.full_name" /></h1>
			<s:hidden name="employee.id" value="%{employee.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

	  <div class="width-one-half">
			<s:if test="employee.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="%{employee.id}" />
				</div>
			</s:if>

			<s:if test="employee.id == ''">
				<div class="form-group">
					<label>Full Name</label>
					<s:textfield name="employee.full_name" value="" size="30" maxlength="70" id="emp_name" /><br /> Start typing employee last name to pick from the list
				</div>
			</s:if>

			<div class="form-group">
				<label>Username</label>
				<s:textfield name="employee.username" size="10" value="%{employee.username}" required="true" id="username_id" />
			</div>

			<div class="form-group">
				<label>First Name </label>
				<s:textfield name="employee.first_name" value="%{employee.first_name}" size="30" maxlength="70" required="true" id="first_name_id" />
			</div>

			<div class="form-group">
				<label>Last Name </label>
				<s:textfield name="employee.last_name" value="%{employee.last_name}" size="30" maxlength="70" required="true" id="last_name_id" />
			</div>

			<div class="form-group">
				<label>ID Code # </label>
				<s:textfield name="employee.id_code" value="%{employee.id_code}" size="10" maxlength="10" id="id_code_id" /><br />(The number on City ID)
			</div>

			<div class="form-group">
				<label>Employee # </label>
				<s:textfield name="employee.employee_number" value="%{employee.employee_number}" size="15" maxlength="15" id="employee_number_id" /><br />(from new world)
			</div>

			<div class="form-group">
				<label>Email</label>
				<s:textfield name="employee.email" size="30" value="%{employee.email}" id="email_id" />
			</div>

			<s:if test="employee.id == ''">
				<div class="form-group">
					<label>Department</label>
					<s:select name="employee.department_id" value="" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" id="department_id" />Group ID: <span id="group_id2">&nbsp;</span>
				</div>

				<div class="form-group">
					<!-- butcherad: we need an input group style for such -->
					<label>Effective Date</label>
					<s:textfield name="employee.effective_date" value="" size="10" maxlength="10" cssClass="date" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Role</label>
				<s:select name="employee.role" value="%{employee.role}" list="#{'Employee':'Employee','Admin':'Admin'}" />
			</div>

			<div class="form-group">
				<label>Inactive ?</label>
				<s:checkbox name="employee.inactive" value="%{employee.inactive}" /> Yes (check to disable)
			</div>

			<s:if test="employee.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>

			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url' />employee.action" class="button">New Employee</a>
					<s:if test="!employee.hasDepartment()">
						<a href="<s:property value='#application.url' />departmentEmployee.action?employee_id=<s:property value='employee.id' />" class="button">Add Employee to Department</a>
					</s:if>
					<s:if test="!employee.hasActiveGroup()">
						<a href="<s:property value='#application.url' />groupEmployee.action?employee_id=<s:property value='employee.id' />&department_id=<s:property value='employee.department_id' />" class="button"> Add Employee to a Group</a>
					</s:if>
					<s:if test="employee.hasNoJob()">
						<a href="<s:property value='#application.url' />jobTask.action?add_employee_id=<s:property value='employee.id' />" class="button"> Add A Job</a>
					</s:if>
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>

	<s:if test="employee.id == ''">
		<s:if test="employees != null && employees.size() > 0">
			<s:set var="employees" value="%{employees}" />
			<s:set var="employeesTitle" value="employeesTitle" />
			<%@ include file="employees.jsp" %>
		</s:if>
	</s:if>

	<s:else>
		<s:if test="employee.hasDepartment()">
			<s:set var="departmentEmployees" value="%{employee.departmentEmployees}" />
			<s:set var="departmentEmployeesTitle" value="'Employee Department'" />
			<%@ include file="departmentEmployees.jsp" %>

			<s:if test="employee.hasGroup()">
				<s:set var="groupEmployees" value="%{employee.groupEmployees}" />
				<s:set var="groupEmployeesTitle" value="'Employee Group'" />
				<%@ include file="groupEmployees.jsp" %>
			</s:if>

			<s:if test="employee.hasJobs()">
				<s:set var="jobTasks" value="%{employee.jobs}" />
				<s:set var="jobTasksTitle" value="'Employee Jobs'" />
				<%@ include file="jobTasks.jsp" %>
			</s:if>
		</s:if>
	</s:else>
</div>
<%@ include file="footer.jsp" %>