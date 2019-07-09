<h1><s:property value="#groupEmployeesTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Group</th>
			<th>Employee</th>
			<th>Effective Date</th>
			<th>Expire Date</th>
			<th>Active?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#groupEmployees">
			<tr>
				<td><a href="<s:property value='#application.url' />groupEmployee.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><a href="<s:property value='#application.url' />group.action?id=<s:property value='group_id' />"> <s:property value="group" /></a></td>				
				<td><a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='employee.id' />"> <s:property value="employee" /></a>
				</td>
				<td><s:property value="effective_date" /></td>
				<td><s:property value="expire_date" /></td>
				<td><s:if  test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
