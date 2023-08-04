<%@  include file="header.jsp" %>
<div class="internal-page">
    <h1> Termination Notification Emails</h1>
    <s:if test="hasErrors()">
	<s:set var="errors" value="errors" />
	<%@ include file="errors.jsp" %>		
    </s:if>
    <div class="width-one-half">
	<s:form action="termNotifications" id="form_id" method="post">
	    <div class="form-group">
		<label>Date Range From:</label>
		<div class="date-range-picker">
		    <div>
			<s:textfield name="date_from" value="%{date_from}" size="10" maxlength="10" />
		    </div>
		</div>
	    </div>
	    <div class="form-group">
		<label>Date Range To:</label>
		<div class="date-range-picker">
		    <div>				
			<s:textfield name="date_to" value="%{date_to}" size="10" maxlength="10" />
		    </div>
		</div>
	    </div>
	    <div class="button-group">
		<s:submit name="action" type="button" value="Find" />
	    </div>
	</s:form>
    </div>
    <s:if test="hasNotifications()">
	<table class="email-logs">
	    <thead>
		<tr>
		    <th>ID </th>
		    <th>Sent Time</th>
		    <th>Sent By</th>
		    <th>Termination</th>
		    <th>Recipients</th>
		    <th>Message</th>
		    <th>Status</th>
		    <th>Send Errors</th>
		</tr>
	    </thead>
	    <tbody>
		<s:iterator var="one" value="notifications">
		    <tr>
			<td><s:property value="id" /></td>			
			<td><s:property value="sentTime" /></td>
			<td><s:property value="sender" /></td>
			<td><s:property value="termination_id" /></td>
			<td><s:property value="receipiantEmails" /></td>
			<td><s:property value="emailText" /></td>
			<td><s:property value="status" /></td>
			<td><s:property value="sendError" /></td>
		    </tr>
		</s:iterator>
	    </tbody>
	</table>
	</s:if>
	
	<s:else>
	    <h3> No Termination Notificaiton found </h3>
	</s:else>

</div>
<%@ include file="footer.jsp" %>
