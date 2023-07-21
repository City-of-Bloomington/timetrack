<%@ include file="header.jsp" %>
<div class="internal-page">
    
    <s:form action="empJobTerminate" id="form_id" method="post">
	<s:hidden name="term.job_ids" value="%{term.job_ids}" />
	<s:hidden name="term.department_id" value="%{term.department_id}" />	
	<s:hidden name="term.employee_id" value="%{term.employee_id}" />
	<s:hidden name="term.supervisor_id" value="%{term.supervisor_id}" />	
	<s:hidden name="term.hours_per_week" value="%{term.hours_per_week}" />
	<s:hidden name="term.employment_type" value="%{term.employment_type}" />	
	<h1>Employee Job(s) Termination </h1>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<p><strong>Instructions </strong></p>
	<ul>
	    <li>Please use the tab key to navigate through the form </li>
	    <li>Do not hit Enter until you are completely finished, or the form will be submitted.</li>
	    <li>If 'Submit' is successful HR and ITS will receive an email
		to take additional actions </li>
	</ul>
	<p><strong>Attention:</strong></p>
	<ul>
	    <li>Please fill out the form completely. Failure to do so may cause unnecessary delays in the termination process!</li>
	</ul>
		
	<div class="width-one-half">
	    <div class="form-group">
		<label>Employment Type</label>
		<s:property value="term.employment_type" />
	    </div>
	    <div class="form-group">
		<label>Hours Per Week</label>
		<s:property value="term.hours_per_week" />
	    </div>
	</div>
	<table border="1"><caption> Employment Information</caption>
	    <tr><td>Employee</td>
		<td><s:property value="term.emp" /></td>
	    </tr>
	    <tr><td>Date of Birth</td>
		<td><s:textfield name="term.date_of_birth" value="%{term.date_of_birth}" size="10" maxlength="10" />(example 07/04/1972)</td> 
		<td>Date of Hire</td>
		<td><s:textfield name="term.date_of_hire" value="%{term.date_of_hire}" size="10" maxlength="10" />(example 07/04/1996)</td> 
	    </tr>
	    <tr><td>Last Day of Work</td>
		<td><s:textfield name="term.last_day_of_work" value="%{term.last_day_of_work}" size="10" maxlength="10" />(example 10/22/2023)</td> 
		<td>Last Pay Period Ending</td>
		<td><s:select name="term.last_pay_period_date" value="%{term.last_pay_period_date}" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick End Date" /></td>
	    </tr>
	    <tr><td>Department</td>
		<td colspan="3"><s:property value="term.department" /></td>
	    </tr>
	    <tr><td>Job Title(s)</td>
		<td colspan="3"><s:property value="term.jobTitles" /></td>
	    </tr>
	    <tr><td>Job Grade</td>
		<td><s:textfield name="term.job_grade" value="%{term.job_grade}" size="30" maxlength="100" /> </td>
		<td>Job Step</td>
		<td><s:textfield name="term.job_step" value="%{term.job_step}" size="30" maxlength="100" /> </td>
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
		<td><s:textfield name="term.emp_address" value="%{term.emp_address}" size="30" maxlength="100" /> </td>
		<td>City</td>
		<td><s:textfield name="term.emp_city" value="%{term.emp_city}" size="30" maxlength="100" /> </td>		
	    </tr>		
	    <tr>
		<td>State</td>
		<td><s:textfield name="term.emp_state" value="%{term.emp_state}" size="10" maxlength="30" /> </td>
		<td>Zip Code</td>
		<td><s:textfield name="term.emp_zip" value="%{term.emp_zip}" size="10" maxlength="10" /> </td>		
	    </tr>
	    <tr>
		<td>Phone Number</td>
		<td><s:textfield name="term.emp_phone" value="%{term.emp_phone}" size="10" maxlength="30" /> </td>
		<td>Alt Phone Number</td>
		<td><s:textfield name="term.emp_alt_phone" value="%{term.emp_alt_phone}" size="10" maxlength="30" /> </td>		
	    </tr>
	    <tr>
		<td>Personal Email Address</td>
		<td><s:textfield name="term.perosnal_email" value="%{term.perosnal_email}" size="30" maxlength="100" /> </td>
	    </tr>	    
	</table>
	<table><caption>ITS Information </caption>
	    <tr><td>Employee Email Address</td>
		<td colspan="3"><s:property value="term.email" /></td>
	    </tr>
	    <tr><td>Email Account Requested Action</td>
		<td><s:select name="term.email_account_action" value="%{term.email_account_action}" list="#{'archive':'Archive','transfer_to_person':'Transfer to Person'}" /></td>

		</td>
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
		<td><s:select name="term.drive_action" value="%{term.drive_action}" list="#{'archive':'Archive','transfer_to_person':'Transfer To Person','transfer_to_shared_drive':'Transfer To Shared Drive'}" /></td>
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
		<td><s:select name="term.calendar_action" value="%{term.calendar_action}" list="#{'close':'Close','transfer_to':'Transfer To'}" /></td>
		<td>If "Transfer To" is selected, enter the email address of the person who should get the Google Calendar Events
		    <s:textfield name="term.calendar_to_email" value="%{term.calendar_to_email}" size="30" maxlength="100" />
		</td>
	    </tr>
	    <tr><td>Zoom Account Requested Action:</td>
		<td><s:select name="term.zoom_action" value="%{term.zoom_action}" list="#{'close':'Close','transfer_to':'Transfer To'}" /></td>
		<td>If 'Transfer To' is selected, enter the email address of the person who should get the Zoom content
		    <s:textfield name="term.zoom_to_email" value="%{term.zoom_to_email}" size="30" maxlength="100" />
		</td>
	    </tr>
	    <tr><td>Employee ID Badge Returned?</td>
		<td><s:select name="term.badge_returned" value="%{term.badge_returned}" list="#{'-1':'NA','y':'Yes','n':'No'}" /></td>
	    </tr>
	</table>
	<table border="1">
	    <caption>Benefit Time Being Paid (Hours):</caption>
	    <tr><td>Number of Hours Worked in the Current Pay Period?</td>
		<td><s:textfield name="term.pay_period_worked_hrs" value="%{term.pay_period_worked_hrs}" size="5" maxlength="5" /></td>
		<td>Vaction Time</td>
		<td><s:textfield name="term.vac_time" value="%{term.vac_time}" size="5" maxlength="5" /></td>
	    </tr>
	    <tr><td>Comp Time</td>
		<td><s:textfield name="term.comp_time" value="%{term.camp_time}" size="5" maxlength="5" /></td>
		<td>PTO</td>
		<td><s:textfield name="term.pto" value="%{term.pto}" size="5" maxlength="5" /></td>
	    </tr>
	</table>
	<div class="button-group">
	    <s:submit name="action" type="button" value="Submit" class="button"/>
	</div>
    </s:form>
</div>
<%@ include file="footer.jsp" %>
