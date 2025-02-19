<h1><s:property value="#groupLeavesTitle" /></h1>
<table class="hour-codes width-full">
    <thead>
	<tr>
	    <th>ID</th>
	    <th>Group</th>
	    <th>Salary Group</th>			
	    <th>Active?</th>
	    <th>Action</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#groupLeaves">
	    <tr>
		<td><a href="<s:property value='#application.url' />group_leave.action?id=<s:property value='id' />"><s:property value="id" /></a></td>

		<td><s:if test="group_id == -1">All</s:if><s:else><s:property value="group" /></s:else></td>
		<td><s:property value="salaryGroup" /></td>
		
		<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
		<td><a href="<s:property value='#application.url' />group_leave.action?id=<s:property value='id' />&action=Delete">Delete</a></td>

	    </tr>
	</s:iterator>
    </tbody>
</table>
