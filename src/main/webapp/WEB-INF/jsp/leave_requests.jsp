<h1><s:property value="#leavesTitle" /></h1>
<table class="width-full">
    <thead>
	<tr>
	    <th>ID</th>
	    <th>Date</th>
	    <th>Employee</th>
	    <th>Job Title</th>
	    <th>Date Range</th>
	    <th>Total Hours</th>
	    <th>Earn Code</th>
	    <th>Notes</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#leave_requests">
	    <tr>
		<td><a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />&action=view">More Details</a></td>
		<td><s:property value="requestDate" /></td>
		<td><s:property value="employee" /></td>
		<td><s:property value="jobTitle" /></td>
		<td><s:property value="date_range" /></td>
		<td><s:property value="totalHours" /></td>
		<td><s:property value="earnCode" /></td>		
		<td><s:property value="requestDetails" /></td>
	    </tr>
	</s:iterator>
    </tbody>
</table>
