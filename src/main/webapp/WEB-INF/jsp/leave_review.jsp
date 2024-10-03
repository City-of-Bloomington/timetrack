<%@  include file="header.jsp" %>
<div class="internal-page">
    <div class="width-one-half">    
	<s:if test="hasLeaveRequest()">
	    <s:form action="leave_review" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="review.leave_id" value="%{review.leave_id}" />
		<s:hidden name="leave_id" value="%{review.leave_id}" />			
		<s:if test="review.id == ''">
		    <h1>New Leave Review</h1>
		</s:if>
		<s:else>
		    <s:hidden name="review.id" value="%{review.id}" />
		    <s:hidden name="id" value="%{review.id}" />	    
		    <h1>Edit Leave Review <s:property value="review.id" /></h1>
		</s:else>
		
		<s:if test="hasErrors()">
		    <s:set var="errors" value="errors" />
		    <%@ include file="errors.jsp" %>
		</s:if>
		
		<s:elseif test="hasMessages()">
		    <s:set var="messages" value="messages" />
		    <%@ include file="messages.jsp" %>
		</s:elseif>
		<fieldset><caption> Request Details</caption>
		    <p>
			<br />
			<b>Request Date:</b> <s:property value="leave.requestDate" /><br />
			<br />	    
			<b>Employee:</b> <s:property value="leave.employee" /><br />
			<br />	
			<b>Leave Request for Job:</b> <s:property value="leave.jobTitle" /><br />
			<br />
			<b>Work Group:</b> <s:property value="leave.group" /><br />
			<br />
			<b>Group Manager to be notified:</b> <s:property value="leave.managerName"/><br />
			<br />
			<b>Date Range: </b><s:property value="leave.date_range" /><br />
			<br />
			<b>Proposed Hour Codes to be used: </b><s:property value="leave.earnCodes" /><br />
			<br />
			<b>Length of Proposed Leave (Total Hours): </b><s:property value="leave.totalHours" /><br />
			<br />
			<b>Proposed Leave Description: </b><s:property value="leave.requestDetails" /><br />
			<br />
		    </p>
		</fieldset>
		<br />
		<br />
		<fieldset><caption> Leave Review </caption>
		    <br /><br />
		    <div class="form-group" style="border-bottom: none;">
			<div class="date-range-picker">
			    <div>	    
				<label>Review Descision: 
				    <s:radio name="review.reviewStatus" value="%{review.reviewStatus}" list="statusVals" />  </label>
			    </div>
			</div>
			<br />
			<br />
			(if denied please state your reason in 'Review Notes' below)
			<br /><br />
			<div class="date-range-picker">
			    <div>	    
				<label>Review Notes </label>
				<s:textarea name="review.notes" value="%{review.notes}" rows="4" cols="30" wrap="true" />
			    </div>
			</div>	    
			<s:if test="review.id == ''">
			    <div class="button-group">
				<s:submit name="action" type="button" value="Save" class="button"/>
			    </div>
			</s:if>
			<s:else>
			    <div class="button-group">
				<s:submit name="action" type="button" value="Save Changes" class="button"/>
			    </div>
			</s:else>
		    </div>
		</fieldset>
	    </s:form>
	</s:if>
    </div>
</div>
<s:if test="hasLeaves()">
    <s:set var="leavesTitle" value="'Leave Requests'" />
    <s:set var="leave_requests" value="leaves" />
    <s:set var="forReview" value="'true'" />
    <%@ include file="leave_requests.jsp" %>
</s:if>
<s:else>
    <p>No active leave request available </p>
</s:else>
<s:if test="hasReviews()">
    <s:set var="reviews" value="reviews" />
    <%@ include file="leave_reviews.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>
