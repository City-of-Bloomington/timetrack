<%@ include file="header.jsp" %>
<div class="internal-page">
  <s:form action="switch" id="form_id" method="post" >
  	<s:hidden name="action2" id="action2" value="" />
  	<h1>Change Target Employee</h1>
  	<p><strong>Note:</strong> start typing the first name or last name of the employee in the employee field then pick from a list.</p>

    <%@ include file="strutMessages.jsp" %>

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