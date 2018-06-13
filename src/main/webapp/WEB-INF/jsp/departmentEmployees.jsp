<h1><s:property value="#departmentEmployeesTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Employee</th>
			<th>Department</th>
			<th>Seconday Department</th>
			<th>Start Date</th>
			<th>Expire Date</th>
			<th>Active</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#departmentEmployees">
			<tr>
				<td><a href="<s:property value='#application.url' />departmentEmployee.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="employee" /></td>
				<td><s:property value="department" /></td>
				<td><s:if test="hasSecondaryDept()"><s:property value="department2" /></s:if>&nbsp;</td>
				<td><s:property value="effective_date" /></td>
				<td><s:property value="expire_date" /></td>
				<td><s:if test="active">Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
