<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="accrual" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="accrual.id == ''">
		<h1>New Accrual Type</h1>
	</s:if>
	<s:else>
		<h1>Edit Accrual Type: <s:property value="%{accrual.name}" /> </h1>
		<s:hidden name="accrual.id" value="%{accrual.id}" />
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
		<s:if test="accrual.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="accrual.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="accrual.name" value="%{accrual.name}" size="30" maxlength="70" requiredLabel="true" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Description </dt>
			<dd><s:textarea name="accrual.description" value="%{accrual.description}" rows="5" maxlength="50" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dd>Note: Pref Max Level below is the max number of hours that the employee should not pass by using the relate hour code instead of PTO for example.
			</dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Pref Max Level </dt>
			<dd><s:textfield name="accrual.pref_max_level" value="%{accrual.pref_max_level}" size="2" maxlength="2" />(The max level the employee should keep) </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Inactive?</dt>
			<dd><s:checkbox name="accrual.inactive" value="%{accrual.inactive}" fieldValue="true" />Yes </dd>
		</dl>
		<s:if test="accrual.id == ''">
			<s:submit name="action" accrual="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" accrual="button" value="Save Changes" class="fn1-btn"/>
		</s:else>
	</div>
</s:form>
<s:if test="accruals != null">
	<s:set var="accruals" value="accruals" />
	<s:set var="accrualsTitle" value="accrualsTitle" />
	<%@  include file="accruals.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


