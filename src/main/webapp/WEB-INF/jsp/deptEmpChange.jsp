<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="deptEmpChange" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="departmentEmployee.id" value="%{departmentEmployee.id}" />
	<s:hidden name="departmentEmployee.employee_id" value="%{departmentEmployee.employee_id}" />
	<s:hidden name="departmentEmployee.department_id" value="%{departmentEmployee.department_id}" />	
	<s:property value="%{departmentEmployee.employee.user}" />
	<h3>Change Employee Department </h3>
	Note: If the department is changed, the old department expire date will be the effective change date and the effective date for the new department will be the new effective change date. <br />
	All employee old groups expire date will be set to the effecitve change date set below and you have to assign the employee to new groups in the new department.<br />
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
	<dl class="fn1-output-field">
		<dt>Employee</dt>
		<dd><a href="<s:property value='#application.url' />employee.action?id=<s:property value='departmentEmployee.employee_id' />" /><s:property value="%{departmentEmployee.employee.user}" /></a></dd>
	</dl>		
	<dl class="fn1-output-field">
		<dt>Old Department</dt>
			<dd><a href="<s:property value='#application.url' />type.action?id=<s:property value='departmentEmployee.department_id' />" /><s:property value="%{departmentEmployee.department}" /></a></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>New Department</dt>	
		<dd><s:select name="departmentEmployee.new_department_id" value="%{departmentEmployee.new_department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" /></dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>Change Effective Date</dt>
		<dd><s:textfield name="departmentEmployee.change_date" value="%{departmentEmployee.change_date}" size="10" maxlength="10" cssClass="date" required="true" /> (this will be expire date for old department and effective date for the new one)</dd>
	</dl>
	<s:submit name="action" type="button" value="Change Department" class="fn1-btn"/>		

</s:form>

<%@  include file="footer.jsp" %>


