<%@ include file="header.jsp" %>
<div class="internal-page">
    
    <s:form action="terminateJobs" id="form_id" method="post">
	<s:if test="term.id != ''">
	    <s:hidden name="term.id" value="%{term.id}" />
	</s:if>
	<s:hidden name="term.submitted_by_id" value="%{term.submitted_by_id}" />	
	<s:hidden name="term.department_id" value="%{term.department_id}" />	
	<s:hidden name="term.employee_id" value="%{term.employee_id}" />
	<s:hidden name="term.group_id" value="%{term.group_id}" />	
	<s:hidden name="term.full_name" value="%{term.full_name}" />
	<s:hidden name="term.job_ids" value="%{term.job_ids}" />
	<s:hidden name="term.email" value="%{term.email}" />	
	<s:hidden name="term.supervisor_id" value="%{term.supervisor_id}" />	
	<s:hidden name="term.hours_per_week" value="%{term.hours_per_week}" />
	<s:hidden name="term.employment_type" value="%{term.employment_type}" />
	<s:hidden name="term.last_pay_period_date" value="%{term.last_pay_period_date}" />
	<s:if test="term.id != ''">
	    <h1>Employee Termination <s:property value="term.id" /> </h1>
	</s:if>	    
	<s:else>
	    <h1>New Employee Termination </h1>
	</s:else>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<s:if test="term.id != ''">	
	    <p>If you need to update any data, do so and hit the 'Save Changes'.
		<br />
		If all data are correct, do not forget to click on 'Send Notification' so that related parties are informed
	    </p>
	</s:if>
	<p><strong>Instructions </strong></p>
	<ul>
	    <li>Please use the tab key to navigate through the form </li>
	    <li>Do not hit Enter until you are completely finished, or the form will be submitted.</li>
	    <li>After you enter the employee related data, please click on 'Submit'.</li>
	    <li>If 'Submint' is successful, click on 'Send Notification' HR and ITS will receive an email to take additional actions </li>
	</ul>
	<p><strong>Attention:</strong></p>
	<ul>
	    <li>Please fill out the form completely. Failure to do so may cause unnecessary delays in the termination process!</li>
	</ul>
		
	<table border="1"><caption> Employment Information</caption>
	    <tr><td>Employee</td>
		<td><s:property value="term.full_name" /></td>
	    </tr>
	    <tr><td>Employee Type</td>
		<td><s:property value="term.employment_type" /></td>
		<td>Hours Per Week</td>
		<td><s:property value="term.hours_per_week" /></td>
	    </tr>
	    <s:if test="term.employee != null">
		<tr><td>New World Employee #:</td>
		    <td><s:property value="term.employee.employee_number" /></td>
		    <td>Badge ID: </td>
		    <td><s:property value="term.employee.id_code" /></td>
		</tr>
	    </s:if>
	    <tr><td>Date of Birth</td>
		<td><s:textfield name="term.date_of_birth" value="%{term.date_of_birth}" size="10" maxlength="10" /></td> 
		<td>Date of Hire</td>
		<td><s:textfield name="term.date_of_hire" value="%{term.date_of_hire}" type="date" size="10" maxlength="10" pattern="[0-9]{2}-[0-9]{2}-[0-9]{4}" /></td> 
	    </tr>
	    <tr><td>Last Day of Work</td>
		<td><s:textfield name="term.last_day_of_work" type="date" value="%{term.last_day_of_work}" size="10" maxlength="10" pattern="[0-9]{2}-[0-9]{2}-[0-9]{4}" /></td> 
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
		<td><s:textfield name="term.job_grade" value="%{term.job_grade}" size="30" maxlength="100" /> </td>
		<td>Job Step</td>
		<td><s:textfield name="term.job_step" value="%{term.job_step}" size="30" maxlength="100" /> </td>
	    </tr>
	    <tr><td>Rate of Pay</td>
		<td><s:textfield name="term.payRate" value="%{term.payRate}" size="30" maxlength="100" /> </td>
	    </tr>	    
	    <tr><td>Supervisor Name</td>
		<td><s:property value="term.supervisor" /> </td>
		<td>Supervisor Phone</td>
		<td><s:textfield name="term.supervisor_phone" value="%{term.supervisor_phone}" size="10" maxlength="20" /> </td>
	    </tr>
	</table>
	<table border="1">
	    <caption>Permanent Mailing Address</caption>
	    <tr>
		<td>Address</td>
		<td><s:textfield name="term.emp_address" value="%{term.emp_address}" size="30" maxlength="100" required="true" class="required" /> </td>
		<td>City</td>
		<td><s:textfield name="term.emp_city" value="%{term.emp_city}" size="30" required="true" maxlength="100" class="required" /> </td>		
	    </tr>		
	    <tr>
		<td>State</td>
		<td><s:textfield name="term.emp_state" value="%{term.emp_state}" size="10" maxlength="30" required="true" class="required" /> </td>
		<td>Zip Code</td>
		<td><s:textfield name="term.emp_zip" value="%{term.emp_zip}" size="10" maxlength="10" required="true" class="required" /> </td>		
	    </tr>
	    <tr>
		<td>Phone Number</td>
		<td><s:textfield name="term.emp_phone" value="%{term.emp_phone}" size="10" maxlength="30" /> </td>
		<td>Alt Phone Number</td>
		<td><s:textfield name="term.emp_alt_phone" value="%{term.emp_alt_phone}" size="10" maxlength="30" /> </td>		
	    </tr>
	    <tr>
		<td>Personal Email Address</td>
		<td><s:textfield name="term.personal_email" value="%{term.personal_email}" size="30" maxlength="100" /> </td>
	    </tr>	    
	</table>
	<table border="1"><caption>ITS Information </caption>
	    <tr><td>Employee Email Address</td>
		<td colspan="2"><s:property value="term.email" /></td>
	    </tr>
	    <tr>
		<td>Email Account Requested Action</td>
		<td><s:select name="term.email_account_action" value="%{term.email_account_action}" list="#{'Archive':'Archive','Personal':'Forward to'}" /></td>
		<td>If 'Forward To' is selected, enter the email address(es) where email should be forwarded:
		    <s:textfield name="term.forward_email" value="%{term.forward_email}" size="50" maxlength="100" />
		    <s:textfield name="term.forward_email2" value="%{term.forward_email2}" size="50" maxlength="100" />
		    <s:textfield name="term.forward_email3" value="%{term.forward_email3}" size="50" maxlength="100" />
		    <s:textfield name="term.forward_email4" value="%{term.forward_email4}" size="50" maxlength="100" />
		</td>
	    </tr>
	    <tr><td>Forward Email for # Days:</td>
		<td><s:textfield name="term.forward_days_cnt" value="%{term.forward_days_cnt}" size="2" maxlength="2" /></td>
	    </tr>
	    <tr><td>Google Drive/H Drive Requested Action:</td>
		<td><s:select name="term.drive_action" value="%{term.drive_action}" list="#{'Archive':'Archive','Person':'Transfer To Person','Shared':'Transfer To Shared Drive'}" /></td>
		<td>If "Transfer To Person" is selected, enter the email address of the person who should receive the files:<br />
		    <s:textfield name="term.drive_to_person_email" value="%{term.drive_to_person_email}" size="50" maxlength="100" />
		</td>
	    </tr>
	    <tr><td></td><td></td>
		<td>If 'Transfer to Shared Drive' is selected, enter the email address(es) of the people who should have access to the Shared Drive: <br />
		    <s:textfield name="term.drive_to_shared_email" value="%{term.drive_to_shared_email}" size="50" maxlength="100" /><br />
		    <s:textfield name="term.drive_to_shared_email2" value="%{term.drive_to_shared_email2}" size="50" maxlength="100" /><br />
		    <s:textfield name="term.drive_to_shared_email3" value="%{term.drive_to_shared_email3}" size="50" maxlength="100" /><br />
		    <s:textfield name="term.drive_to_shared_email4" value="%{term.drive_to_shared_email4}" size="50" maxlength="100" />
		</td>
	    </tr>
	    <tr><td>Google Calendar Requested Action:</td>
		<td><s:select name="term.calendar_action" value="%{term.calendar_action}" list="#{'Close':'Close','Transfer':'Transfer To'}" /></td>
		<td>If "Transfer To" is selected, enter the email address of the person who should get the Google Calendar Events
		    <s:textfield name="term.calendar_to_email" value="%{term.calendar_to_email}" size="30" maxlength="100" />
		</td>
	    </tr>
	    <tr><td>Zoom Account Requested Action:</td>
		<td><s:select name="term.zoom_action" value="%{term.zoom_action}" list="#{'close':'Close','Transfer':'Transfer To'}" /></td>
		<td>If 'Transfer To' is selected, enter the email address of the person who should get the Zoom content
		    <s:textfield name="term.zoom_to_email" value="%{term.zoom_to_email}" size="30" maxlength="100" />
		</td>
	    </tr>
	</table>
	<s:if test="term.hasBenefits()">
	    <table border="1">
		<caption>Benefit Time Being Paid (Hours):</caption>
		<tr><td>Number of Hours Worked in the Current Pay Period?</td>
		    <td><s:textfield name="term.pay_period_worked_hrs" value="%{term.pay_period_worked_hrs}" size="5" maxlength="5" /></td>
		    <td>Vacation Time</td>
		    <td><s:textfield name="term.vac_time" value="%{term.vac_time}" size="5" maxlength="5" /></td>
		</tr>
		<tr><td>Comp Time</td>
		    <td><s:textfield name="term.comp_time" value="%{term.comp_time}" size="5" maxlength="5" /></td>
		    <td>PTO</td>
		    <td><s:textfield name="term.pto" value="%{term.pto}" size="5" maxlength="5" /></td>
		</tr>
	    </table>
	</s:if>
	<table border="1" width="70%">	
	    <tr><td>Employee ID Badge Returned?</td>
		<td><s:select name="term.badge_returned" value="%{term.badge_returned}" list="#{'-1':'NA','Yes':'Yes','No':'No'}" /></td>
	    </tr>
	    <tr><td>Remarks</td>
		<td colspan="2"><s:textarea name="term.remarks" value="%{term.remarks}" row="5" columns="60"/>
		</td>
	    </tr>
	</table>
	<div class="button-group">
	    <s:if test="term.id != '' && term.needSend()">	    
		<s:submit name="action" type="button" value="Save Changes" class="button"/>
		<s:submit name="action" type="button" value="Send Notification" class="button"/>		
	    </s:if>
	    <s:else>
		<s:submit name="action" type="button" value="Submit" class="button"/>
	    </s:else>
	</div>
    </s:form>
</div>
<%@ include file="footer.jsp" %>
