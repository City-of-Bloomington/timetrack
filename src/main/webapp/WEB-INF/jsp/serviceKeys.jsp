<h1><s:property value="#keysTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Value</th>
			<th>Active?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#keys">
			<tr>
				<td><a href="<s:property value='#application.url' />serviceKey.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="keyName" /></td>
				<td><s:property value="keyValue" /></td>
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
