<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="departmentEmployee" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="departmentEmployee.employee_id" value="%{departmentEmployee.employee_id}" />
	<s:if test="departmentEmployee.id != ''">
		<s:hidden name="departmentEmployee.department_id" value="%{departmentEmployee.department_id}" />
		<s:if test="departmentEmployee.hasSecondaryDept()">
			<s:hidden name="departmentEmployee.department2_id" value="%{departmentEmployee.department2_id}" />
		</s:if>
	</s:if>	
	<s:property value="%{departmentEmployee.employee}" />
	<s:if test="departmentEmployee.id == ''">
		<h3>Add Employee to a Department </h3>
	</s:if>
	<s:else>
		<h3>Edit Employee Department </h3>
		To change employee department use 'Change Employee Department' option below <br />
		<s:hidden name="departmentEmployee.id" value="%{departmentEmployee.id}" />
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
	<dl class="fn1-output-field">
		<dt>Employee</dt>
		<dd><a href="<s:property value='#application.url' />employee.action?id=<s:property value='departmentEmployee.employee_id' />" /><s:property value="%{departmentEmployee.employee}" /></a></dd>
	</dl>
	<s:if test="departmentEmployee.id == ''">			
		<dl class="fn1-output-field">
			<dt>Department</dt>
			<dd><s:select name="departmentEmployee.department_id" value="%{departmentEmployee.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" /></dd>
		</dl>
	</s:if>
	<s:else>
		<dl class="fn1-output-field">
			<dt>Department</dt>		
			<dd><a href="<s:property value='#application.url' />department.action?id=<s:property value='departmentEmployee.department_id' />" /><s:property value="%{departmentEmployee.department}" /></a></dd>
		</dl>
	</s:else>
	<dl class="fn1-output-field">
		<dt>Secondary Department</dt>		
		<dd><s:select name="departmentEmployee.department2_id" value="%{departmentEmployee.department2_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" />(needed for department directors only)</dd>
	</dl>			
	<dl class="fn1-output-field">
		<dt>Effective Date</dt>
		<dd><s:textfield name="departmentEmployee.effective_date" value="%{departmentEmployee.effective_date}" size="10" maxlength="10" cssClass="date" required="true" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Expire Date</dt>
		<dd><s:textfield name="departmentEmployee.expire_date" value="%{departmentEmployee.expire_date}" size="10" maxlength="10" cssClass="date" /> </dd>
	</dl>	
	<s:if test="departmentEmployee.id == ''">	
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>		
	</s:if>
	<s:else>
		<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		<a href="<s:property value='#application.url' />deptEmpChange.action?id=<s:property value='departmentEmployee.id' />" class="fn1-btn"> Change Employee Department</a>				
		<a href="<s:property value='#application.url' />groupEmployee.action?employee_id=<s:property value='departmentEmployee.employee_id' />&department_id=<s:property value='departmentEmployee.department_id' />" class="fn1-btn"> Add Employee to a Group</a>
	</s:else>
</s:form>

<%@  include file="footer.jsp" %>


