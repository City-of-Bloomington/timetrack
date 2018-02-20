<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="department" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="department.id == ''">
		<h1>New Department</h1>
	</s:if>
	<s:else>
		<h1>Edit <s:property value="%{department.name}" /></h1>
		<s:hidden name="department.id" value="%{department.id}" />
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
			If you make any change, please hit the 'Save Changes' button <br />
		</s:if>
		<s:else>
			You must hit 'Save' button to save data.<br />
		</s:else>
		Note: Reference ID is New World app ID for the specified department <br />
		Ldap/AD name is the department name in ldap, needed for data import. <br />
	</p>
	<div class="tt-row-container">
		<s:if test="department.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="department.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="department.name" value="%{department.name}" size="30" maxlength="70" requiredLabel="true" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Ldap/AD Name </dt>
			<dd><s:textfield name="department.ldap_name" value="%{department.ldap_name}" size="30" maxlength="70" requiredLabel="true" required="true" />* </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Description </dt>
			<dd><s:textarea name="department.description" value="%{department.description}" rows="5" maxlength="50" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Referance ID(s) </dt>
			<dd><s:textfield name="department.ref_id" value="%{department.ref_id}" size="6" maxlength="10" />(for multiple ID's, use comma in between) </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Inactive?</dt>
			<dd><s:checkbox name="department.inactive" value="%{department.inactive}" fieldValue="true" />Yes </dd>
		</dl>
		<s:if test="department.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
			<a href="<s:property value='#application.url'/>department.action" class="fn1-btn">New Department</a></li>				
		</s:else>
	</div>
</s:form>
<s:if test="department.id != ''">
	
	<s:if test="groups != null">
		<s:set var="groups" value="groups" />
		<s:set var="groupsTitle" value="'Groups in this Department'" />
		<%@  include file="groups.jsp" %>
	</s:if>
	<s:if test="hasEmployees()">
		<s:set var="employees" value="employees" />
		<s:set var="employeesTitle" value="'Employees in this Department'" />
		<%@  include file="employees.jsp" %>
	</s:if>
</s:if>
<s:else>
	<s:if test="departments != null">
		<s:set var="departments" value="departments" />
		<s:set var="departmentsTitle" value="deptsTitle" />
		<%@  include file="departments.jsp" %>
	</s:if>
</s:else>
<%@  include file="footer.jsp" %>


