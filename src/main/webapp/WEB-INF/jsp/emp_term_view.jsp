<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:if test="hasMessages()">
	<s:set var="messages" value="%{messages}" />
	<%@ include file="messages.jsp" %>
    </s:if>
    <s:if test="hasErrors()">
	<s:set var="errors" value="%{errors}" />
	<%@ include file="errors.jsp" %>
    </s:if>	
    <h1>Employee Job(s) Termination </h1>
    <table border="1"><caption> Employment Information</caption>
	<tr><td>Employee</td>
	    <td><s:property value="term.full_name" /></td>
	    <td>Employee Type</td>
	    <td><s:property value="term.employment_type" /></td>
	</tr>
	<tr><td>New World Employee #:</td>
	    <td><s:property value="term.employee.employee_number" /></td>
	    <td>Date of Birth</td>
	    <td><s:property value="term.date_of_birth" /></td>
	</tr>
	<tr>
	    <td>Last Pay Period Ending</td>
	    <td><s:property value="term.last_pay_period_date"/></td>
	    <td>Department</td>
	    <td><s:property value="term.department" /></td>
	</tr>
	<s:if test="term.hasOtherActiveJobs()">
	    <tr><td>Other Active Job(s)</td>
		<td colspan="3"><s:property value="term.otherJobTitles" /></td>
	    </tr>
	</s:if>
    </table>
    <s:if test="term.hasJobTerms()">
	<table>
	    <caption>Terminating Job(s)</caption>
	    <tr>
		<th>Job Title</th>
		<th>Grade</th>
		<th>Step</th>
		<th>Pay Rate</th>
		<th>Weekly Hrs</th>
		<th>Supervisor</th>
		<th>supervisor Phone</th>
		<th>Start Date</th>
		<th>Last Day Of Work</th>
		<th>Badge Code</th>
		<th>Badge Returned</th>
	    </tr>
	    <s:iterator var="one" value="term.jobTerms">
		<tr>
		    <td><s:property value="job_title" /></td>
		    <td><s:property value="job_grade" /></td>
		    <td><s:property value="job_step" /></td>
		    <td><s:property value="payRate" /></td>
		    <td><s:property value="weeklyHours" /></td>
		    <td><s:property value="supervisor" /></td>
		    <td><s:property value="supervisor_phone" /></td>
		    <td><s:property value="start_date" /></td>
		    <td><s:property value="last_day_of_work" /></td>
		    <td><s:property value="badge_code" /></td>
		    <td><s:property value='badge_returned' /></td>
		</tr>
	    </s:iterator>
	</table>
    </s:if>    
    <table border="1">
	<caption>Permanent Mailing Address</caption>
	<tr>
	    <td>Address</td>
	    <td><s:property value="term.emp_address" /> </td>
	    <td>City, State Zip</td>
	    <td><s:property value="term.empCityStateZip" /> </td>		
	</tr>		
	<tr>
	    <td>Phone(s)</td>
	    <td><s:property value="term.phones" /> </td>
	</tr>
	<tr>
	    <td>Personal Email Address</td>
	    <td><s:property value="term.personal_email" /> </td>
	</tr>	    
    </table>
    <s:if test="term.hasEmail()">
	<table border="1"><caption>ITS Information </caption>
	    <tr><td>Employee Email Address</td>
		<td colspan="2"><s:property value="term.email" /></td>
	    </tr>
	    <tr><td>Email Account Requested Action</td>
		<s:if test="term.forward_emails != ''">		
		    <td>
			Forward to: <s:property value="term.forward_emails" />
		    </td>
		    <td>
			For days <s:property value="term.forward_days_cnt" />
		    </td>
		</s:if>
		<s:else>
		    <td>Archive</td>
		</s:else>
	    </tr>
	    <tr>
		<td>Google Drive/H Drive Requested Action:</td>
		<td>
		    <s:if test="term.drive_to_person_email != ''">
			Drive to Person:  
			<s:property value="term.drive_to_person_email" />
		    </s:if>
		    <s:elseif test="term.drive_to_shared_emails != ''">
			Drive to Shared: 
			<s:property value="term.drive_to_shared_emails" />
		    </s:elseif>
		    <s:else>
			Close
		    </s:else>
		</td>
	    </tr>
	    <tr><td>Google Calendar Requested Action:</td>
		<td>
		    <s:if test="term.calendar_to_email != ''">
			Transfer To:  
			<s:property value="term.calendar_to_email" />
		    </s:if>
		    <s:else>
		Close
		    </s:else>
		</td>
	    </tr>
	    <tr><td>Zoom Account Requested Action:</td>
		<td>
		    <s:if test="term.zoom_to_email != '''">
			Transfer To: <s:property value="term.zoom_to_email" />
		    </s:if>
		    <s:else>
			Close
		    </s:else>
		</td>
	    </tr>
	</table>
    </s:if>
    <s:if test="term.hasBenefits()">
	<table border="1">	    
	    <tr>
		    <td>Number of Hours Worked in the Current Pay Period?</td>
		    <td><s:property value="term.pay_period_worked_hrs" /></td>
		    <td>Vacation Time</td>
		    <td><s:property value="term.vac_time" /></td>
		</tr>
		<tr><td>Comp Time</td>
		    <td><s:property value="term.comp_time" /></td>
		    <td>PTO</td>
		    <td><s:property value="term.pto" /></td>
		</tr>
	</table>
    </s:if>
    <table border="1" width="70%">	    	
	<tr><td>Remarks</td>
	    <td><s:property value="term.remarks"/>
	    </td>
	</tr>
	<tr><td>Notification Status</td>
	    <td>
		<s:if test="term.needSend()">Not Sent</s:if><s:else>Sent</s:else>
	    </td>
	</tr>
    </table>
    <s:if test="term.needSend()">
	<div class="button-group">
	    <ul>
		<li>If you want to make any changes
		<a href="<s:property value='#application.url' />terminateJobs.action?id=<s:property value='id' />&action=Edit">Edit</a>
		</li>
		<li>
		    <a href="<s:property value='#application.url' />terminateJobs.action?id=<s:property value='id' />&action=Send">Send Notification</a>
		</li>
	    </ul>
	</div>
    </s:if>
    
</div>
<%@ include file="footer.jsp" %>
