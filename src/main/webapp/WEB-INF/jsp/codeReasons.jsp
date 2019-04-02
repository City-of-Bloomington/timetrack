<h1><s:property value="#reasonsTitle" /></h1>
<table class="hour-codes">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Description</th>
			<th>Active?</th>
		</tr>
	<tbody>
		<s:iterator var="one" value="#reasons">
			<tr>
				<td><a href="<s:property value='#application.url' />codeReason.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="description" /></td>
				<td><s:if test="active">Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
