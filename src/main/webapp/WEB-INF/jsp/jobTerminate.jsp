<%@ include file="header.jsp" %>
<div class="internal-page">
    
    <s:form action="jobTerminate" id="form_id" method="post">
	<s:hidden name="job_id" value="%{job_id}" />
	<h1>Employee Job Termination </h1>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<p>This process will terminate one of the employee jobs (if he has more than one),  and also will terminate group management at once by adding expire date to these records </p>
	<s:if test="action == ''">			
	    <ul>
		<li>Enter the expire date from the list that is within last day of work.
		</li>
	    </ul>
	</s:if>
	<div class="width-one-half">
	    <div class="form-group">
		<label>Employee</label>
		<a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='term.emp_id' />"> <s:property value="%{term.emp}" /></a>
	    </div>
	    <div class="form-group">
		<label>Job Title</label>
		<s:property value="%{term.job}" /> 
	    </div>
	    <div class="form-group">
		<label>Expire Date</label>
		<div class="date-range-picker">
		    <div>
			<s:select name="expire_date" value="%{expire_date}" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick Expire Date" /> (End of Pay Period Date)	
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
