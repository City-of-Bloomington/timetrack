<h1><s:property value="#categoriesTitle" /></h1>
<table class="hour-codes">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
		</tr>
	<tbody>
		<s:iterator var="one" value="#categories">
			<tr>
				<td><a href="<s:property value='#application.url' />reasonCategory.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="name" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
