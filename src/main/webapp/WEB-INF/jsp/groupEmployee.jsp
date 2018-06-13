<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="groupEmployee" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="groupEmployee.employee_id" value="%{groupEmployee.employee_id}" />
		<s:if test="groupEmployee.id == ''">
			<h1>Add Employee to a Group</h1>
		</s:if>
		<s:else>
			<h1>Edit Employee Group </h1>
			<s:hidden id="groupEmployee.id" name="groupEmployee.id" value="%{groupEmployee.id}" />
			<s:if test="groupEmployee.hasTimeData()">
				<s:hidden id="groupEmployee.id" name="groupEmployee.group_id" value="%{groupEmployee.group_id}" />
			</s:if>
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

	  <div class="width-one-half">
			<div class="form-group">
				<label>Employee</label>
				<a href="<s:property value='#application.url' />employee.action?id=<s:property value='groupEmployee.employee_id' />" /><s:property value="%{groupEmployee.employee}" /></a>
			</div>

			<div class="form-group">
				<label>Group</label>
				<s:if test="groupEmployee.hasTimeData()">
					<s:property value="groupEmployee.group" />
				</s:if>

				<s:else>
					<s:select name="groupEmployee.group_id" value="%{groupEmployee.group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Group" />
				</s:else>
			</div>

			<div class="form-group">
				<label>Effective Date</label>
				<s:textfield name="groupEmployee.effective_date" value="%{groupEmployee.effective_date}" size="10" maxlength="10" cssClass="date" required="true" />
			</div>

			<div class="form-group">
				<label>Expire Date</label>
				<s:textfield name="groupEmployee.expire_date" value="%{groupEmployee.expire_date}" size="10" maxlength="10" cssClass="date" />
			</div>

			<div class="form-group">
				<label>Inactive</label>
				<s:checkbox name="groupEmployee.inactive" value="%{groupEmployee.inactive}" /> Yes (check to dissable)
			</div>

			<s:if test="groupEmployee.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>

			<s:else>
				<div class="button-group">
					<s:if test="groupEmployee.hasTimeData()">
						<a href="<s:property value='#application.url' />groupEmpChange.action?id=<s:property value='groupEmployee.id' />" class="button"> Change Employee Group</a>
					</s:if>
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>

	<s:if test="hasGroupEmployees()">
		<s:set var="groupEmployees" value="%{groupEmployees}" />
		<s:set var="groupEmployeesTitle" value="%{groupEmployeesTitle}" />
		<%@ include file="groupEmployees.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>