<table style="border:1px solid">
	<thead>
	<tr>
	    <th>&nbsp;</th>
	    <th>Request Date</th>
	    <th>Employee</th>
	    <th>Job Title</th>
	    <th>Date Range</th>
	    <th>Hour Code(s)</th>	    
	    <th>Total Hours</th>
	    <th>Leave Description</th>
	    <th>Action Date</th>
	    <th>Review Status</th>
	    <th>Reviewer</th>
	    <th>Review Comments</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#leave_logs">
	    <tr>
		<td>&nbsp;</td>
		<td><s:property value="requestDate" /></td>
    	        <td><s:property value="employee" /></td>
		<td><s:property value="jobTitle" /></td>
		<td><s:property value="date_range" /></td>
		<td><s:property value="earnCodes" /></td>
		<td><s:property value="totalHours" /></td>
		<td><s:property value="requestDetails" /></td>
		<td><s:property value="reviewDate" /></td>		
		<td><s:property value="reviewStatus" /></td>
		<td><s:if test="hasReviewer()"><s:property value="reviewer" /></s:if><e:else>&nbsp;</e:else></td>
		<td><s:if test="hasReviewNotes()"><s:property value="reviewNotes" /></s:if><e:else>&nbsp;</e:else></td>		
	    </tr>
	</s:iterator>
    </tbody>
</table>
