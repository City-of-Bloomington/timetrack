<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="groupManager" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="groupManager.group_id" value="%{groupManager.group_id}" />
	<s:hidden name="groupManager.id" value="%{groupManager.id}" />	
	<h4>Edit Group Manager </h4>
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
		<dt>ID </dt>
		<dd><s:property value="groupManager.id" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Group </dt>
		<dd><a href="<s:property value='#application.url' />group.action?id=<s:property value='groupManager.group_id' />" /><s:property value="%{groupManager.group}" /></a></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Employee </dt>
		<dd><a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='groupManager.employee_id' />" /><s:property value="%{groupManager.employee}" /></a></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Workflow Action </dt>
		<s:if test="hasNodes()">		
			<dd><s:select name="groupManager.wf_node_id" value="%{groupManager.wf_node_id}" list="nodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Action" /></dd>
		</s:if>
	</dl>
	<dl class="fn1-output-field">
		<dt>Start Date </dt>
		<dd><s:textfield name="groupManager.start_date" value="%{groupManager.start_date}" size="10" maxlength="10" required="true" cssClass="date" />* </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Primary? </dt>
		<dd><s:checkbox name="groupManager.primary" value="%{groupManager.primary}"  /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Expire Date </dt>
		<dd>
			<s:if test="groupManager.hasExpireDate()">
				<s:textfield name="groupManager.expire_date" value="%{groupManager.expire_date}" size="10" maxlength="10" cssClass="date" />
			</s:if>
			<s:else>
				<s:select name="groupManager.expire_date" value="" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick Expire Date" /> (End pay period date)	
			</s:else>
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Inactive? </dt>
		<dd><s:checkbox name="groupManager.inactive" value="%{groupManager.inactive}"  /> (check to dissable)</dd>
	</dl>
	<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
	<a href="<s:property value='#application.url' />groupManagerAdd.action?group_id=<s:property value='groupManager.group_id' />" class="fn1-btn">Assign managers to group </a>
</s:form>
<s:if test="hasGroupManagers()">
	<s:set var="groupManagers" value="%{groupManagers}" />
	<s:set var="groupManagersTitle" value="'Managers of this Group'" />
	<%@  include file="groupManagers.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


