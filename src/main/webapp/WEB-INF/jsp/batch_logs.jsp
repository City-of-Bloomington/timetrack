<h1><s:property value="#employeesTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Employee Names</th>
			<th>Run By</th>
			<th>Date & Time</th>
			<th>Status</th>
			<th>Failure Reasons</th>			
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#batch_logs">
			<tr>
				<td><s:property value="id" /></td>				
				<td><s:property value="names" /></td>
				<td><s:property value="runner" /></td>
				<td><s:property value="date" /></td>				
				<td><s:property value="status" /></td>
				<td><s:property value="failureReason" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
