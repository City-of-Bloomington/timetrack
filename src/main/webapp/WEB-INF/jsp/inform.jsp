<%@ include file="header.jsp" %>

<div>
	<s:form action="inform" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<h1>Email Inform Employees</h1>
		The following employees will be informed about <s:property value="inform_type" /> <br />
		Uncheck the ones you do not want to be included in the email.<br />
		Then modify the email message below as needed. <br />
		You may add your email or other email addresses for information purpose <br />
		<ul>
		<s:iterator var="one" value="employees" status="row">
			<li><s:checkbox name="employee_ids" value="<s:property value='id' />" checked="checked" /><s:property value="full_name" /></li>
		</s:iterator>
		</ul>
		<div class="form-group">
		<label>Email CC:</label>
		<s:textfield name="email_cc" value="" size="50" maxlength="80" />
		</div>
		<div class="form-group">
			<label>Email Subject:</label>
			<s:textfield name="subject" value="%{subject}" size="50" maxlength="80" />
		</div>	
		<div class="form-group">
			<label>Email Message:</label>
			<s:textarea name="text_message" value="%{text_message}" rows="12" cols="60" />
		</div>
		<s:submit name="action" type="button" value="Send" />
	</s:form>
</div>
<%@ include file="footer.jsp" %>
