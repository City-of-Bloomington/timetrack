<h1><s:property value="#typesTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Alias</th>			
			<th>Description</th>
			<th>Inactive?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#positions">
			<tr>
				<td><a href="<s:property value='#application.url' />position.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="alias" /></td>				
				<td><s:property value="description" /></td>
				<td><s:if test="inactive">Yes</s:if><s:else>&nbsp;</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
