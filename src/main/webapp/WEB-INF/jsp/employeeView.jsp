<%@ include file="header.jsp" %>
<div class="internal-page">
	<h1>Employee: <s:property value="emp.full_name" /></h1>
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
		<s:property value="emp.id" /> <br /><br />
		
		<label>Username: </label>
		<s:property value="emp.username" /> <br /> <br />
		
		<label>First Name: </label>
		<s:property value="emp.first_name" /> <br /> <br />
		
		<label>Last Name: </label>
		<s:property value="emp.last_name" /> <br /><br />
		
		<label>ID Code #: </label>
		<s:property value="emp.id_code" /> <br /><br />

		<label>Employee #: </label>
		<s:property value="emp.employee_number" /> <br /><br />
		
		<label>AD Sid </label>
		<s:property value="emp.ad_sid" /> <br /><br />

		<label>Email: </label>
		<s:property value="emp.email" /> <br /><br />
		
		<s:if test="emp.hasDepartment()">
			<label>Department: </label>
			<s:property value="emp.department" /> <br /><br />
		</s:if>
		
		<label>Added Date: </label>
		<s:property value="emp.added_date" /> <br /><br />
		
		<label>Roles: </label>
		<s:property value="emp.rolesText" /> <br /><br />
		
		<label>Active? </label>
		<s:if test="emp.inactive"> No </s:if><s:else>Yes</s:else> <br /><br />
	</div>		
	<div class="button-group">
	    <a href="<s:property value='#application.url' />employee.action?action=Edit&emp_id=<s:property value='emp.id' />" class="button">Edit</a> 
	    <s:if test="!emp.hasDepartments()">
		<a href="<s:property value='#application.url' />departmentEmployee.action?emp_id=<s:property value='emp.id' />" class="button">Add to Department</a>
	    </s:if>
	    <s:else>
		<a href="<s:property value='#application.url' />departmentEmployee.action?emp_id=<s:property value='emp.id' />" class="button">Add to Another Department</a>			
		<a href="<s:property value='#application.url' />deptEmpChange.action?id=<s:property value='emp.departmentEmployee.id' />" class="button">Change Department</a>
	    </s:else>
	    <s:if test="emp.hasNoJob()">
		<a href="<s:property value='#application.url' />jobTask.action?add_employee_id=<s:property value='emp.id' />&employee_number=<s:property value='emp.employee_number' />&effective_date=<s:property value='effective_date' />" class="button"> Add A Job</a>
	    </s:if>
	    <s:else>
		<a href="<s:property value='#application.url' />jobTask.action?add_employee_id=<s:property value='emp.id' />&effective_date=<s:property value='effective_date' />" class="button"> Add Another Job</a>
		<a href="<s:property value='#application.url' />terminate.action?emp_id=<s:property value='emp.id' />" class="button"> Terminate </a>					    
	    </s:else>
	</div>
	<s:if test="emp.hasDepartments()">
	    <s:set var="departmentEmployees" value="%{emp.departmentEmployees}" />
	    <s:set var="departmentEmployeesTitle" value="'Employee Department'" />
	    <%@ include file="departmentEmployees.jsp" %>
	    <s:if test="emp.hasAllJobs2()">
		<s:set var="jobTasks" value="%{emp.allJobs2}" />
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
		<s:set var="groupManagersTitle" value="'Maintainers of Groups'" />
		<%@ include file="groupManagers.jsp" %>
	    </s:if>
	    <s:if test="emp.canLeaveReview()">
		<s:set var="groupManagers" value="%{emp.leaveReviewers}" />
		<s:set var="groupManagersTitle" value="'Leave Reviewer of Groups'"/>
		<%@ include file="groupManagers.jsp" %>
	    </s:if>	    
	</s:if>
	
</div>
<%@ include file="footer.jsp" %>
