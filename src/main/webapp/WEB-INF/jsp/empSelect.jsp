<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="empAccrual" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<h1>Employee Accruals</h1>

		<%@ include file="strutMessages.jsp" %>

		<div class="form-group">
			<label>Select an Employee</label>
			<s:select name="employee_selected_id" value="%{employee_selected_id}" list="employees" listKey="id" listValue="full_name" headerKey="-1" headerValue="Pick Employee" />
		</div>
		<s:submit name="action" type="button" value="Next" class="button"/>
	</s:form>
</div>
<%@ include file="footer.jsp" %>