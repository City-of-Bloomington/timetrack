<h1><s:property value="#shiftsTitle" /></h1>
<table class="groups width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Group</th>
			<th>Pay Period</th>
			<th>Hour Code</th>
			<th>Start time, end time</th>
			<th>Dates</th>
			<th>Added</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#shifts">
			<tr>
				<td><a href="<s:property value='#application.url' />shiftTime.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="group" /></td>
				<td><s:property value="payPeriod" /></td>
				<td><s:property value="defaultHourCode" /></td>
				<td><s:property value="startEndTimes" /></td>
				<td><s:property value="dates" /></td>				
				<td><s:property value="addedTime" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
