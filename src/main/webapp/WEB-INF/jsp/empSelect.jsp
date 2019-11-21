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
	<div class="width-one-half">
		<s:form action="empAccrual" id="form_id" method="post" >
			<s:hidden name="action2" id="action2" value="" />
			<input type="hidden" name="department_id" id="department_id" value=""  />
			<h1>Employee Accruals</h1>
			<p>Start typing the employee name to pick from a list </p>
			<div class="form-group">
  			<label>Employee Name</label>
				<s:textfield name="employee_name" value="" id="employee_name" size="20" />
  		</div>
			<div class="form-group">
				<label>ID</label>
				<s:textfield name="employee_selected_id" value="" id="employee_id" size="5" />
			</div>
			<s:submit name="action" type="button" value="Next" class="button"/>
		</s:form>
	</div>
</div>
<%@ include file="footer.jsp" %>
