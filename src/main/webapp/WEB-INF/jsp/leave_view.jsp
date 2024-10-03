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
    <h1>Leave Request <s:property value="leave.id" /> </h1>
    <p>
	<b>Employee:</b> <s:property value="leave.employee" /><br />
	<br />	
	<b>Leave Request for Job:</b> <s:property value="leave.jobTitle" /><br />
	<br />
	<b>Request Date:</b> <s:property value="leave.requestDate" /><br />
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
	<a href="<s:property value='#application.url' />leave_request.action?id=<s:property value='leave.id' />&action=Edit">Edit</a><br />
	-------------------------------- <br />
    </p>
</div>
<s:if test="hasRequests()">
    <s:set var="leave_requests" value="requests" />
    <s:set var="leavesTitle" value="leavesTitle" />
    <%@ include file="leave_requests.jsp" %>
</s:if>
<%@ include file="footer.jsp" %>
