<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="accrual" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="accrual.id == ''">
			<h1>New Accrual Type</h1>
		</s:if>
		<s:else>
			<h1>Edit Accrual Type: <s:property value="%{accrual.name}" /> </h1>
			<s:hidden name="accrual.id" value="%{accrual.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

		<div class="width-one-half">
			<p><strong>Note:</strong> Pref Max Level below is the max number of hours that the employee should not pass by using the relate earn code instead of PTO for example.</p>

			<s:if test="accrual.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="accrual.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Name</label>
				<s:textfield name="accrual.name" value="%{accrual.name}" size="30" maxlength="70" requiredLabel="true" required="true" />
			</div>

			<div class="form-group">
				<label>Description</label>
				<s:textarea name="accrual.description" value="%{accrual.description}" rows="5" maxlength="50" required="true"/>
			</div>

			<div class="form-group">
				<label>Pref Max Level</label>
				<s:textfield name="accrual.pref_max_level" value="%{accrual.pref_max_level}" size="2" maxlength="2" />(The max level the employee should keep)
			</div>

			<div class="form-group">
				<label>Inactive?</label>
				<s:checkbox name="accrual.inactive" value="%{accrual.inactive}" fieldValue="true" />Yes
			</div>

			<s:if test="accrual.id == ''">
				<s:submit name="action" accrual="button" value="Save" class="button"/>
			</s:if>
			<s:else>
				<s:submit name="action" accrual="button" value="Save Changes" class="button"/>
			</s:else>
		</div>
	</s:form>
	<s:if test="accruals != null">
		<s:set var="accruals" value="accruals" />
		<s:set var="accrualsTitle" value="accrualsTitle" />
		<%@ include file="accruals.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
