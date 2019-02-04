<%@ include file="header.jsp" %>
<div class="internal-page">
  <s:form action="switch" id="form_id" method="post" >
  	<input type="hidden" name="action2" id="action2" value="" />
  	<input type="hidden" name="department_id" id="department_id"
							value="<s:property value='department_id' />"  />

  	<h1>Change Target Employee</h1>
  	<p><strong>Note:</strong> start typing the first name or last name of the employee in the employee field then pick from a list.</p>

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
  	<div class="width-one-half">
  		<div class="form-group">
  			<label>Employee</label>
        <s:textfield name="employee_name" value="" id="employee_name" size="20" />
  		</div>

      <div class="form-group">
        <label>ID</label>
        <s:textfield name="new_employee_id" value="" id="employee_id" size="5" />
      </div>

  		<s:submit name="action" type="button" value="Change" class="button"/></dd>
  	</div>
  </s:form>
</div>
<%@ include file="footer.jsp" %>
