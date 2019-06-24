<h1><s:property value="#departmentsTitle" /></h1>
<table class="departments width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Ldap Name</th>
			<th>Referance ID(s)</th>
			<th>Description</th>
			<th>Pending Accrual Allowed?</th>
			<th>Inactive?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#departments">
			<tr>
				<td><a href="<s:property value='#application.url' />department.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="ldap_name" /></td>
				<td><s:property value="ref_id" /></td>
				<td><s:property value="description" /></td>
				<td><s:if test="allowPendingAccrual">Yes</s:if><s:else>No</s:else></td>									
				<td><s:if test="inactive">Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
