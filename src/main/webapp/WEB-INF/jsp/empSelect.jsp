<%@ include file="header.jsp" %>
<div class="internal-page">
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
