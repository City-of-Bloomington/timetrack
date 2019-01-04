<h1><s:property value="#shiftsTitle" /></h1>
<table class="groups width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Start Hour:Minute</th>
			<th>Duration</th>
			<th>Shift Start Minutes Window</th>
			<th>Shift End Minutes Window</th>			
			<th>Minutes Rounding</th>
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
				<td><s:property value="endMinuteWindow" /></td>				
				<td><s:property value="minuteRounding" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
