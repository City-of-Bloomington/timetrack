<table class="width-full">
    <thead>
	<tr>
	    <th>Request Date</th>
	    <th>Date Range</th>
	    <th>Hour Code(s)</th>	    
	    <th>Total Hours</th>
	    <th>Review Status</th>
	    <th>Reviewer</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#leave_requests">
	    <tr>
		<td><s:property value="requestDate" /></td>
		<td><s:property value="date_range" /></td>
		<td><s:property value="earnCodes" /></td>
		<td><s:property value="totalHours" /></td>
		<td style="background-color:#CCFFCD">
		    <s:property value="reviewStatus" />
		</td>
		<td><s:property value="reviewer" /></td>
	    </tr>
	</s:iterator>
    </tbody>
</table>
