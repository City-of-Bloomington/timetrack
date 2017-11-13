<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="group" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="group.id == ''">
		<h3>New group</h3>
	</s:if>
	<s:else>
		<h3>Edit Group: <s:property value="%{group.name}" /></h3>
		<s:hidden id="group.id" name="group.id" value="%{group.id}" />
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
  <p>* Required field <br />
		<s:if test="id != ''">
			If you make any change, please hit the 'Save Changes' button
		</s:if>
		<s:else>
			You must hit 'Save' button to save data.
		</s:else>
	</p>
	<div class="tt-row-container">
		<s:if test="group.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="group.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="group.name" value="%{group.name}" size="30" maxlength="70" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Department </dt>
			<dd><s:select name="group.department_id" value="%{group.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Dept" required="true" />* </dd>
		</dl>				
		<dl class="fn1-output-field">
			<dt>Description </dt>
			<dd><s:textarea name="group.description" value="%{group.description}" rows="5" cols="50" /> </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Inactive?</dt>
			<dd><s:checkbox name="group.inactive" value="%{group.inactive}" fieldValue="true" />Yes </dd>
		</dl>
		<s:if test="group.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
			<a href="<s:property value='#application.url' />groupManager.action?group_id=<s:property value='group.id' />" class="fn1-btn">Assign managers to group </a>
			<a href="<s:property value='#application.url'/>group.action" class="fn1-btn">New Employee Group</a></li>			
		</s:else>
	</div>
</s:form>
<s:if test="group.id != ''">
	<s:if test="group.hasGroupEmployees()">
		<s:set var="groupEmployees" value="%{group.groupEmployees}" />
		<s:set var="groupEmployeesTitle" value="'Group Employees'" />
		<%@  include file="groupEmployees.jsp" %>	
	</s:if>
	<s:if test="hasGroupManagers()">
		<s:set var="groupManagers" value="%{groupManagers}" />
		<s:set var="groupManagersTitle" value="'Group Managers'" />
		<%@  include file="groupManagers.jsp" %>	
	</s:if>
</s:if>
<s:else>
	<s:if test="groups != null">
		<s:set var="groups" value="groups" />
		<s:set var="groupsTitle" value="groupsTitle" />
		<%@  include file="groups.jsp" %>
	</s:if>
</s:else>
<%@  include file="footer.jsp" %>


