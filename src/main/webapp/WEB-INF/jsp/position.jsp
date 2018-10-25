<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="position" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="position.id == ''">
			<h1>New Position</h1>
		</s:if>
		<s:else>
			<h1>Edit <s:property value="%{position.name}" /></h1>
			<s:hidden name="position.id" value="%{position.id}" />
		</s:else>
		<s:if test="hasMessages()">
			<s:set var="messages" value="messages" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="errors" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>
	  <div class="width-one-half">
			<s:if test="position.id != ''">
				<div class="form-group">
					<label>ID: <s:property value="position.id" /> </label>
				</div>
			</s:if>
			<div class="form-group">
				<label>Name</label>
				<s:textfield name="position.name" value="%{position.name}" size="30" maxlength="64" requiredLabel="true" required="true" id="position_name_id" />
			</div>
			<div class="form-group">
				<label>Alias</label>
				<s:textfield name="position.alias" value="%{position.alias}" size="30" maxlength="64" requiredLabel="true" required="true" />
			</div>
			<div class="form-group">
				<label>Description</label>
				<s:textarea name="position.description" value="%{position.description}" rows="5" cols="50" />
			</div>
			<div class="form-group">
				<label>Inactive?</label>
				<s:checkbox name="position.inactive" value="%{position.inactive}" fieldValue="true" />Yes
			</div>
			<s:if test="position.id == ''">
				<s:submit name="action" position="button" value="Save" class="button"/></dd>
			</s:if>
			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url'/>position.action" class="button">New Position</a>&nbsp;&nbsp;
					<s:submit name="action" position="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>
	<s:if test="position.id == ''">
		<s:if test="positions != null">
			<s:set var="positions" value="positions" />
			<s:set var="positionsTitle" value="positionsTitle" />
			<%@ include file="positions.jsp" %>
		</s:if>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
