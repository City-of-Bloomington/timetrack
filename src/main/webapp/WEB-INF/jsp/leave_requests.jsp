<h1><s:property value="#leavesTitle" /></h1>
<table class="width-full">
    <thead>
	<tr>
	    <th>Request Date</th>
	    <th>Employee</th>
	    <th>Job Title</th>
	    <th>Date Range</th>
	    <th>Hour Code(s)</th>	    
	    <th>Total Hours</th>
	    <th>More Details</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#leave_requests">
	    <tr>
		<td><s:property value="requestDate" /></td>
		<td><s:property value="employee" /></td>
		<td><s:property value="jobTitle" /></td>
		<td><s:property value="date_range" /></td>
		<td><s:property value="earnCodes" /></td>
		<td><s:property value="totalHours" /></td>
		<s:if test="#forReview">
		    <td><a href="<s:property value='#application.url' />leave_review.action?leave_id=<s:property value='id' />&action=view">Review</a></td>
		</s:if>
		<s:else>
		    <td><a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />&action=view">More Details</a></td>
		</s:else>
	    </tr>
	</s:iterator>
    </tbody>
</table>
