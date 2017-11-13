<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="salaryGroup" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="salaryGroup.id == ''">
		<h1>New Salary Group</h1>
	</s:if>
	<s:else>
		<h1>Edit Salary Group </h1>
		<s:hidden name="salaryGroup.id" value="%{salaryGroup.id}" />
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
		<s:if test="type.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="salaryGroup.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="salaryGroup.name" value="%{salaryGroup.name}" size="30" maxlength="70" requiredLabel="true" required="true" id="type_name_id" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Description </dt>
			<dd><s:textarea name="salaryGroup.description" value="%{salaryGroup.description}" rows="5" maxlength="50" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Default Reg Code </dt>
			<dd><s:select name="salaryGroup.default_regular_id" value="%{salaryGroup.default_regular_id}" list="hourCodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Default Hour Code"/>(Each salary group need one default regular hour code) </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Inactive?</dt>
			<dd><s:checkbox name="salaryGroup.inactive" value="%{salaryGroup.inactive}" fieldValue="true" />Yes </dd>
		</dl>
		<s:if test="salaryGroup.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		</s:else>
	</div>
</s:form>
<s:if test="salaryGroups != null">
	<s:set var="salaryGroups" value="salaryGroups" />
	<s:set var="salaryGroupsTitle" value="'Salary Groups'" />
	<%@  include file="salaryGroups.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


