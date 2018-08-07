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
	<s:form action="empImport" id="form_id" method="post" enctype="multipart/form-data">
		<h1>Employees Data Import Using CSV file</h1>
		<ul>
			<li>Before you do data import, it is preferred you backup the database first</li>
			<li>If during the import process errors occurred, you need to fix the errors on your csv file, then do Rollback and then do import again</li>
		</ul>
	  <%@ include file="strutMessages.jsp" %>
	  <div class="width-full">
			<div class="form-group">
				<label>Upload File</label>
				<input type="file" name="csv_file" value="Pick a CSV file to upload" size="30" maxlength="70" required="true" />
			</div>
			<s:submit name="action" holiday="button" value="Import" class="button"/>
		</div>
	</s:form>
	<br />
	<s:form action="empImport" id="form_id2" method="post">		
		<h1>Employees Import Rollback</h1>
		</p>
		<div class="width-full">
			<div class="form-group">
				<label>&nbsp;</label>
				<label>&nbsp;</label>
			</div>	
			<s:submit name="action" holiday="button" value="Rollback" class="button"/>
		</div>
	</s:form>
</div>
<%@ include file="footer.jsp" %>
