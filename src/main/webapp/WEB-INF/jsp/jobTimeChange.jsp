<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page">
	<s:form action="jobTimeChange" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="related_employee_id" value="%{related_employee_id}" />		
		<h1>Transfer Logged Times from one Job to Another for: <s:property value="%{relatedEmployee}" /></h1>
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="%{errors}" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>
		Note: To transfer employee's times from job to another
		<ul>
			<li> We assume the employee has at least two active jobs for the related pay period </li>
			<li> We want to transfer time blocks from one job to another </li>
			<li> Pick the pay period </li>
			<li> Pick the 'From Job' that we want the times to be transfered from </li>
			<li> Pick the 'To Job' that  we want the times to be transfered to </li>
			<li> Then click on  'Change'</li>
			<li> Now you can go to employee jobs list and expire the 'From Job' to the desired date, normally the day before the related pay period </li>
		</ul>
		<div class="width-one-half">
			<div class="form-group">
        <label>Employee </label>
				<div>
				<a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='related_employee_id' />"><s:property value="relatedEmployee" /></a>
				</div>
			</div>
			<div class="form-group">
        <label>Pay Period</label>
				<div class="date-range-picker">
					<div>					
						<s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Pay Period " />
					</div>
				</div>
			</div>
			<s:if test="hasJobs()">
				<div class="form-group">
					<label>From Job </label>
					<div class="date-range-picker">
						<div>					
							<s:select name="from_job_id" value="" list="jobs" listKey="id" listValue="name" headerKey="-1" headerValue="Pick a job" />
						</div>
					</div>
				</div>
				<div class="form-group">
					<label>To Job</label>
					<div class="date-range-picker">
						<div>					
							<s:select name="to_job_id" value="" list="jobs" listKey="id" listValue="name" headerKey="-1" headerValue="Pick a job" />
						</div>
					</div>
				</div>
				<div class="button-group">	
					<s:submit name="action" type="button" value="Transfer Times"/>		
				</div>				
			</s:if>
		</div>
	</s:form>		
</div>

<%@  include file="footer.jsp" %>


