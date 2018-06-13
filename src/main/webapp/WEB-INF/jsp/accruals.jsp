<h1><s:property value="#accrualsTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Description</th>
			<th>Pref Max Level</th>
			<th>Inactive?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#accruals">
			<tr>
				<td><a href="<s:property value='#application.url' />accrual.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="description" /></td>
				<td><s:property value="pref_max_level" /></td>
				<td><s:if test="inactive">Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
