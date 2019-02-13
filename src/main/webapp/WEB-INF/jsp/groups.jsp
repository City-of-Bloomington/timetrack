<h1><s:property value="#groupsTitle" /></h1>
<table class="groups width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Department</th>
			<th>Excess Hours Calculation Method</th>			
			<th>Description</th>
			<th>Active?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#groups">
			<tr>
				<td><a href="<s:property value='#application.url' />group.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="department" /></td>
				<td><s:property value="excessHoursCalculationMethod" /></td>
				<td><s:property value="description" /></td>
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
