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
	<s:if test="hasLeaves()">
	    <s:form action="leave_review" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="displayed_ids" value="%{display_ids}" />
		<table>
		    <caption>Current Leave Requests</caption>
		    <tr>
			<th>&nbsp</th>
			<th>Employee</th>			
			<th>Request Date</th>
			<th>Job Title</th>
			<th>Date Range</th>
			<th>Hour Code(s)</th>	    
			<th>Total Hours<</th>
			<th>Request Notes</th>
		    </tr>
		    <s:iterator var="one" value="leaves" status="rowStatus">
			<tr>
			    <td>&nbsp;</td>
			    <td><s:property value="employee" /></td>
			    <td><s:property value="requestDate" /></td>
			    <td><s:property value="jobTitle" /></td>
			    <td><s:property value="date_range" /></td>
			    <td><s:property value="earnCodes" /></td>
			    <td><s:property value="totalHours" /></td>
			    <td><s:property value="requestDetails" /></td>
			</tr>
			<tr>
			    <input type="hidden" name="review.leave_id_<s:property value='#rowStatus.count'/>" value="<s:property value='id' />" />
			    <td colspan="4"><b>Review Decision: </b>
				<select name="review.rev_status_<s:property value='#rowStatus.count'/>">
				    <option value="-1">Undecided</option>
				    <option value="Approved">Approve</option>
				    <option value="Denied">Deny</option>
				</select>
			    </td>
			    <td colspan="4"><b>Review Comments: </b><input type="text" name="review.notes_<s:property value='#rowStatus.count'/>" value="" size="40" maxlength="360" />
			    </td>
			</tr>
			<tr><td colspan="8">&nbsp;</td></tr>
		    </s:iterator>
		</table>
		<div class="button-group">
		    <s:submit name="action" type="button" value="Submit" class="button"/>
		</div>
	    </s:form>
	</s:if>
	<br />
	<fieldset>
	    Please review the current leave requests. <br />
	    <ul>
		<li>You may 'Approve' or 'Deny' each request. </li>
		<li>If you Deny a request, please provide the reason in the
		    'Review Comments' field. </li>
		<li>The employee will be notified by your decision.</li>
		<li>Each review will have no more than three request at a time</li>
	    </ul>
	</fieldset>
    </div>
</div>
<s:if test="hasReviews()">
    <s:set var="reviews" value="reviews" />
    <%@ include file="leave_reviews.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>
