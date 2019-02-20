<table class="width-full">
	<caption><s:property value="#jobsTitle" /></caption>
	<thead>
		<tr>
			<th>Job ID</th>
			<th>Employee</th>			
			<th>Job Title</th>
			<th>Salary Group</th>
			<th>Group</th>
			<th>ID card #</th>
			<th>Employee NW #</th>
			<th>Changes</th>
			<th>Effective Date</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#jobs">
			<tr>
				<td><s:property value="id" /></td>
				<td><s:property value="employee.nameLastFirst" /></td>				
				<td><s:property value="position" /></td>
				<td><s:property value="salaryGroup" /></td>
				<td><s:property value="group" /></a></td>
				<td><s:property value="employee.employee_number" /></td>
				<td><s:property value="employee.id_code" /></td>		
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</s:iterator>
	</tbody>
</table>
