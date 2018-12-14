<%@ include file="header.jsp" %>
<div class="internal-page">
	<h1>Employee: <s:property value="employee.full_name" /></h1>
	<s:if test="hasMessages()">
		<s:set var="messages" value="%{messages}" />
		<%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
		<s:set var="errors" value="%{errors}" />
		<%@ include file="errors.jsp" %>
	</s:if>		
	<div class="width-one-half">
		<label>ID: </label>
		<s:property value="employee.id" /> <br /><br />
		
		<label>Username: </label>
		<s:property value="employee.username" /> <br /> <br />
		
		<label>First Name: </label>
		<s:property value="employee.first_name" /> <br /> <br />
		
		<label>Last Name: </label>
		<s:property value="employee.last_name" /> <br /><br />
		
		<label>ID Code #: </label>
		<s:property value="employee.id_code" /> <br /><br />

		<label>Employee #: </label>
		<s:property value="employee.employee_number" /> <br /><br />
		
		<label>Email: </label>
		<s:property value="employee.email" /> <br /><br />
		
		<s:if test="employee.hasDepartment()">
			<label>Department: </label>
			<s:property value="employee.department" /> <br /><br />
		</s:if>
		
		<label>Effective Date: </label>
		<s:property value="employee.effective_date" /> <br /><br />
		
		<label>Role: </label>
		<s:property value="employee.role" /> <br /><br />
		
		<label>Active? </label>
		<s:if test="employee.inactive"> No </s:if><s:else>Yes</s:else> <br /><br />
	</div>		
	<div class="button-group">
		<a href="<s:property value='#application.url' />employee.action?action=Edit&id=<s:property value='id' />" class="button">Edit</a> 
		<s:if test="!employee.hasDepartment()">
			<a href="<s:property value='#application.url' />departmentEmployee.action?employee_id=<s:property value='employee.id' />" class="button">Add Employee to Department</a>
		</s:if>
		<s:else>
			<s:if test="!employee.hasActiveGroup()">
				<a href="<s:property value='#application.url' />groupEmployee.action?employee_id=<s:property value='employee.id' />&department_id=<s:property value='employee.department_id' />" class="button"> Add Employee to a Group</a>
				</s:if>
				<s:if test="employee.hasNoJob()">
					<a href="<s:property value='#application.url' />jobTask.action?add_employee_id=<s:property value='employee.id' />" class="button"> Add A Job</a>
				</s:if>
		</s:else>
	</div>
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
		<s:if test="employee.canPayrollProcess()">
			<s:set var="groupManagers" value="%{employee.processors}" />
			<s:set var="groupManagersTitle" value="'Payroll Approver of Groups '" />
			<%@ include file="groupManagers.jsp" %>
		</s:if>			
		<s:if test="employee.canApprove()">
			<s:set var="groupManagers" value="%{employee.approvers}" />
			<s:set var="groupManagersTitle" value="'Approver of Groups '" />
			<%@ include file="groupManagers.jsp" %>
		</s:if>
		<s:if test="employee.canReview()">
			<s:set var="groupManagers" value="%{employee.reviewers}" />
			<s:set var="groupManagersTitle" value="'Review of Groups'" />
			<%@ include file="groupManagers.jsp" %>
		</s:if>
		<s:if test="employee.canMaintain()">
			<s:set var="groupManagers" value="%{employee.enterors}" />
			<s:set var="groupManagersTitle" value="'Maintain of Groups'" />
			<%@ include file="groupManagers.jsp" %>
		</s:if>					
	</s:if>
	
</div>
<%@ include file="footer.jsp" %>
