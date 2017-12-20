<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="importEmployees" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="dept_name" id="dept_name_id" value="%{dept_name}" />	
	<h3>Import employees data from Ldap (AD)</h3>
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
	<p>To import employees data from ldap or AD, pick the department, you may pick a certain group if you do not want to import only certain group. </p>
	<dl class="fn1-output-field">
		<dt>Department</dt>
		<dd><s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" id="department_id_change" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Group </dt>
		<dd>
			<s:if test="hasGroups()">
				<s:select name="group_name" value="%{group_name}" list="groups" listKey="name" listValue="name" id="group_name_set" headerKey="-1" headerValue="Pick a group"/>
			</s:if>
			<s:else>
				<select name="group_name" value=""  id="group_name_set" disabled="disabled" ></select>(optional)
			</s:else>
		</dd>
	</dl>		
	<dl class="fn1-output-field">
		<dt>Effective Date</dt>
		<dd><s:textfield name="effective_date" value="" size="10" maxlength="10" cssClass="date" /></dd>
	</dl>
	<s:submit name="action" type="button" value="Import" class="fn1-btn"/>
</s:form>
<%@  include file="footer.jsp" %>


