<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="benefitGroup" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="benefitGroup.id == ''">
		<h1>New Benefit Group</h1>
	</s:if>
	<s:else>
		<h1>Edit Benefit Group: <s:property value="%{benefitGroup.name}" /> </h1>
		<s:hidden name="benefitGroup.id" value="%{benefitGroup.id}" />
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
		<s:if test="benefitGroup.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="benefitGroup.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Group Code </dt>
			<dd><s:textfield name="benefitGroup.name" value="%{benefitGroup.name}" size="20" maxlength="20" requiredLabel="true" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Full Time?</dt>
			<dd><s:checkbox name="benefitGroup.fullTime" value="%{benefitGroup.fullTime}" />Yes </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Exempt? </dt>
			<dd><s:checkbox name="benefitGroup.exempt" value="%{benefitGroup.exempt}" />Yes </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Unioned? </dt>
			<dd><s:checkbox name="benefitGroup.unioned" value="%{benefitGroup.unioned}" />Yes </dd>
		</dl>		
		<s:if test="benefitGroup.id == ''">
			<s:submit name="action" benefitGroup="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" benefitGroup="button" value="Save Changes" class="fn1-btn"/>
			<s:submit name="action" benefitGroup="button" value="Delete" class="fn1-btn"/>			
		</s:else>
	</div>
</s:form>
<s:if test="benefitGroups != null">
	<s:set var="benefitGroups" value="benefitGroups" />
	<s:set var="benefitGroupsTitle" value="benefitGroupsTitle" />
	<%@  include file="benefitGroups.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


