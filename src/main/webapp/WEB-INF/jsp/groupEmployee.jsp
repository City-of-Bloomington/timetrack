<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="groupEmployee" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="groupEmployee.employee_id" value="%{groupEmployee.employee_id}" />
	<s:if test="groupEmployee.id == ''">
		<h3>Add Employee to a Group</h3>
	</s:if>
	<s:else>
		<h3>Edit Employee Group </h3>
		<s:hidden id="groupEmployee.id" name="groupEmployee.id" value="%{groupEmployee.id}" />
		<s:hidden id="groupEmployee.id" name="groupEmployee.group_id" value="%{groupEmployee.group_id}" />		
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
		<dd><a href="<s:property value='#application.url' />employee.action?id=<s:property value='groupEmployee.employee_id' />" /><s:property value="%{groupEmployee.employee}" /></a></dd>
	</dl>			
	<dl class="fn1-output-field">
		<dt>Group</dt>
		<s:if test="groupEmployee.id != ''">
			<dd><s:property value="groupEmployee.group" />
		</s:if>
		<s:else>
			<dd><s:select name="groupEmployee.group_id" value="%{groupEmployee.group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Group" /></dd>
		</s:else>
	</dl>
	<dl class="fn1-output-field">
		<dt>Effective Date</dt>
		<dd><s:textfield name="groupEmployee.effective_date" value="%{groupEmployee.effective_date}" size="10" maxlength="10" cssClass="date" required="true" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Expire Date</dt>
		<dd><s:textfield name="groupEmployee.expire_date" value="%{groupEmployee.expire_date}" size="10" maxlength="10" cssClass="date" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Inactive</dt>
		<dd><s:checkbox name="groupEmployee.inactive" value="%{groupEmployee.inactive}" /> Yes (check to dissable)</dd>
	</dl>	
	<s:if test="groupEmployee.id == ''">	
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>		
	</s:if>
	<s:else>
		<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		<a href="<s:property value='#application.url' />groupEmpChange.action?id=<s:property value='groupEmployee.id' />" class="fn1-btn"> Change Employee Group</a>				
	</s:else>
</s:form>
<s:if test="hasGroupEmployees()">
	<s:set var="groupEmployees" value="%{groupEmployees}" />
	<s:set var="groupEmployeesTitle" value="%{groupEmployeesTitle}" />
	<%@  include file="groupEmployees.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


