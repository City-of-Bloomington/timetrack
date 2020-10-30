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
	<s:form action="lookup" id="form_id" method="post">
		<s:hidden name="other_employee_id" value="%{other_employee_id}" id="employee_id" />
		<h1>Lookup times for Certain Employee</h1>
		
		To do lookup time entries for certain employee, enter the employee name and then pick from the list, then<br />
		pick the related pay period <br />
		Then hit Find button.
	  <div class="width-one-half">
			<div class="form-group">
				<label>Employee Name:</label>
				<s:textfield name="employee_name" value="%{other_employee_name}" size="30" id="employee_name2" />(start with first name)<br />
			</div>
			<div class="form-group">
				<label>Pay Period:</label>			
				<s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick pay period" />
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Find" class="button"/>
			</div>
		</div>
	</s:form>
</div>
<s:if test="hasDocuments()">
	<s:set var="week1DateRange" value="payPeriod.week1DateRange" />
  <s:set var="week2DateRange" value="payPeriod.week2DateRange" />
  <s:iterator var="one" value="documents">		
		<s:if test="hasDaily()">
			<h1><s:property value="employee" />
				<s:if test="isApproved()">
					<small class="status-tag approved">Time Approved</small>
				</s:if>
				<s:elseif test="!.isApproved()">
					<s:if test="!isSubmitted()"><small class="status-tag not-submitted">Time Not Submitted</small></s:if>
					<s:elseif test="!isApproved()"><small class="status-tag not-approved">Time Not Approved</small></s:elseif>
				</s:elseif>
				<s:elseif test="isProcessed()">
					<small class="status-tag processed">Payroll Approved</small>
				</s:elseif>
			</h1>
			<ul>
				<s:if test="hasAccruals()">
					<li>
						<strong>Available Accruals:</strong> <s:property value="employeeAccrualsShort" />
					</li>
				</s:if>
				<s:if test="hasJob()">
					<li>
						<strong>Weekly Standard Work Hours: </strong> <s:property value="job.weekly_regular_hours" />
					</li>
					<li>
						<strong>Weekly Compt Time Earned After Hours: </strong> <s:property value="job.comp_time_weekly_hours" />
					</li>
				</s:if>
			</ul>
			<s:if test="hasWarnings()">
				<s:set var="warnings" value="warnings" />
				<%@ include file="warnings.jsp" %>
			</s:if>
			<s:set var="daily" value="daily" />
			<s:set var="payPeriodTotal" value="payPeriodTotal" />
			<s:set var="payPeriodAmount" value="payPeriodAmount" />
			<div class="m-b-40">
				<%@ include file="dailySummary.jsp" %>
			</div>
			<s:if test="hasTimeIssues()">
        <s:set var="timeIssuesTitle" value="'Outstanding Issues'" />
				<s:set var="timeIssues" value="timeIssues" />
				<%@ include file="timeIssues.jsp" %>
			</s:if>
			<div class="monetary-hours-tables">							
				<s:if test="hasTmwrpRun()">
					<s:if test="tmwrpRun.hasWeek1Rows()">
						<s:set var="rows" value="tmwrpRun.week1Rows" />
						<s:set var="weeklyTitle" value="'Week 1 (Earn Codes)'" />
						<s:set var="whichWeek" value="'week-one'" />
						<%@ include file="weeklyTmwrp.jsp" %>
					</s:if>
					<s:if test="tmwrpRun.hasWeek2Rows()">
						<s:set var="rows" value="tmwrpRun.week2Rows" />
						<s:set var="weeklyTitle" value="'Week 2 (Earn Codes)'" />
						<s:set var="whichWeek" value="'week-two'" />
						<%@ include file="weeklyTmwrp.jsp" %>
					</s:if>
				</s:if>
			</div>
			<s:if test="hasUnscheduleds()">
				<s:set var="unscheduledTitle" value="'Unscheduled Times'" />
				<s:set var="unscheduleds" value="unscheduleds" />
				<%@ include file="unscheduledTimes.jsp" %>				
			</s:if>
			<s:if test="hasTimeActions()">
				<s:set var="timeActions" value="timeActions" />				
				<%@ include file="timeActionsView.jsp" %>
			</s:if>
		</s:if>
	</s:iterator>
</s:if>
	
<%@ include file="footer.jsp" %>
