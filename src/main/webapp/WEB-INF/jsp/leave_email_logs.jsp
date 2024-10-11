<%@ include file="header.jsp" %>
<div class="internal-page">
    <h1> Leave Request and Review Email Logs </h1>	
    <s:if test="hasMessages()">
	<s:set var="messages" value="%{messages}" />
	<%@ include file="messages.jsp" %>
    </s:if>
    <s:if test="hasErrors()">
	<s:set var="errors" value="%{errors}" />
	<%@ include file="errors.jsp" %>
    </s:if>
    <div>
	<s:if test="!hasLogs()">
	    <p>No records found </p>
	</s:if>
	<s:else>
	    <h2>Most recent leave email logs </h2>
	    <table>
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
