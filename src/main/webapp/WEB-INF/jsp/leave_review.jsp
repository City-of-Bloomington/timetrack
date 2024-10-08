<%@  include file="header.jsp" %>
<div class="internal-page">
    <div>
	<h1>Leave Review</h1>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="errors" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<s:elseif test="hasMessages()">
	    <s:set var="messages" value="messages" />
	    <%@ include file="messages.jsp" %>
	</s:elseif>
	Please review the current leave requests. <br />	
	<ul>
	    <li>You may 'Approve' or 'Deny' each request. </li>
	    <li>If you Deny a request, please provide the reason in the
		'Review Decision' field. </li>
	    <li>The employee will be notified by your decision.</li>
	</ul>
	<s:if test="hasLeaves()">
	    <s:form action="leave_review" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<table>
		    <caption>Current Leave Requests</caption>
		    <tr>
			<td><b>Request Date</b></td>
			<td><b>Employee</b></td>
			<td><b>Job Title</b></td>
			<td><b>Date Range</b></td>
			<td><b>Hour Code(s)</b></td>	    
			<td><b>Total Hours</b></td>
			<td><b>Request Notes</b></td>
		    </tr>
		    <s:iterator var="one" value="leaves" status="rowStatus">
			<tr>
			    <td><s:property value="requestDate" /></td>
			    <td><s:property value="employee" /></td>
			    <td><s:property value="jobTitle" /></td>
			    <td><s:property value="date_range" /></td>
			    <td><s:property value="earnCodes" /></td>
			    <td><s:property value="totalHours" /></td>
			    <td><s:property value="requestDetails" /></td>
			</tr>
			<tr>
			    <input type="hidden" name="review.leave_id_<s:property value='#rowStatus.count'/>" value="<s:property value='id' />" />
			    <td colspan="3"><b>Review Decision: </b>
				<input type="radio" name="review.rev_status_<s:property value='#rowStatus.count' />" value="Approved" />Approve
				<input type="radio" name="review.rev_status_<s:property value='#rowStatus.count' />" value="Denied" />Deny
				<td colspan="4"><b>Review Notes: </b><input type="text" name="review.notes_<s:property value='#rowStatus.count'/>" value="" size="45" maxlength="360" />
			    </td>
			</tr>
		    </s:iterator>
		</table>
		<div class="button-group">
		    <s:submit name="action" type="button" value="Submit" class="button"/>
		</div>
	    </s:form>
	</s:if>
    </div>
</div>
<s:if test="hasReviews()">
    <s:set var="reviews" value="reviews" />
    <%@ include file="leave_reviews.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>
