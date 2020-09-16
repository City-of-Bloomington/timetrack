<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:if test="hasErrors()">
    <s:set var="errors" value="errors" />
    <%@ include file="errors.jsp" %>
  </s:if>
  <s:elseif test="hasMessages()">
    <s:set var="messages" value="messages" />
    <%@ include file="messages.jsp" %>
  </s:elseif>
	<s:form action="cleanUp" id="form_id" method="post">
		<h1>Time Documents Cleanup</h1>
		This is intended for employees who are terminated on a certain date and we need to delete any time entry residuals passed the expire date of the termination date. <br />
		To do cleanup of time document, time entries,.. etc, enter the employee name and then <br />
		pick the pay period following employee termination/expire date from the list. <br />
		Then hit Submit button.
	  <div class="width-one-half">
			<div class="form-group">
				<label>Employee Name:</label>
				<s:textfield name="employee_name" value="%{employee_name}" size="30" id="employee_name2" /><br />
				(key words)
			</div>
			<div class="form-group">			
				<label>Employee ID:</label>
				<s:textfield name="other_employee_id" value="%{other_employee_id}" size="10" id="employee_id" />
			</div>
			<div class="form-group">
				<label>Pay Period:</label>			
				<s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick next pay period to termination date" />
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="button"/>
			</div>
		</div>
	</s:form>
<%@ include file="footer.jsp" %>
