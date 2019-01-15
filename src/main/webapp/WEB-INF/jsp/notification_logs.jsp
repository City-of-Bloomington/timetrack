<div class="internal-page">
	<h1>Recent Notification Logs</h1>
	<table class="email-logs">
		<thead>
			<tr>
				<th>Date Time</th>
				<th>Receipiants</th>
				<th>Message</th>
				<th>Status</th>
				<th>Errors (if any)</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator var="one" value="#logs">
				<tr>
					<td><s:property value="dateTime" /></td>
					<td><s:property value="receipiants" /></td>
					<td><s:property value="message" /></td>
					<td><s:property value="status" /></td>
					<td><s:property value="error_msg" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>

