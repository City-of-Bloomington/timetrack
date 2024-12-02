<table style="border:1px solid">
	<thead>
	<tr>
	    <th>&nbsp;</th>
	    <th>Request Date</th>
	    <th>Date Range</th>
	    <th>Hour Code(s)</th>	    
	    <th>Total Hours</th>
	    <th>Leave Description</th>
	    <th>Review Status</th>
	    <th>Reviewer</th>
	    <th>Details</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#leave_history">
	    <tr>
		<td>&nbsp;</td>
		<td><s:property value="requestDate" /></td>
		<td><s:property value="date_range" /></td>
		<td><s:property value="earnCodes" /></td>
		<td><s:property value="totalHours" /></td>
		<td><s:property value="requestDetails" /></td>
		<s:if test="hasReviewer()">
		    <s:if test="reviewStatus=='Approved'">
			<td style="background-color:#CCFFCD">
			    <s:property value="reviewStatus" />
			</td>
		    </s:if>
		    <s:else>
			<td style="background-color:#FFCCCB">
			    <s:property value="reviewStatus" />
			</td>
		    </s:else>
		</s:if>
		<s:else>
		    <td>Pending</td>
		</s:else>
		<td><s:if test="hasReviewer()"><s:property value="reviewer" /></s:if><e:else>&nbsp;</e:else></td>
		<td><a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />&action=view">More Details</a></td>
	    </tr>
	</s:iterator>
    </tbody>
</table>
