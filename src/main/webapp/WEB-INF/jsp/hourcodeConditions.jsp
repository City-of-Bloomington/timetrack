<h1><s:property value="#hourcodeConditionsTitle" /></h1>
<table class="hour-codes width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Hour Code</th>
			<th>Salary Group</th>			
			<th>Department</th>
			<th>Group</th>
			<th>Date</th>
			<th>Active?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#hourcodeConditions">
			<tr>
				<td><a href="<s:property value='#application.url' />hourcodeCondition.action?id=<s:property value='id' />"><s:property value="id" /></a></td>
				<td><s:property value="hourCode" /></td>
				<td><s:property value="salaryGroup" /></td>				
				<td><s:property value="department" /></td>
				<td><s:if test="group_id == -1">All</s:if><s:else><s:property value="group" /></s:else></td>
				<td><s:property value="date" /></td>
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
