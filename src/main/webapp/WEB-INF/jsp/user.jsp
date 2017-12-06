<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="user" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="usser.id == ''">
		<h1>New User</h1>
	</s:if>
	<s:else>
		<h1>User <s:property value="usser.full_name" /></h1>
		<s:hidden name="usser.id" value="%{usser.id}" />
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
	<s:if test="usser.id != ''">
		<dl class="fn1-output-field">
			<dt>ID</dt>
			<dd><s:property value="%{usser.id}" /></dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Email</dt>
			<dd><s:property value="%{usser.email}" /></dd>
		</dl>
	</s:if>
	<dl class="fn1-output-field">
		<dt>Ussername</dt>
		<dd><s:textfield name="usser.username" size="10" value="%{usser.username}" required="true" /></dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>First Name </dt>
		<dd><s:textfield name="usser.first_name" value="%{usser.first_name}" size="30" maxlength="70" required="true" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Last Name </dt>
		<dd><s:textfield name="usser.last_name" value="%{usser.last_name}" size="30" maxlength="70" required="true" /> </dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>Role</dt>
		<dd><s:select name="usser.role" value="%{usser.role}" list="#{'Employee':'Employee','Admin':'Admin'}" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Inactive ?</dt>
		<dd><s:checkbox name="usser.inactive" value="%{usser.inactive}" /> Yes (check to disable)
		</dd>
	</dl>	
	<s:if test="usser.id == ''">
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
	</s:if>
	<s:else>
		<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>		<s:if test="usser.hasEmployee()"> <a href="<s:property value='#application.url' />employee.action?id=<s:property value='%{usser.employee.id}' />" class="fn1-btn">Edit Employee Info </a></s:if>
		<s:else>
			<a href="<s:property value='#application.url' />employee.action?user_id=<s:property value='%{usser.id}' />"  class="fn1-btn">Add Employee Info </a>
		</s:else>
		<a href="<s:property value='#application.url' />user.action" class="fn1-btn">New User </a>		
	</s:else>
</s:form>
<s:if test="hasUsers()">
	<s:set var="users" value="%{users}" />
	<s:set var="usersTitle" value="usersTitle" />
	<%@  include file="users.jsp" %>
</s:if>

<%@  include file="footer.jsp" %>


