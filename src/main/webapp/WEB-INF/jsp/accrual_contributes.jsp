<h1><s:property value="#contributesTitle" /></h1>
<table class="hour-codes">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Accrual</th>
			<th>Hour Code Contributer</th>
			<th>Multiple Factor</th>
		</tr>
	<tbody>
		<s:iterator var="one" value="#contributes">
			<tr>
				<td><a href="<s:property value='#application.url' />contribute.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="accrual" /></td>
				<td><s:property value="hourCode" /></td>
				<td><s:property value="factor" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
