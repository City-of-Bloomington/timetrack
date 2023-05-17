<%@ include file="header.jsp" %>
<div class="internal-page">
    
    <s:form action="terminate" id="form_id" method="post">
	<input type="hidden" name="department_id" id="department_id"
	       value="<s:property value='department_id' />"  />
	<h1>Employee Termination Process</h1>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<p>This process will terminate employee's job, group and group management at once by adding expire date to these records </p>
	<s:if test="action == ''">			
	    <ul>
		<li>This process is intended for full time or part time employee. </li>
		<li>It may be used for temp employees if they have only one job and one group or that all employee jobs need to be terminated</li>
		<li>For temp employee if you need to terminate certain jobs and groups do not use this tool</li>
		<li>First find the employee by typing his/her full name and pick from the list </li>
		<li>Enter the expire date from the list that is within last day of work.
		</li>
	    </ul>
	</s:if>
	<div class="width-one-half">
	    <div class="form-group">
		<label>Employee Full Name</label>
		<s:textfield name="full_name" value="%{full_name}" size="30" maxlength="70" id="employee_name" /><br /> Start typing employee last name or ID code then pick from the list
	    </div>
	    <div class="form-group">
		<label>ID</label>
		<s:textfield name="emp_id" value="%{emp_id}" id="employee_id" size="10" />
	    </div>
	    <div class="form-group">
		<label>Expire Date</label>
		<div class="date-range-picker">
		    <div>
			<s:select name="expire_date" value="%{expire_date}" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick Expire Date" /> (End pay period date)	
		    </div>
		</div>
	    </div>
	    <s:if test="action == ''">
		<s:submit name="action" type="button" value="Submit" class="button"/>
	    </s:if>
	</div>
    </s:form>
    <s:if test="action != null">
	<s:if test="hasEmpId()">
	    <s:if test="term.hasDepartments()">
		<s:set var="departmentEmployees" value="%{term.departmentEmployees}" />
		<s:set var="departmentEmployeesTitle" value="'Employee Department'" />
		<%@ include file="departmentEmployees.jsp" %>
	    </s:if>
	    <s:if test="term.hasGroupEmployees()">
		<s:set var="groupEmployees" value="%{term.groupEmployees}" />
		<s:set var="groupEmployeesTitle" value="'Employee Group'" />
		<%@ include file="groupEmployees.jsp" %>
	    </s:if>
	    <s:if test="term.hasJobs()">
		<s:set var="jobTasks" value="%{term.jobs}" />
		<s:set var="jobTasksTitle" value="'Employee Jobs'" />
		<%@ include file="jobTasks.jsp" %>
	    </s:if>
	    <s:if test="term.isGroupManager()">
		<s:set var="groupManagers" value="%{term.groupManagers}" />
		<s:set var="groupManagersTitle" value="'Manager of Groups '" />
		<%@ include file="groupManagers.jsp" %>
	    </s:if>			
	</s:if>
    </s:if>
</div>
<%@ include file="footer.jsp" %>
