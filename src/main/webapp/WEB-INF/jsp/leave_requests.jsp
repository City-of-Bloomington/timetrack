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
	    <s:if test="#forReview">	    
		<th>Review</th>
	    </s:if>
	    <s:else>
		<th>Review Status</th>
		<th>Reviewer</th>
		<th>Details</th>
	    </s:else>
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
		    <td><s:property value="reviewStatus" /></td>
		    <td><s:if test="hasReviewer()">&nbsp;</s:if><e:else><s:property value="reviewer" /></e:else></td>
		    <td><a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />&action=view">More Details</a></td>
		</s:else>
	    </tr>
	</s:iterator>
    </tbody>
</table>
