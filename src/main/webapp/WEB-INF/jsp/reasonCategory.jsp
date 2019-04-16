<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="reasonCategory" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="reasonCategory.id == ''">
			<h1>Code Reason Category</h1>
		</s:if>
		<s:else>
			<h1>Code Reason Category: <s:property value="reasonCategory.name" /></h1>
			<s:hidden name="reasonCategory.id" value="%{reasonCategory.id}" />
		</s:else>
		<s:if test="hasErrors()">
			<s:set var="errors" value="%{errors}" />		
			<%@ include file="errors.jsp" %>
		</s:if>		
		<s:elseif test="hasMessages()">
			<s:set var="messages" value="%{messages}" />					
			<%@ include file="messages.jsp" %>
		</s:elseif>
		<div class="width-one-half">
			<s:if test="reasonCategory.id != ''">
				<div class="form-group">
					<label>ID:</label>
					<s:property value="%{reasonCategory.id}" />
				</div>
			</s:if>
			<div class="form-group">
				<label>Name:</label>
				<s:textfield name="reasonCategory.name" value="%{reasonCategory.name}" required="true" size="30" maxlength="50" />
			</div>
		</div>
		<div class="button-group">
			<s:if test="reasonCategory.id == ''">
				<s:submit name="action" type="button" value="Save" />
			</s:if><br />
			<s:else>
				<a href="<s:property value='#application.url' />reasonCategory.action?" class="button">New Reason Category</a>
				<s:submit name="action" type="button" value="Save Changes" />
			</s:else>				
		</div>
	</s:form>
	<s:if test="hasCategories()">
		<s:set var="categories" value="%{categories}" />
		<s:set var="categoriesTitle" value="categoriesTitle" />
		<%@ include file="reasonCategories.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
