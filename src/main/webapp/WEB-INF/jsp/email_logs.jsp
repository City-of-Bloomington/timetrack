
<%@  include file="header.jsp" %>
<div class="internal-page container clearfix">

<s:if test="hasEmailLogs()">
	<h1>Most Recent Email Logs</h1>
	<table class="width-full">
		<tr>
			<th>Date Time</th>
			<th>Type</th>
			<th>Performed By</th>
			<th>Email From</th>
			<th>Email Receipiants</th>
			<th>Email Subject</th>
			<th>Message</th>
			<th>Status</th>
			<th>Send Errors</th>
		</tr>
		<s:iterator var="one" value="emailLogs">
			<tr>
				<td><s:property value="dateTime" /></td>
				<td><s:property value="type" /></td>
				<td><s:property value="runBy" /></td>
				<td><s:property value="emailFrom" /></td>
				<td><s:property value="receipiants" /></td>
				<td><s:property value="subject" /></td>
				<td><s:property value="textMessage" /></td>
				<td><s:property value="status" /></td>
				<td><s:property value="sendErrors" /></td>
			</tr>
		</s:iterator>
	</table>
</s:if>
<s:else>
	<h3> No email logs found </h3> 
</s:else>
<%@ include file="footer.jsp" %>
