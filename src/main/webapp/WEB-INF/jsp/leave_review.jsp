<%@  include file="header.jsp" %>
<div class="internal-page">
    <div>
	<h1>Leave Review <small><b>Reviewer:&nbsp;</b><s:property value="reviewer" /></small> </h1>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="errors" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<s:elseif test="hasMessages()">
	    <s:set var="messages" value="messages" />
	    <%@ include file="messages.jsp" %>
	</s:elseif>
	<s:if test="hasLeaves()">
	    <br />
	    <h1 style="border-bottom:0">Current Leave Requests (<s:property value="leavesTotalNumber" /> Pending Reviews)</h1>	    
	    <s:form action="leave_review" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<table border="1" width="100%"><tr><td>
		<div class="time-block">	    
		    <div class="form-group" style="border-bottom: none;">
			<div class="date-range-picker">
			    <div>
				<label>&nbsp;&nbsp;</label>
				<b>Filter by </b>
			    </div>
			    <s:if test="hasEmployees()">
				<div>
				    <label>Employee </label>
				    <s:select name="filter_emp_id" list="employees" listKey="id" listValue="full_name" headerKey="-1" headerValue="All" style="height:31px;width:150px" /> 
				</div>
			    </s:if>			    
			    <div>	    
				<label>Date From </label>
				<s:textfield name="date_from" value="%{date_from}" type="date" pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" placeholder="MM/DD/YYYY" id="date_from" />&nbsp;&nbsp;
			    </div>
			    <div>
				&nbsp;&nbsp;&nbsp;
			    </div>
			    <div>		    
				<label>Date To</label>
				<s:textfield name="date_to" value="%{date_to}" type="date" pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" placeholder="MM/DD/YYYY" />
			    </div>
			    <div>
			    &nbsp;&nbsp;&nbsp;
			    </div>			    
			    <div>		    
				<label>&nbsp;</label>
				<s:submit name="action" type="button" value="Refresh List" class="button" style="height:31px;width:150px" />
			    </div>
			</div>
		    </div>
		</div>
		</td></tr></table>
		<table>
		    <tr>
			<th>&nbsp;</th>
			<th>Employee</th>			
			<th>Request Date</th>
			<th>Job Title</th>
			<th>Date Range</th>
			<th>Total Hours<</th>			
			<th>Hour Code(s)</th>	    
		    </tr>
		    <tr style="background-color:gainsboro"><td colspan="7">&nbsp;</td></tr>		    
		    <s:iterator var="one" value="leaves" status="rowStatus">
			<s:if test="#rowStatus.count%2 == 0">
			    <tr style="background-color:#efefef">
			</s:if>
			<s:else>
			    <tr>
			</s:else>
			    <td>&nbsp;</td>
			    <td><s:property value="employee" /></td>
			    <td><s:property value="requestDate" /></td>
			    <td><s:property value="jobTitle" /></td>
			    <td><s:property value="date_range" /></td>
			    <td><s:property value="totalHours" /></td>
			    <td><s:property value="earnCodes" /></td>
			    </tr>
			    
			<s:if test="#rowStatus.count%2 == 0">
			    <tr style="background-color:#efefed">
			</s:if>
			<s:else>
			    <tr>
			</s:else>			
			<td>&nbsp;</td><td><b>Request Notes:</b></td><td colspan="6"> <s:property value="requestDetails" /></td>
			</tr>
			<input type="hidden" name="review.leave_id_<s:property value='#rowStatus.count'/>" value="<s:property value='id' />" />			
			<s:if test="#rowStatus.count%2 == 0">
			    <tr style="background-color:#efefef">
			</s:if>
			<s:else>
			    <tr>
			</s:else>			
			    <td>&nbsp;</td>
			    <td colspan="3"><b>Review Decision: </b>
				<select name="review.rev_status_<s:property value='#rowStatus.count'/>" style="background-color:#1e59ae;color:white;"> 
				    <option value="-1">No Action</option>
				    <option value="Approved">Approve</option>
				    <option value="Denied">Deny</option>
				</select> *
			    </td>
			    <td colspan="3"><b>Review Notes: </b><input type="text" name="review.notes_<s:property value='#rowStatus.count'/>" value="" size="40" maxlength="360" />
			    </td>
			</tr>
			<tr style="background-color:gainsboro"><td colspan="7">&nbsp;</td></tr>
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
	    <ul style="margin:0;padding:0">
		<li>You may 'Approve' or 'Deny' or take No Action on each listed request.</li>
		<li>If you Deny a request, please provide the reason in the 'Review Notes' field.</li>
		<li>If you take No Action, then the request will remain in your queue for later consideration.</li>
		<li>The employee will be notified by Time Track of your decision.</li>

	    </ul>
	</fieldset>
    </div>
    <br />
    <s:if test="hasReviews()">
	<hr />
	<br />
	<h1 style="border-bottom:0">Leave Approval History</h1>
	<s:set var="reviews" value="reviews" />
	<%@ include file="leave_reviews.jsp" %>
    </s:if>
</div>
<%@  include file="footer.jsp" %>