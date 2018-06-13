<h1><s:property value="#nodesTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Description</th>
			<th>Managers Only</th>
			<th>Annotation</th>
			<th>Active?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#nodes">
			<tr>
				<td><a href="<s:property value='#application.url' />node.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="description" /></td>
				<td><s:if test="managers_only">Yes</s:if><s:else>No</s:else></td>
				<td><s:property value="annotation" /></td>
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>