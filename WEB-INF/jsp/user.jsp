<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="user" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="user.id == ''">
		<h1>New User</h1>
	</s:if>
	<s:else>
		<h1>User <s:property value="user.fullname" /></h1>
		<s:hidden name="user.id" value="%{user.id}" />
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
	<s:if test="user.id != ''">
		<dl class="fn1-output-field">
			<dt>ID</dt>
			<dd><s:property value="%{user.id}" /></dd>
		</dl>
	</s:if>
	<dl class="fn1-output-field">
		<dt>Username</dt>
		<dd><s:textfield name="user.username" size="10" value="%{user.username}" required="true" /></dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>First Name </dt>
		<dd><s:textfield name="user.first_name" value="%{user.first_name}" size="30" maxlength="70" required="true" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Last Name </dt>
		<dd><s:textfield name="user.last_name" value="%{user.last_name}" size="30" maxlength="70" required="true" /> </dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>ID Code # </dt>
		<dd><s:textfield name="user.id_code" value="%{user.id_code}" size="10" maxlength="10" />(The number on City ID) </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Employee Number </dt>
		<dd><s:textfield name="user.employee_number" value="%{user.employee_number}" size="15" maxlength="15" />(from new world) </dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>Role</dt>
		<dd><s:select name="user.role" value="%{user.role}" list="#{'User':'User','Admin':'Admin'}" /></dd>
	</dl>
	
	<dl class="fn1-output-field">
		<dt>Inactive ?</dt>
		<dd><s:checkbox name="user.inactive" value="%{user.inactive}" /> Yes (check to disable)
		</dd>
	</dl>	
	<s:if test="user.id == ''">
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
	</s:if>
	<s:else>
		<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
	</s:else>
	<a href="<s:property value='#application.url' />groupUser.action?" class="fn1-btn"> Manage Users in Groups</a>
</s:form>
<s:if test="employees != null && employees.size() > 0">
	<s:set var="employees" value="%{employees}" />
	<s:set var="employeesTitle" value="employeesTitle" />
	<%@  include file="employees.jsp" %>
</s:if>

<%@  include file="footer.jsp" %>


