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
	<ul>
		<li>When an employee group is changed to another one,
			the old group will be set to expire on the day before the
			pay period selected for effective date for the new group.</li>
		<li>All related group related jobs will be expired as well to the same
			group expire date set above.</li>
		<li>Then you need to add new job(s) to the new group</li>
	</ul>
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
		<dt>Old Group</dt>
		<dd><s:property value="groupEmployee.group" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>New Group</dt>	
			<dd><s:select name="groupEmployee.new_group_id" value="%{groupEmployee.new_group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick A Group" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>New Group Effective Start Pay Period</dt>
		<dd><s:select name="groupEmployee.change_pay_period_id" value="" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Effective start pay period" />
    </dd>
	</dl>
	<s:submit name="action" type="button" value="Change Group" class="fn1-btn"/>
</s:form>
<%@  include file="footer.jsp" %>


