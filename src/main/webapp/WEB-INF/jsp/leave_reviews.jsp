
<table>
    <tr>
	<th>&nbsp;</th>
	<th>Review ID</th>
	<th>Review Date</th>
	<th>Employee </th>
	<th>Job Title</th>
	<th>Leave Date Range </th>
	<th>Hour Code(s)</th>	 
	<th>Total Hours</th>
	<th>Leave Description</th>
	<th>Review Status</th>
	<th>Reviewer</th>	
	<th>Review Comments</th>
    </tr>
    <s:iterator var="one" value="#reviews" status="rowStatus">
	<s:if test="#rowStatus.count%2 == 0">
	    <tr style="background-color:#efefef">
	</s:if>
	<s:else>
	    <tr>
	</s:else>	
	    <td>&nbsp;</td>
	    <td>
		<a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='leave_id' />"><s:property value="id" /></a>
	    </td>
	    <td><s:property value="date" /></td>
	    <td><s:property value="leave.employee" /></td>
	    <td><s:property value="leave.jobTitle" /></td>
	    <td><s:property value="leave.date_range" /></td>
	    <td><s:property value="leave.earnCodes" /></td>	    
	    <td><s:property value="leave.totalHours" /></td>
	    <td><s:property value="leave.requestDetails" /></td>	    
	    <td><s:property value="reviewStatus" /></td>
	    <td><s:property value="reviewer" /></td>	    
	    <td><s:property value="notes" /></td>	
	</tr>
    </s:iterator>
</table>


