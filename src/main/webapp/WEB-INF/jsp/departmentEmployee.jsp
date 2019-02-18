<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="departmentEmployee" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="departmentEmployee.employee_id" value="%{departmentEmployee.employee_id}" />
		<s:if test="departmentEmployee.id != ''">
			<s:hidden name="departmentEmployee.department_id" value="%{departmentEmployee.department_id}" />
			<s:if test="departmentEmployee.hasSecondaryDept()">
				<s:hidden name="departmentEmployee.department2_id" value="%{departmentEmployee.department2_id}" />
			</s:if>
		</s:if>


		<s:if test="departmentEmployee.id == ''">
			<h1>Add Employee to a Department</h1>
		</s:if>

		<s:else>
			<h1>Edit Employee Department: <s:property value="%{departmentEmployee.employee}" /></h1>
			<s:hidden name="departmentEmployee.id" value="%{departmentEmployee.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

	  <div class="width-one-half">
			<div class="form-group">
				<label>Employee</label>
				<a href="<s:property value='#application.url' />employee.action?id=<s:property value='departmentEmployee.employee_id' />" /><s:property value="%{departmentEmployee.employee}" /></a>
			</div>

			<s:if test="departmentEmployee.id == ''">
				<div class="form-group">
					<label>Department</label>
					<s:select name="departmentEmployee.department_id" value="%{departmentEmployee.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" />
				</div>
			</s:if>

			<s:else>
				<div class="form-group">
					<label>Department</label>
					<a href="<s:property value='#application.url' />department.action?id=<s:property value='departmentEmployee.department_id' />" /><s:property value="%{departmentEmployee.department}" /></a>
				</div>
			</s:else>

			<div class="form-group">
				<label>Secondary Department</label>
				<s:select name="departmentEmployee.department2_id" value="%{departmentEmployee.department2_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" /><br />(needed for department directors only)
			</div>

			<div class="form-group">
				<label>Effective Date</label>
				<s:textfield name="departmentEmployee.effective_date" value="%{departmentEmployee.effective_date}" size="10" maxlength="10" cssClass="date" required="true" />
			</div>

			<div class="form-group">
				<label>Expire Date</label>
				<s:textfield name="departmentEmployee.expire_date" value="%{departmentEmployee.expire_date}" size="10" maxlength="10" cssClass="date" />
			</div>

			<s:if test="departmentEmployee.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>

			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url' />deptEmpChange.action?id=<s:property value='departmentEmployee.id' />" class="button">Change Employee Department</a>
					<a href="<s:property value='#application.url' />groupEmployee.action?emp_id=<s:property value='departmentEmployee.employee_id' />&department_id=<s:property value='departmentEmployee.department_id' />" class="button">Add Employee to a Group</a>
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>
</div>
<%@ include file="footer.jsp" %>
