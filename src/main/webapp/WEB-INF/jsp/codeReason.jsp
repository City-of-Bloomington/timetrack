<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="codeReason" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="reason.id == ''">
			<h1>New Earn Code Reason</h1>
		</s:if>
		<s:else>
			<h1>Edit Earn Code Reason: <s:property value="reason.name" /></h1>
			<s:hidden name="reason.id" value="%{reason.id}" />
		</s:else>
		<s:if test="hasErrors()">
			<%@ include file="errors.jsp" %>
		</s:if>		
		<s:elseif test="hasMessages()">
			<%@ include file="messages.jsp" %>
		</s:elseif>
		<div class="width-one-half">
			<s:if test="reason.id != ''">
				<div class="form-group">
					<label>ID:</label>
					<s:property value="%{reason.id}" />
				</div>
			</s:if>
			<div class="form-group">
				<label>Name:</label>
				<s:textfield name="reason.name" value="%{reason.name}" required="true" size="20" maxlength="20" />
			</div>
			<div class="form-group">
				<label>Description:</label>
				<s:textfield name="reason.description" value="%{reason.description}" size="30" maxlength="80" />
			</div>
			<s:if test="reason.id != ''">
				<div class="form-group">
					<label>Inactive?:</label>
					<s:checkbox name="reason.inactive" value="%{reason.inactive}" /> Yes (check to dissable)
				</div>
			</s:if>
		</div>
		<div class="button-group">
			<s:if test="reason.id == ''">
				<s:submit name="action" type="button" value="Save" />
			</s:if><br />
			<s:else>
				<a href="<s:property value='#application.url' />codeReason.action?" class="button">New Earn Code Reason </a>
				<s:submit name="action" type="button" value="Save Changes" />
			</s:else>				
		</div>
	</s:form>
	<s:if test="hasReasons()">
		<s:set var="reasons" value="%{reasons}" />
		<s:set var="reasonsTitle" value="reasonsTitle" />
		<%@ include file="codeReasons.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
