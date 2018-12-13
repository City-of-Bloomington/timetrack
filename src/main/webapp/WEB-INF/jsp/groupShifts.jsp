<h1><s:property value="#groupShiftsTitle" /></h1>
<table class="groups width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Group</th>
			<th>Shift</th>
			<th>Start Date</th>
			<th>Expire Date</th>			
			<th>Active?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#groupShifts">
			<tr>
				<td><a href="<s:property value='#application.url' />groupShift.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="group" /></td>
				<td><s:property value="shift" /></td>
				<td><s:property value="startDate" /></td>
				<td><s:property value="expireDate" /></td>				
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
