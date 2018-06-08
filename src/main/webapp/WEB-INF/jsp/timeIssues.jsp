<s:if test="document.hasTimeIssues()">
	<s:set var="timeIssuesTitle" value="'Outstanding Issues'" />
	<s:set var="timeIssues" value="document.timeIssues" />
	<h1><s:property value="#timeIssuesTitle" /></h1>
	<table class="time-issues width-full">
		<tr>
			<th>ID</th>
			<th>Time block ID</th>
			<th>Reported By</th>
			<th>Date & Time</th>
			<th>Issue</th>
			<th>Status</th>
			<th>Closed Date</th>
			<th>Closed By</th>
		</tr>
		<s:iterator var="one" value="#timeIssues">
			<tr>
				<s:if test="isOpen()">
					<td><a href="<s:property value='#application.url' />handleIssue.action?id=<s:property value='id' />">Handle Issue</a></td>
					<td><a href="#" onclick="return popwit('<s:property value='#application.url' />timeBlock?id=<s:property value='time_block_id' />','timeBlock');">Edit Clock-In/Clock-Out Times</a></td>
				</s:if>
				<s:else>
					<td><s:property value='id' /></td>
					<td><s:property value="time_block_id" /></td>
				</s:else>
				<td><s:property value="reporter" /></td>
				<td><s:property value="date" /></td>
				<td><s:property value="issue_notes" /></td>
				<td><s:property value="status" /></td>
				<td><s:property value="closed_date" /></td>
				<td><s:property value="closed_by" /></td>
			</tr>
		</s:iterator>
	</table>
</s:if>