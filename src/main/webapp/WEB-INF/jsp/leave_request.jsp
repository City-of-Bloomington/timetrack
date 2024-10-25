<%@ include file="header.jsp" %>
<div class="internal-page">
    <h1>LEAVE REQUESTS</h1>
    <s:if test="hasDecidedRequests()">
	<br />
	<h2>Recent Leave Requests</h2>
	<s:set var="leave_requests" value="requests" />
	<s:set var="leavesTitle" value="leavesTitle" />
	<s:set var="skipEmployee" value="'true'" />
	<%@ include file="leave_requests.jsp" %>
	<br />
    </s:if>    
    <s:form action="leave_request" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="leave.job_id" value="%{leave.job_id}" />
	<s:hidden name="job_id" value="%{leave.job_id}" />
	<h2>New Leave Request</h2>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="errors" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<s:elseif test="hasMessages()">
	    <s:set var="messages" value="messages" />
	    <%@ include file="messages.jsp" %>
	</s:elseif>
	
	Leave Request for Job: <s:property value="leave.jobTitle" />, <s:property value="leave.group" />, Manager to be notified: <s:property value="leave.managerName"/><br /><br />
	<div class="time-block">	    
	    <div class="form-group" style="border-bottom: none;">
		<label>Date Range</label>
		<div class="date-range-picker">
		    <div>	    
			<label>Leave Start Date *</label>
			<s:textfield name="leave.startDate" value="%{leave.startDate}" type="date" pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" placeholder="MM/DD/YYYY" id="start" requiredLabel="true" required="true" />
		    </div>
		    <div>		    
			<label>Leave End Date *</label>
			<s:textfield name="leave.lastDate" value="%{leave.lastDate}" type="date" pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" placeholder="MM/DD/YYYY" requiredLabel="true"  required="true" />
		    </div>
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    <div id="div_time_in" class="form-group">
			<label>Length of Proposed Leave *</label>
			<s:if test="leave.id == ''">
			    <s:textfield style="height:31px;width:150px" name="leave.totalHours" value="" size="5" maxlength="5" placeholder="Total hrs (xxx.xx)" required="true" /><br />
			</s:if>
			<s:else>
			      <s:textfield style="height:31px;width:150px" name="leave.totalHours" value="%{leave.totalHours}" size="5" maxlength="5" placeholder="(xxx.xx)" required="true" /><br />
			</s:else>
		   </div>
		</div>
	    </div>
	    <strong>Proposed Hour Codes to be used *</strong><br />
	    <table style="border:3px solid">
		<s:iterator value="hourCodes" var="one" status="row">
		    <s:if test="#row.index%3 == 0">
			<tr>
		    </s:if>
		    <s:if test="id in earn_code_ids">
			<td style="border:1px solid"><input type="checkbox" name="earn_code_ids" value="<s:property value='id' />" checked="checked" /><s:property value="codeInfo" /></td>
		    </s:if>
		    <s:else>
			<td style="border:1px solid"><input type="checkbox" name="earn_code_ids" value="<s:property value='id' />" /><s:property value="codeInfo" /></td>
		    </s:else>
		    <s:if test="#row.index%3 == 2">
			</tr>
		    </s:if>			    
		</s:iterator>
		<s:if test="hourCodesListSize%3 == 1">
		    <td>&nbsp;</td><td>&nbsp;</td></tr>
		</s:if>
		<s:elseif test="hourCodesListSize%3 == 2">
		    <td>&nbsp;</td></tr>
		</s:elseif>
	    </table>
	    <br />
	    <div class="form-group">
		<label>Proposed Leave Description *</label>
		<s:textarea name="leave.requestDetails" value="%{leave.requestDetails}" rows="4" cols="50" wrap="true" required="true" /><br />
	    </div>
	    <div class="button-group">
		<s:submit name="action" type="button" value="Save" class="button"/>
	    </div>
	    <br />
	    <br />
	    <fieldset>
		<br />
		Note: Please consider the following to request a leave <br />
		<ul>
		    <li> Documentation/approval is required for Leave over 16 hours.  It is optional for leave under 16.</li>
		    <li> After you Submit your request, your supervisor will be notified by your request </li>
		    <li> After your supervisor receives the notification, he/she will review your request and may 'Approve' or Deny the request </li>
		    <li> You will receive an email from your supervisor about the descision </li>
		    <li> If your request is approved, you can go ahead and add the related leave dates and hours to your timesheet </li>
		    <li> * Fields are required</li>	    
		</ul>
	    </fieldset>
	</div>
    </s:form>
    
</div>

<%@ include file="footer.jsp" %>
