<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="searchDeptEmployees" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
  	<input type="hidden" name="department_id" id="department_id"
							value="<s:property value='department_id' />"  />
		<h1>Department Employee Search</h1>
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
				<label>Employee ID</label>
				<s:textfield name="deptEmpLst.employee_id" value="%{deptEmpLst.employee_id}" size="10" id="employee_id" />
			</div>			
			<div class="form-group">
				<label>Name</label>
				<s:textfield name="deptEmpLst.name" value="%{deptEmpLst.name}" size="30" id="employee_name" /><br />
				(key words)
			</div>
			<div class="form-group">
				<label>ID Code #</label>
				<s:textfield name="deptEmpLst.id_code" value="%{deptEmpLst.id_code}" size="10" maxlength="10" /><br />
				(The number on City ID)
			</div>
			<div class="form-group">
				<label>Employee #</label>
				<s:textfield name="deptEmpLst.employee_number" value="%{deptEmpLst.employee_number}" size="15" maxlength="15" /><br />
				(from new world)
			</div>
			<div class="form-group">
				<label>Department</label>
				<s:select name="deptEmpLst.department_id" value="%{deptEmpLst.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" id="department_id_change" />
			</div>
			<div class="form-group">
				<label>Active?</label>
				<s:radio name="deptEmpLst.activeStatus" value="%{deptEmpLst.activeStatus}" list="#{'-1':'All','Active':'Active only','Inactive':'Inactive only'}"/>
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="button"/>
				<a href="<s:property value='#application.url' />employee.action" class="button">New Employee</a>
			</div>
		</div>
	</s:form>
</div>
<s:if test="hasDepartmentEmployees()">
	<s:set var="departmentEmployees" value="%{departmentEmployees}" />
	<s:set var="departmentEmployeesTitle" value="deptEmployeesTitle" />
	<%@ include file="departmentEmployees.jsp" %>
</s:if>
<%@ include file="footer.jsp" %>
