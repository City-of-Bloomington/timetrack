<table style="border:1px solid">
	<thead>
	<tr>
	    <th>&nbsp;</th>
	    <th>Request Date</th>
	    <th>Date Range</th>
	    <th>Hour Code(s)</th>	    
	    <th>Total Hours</th>
	    <th>Leave Description</th>
	    <th>Status</th>
	    <th>Action</th>
	    <th>Details</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#pending_leaves">
	    <tr>
		<td>&nbsp;</td>
		<td><s:property value="requestDate" /></td>
		<td><s:property value="date_range" /></td>
		<td><s:property value="earnCodes" /></td>
		<td><s:property value="totalHours" /></td>
		<td><s:property value="requestDetails" /></td>
		<td>Pending</td>
		<td><a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />&action=Cancel">Cancel Request</a></td>
		<td><a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />&action=view">More Details</a></td>
	    </tr>
	</s:iterator>
    </tbody>
</table>
