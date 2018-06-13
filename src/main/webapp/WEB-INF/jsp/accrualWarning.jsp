<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="accrualWarning" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="accrualWarning.id == ''">
			<h1>New Accrual Usage Warning</h1>
		</s:if>
		<s:else>
			<h1>Edit Accrual Usage Warning: <s:property value="%{accrualWarning.id}" /></h1>
			<s:hidden id="accrualWarning.id" name="accrualWarning.id" value="%{accrualWarning.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

		<p>These warnings are intended to offer some feedback to the employees and their group managers in case excess of times were used or these times will not comply with certain rules.</p>
		<p>Some of the fields are required, such as the hour code and the excess warning text. Others are optional, such as minimum hours and step hour (fraction used).</p>

		<div class="width-one-half">
			<s:if test="accrualWarning.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="accrualWarning.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Related Hour Code</label>
				<s:select name="accrualWarning.hour_code_id" value="%{accrualWarning.hour_code_id}" list="hourCodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Position" required="true" />
			</div>

			<div class="form-group">
				<label>Minum Hours</label>
				<s:textfield name="accrualWarning.min_hrs" value="%{accrualWarning.min_hrs}" size="5" /><br />(required minimum hour if any)
			</div>

			<div class="form-group">
				<label>Step Hour</label>
				<s:textfield name="accrualWarning.step_hrs" value="%{accrualWarning.step_hrs}" size="5" /><br />(fraction of hour to use if any)
			</div>

			<div class="form-group">
				<label>Related Accrual Max Level</label>
				<s:textfield name="accrualWarning.related_accrual_max_level" value="%{accrualWarning.related_accrual_max_level}" size="6" /><br />
				(Related prefered max level of accrual if any. Anything more than that the employee is required to use before using other accruals)
			</div>

			<div class="form-group">
				<p>The following three text fields will be used to display the warning text if the related condition was not met.</p>
			</div>

			<div class="form-group">
				<label>Step Warning</label>
				<s:textfield name="accrualWarning.step_warning_text" value="%{accrualWarning.step_warning_text}" size="40" maxlength="80" /><br />
				(if step hour is required , the warning text)
			</div>

			<div class="form-group">
				<label>Min Warning </label>
				<s:textfield name="accrualWarning.min_warning_text" value="%{accrualWarning.min_warning_text}" size="40" maxlength="80" /><br />
				(if min hours is required , the warning text)
			</div>

			<div class="form-group">
				<label>Excess Warning </label>
				<s:textfield name="accrualWarning.excess_warning_text" value="%{accrualWarning.excess_warning_text}" size="40" maxlength="80" required="true" /><br />
				(if excess hours are used, the warning text)
			</div>

			<s:if test="accrualWarning.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>

			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url' />accrualWarning.action" class="button">New Warning</a>
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>
	<s:if test="hasAccrualWarnings()">
		<s:set var="accrualWarnings" value="accrualWarnings" />
		<s:set var="accrualWarningsTitle" value="accrualWarningsTitle" />
		<%@ include file="accrualWarnings.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>