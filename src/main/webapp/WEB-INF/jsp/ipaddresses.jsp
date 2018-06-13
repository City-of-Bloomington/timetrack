<h1><s:property value="#ipaddressesTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>IP</th>
			<th>Description</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#ipaddresses">
			<tr>
				<td><a href="<s:property value='#application.url' />ipaddress.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="ip_address" /></td>
				<td><s:property value="description" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
