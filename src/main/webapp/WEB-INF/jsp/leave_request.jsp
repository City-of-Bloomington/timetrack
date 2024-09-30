<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="leave_request" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="leave.job_id" value="%{leave.job_id}" />
	<s:hidden name="job_id" value="%{leave.job_id}" />			
	<s:if test="leave.id == ''">
	    <h1>New Leave Request</h1>
	</s:if>
	<s:else>
	    <s:hidden name="leave.id" value="%{leave.id}" />
	    <s:hidden name="id" value="%{leave.id}" />	    
	    <h1>Edit Leave Request <s:property value="leave.id" /></h1>
	</s:else>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="errors" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<s:elseif test="hasMessages()">
	    <s:set var="messages" value="messages" />
	    <%@ include file="messages.jsp" %>
	</s:elseif>
	Note: Please consider the following to Request a leave <br />
	<ul>
	    <li> The leave has to be more than two working days (more than 16 hours)</li>
	    <li>Enter the leave 'Start Date', 'End Date', 'Hour Code Type' and Total Hours. </li>
	    <li> * Fields are required</li>
	    <li> After you Submit your request, an email will be sent to you and your supervisor </li>
	    <li> After your supervisor receives the email, he/she may 'Approve' or Deny your request </li>
	    <li> You will receive an email from your supervisor about the descision </li>
	    <li> If your request is approved, you can go ahead and add the related leave dates and hours to your timesheet </li>
	</ul>
	<p>
	    Leave Request for Job; <s:property value="leave.jobTitle" /><br />
	    Work Group: <s:property value="leave.group" /><br />
	    Group Manager to be notified: <s:property value="leave.managerName"/><br />
	</p>
	<div class="width-full">
	    <div class="form-group" style="border-bottom: none;">
		<label>Date Range</label>
		<div class="date-range-picker">
		    <div>	    
			<label>Leave Start Date: *</label>
			
			<s:textfield name="leave.startDate" value="%{leave.startDate}" type="date" pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" placeholder="MM/DD/YYYY" id="start" requiredLabel="true" required="true" />
		    </div>
		    <div>		    
			<label>Leave End Date: *</label>
			<s:textfield name="leave.lastDate" value="%{leave.lastDate}" type="date" pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" placeholder="MM/DD/YYYY" requiredLabel="true"  required="true" />
		    </div>
		</div>
	    </div>
	    <div class="form-group">
		<label>Hour Code *</label>
		<s:select name="earn_code_id" value="%{earn_code_id}" list="hourCodes" listKey="id"  listValue="codeInfo" id="hour_code_select" />
	    </div>
	    <div class="form-group" style="border-bottom: none;">
		<div class="date-range-picker">
		    <div>	    
			<label>Total Hours *</label>
			<s:textfield name="leave.totalHours" value="%{leave.totalHours}" size="5" maxlength="5" required="true" /><br />(should be more than 16)
		    </div>
		</div>
	    </div>
	    <div class="form-group">
		<label>Leave Notes (optional)</label>
		<s:textfield name="leave.requestDetails" value="%{leave.requestDetails}" size="50" maxlength="300" /><br />
	    </div>
	    <div>
	    <s:if test="hasDocument()">
	    <s:if test="document.hasAllAccruals()">
		<h3>Accrual Summary</h3>
			<table class="accruals-summary width-full">
			<tr>
			<th width="40%">Accrual Category</th>
			<th width="15%">Carry Over Balance (as of <s:property value="document.accrualAsOfDate" />)</th>
			<s:if test="document.isPendingAccrualAllowed()">
				<th width="15%">Pending Accrual</th>
			</s:if>
			<th width="15%">Usage</th>
			<th>Available Balance </th>
			</tr>
		<s:iterator value="document.allAccruals" var="one" >
		    <s:set var="key" value="#one.key" />
		    <s:set var="list" value="#one.value" />
		    <tr>
			<td><s:property value="#key" /></td>
			<s:iterator value="#list" status="row">
			    <td><s:property /></td>
			</s:iterator>
		    </tr>
		</s:iterator>
	</table>
</s:if>
</s:if>
	    </div>
	    <s:if test="leave.id == ''">
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
    </s:form>
</div>
<s:if test="hasRequests()">
    <s:set var="leave_requests" value="requests" />
    <s:set var="leavesTitle" value="leavesTitle" />
    <%@ include file="leave_requests.jsp" %>
</s:if>
<%@ include file="footer.jsp" %>
