<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:if test="hasErrors()">
	<s:set var="errors" value="errors" />
	<%@ include file="errors.jsp" %>
    </s:if>
    <s:elseif test="hasMessages()">
	<s:set var="messages" value="messages" />
	<%@ include file="messages.jsp" %>
    </s:elseif>    
    <h1>Cancel Leave Request <s:property value="leave.id" /> </h1>
    <br />
    <ul>
	<li><b>Employee:</b> <s:property value="leave.employee" /></li>	
	<li><b>Leave Request for Job:</b> <s:property value="leave.jobTitle" /><br /></li>
	<Li><b>Request Date:</b> <s:property value="leave.requestDate" /></Li>
	<li><b>Work Group:</b> <s:property value="leave.group" /></li>
	<li><b>Group Manager to be notified:</b> <s:property value="leave.managerName"/></li>
	<li><b>Date Range: </b><s:property value="leave.date_range" /></li>
	<li><b>Proposed Hour Codes to be used: </b><s:property value="leave.earnCodes" /></li>
	<li><b>Length of Proposed Leave (Total Hours): </b><s:property value="leave.totalHours" /></li>
	<li><b>Proposed Leave Description: </b><s:property value="leave.requestDetails" /></li>
	<li><b>Review Status: </b><s:property value="leave.reviewStatus" /></li>
    </ul>
    <s:form action="leave_request" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="leave.id" value="%{leave.id}" />
	<s:hidden name="id" value="%{leave.id}" />	
	<s:hidden name="job_id" value="%{leave.job_id}" />
	<div class="form-group">
	    <label>Cancel Reason(s) *</label>
	    <s:textarea name="cancel_reason" value="%{cancel_reason}" rows="4" cols="50" wrap="true" required="true" /><br />
	</div>
	<div class="button-group">
	    <s:submit name="action" type="button" value="Submit Cancel" class="button"/>	
	</div>
    </s:form>
</div>
<s:if test="hasCurrentRequests()">
    <br />
    <h1 style="border-bottom:0">Active Leave Requests</h1>    
    <s:set var="leave_requests" value="requests" />
    <s:set var="skipEmployee" value="'true'" />
    <%@ include file="leave_requests.jsp" %>
</s:if>
<%@ include file="footer.jsp" %>
