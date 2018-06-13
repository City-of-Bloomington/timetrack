<h1><s:property value="#holidaysTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Date</th>
			<th>Name</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#holidays">
			<tr>
				<td><a href="<s:property value='#application.url' />holiday.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="date" /></td>
				<td><s:property value="description" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
