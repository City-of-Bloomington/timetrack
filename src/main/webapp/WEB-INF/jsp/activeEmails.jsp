<%@ include file="header.jsp" %>
<div class="internal-page">
	<h1><s:property value="#activeEmailsTitle" /></h1>	
	<s:form action="activeEmails" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:if test="hasErrors()">
			<s:set var="errors" value="%{errors}" />
			<%@ include file="errors.jsp" %>
		</s:if>
		<p>The list of active employees who used timetrack within the last two weeks</p>
		<div class="width-one-half">
			<div class="form-group">
				<label>Department</label>
				<s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" />
			</div>
			<s:submit name="action" type="button" value="Submit" class="button"/>			
		</div>
	</s:form>
</div>
<s:if test="hasEmployees()">
    <table class="employees width-full">
	<thead>
	    <tr>
		<th>Full Name</th>
		<th>Email</th>
	    </tr>
	</thead>
	<tbody>
	    <s:iterator var="one" value="employees">
		<tr>
		    <td><s:property value="full_name" /></td>
		    <td><s:property value="email" /></td>
		</tr>
	    </s:iterator>
	</tbody>
    </table>
</s:if>
<%@ include file="footer.jsp" %>
