<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="empsUpdateSchedule" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<h1>Current Employees Update</h1>

	  <%@ include file="strutMessages.jsp" %>

	  <p>This function is designed to run on a schedule once every week to update current employees info and mark inactive ones using data from acitve directory (ldap or AD).</p>

		<p>Run the Schedular if the 'Next Schedule Date' and 'Previous Schedule Date' are not shown.</p>

		<p>To verify current employees today click on 'Submit' button, we may need to run this manually if the scheduling fails.</p>

		<p>For new Employees use 'Import AD (ldap) Employee' option in the setting menu.</p>

		<div class="width-one-half">
			<s:if test="hasPrevDates()">
				<div class="form-group">
					<label>Next Schedule Date</label>
					<s:property value="%{next_date}" />
				</div>

				<div class="form-group">
					<label>Previous Schedule Date</label>
					<s:property value="%{prev_date}" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Date</label>
				<s:textfield name="date" value="%{date}" size="10" maxlength="10" cssClass="date" />
			</div>

			<div class="button-group">
				<s:submit name="action" accrual="button" value="Schedule" class="fn1-btn"/>
				<s:submit name="action" accrual="button" value="Submit" class="fn1-btn"/></dd>
			</div>
		</div>
	</s:form>
</div>
<%@ include file="footer.jsp" %>