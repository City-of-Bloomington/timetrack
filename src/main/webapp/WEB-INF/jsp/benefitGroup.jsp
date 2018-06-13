<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="benefitGroup" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="benefitGroup.id == ''">
			<h1>New Benefit Group</h1>
		</s:if>
		<s:else>
			<h1>Edit Benefit Group: <s:property value="%{benefitGroup.name}" /> </h1>
			<s:hidden name="benefitGroup.id" value="%{benefitGroup.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

		<div class="wilabelh-one-half">
			<s:if test="benefitGroup.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="benefitGroup.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Group Code</label>
				<s:textfield name="benefitGroup.name" value="%{benefitGroup.name}" size="20" maxlength="20" requiredLabel="true" required="true" />
			</div>

			<div class="form-group">
				<label>Full Time?</label>
				<s:checkbox name="benefitGroup.fullTime" value="%{benefitGroup.fullTime}" />Yes
			</div>

			<div class="form-group">
				<label>Exempt?</label>
				<s:checkbox name="benefitGroup.exempt" value="%{benefitGroup.exempt}" />Yes
			</div>

			<div class="form-group">
				<label>Unioned?</label>
				<s:checkbox name="benefitGroup.unioned" value="%{benefitGroup.unioned}" />Yes
			</div>

			<s:if test="benefitGroup.id == ''">
				<s:submit name="action" benefitGroup="button" value="Save" class="button"/>
			</s:if>

			<s:else>
				<div class="button-group">
					<s:submit name="action" benefitGroup="button" value="Delete" class="button"/>
					<s:submit name="action" benefitGroup="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>

	<s:if test="benefitGroups != null">
		<s:set var="benefitGroups" value="benefitGroups" />
		<s:set var="benefitGroupsTitle" value="benefitGroupsTitle" />
		<%@ include file="benefitGroups.jsp" %>
	</s:if>
<%@ include file="footer.jsp" %>