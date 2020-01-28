<%@ include file="header.jsp" %>
<div class="internal-page">
	<div class="width-one-half">
  <s:form action="empTimeChange" id="form_id" method="post" >
  	<input type="hidden" name="action2" id="action2" value="" />
  	<input type="hidden" name="department_id" id="department_id"
							value="<s:property value='department_id' />"  />

  	<h1>Employee Job Time Changing Process</h1>
		<ul>
			<li>This is intended for employee who changed job in certain pay period</li>
			<li>The employee used the old job to enter work times </li>
			<li>This function will change the times from the old job to the new one </li>
			<li>First you need to pick the employee by start typing in the 'Employee' field below and then pick from the list </li>
			<li>Click on Next</li>
		</ul>
		<s:if test="hasErrors()">
			<s:set var="errors" value="errors" />
			<div class="errors">
				<%@  include file="errors.jsp" %>
			</div>
		</s:if>
		<s:elseif test="hasMessages()">
			<s:set var="messages" value="messages" />
			<div class="welcome">
				<%@  include file="messages.jsp" %>
			</div>
		</s:elseif>
  		<div class="form-group">
  			<label>Employee</label>
        <s:textfield name="employee_name" value="" id="employee_name" size="20" />
  		</div>
      <div class="form-group">
        <label>ID</label>
        <s:textfield name="related_employee_id" value="" id="employee_id" size="5" />
      </div>
  		<s:submit name="action" type="button" value="Next" class="button"/></dd>
  	</div>
  </s:form>
</div>
<%@ include file="footer.jsp" %>
