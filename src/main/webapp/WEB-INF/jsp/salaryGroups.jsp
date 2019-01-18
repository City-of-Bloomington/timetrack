<h1><s:property value="#salaryGroupsTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Description</th>
			<th>Default Regular Code</th>
			<th>Excess Culculation Type</th>
			<th>Inactive?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#salaryGroups">
			<tr>
				<td><a href="<s:property value='#application.url' />salaryGroup.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="description" /></td>
				<td><s:property value="defaultRegularCode" /></td>
				<td><s:property value="excess_culculation" /></td>
				<td><s:if test="inactive">Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
