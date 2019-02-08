<h1><s:property value="#employeesTitle" /></h1>
<table class="employees width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Username</th>
			<th>Full Name</th>
			<th>ID Code #</th>
			<th>Employee #</th>
			<th>Email</th>
			<th>Role</th>
			<th>Added Date</th>			
			<th>Active?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#employees">
			<tr>
				<td><a href="<s:property value='#application.url' />employee.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="username" /></td>
				<td><s:property value="full_name" /></td>
				<td><s:property value="id_code" /></td>
				<td><s:property value="employee_number" /></td>
				<td><s:property value="email" /></td>
				<td><s:property value="rolesText" /></td>
				<td><s:property value="added_date" /></td>				
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
