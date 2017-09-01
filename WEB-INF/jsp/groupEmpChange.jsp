<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="groupEmpChange" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="groupEmployee.employee_id" value="%{groupEmployee.employee_id}" />
	<s:hidden id="groupEmployee.id" name="groupEmployee.id" value="%{groupEmployee.id}" />
	<s:hidden id="groupEmployee.group_id" name="groupEmployee.group_id" value="%{groupEmployee.group_id}" />			
	<h3>Change Employee Group </h3>
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
		<dd><a href="<s:property value='#application.url' />employee.action?id=<s:property value='groupEmployee.employee_id' />" /><s:property value="%{groupEmployee.employee.user}" /></a></dd>
	</dl>			
	<dl class="fn1-output-field">
		<dt>Old Group</dt>
		<dd><s:property value="groupEmployee.group" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>New Group</dt>	
			<dd><s:select name="groupEmployee.new_group_id" value="%{groupEmployee.new_group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick A Group" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Change Date</dt>
		<dd><s:textfield name="groupEmployee.change_date" value="%{groupEmployee.change_date}" size="10" maxlength="10" cssClass="date" required="true" /> </dd>
	</dl>
	<s:submit name="action" type="button" value="Change Group" class="fn1-btn"/>
</s:form>
<%@  include file="footer.jsp" %>


