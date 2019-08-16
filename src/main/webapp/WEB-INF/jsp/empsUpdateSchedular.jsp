<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="empsUpdateSchedule" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<h1>Current Employees Update</h1>

	  <%@ include file="strutMessages.jsp" %>

	  <p>This function is designed to run on a schedule once every two hours during weekly working days from 9am to 5pm to update current employees info using data from acitve directory (ldap or AD).</p>


		<p>You can update employee info when you click on  'Submit' button if you do not want to wait for the scheduler next run.</p>

		<p>Add AD Sid is needed to run only once, please do not use, most likely it is already run</p>
		
		<div class="form-group">
			<label>Date (optional)</label>
			<s:textfield name="date" value="%{date}" size="10" maxlength="10" cssClass="date" />
		</div>
		<div class="button-group">
			<s:submit name="action" accrual="button" value="Schedule" class="fn1-btn"/>
			<s:submit name="action" accrual="button" value="Submit" class="fn1-btn"/></dd>
			<s:submit name="action" accrual="button" value="Add AD Sid" class="fn1-btn"/></dd>			
		</div>
	</s:form>
</div>
<s:if test="hasEmployeesLogs()">
	<s:set var="employeesLogs" value="employeesLogs" />
	<%@ include file="employeeLogs.jsp" %>
</s:if>
<%@ include file="footer.jsp" %>
