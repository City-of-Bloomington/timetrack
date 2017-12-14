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
	<h4>New Group Manager(s) </h4>
	Note: You can assign one or two managers at a time <br />
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
		<dt>Group </dt>
		<dd><a href="<s:property value='#application.url' />group.action?id=<s:property value='groupManager.group_id' />" /><s:property value="%{groupManager.group}" /></a></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>1 - Employee: </dt>
		<s:if test="hasEmployees()">
			<dd><s:select name="groupManager.employee_id" value="%{groupManager.employee_id}" list="employees" listKey="id" listValue="full_name" headerKey="-1" headerValue="Pick Employee" /> Workflow Action: <s:if test="hasNodes()"><s:select name="groupManager.wf_node_id" value="%{groupManager.wf_node_id}" list="nodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Action" /></s:if>
			</dd>
		</s:if>
		<s:else>
			<dd>No managers available to add </dd>
		</s:else>
	</dl>
	<dl class="fn1-output-field">
		<dt>2 - Employee: </dt>
		<s:if test="hasEmployees()">
			<dd><s:select name="groupManager.employee_id2" value="%{groupManager.employee_id2}" list="employees" listKey="id" listValue="full_name" headerKey="-1" headerValue="Pick Employee" /> Workflow Action: <s:if test="hasNodes()"><s:select name="groupManager.wf_node_id2" value="%{groupManager.wf_node_id2}" list="nodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Action" /></s:if>
			</dd>
		</s:if>
		<s:else>
			<dd>No managers available to add </dd>
		</s:else>
	</dl>	
	<dl class="fn1-output-field">
		<dt>Start Date </dt>
		<dd><s:textfield name="groupManager.start_date" value="%{groupManager.start_date}" size="10" maxlength="10" required="true" cssClass="date" />* </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Expire Date </dt>
		<dd><s:textfield name="groupManager.expire_date" value="%{groupManager.expire_date}" size="10" maxlength="10" cssClass="date" /> </dd>
	</dl>
	<s:if test="hasEmployees()">		
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
	</s:if>
</s:form>
<s:if test="hasGroupManagers()">
	<s:set var="groupManagers" value="%{groupManagers}" />
	<s:set var="groupManagersTitle" value="'Managers of this Group'" />
	<%@  include file="groupManagers.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


