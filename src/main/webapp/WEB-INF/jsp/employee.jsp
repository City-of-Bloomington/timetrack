<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="employee" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="employee.id == ''">
		<h3>New Employee</h3>
		<s:hidden name="employee.group_id" value="" id="group_id" />
	</s:if>
	<s:else>
		<h3>Employee <s:property value="employee.full_name" /></h3>
		<s:hidden name="employee.id" value="%{employee.id}" />
	</s:else>
  <s:if test="hasActionErrors()">
		<div class="errors">
      <s:actionerror/>
		</div>
  </s:if>
  <s:elseif test="hasActionMessages()">
		<div class="welcome">
      <s:actionmessage/>
		</div>
  </s:elseif>
	<s:if test="employee.id != ''">
		<dl class="fn1-output-field">
			<dt>ID</dt>
			<dd><s:property value="%{employee.id}" /></dd>
		</dl>
	</s:if>
	<s:if test="employee.id == ''">
	<dl class="fn1-output-field">
		<dt>Full Name</dt>
		<dd><s:textfield name="employee.full_name" value="" size="30" maxlength="70" id="emp_name" /> Start typing employee last name to pick from the list</dd>
	</dl>
	</s:if>
	<dl class="fn1-output-field">
		<dt>Username</dt>
		<dd><s:textfield name="employee.username" size="10" value="%{employee.username}" required="true" id="username_id" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>First Name </dt>
		<dd><s:textfield name="employee.first_name" value="%{employee.first_name}" size="30" maxlength="70" required="true" id="first_name_id" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Last Name </dt>
		<dd><s:textfield name="employee.last_name" value="%{employee.last_name}" size="30" maxlength="70" required="true" id="last_name_id" /> </dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>ID Code # </dt>
		<dd><s:textfield name="employee.id_code" value="%{employee.id_code}" size="10" maxlength="10" id="id_code_id" />(The number on City ID) </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Employee # </dt>
		<dd><s:textfield name="employee.employee_number" value="%{employee.employee_number}" size="15" maxlength="15" id="employee_number_id" />(from new world) </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Email</dt>
		<dd><s:textfield name="employee.email" size="30" value="%{employee.email}" id="email_id" /></dd>
	</dl>	
	<s:if test="employee.id == ''">
		<dl class="fn1-output-field">
			<dt>Department</dt>
			<dd><s:select name="employee.department_id" value="" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" id="department_id" />Group ID: <span id="group_id2">&nbsp;</span>
			</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Effective Date</dt>
			<dd><s:textfield name="employee.effective_date" value="" size="10" maxlength="10" cssClass="date" /></dd>
		</dl>
	</s:if>
	<dl class="fn1-output-field">
		<dt>Role</dt>
		<dd><s:select name="employee.role" value="%{employee.role}" list="#{'Employee':'Employee','Admin':'Admin'}" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Inactive ?</dt>
		<dd><s:checkbox name="employee.inactive" value="%{employee.inactive}" /> Yes (check to disable)
		</dd>
	</dl>	
	<s:if test="employee.id == ''">
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
	</s:if>
	<s:else>
		<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		<a href="<s:property value='#application.url' />employee.action" class="fn1-btn">New Employee</a>		
		<s:if test="!employee.hasDepartment()">
			<a href="<s:property value='#application.url' />departmentEmployee.action?employee_id=<s:property value='employee.id' />" class="fn1-btn">Add Employee to Department</a>
		</s:if>
		<s:if test="!employee.hasActiveGroup()">
			<a href="<s:property value='#application.url' />groupEmployee.action?employee_id=<s:property value='employee.id' />&department_id=<s:property value='employee.department_id' />" class="fn1-btn"> Add Employee to a Group</a>
		</s:if>			
	</s:else>
</s:form>
<s:if test="employee.id == ''">
	<s:if test="employees != null && employees.size() > 0">
		<s:set var="employees" value="%{employees}" />
		<s:set var="employeesTitle" value="employeesTitle" />
		<%@  include file="employees.jsp" %>
	</s:if>
</s:if>
<s:else>
	<s:if test="employee.hasDepartment()">
		<s:set var="departmentEmployees" value="%{employee.departmentEmployees}" />
		<s:set var="departmentEmployeesTitle" value="'Employee Department'" />
		<%@  include file="departmentEmployees.jsp" %>
		<s:if test="employee.hasGroup()">		
			<s:set var="groupEmployees" value="%{employee.groupEmployees}" />
			<s:set var="groupEmployeesTitle" value="'Employee Group'" />
			<%@  include file="groupEmployees.jsp" %>
		</s:if>
	</s:if>
</s:else>
<%@  include file="footer.jsp" %>


