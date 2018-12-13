<h1><s:property value="#shiftsTitle" /></h1>
<table class="groups width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Start Hour:Minute</th>
			<th>Duration</th>
			<th>Start Minutes Window</th>
			<th>Minutes Rounding</th>
			<th>Prefered Earn Time</th>
			<th>Active?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#shifts">
			<tr>
				<td><a href="<s:property value='#application.url' />shift.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="startHourMinute" /></td>
				<td><s:property value="duration" /></td>
				<td><s:property value="startMinuteWindow" /></td>
				<td><s:property value="minuteRounding" /></td>
				<td><s:property value="preferedEarnTimeName" /></td>
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
