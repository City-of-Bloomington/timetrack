<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="timeBlockLog" id="form_id" method="post">
		<h1>Time Entry History Search</h1>
		To search for time entry history, enter the employee name and then pick
		the pay period from the list. Then hit Submit button.
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
		  <s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Period" onchange="doRefresh()" />
	      </div>
	      <div class="form-group">			
		  <label>Start Date:</label>
		  <s:textfield name="start_date" value="%{start_date}" size="10" id="start_date" />
	      </div>
	      <div class="form-group">			
		  <label>End Date:</label>
		  <s:textfield name="end_date" value="%{end_date}" size="10" id="start_date" />
	      </div>	      
	      <div class="button-group">
		  <s:submit name="action" type="button" value="Submit" class="button"/>
	      </div>
	  </div>
	</s:form>
<%@ include file="footer.jsp" %>
