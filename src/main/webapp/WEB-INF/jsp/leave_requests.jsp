<table style="border:1px solid">
	<thead>
	<tr>
	    <th>&nbsp;</th>
	    <th>Request Date</th>
	    <s:if test="!#skipEmployee">
		<th>Employee</th>
		<th>Job Title</th>
	    </s:if>
	    <th>Date Range</th>
	    <th>Hour Code(s)</th>	    
	    <th>Total Hours</th>
	    <th>Leave Description</th>
	    <th>Review Status</th>
	    <th>Reviewer</th>
	    <s:if test="#skipEmployee">		
		<th>Action</th>
	    </s:if>
	    <th>Details</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#leave_requests">
	    <tr>
		<td>&nbsp;</td>
		<td><s:property value="requestDate" /></td>
		<s:if test="!#skipEmployee">		
		    <td><s:property value="employee" /></td>
		    <td><s:property value="jobTitle" /></td>
		</s:if>
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
		<s:if test="#skipEmployee">
		    <td>
			<s:if test="hasReviewer()">
			    &nbsp;
			</s:if>
			<s:else>
			    <a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />&action=Cancel">Cancel Request</a>
			</s:else>
		    </td>
		</s:if>
		<td><a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />&action=view">More Details</a></td>
	    </tr>
	</s:iterator>
    </tbody>
</table>
