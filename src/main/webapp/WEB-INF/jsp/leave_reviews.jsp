
<table><legend>Leave Reviews</legend>
    <tr>
	<th>ID</th>
	<th>Review Date</th>
	<th>Employee </th>
	<th>Job Title</th>
	<th>Request Date Range </th>
	<th>Total Hours</th>
	<th>Review Status</th>
	<th>Reviewer</th>	
	<th>Review Notes</th>
    </tr>
    <s:iterator var="one" value="#reviews">
	<tr>
	    <td><s:property value="id" /></td>
	    <td><s:property value="date" /></td>
	    <td><s:property value="leave.employee" /></td>
	    <td><s:property value="leave.jobTitle" /></td>
	    <td><s:property value="leave.date_range" /></td>
	    <td><s:property value="leave.totalHours" /></td>	    
	    <td><s:property value="reviewStatus" /></td>
	    <td><s:property value="reviewer" /></td>	    
	    <td><s:property value="notes" /></td>	
	</tr>
    </s:iterator>
</table>


