<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="empWizard" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
  	<input type="hidden" name="department_id" id="department_id"
							value="<s:property value='department_id' />"  />
		<h1>Check if Employee is Already in Our System</h1>
		<p>Read carefully the following steps</p>
		<ul>
			<li>Start typing employee first name in the 'Name' field below, if you see a list of names that one matches the employee name, pick that one. </li>
			<li>If the employee exists in our system, the Next page will show the employee info that you can modify or keep as is.</li>
			<li>If the employee is not in our system then, the Next page will allow you to enter the info about the new employee.</li>
			<li>You also need to provide employee effective start date, this is related pay period start date you pick from the list.</li>
			<li>Click Next</li>
		</ul>
		<s:if test="hasMessages()">
			<s:set var="messages" value="messages" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="errors" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>
	  <div class="width-one-half">			
			<div class="form-group">
				<label>Employee</label>
				<div class="date-range-picker">
					<div>
						<label>Name</label>							
						<s:textfield name="emplst.name" value="%{emplst.name}" size="20" id="employee_name" required="true" />
						</div>
						<div>
							<label>ID</label>
							<s:textfield name="emplst.id" value="%{emplst.id}" size="6" id="employee_id" />
						</div>
				</div>
			</div>
			<div class="form-group">
				<div class="form-group">
					<label>Effective Date</label>
					<div class="date-range-picker">
						<div>
							<s:select name="effective_date" value="" list="payPeriods" listKey="startDate" listValue="startDate" headerKey="-1" required="true" headerValue="Pick Start Date" /> (Start pay period date)	
						</div>
					</div>
				</div>
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Next" class="button"/>
			</div>
		</div>
	</s:form>
</div>
<%@ include file="footer.jsp" %>
