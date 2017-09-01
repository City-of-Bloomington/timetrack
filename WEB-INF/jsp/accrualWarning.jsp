<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="accrualWarning" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="accrualWarning.id == ''">
		<h3>New Accrual Usage Warning</h3>
	</s:if>
	<s:else>
		<h3>Edit Accrual Usage Warning: <s:property value="%{accrualWarning.id}" /></h3>
		<s:hidden id="accrualWarning.id" name="accrualWarning.id" value="%{accrualWarning.id}" />
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
	<p>These warnings are intended to offer some feedback to the employees and their group managers in case excess of times were used or these times will not comply with certain rules.<br />
		Some of the fields are required, such as the hour code and the excess warning text. Others are optional, such as minimum hours and step hour (fraction used).<br />
	</p>
	<div class="tt-row-container">
		<s:if test="accrualWarning.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="accrualWarning.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Related Hour Code </dt>
			<dd><s:select name="accrualWarning.hour_code_id" value="%{accrualWarning.hour_code_id}" list="hourCodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Position" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Minum Hours </dt>
			<dd><s:textfield name="accrualWarning.min_hrs" value="%{accrualWarning.min_hrs}" size="5" />(required minimum hour if any) </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Step Hour </dt>
			<dd><s:textfield name="accrualWarning.step_hrs" value="%{accrualWarning.step_hrs}" size="5" />(fraction of hour to use if any) </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Related Accrual Max Level </dt>
			<dd><s:textfield name="accrualWarning.related_accrual_max_level" value="%{accrualWarning.related_accrual_max_level}" size="6" />(Related prefered max level of accrual if any. Anything more than that the employee is required to use before using other accruals) </dd>
		</dl>
		<dl class="fn1-output-field">
			<dd>The following three text fields will be used to display the warning text if the related condition was not met </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Step Warning </dt>
			<dd><s:textfield name="accrualWarning.step_warning_text" value="%{accrualWarning.step_warning_text}" size="40" maxlength="80" />(if step hour is required , the warning text) </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Min Warning </dt>
			<dd><s:textfield name="accrualWarning.min_warning_text" value="%{accrualWarning.min_warning_text}" size="40" maxlength="80" />(if min hours is required , the warning text) </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Excess Warning </dt>
			<dd><s:textfield name="accrualWarning.excess_warning_text" value="%{accrualWarning.excess_warning_text}" size="40" maxlength="80" required="true" />(if excess hours are used, the warning text) </dd>
		</dl>		
		<s:if test="accrualWarning.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
			<a href="<s:property value='#application.url' />accrualWarning.action" class="fn1-btn">New Warning</a>					
		</s:else>
	</div>
</s:form>
<s:if test="hasAccrualWarnings()">
	<s:set var="accrualWarnings" value="accrualWarnings" />
	<s:set var="accrualWarningsTitle" value="accrualWarningsTitle" />
	<%@  include file="accrualWarnings.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


