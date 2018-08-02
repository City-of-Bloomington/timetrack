<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:if test="hasErrors()">
		<s:set var="errors" value="errors" />
		<%@ include file="errors.jsp" %>
	</s:if>
	<s:elseif test="hasMessages()">
		<s:set var="messages" value="messages" />
		<%@ include file="messages.jsp" %>
	</s:elseif>
	<s:form action="empImport" id="form_id" method="post" >
		<h1>Employees Date Import Using CSV file</h1>
	  <%@ include file="strutMessages.jsp" %>
	  <div class="width-full">
			<div class="form-group">
				<label>CSV File</label>
				<s:textfield name="file_name" value="%{file_name}" size="30" maxlength="70" required="true" />
			</div>
			<s:submit name="action" holiday="button" value="Import" class="button"/>
		</div>
	</s:form>
</div>
<%@ include file="footer.jsp" %>
