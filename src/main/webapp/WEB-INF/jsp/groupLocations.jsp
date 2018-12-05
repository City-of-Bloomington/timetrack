<h1><s:property value="#groupManagersTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Group</th>
			<th>Location</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#groupLocations">
			<tr>
				<td><a href="<s:property value='#application.url' />groupLocation.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="group" /></td>
				<td><s:property value="location" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
