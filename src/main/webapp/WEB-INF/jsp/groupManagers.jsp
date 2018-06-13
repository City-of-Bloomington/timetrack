<h1><s:property value="#groupManagersTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Group</th>
			<th>Employee</th>
			<th>Workflow Action</th>
			<th>Start Date</th>
			<th>Expire Date</th>
			<th>Active?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#groupManagers">
			<tr>
				<td><a href="<s:property value='#application.url' />groupManager.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="group" /></td>
				<td><s:property value="employee" /></td>
				<td><s:property value="node" /></td>
				<td><s:property value="start_date" /></td>
				<td><s:property value="expire_date" /></td>
				<td><s:if  test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
