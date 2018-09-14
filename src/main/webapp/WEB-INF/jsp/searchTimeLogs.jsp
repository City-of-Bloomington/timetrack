<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="timeBlockLog" id="form_id" method="post">
		<h1>Time Block Log Search</h1>
		To search for time block history, enter the employee name and then pick
		the pay period from the list. Then hit Submit button.
	  <div class="width-one-half">
			<div class="form-group">
				<label>Employee Name:</label>
				<s:textfield name="employee_name" value="%{employee_name}" size="20" id="employee_name" /> (key words)
			</div>
			<div class="form-group">			
				<label>Employee ID:</label><s:textfield name="other_employee_id" value="%{other_employee_id}" size="10" id="employee_id" />
			</div>
			<div class="form-group">
				<label>Pay Period:</label>			
				<s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Period" onchange="doRefresh()" />
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="button"/>
			</div>
		</div>
	</s:form>
<%@ include file="footer.jsp" %>
