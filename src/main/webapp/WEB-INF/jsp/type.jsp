<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="type" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="type_name" value="%{type_name}" />

		<s:if test="type.id == ''">
			<h1>New <s:property value="title" /></h1>
		</s:if>

		<s:else>
			<h1>Edit <s:property value="title" /></h1>
			<s:hidden name="type.id" value="%{type.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

	  <div class="width-one-half">
			<s:if test="type.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="type.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Name</label>
				<s:textfield name="type.name" value="%{type.name}" size="30" maxlength="70" requiredLabel="true" required="true" id="type_name_id" />
			</div>

			<div class="form-group">
				<label>Description</label>
				<s:textarea name="type.description" value="%{type.description}" rows="5" maxlength="50" />
			</div>

			<div class="form-group">
				<label>Inactive?</label>
				<s:checkbox name="type.inactive" value="%{type.inactive}" fieldValue="true" />Yes
			</div>

			<s:if test="type.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/></dd>
			</s:if>

			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url'/>type.action?type_name=<s:property value='type_name' />" class="button">New <s:property value="type_name" /></a>
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>

	<s:if test="type.id != ''">
		<s:if test="groups != null">
			<s:set var="groups" value="groups" />
			<s:set var="groupsTitle" value="'Groups in this Department'" />
			<%@ include file="groups.jsp" %>
		</s:if>
	</s:if>

	<s:else>
		<s:if test="types != null">
			<s:set var="types" value="types" />
			<s:set var="typesTitle" value="typesTitle" />
			<s:set var="type_name" value="type_name" />
			<%@ include file="types.jsp" %>
		</s:if>
	</s:else>
</div>
<%@ include file="footer.jsp" %>