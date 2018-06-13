<h1><s:property value="#typesTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Description</th>
			<th>Inactive?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#types">
			<tr>
				<td><a href="<s:property value='#application.url' />type.action?id=<s:property value='id' />&type_name=<s:property value='type_name'/>">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="description" /></td>
				<td><s:if test="inactive">Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
