<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="terminateJobs" id="form_id" method="post">    
	<s:hidden name="job_id" value="%{job_id}" />
	<h1>Employee Termination </h1>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<p>Please select the pay period end date for employee termination </p>
	<p>Click Next to go to termination form</p> 
	<div class="width-one-half">
	    <div class="form-group">
		<label>Employee</label>
		<a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='emp.id' />"> <s:property value="%{emp}" /></a>
	    </div>
	    <div class="form-group">
		<label>Employee Job</label>
		<s:property value="job" />
	    </div>	    
	    <div class="form-group">
		<label>Last Pay Period Ending Date</label>
		<s:select name="last_pay_period_date" value="%{last_pay_period_date}" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick End Date" required="required" />
	    </div>
	    <div class="button-group">
		<s:submit name="action" type="button" value="Next" class="button"/>
	    </div>
	</div>
    </s:form>
</div>
<%@ include file="footer.jsp" %>
