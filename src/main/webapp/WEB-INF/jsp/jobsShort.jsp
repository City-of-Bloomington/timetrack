<table class="width-full">
	<caption><s:property value="#jobsTitle" /></caption>
	<thead>
		<tr>
			<th>Job ID</th>
			<th>Employee</th>			
			<th>Job Title</th>
			<th>Salary Group</th>
			<th>Group</th>
			
			<th>Changes</th>
			<th>Effective Date</th>
			<th>ID card #</th>
			<th>Employee NW #</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#jobs">
			<tr>
				<td><s:property value="id" /></td>
				<td><s:property value="employee" /></td>				
				<td><s:property value="position" /></td>
				<td><s:property value="salaryGroup" /></td>
				<td><s:property value="group" /></a></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</s:iterator>
	</tbody>
</table>
