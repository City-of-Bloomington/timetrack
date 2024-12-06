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
	    <th>Review Comments</th>
	    <s:if test="#skipEmployee">		
		<th>Action</th>
	    </s:if>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#leave_requests">
	    <tr>
		<td>&nbsp;</td>
		<td>
		    <a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />"><s:property value="requestDate" /></a>
		</td>
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
		<td><s:if test="hasReviewNotes()"><s:property value="reviewNotes" /></s:if><e:else>&nbsp;</e:else></td>		
		<td width="14%">
		<s:if test="#skipEmployee">
		    <s:if test="canBeEdited()">
			<a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />&action=Edit">Edit</a>	<br />
		    </s:if>
		    <s:if test="canBeCancelled()">		    
			<a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='id' />&action=startCancel">Cancel Request</a>&nbsp;
		    </s:if>
		</s:if>
		&nbsp;
		</td>
	    </tr>
	</s:iterator>
    </tbody>
</table>
