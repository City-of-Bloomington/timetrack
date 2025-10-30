<%@ include file="header.jsp" %>
<div class="internal-page">
    <h1>Search Leave Request Email Logs </h1>	
    <s:if test="hasMessages()">
	<s:set var="messages" value="%{messages}" />
	<%@ include file="messages.jsp" %>
    </s:if>
    <s:if test="hasErrors()">
	<s:set var="errors" value="%{errors}" />
	<%@ include file="errors.jsp" %>
    </s:if>
    <s:form action="leave_email_logs" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<div class="width-one-half">
	    <p>You may filter the list by: </p>
	    <div class="form-group">
		<label for="email_from">Leave requester username/email</label>
		<s:textfield name="lel.email_from" value="%{lel.email_from}" size="10" id="email_from" />
	    </div>
	    <div class="form-group">
		<label for="email_to">Leave reviewer username/email</label>
		<s:textfield name="lel.email_to" value="%{lel.email_to}" size="10" id="email_to" />
	    </div>
	    <div class="form-group">
		<label for="date_from">Leave request date range from</label>
		<s:textfield name="lel.date_from" value="%{lel.date_from}" size="10" id="date_from" />
	    </div>
	    <div class="form-group">
		<label for="date_to">Leave request date range to</label>
		<s:textfield name="lel.date_to" value="%{lel.date_to}" size="10" id="email_to" />
	    </div>
	    <div class="button-group">
		<s:submit name="action" type="button" value="Submit" class="button"/>
	    </div>
	</div>	    
    </s:form>
    <div>
	<s:if test="!hasLogs()">
	    <p>No records found </p>
	</s:if>
	<s:else>
	    <table>
		<caption><s:property value="logsTitle" /></caption>		
		<tr>
		    <th>Email Date</th>
		    <th>Leave Type</th>
		    <th>From </th>
		    <th>To </th>
		    <th>Email Message</th>
		    <th>Sent Status</th>
		    <th>Error Msg</th>
		</tr>
		<s:iterator var="one" value="logs">
		    <tr>
			<td><s:property value="date" /></td>
			<td><s:property value="email_type" /></td>
			<td><s:property value="email_to" /></td>
			<td><s:property value="email_from" /></td>
			<td><s:property value="message" /></td>
			<td><s:property value="status" /></td>
			<td><s:property value="error_msg" /></td>
		    </tr>
		</s:iterator>
	    </table>
	</s:else>
    </div>
</div>
<%@ include file="footer.jsp" %>
