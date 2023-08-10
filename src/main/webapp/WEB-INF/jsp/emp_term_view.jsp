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
	</tr>
	<tr><td>Employee Type</td>
	    <td><s:property value="term.employment_type" /></td>
	    <td>Hours Per Week</td>
	    <td><s:property value="term.hours_per_week" /></td>
	</tr>
	<s:if test ="hasDate_of_birth()">
	    <tr><td>Date of Birth</td>
		<td><s:property value="term.date_of_birth" /></td>
	    </tr>
	</s:if>
	<s:if test ="term.hasDate_of_hire()">
	    <tr>
		<td>Date of Hire</td>
		<td><s:property value="term.date_of_hire" /></td> 
	    </tr>
	</s:if>	
	<tr><td>Last Day of Work</td>
	    <td><s:property value="term.last_day_of_work"/></td>	    
	    <td>Last Pay Period Ending</td>
	    <td><s:property value="term.last_pay_period_date"/></td>
	</tr>
	<tr><td>Department</td>
	    <td><s:property value="term.department" /></td>
	    <td>Group </td>
	    <td><s:property value="term.group" /></td>		
	</tr>
	<tr><td>Terminating Job Title(s)</td>
	    <td colspan="3"><s:property value="term.jobTitles" /></td>
	</tr>
	<s:if test="term.hasOtherActiveJobs()">
	    <tr><td>Other Active Job(s)</td>
		<td colspan="3"><s:property value="term.otherJobTitles" /></td>
	    </tr>
	</s:if>
	<tr><td>Job Grade</td>
	    <td><s:property value="term.job_grade" /> </td>
	    <td>Job Step</td>
	    <td><s:property value="term.job_step" /> </td>
	</tr>
	<tr><td>Supervisor Name</td>
	    <td><s:property value="term.supervisor" /> </td>
	    <td>Supervisor Phone</td>
	    <td><s:property value="term.supervisor_phone" /> </td>
	</tr>
    </table>
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
	<tr><td>Employee ID Badge Returned?</td>
	    <td><s:property value="term.badge_returned" /></td>
	</tr>
    </table>
    <table border="1">
	<s:if test="term.hasBenefits()">
	    <tr><td>Number of Hours Worked in the Current Pay Period?</td>
		<td><s:property value="term.pay_period_worked_hrs" /></td>
		<td>Vaction Time</td>
		<td><s:property value="term.vac_time" /></td>
	    </tr>
	    <tr><td>Comp Time</td>
		<td><s:property value="term.comp_time" /></td>
		<td>PTO</td>
		<td><s:property value="term.pto" /></td>
		</tr>
	</s:if>
	<tr><td>Remarks</td>
	    <td colspan="2"><s:property value="term.remarks"/>
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
