<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="salaryGroup" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="salaryGroup.id == ''">
			<h1>New Salary Group</h1>
		</s:if>

		<s:else>
			<h1>Edit Salary Group </h1>
			<s:hidden name="salaryGroup.id" value="%{salaryGroup.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

		<div class="width-one-half">
			<s:if test="type.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="salaryGroup.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Name</label>
				<s:textfield name="salaryGroup.name" value="%{salaryGroup.name}" size="30" maxlength="70" requiredLabel="true" required="true" id="type_name_id" />
			</div>

			<div class="form-group">
				<label>Description</label>
				<s:textarea name="salaryGroup.description" value="%{salaryGroup.description}" rows="5" maxlength="50" />
			</div>

			<div class="form-group">
				<label>Default Reg Code</label>
				<s:select name="salaryGroup.default_regular_id" value="%{salaryGroup.default_regular_id}" list="hourCodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Default Hour Code"/><br />
				(Each salary group need one default regular hour code)
			</div>

			<div class="form-group">
				<label>Inactive?</label>
				<s:checkbox name="salaryGroup.inactive" value="%{salaryGroup.inactive}" fieldValue="true" />Yes
			</div>

			<s:if test="salaryGroup.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/></dd>
			</s:if>

			<s:else>
				<s:submit name="action" type="button" value="Save Changes" class="button"/>
			</s:else>
		</div>
	</s:form>

	<s:if test="salaryGroups != null">
		<s:set var="salaryGroups" value="salaryGroups" />
		<s:set var="salaryGroupsTitle" value="'Salary Groups'" />
		<%@ include file="salaryGroups.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>