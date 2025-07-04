<%@  include file="header.jsp" %>
<s:form action="payrollProcess" id="form_id" method="post" class="internal-page">
	<s:hidden name="action2" id="action2" value="" />
	<h1>Payroll Approval
		<small><b>Payroll Approver:&nbsp;</b><s:property value="employee.full_name" /></small>
	</h1>

	<s:if test="hasErrors()">
		<s:set var="errors" value="errors" />
		<%@ include file="errors.jsp" %>
	</s:if>

	<s:elseif test="hasMessages()">
		<s:set var="messages" value="messages" />
		<%@ include file="messages.jsp" %>
	</s:elseif>

	<s:if test="hasGroups()">
	    <div class="calendar-header-controls">
		<div class="pay-period">
		    <label for="pay_period_id"><strong>Pay Period:&nbsp;</strong></label>
		    <s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Period" onchange="doRefresh()" />

		    <s:if test="hasMoreThanOneGroup()">
			<label for="groupd_id"><strong>Group:&nbsp;</strong></label>
			<s:select name="group_id" value="%{group_id}" list="groups" listKey="id" listValue="name" headerKey="all" headerValue="All" onchange="doRefresh()" />
		    </s:if>
		    <s:else>
			<strong>Group:&nbsp;</strong>
			<s:property value="group" />
		    </s:else>
		</div>
	    </div>
	    
	    <div class="calendar-header-controls">
		<div class="button-group">
		    <a href="<s:property value='#application.url' />payrollProcess.action?pay_period_id=<s:property value='previousPayPeriod.id' />" class="button hide-text has-icon chevron-left"><span>Backwards</span></a>
		    
		    <a href="<s:property value='#application.url' />payrollProcess.action?pay_period_id=<s:property value='currentPayPeriod.id' />" class="button today"><span>Current Pay Period</span></a>
		    <a href="<s:property value='#application.url' />payrollProcess.action?pay_period_id=<s:property value='nextPayPeriod.id' />" class="button hide-text has-icon chevron-right"><span>Forwards</span></a>
		  </div>

		  <s:if test="needAction()">
		      <a href="<s:property value='#application.url' />inform.action?group_ids=<s:iterator value='groups' status='row'><s:property value='id' /><s:if test='!#row.last'>_</s:if></s:iterator>&type=noApprove&source=payrollProcess&pay_period_id=<s:property value='pay_period_id' />" class="button">Remind Approvers</a>
		  </s:if>
		  <s:if test="hasNotProcessedEmps()">
		      <hr />
		      
		      <div class="form-group">
			  <label for="check_all"><strong>Check the box to approve all eligible individuals</strong></label>
			  <small class="status-tag approval-ready select-all">
			      <input type="checkbox" name="check_all" value="y" id="approve_select_all"/>Select All
			  </small>
		      </div>
		  </s:if>
	    </div>
	    
	    <hr />
	    
	    <!--  we need these as global since they will be used multiple times -->
	    <s:set var="week1DateRange" value="payPeriod.week1DateRange" />
	    <s:set var="week2DateRange" value="payPeriod.week2DateRange" />
	    
	    <s:if test="hasNonDocEmps() || hasNotSubmittedEmps() || hasNotApprovedMap()">
		<div class="approve-process-header-lists">
		    <s:if test="hasNonDocEmps()">
			<div class="emp-no-time-wrapper">
			    <strong>Employee(s) with no time entry for this pay period:</strong>
			    <s:iterator var="one" value="nonDocEmps" status="row">
				<a href="<s:property value='#application.url' />timeDetails.action?employee_id=<s:property value='id' />&pay_period_id=<s:property value='pay_period_id' />&source=approve">
				    <s:property value="full_name" /></a><s:if test="!#row.last">,&nbsp;</s:if>
			    </s:iterator>
			    <br />
			</div>
		    </s:if>
		    
		    <!-- these ifs below should only display if there are users within -->
		    <s:if test="hasNotSubmittedDocs()">
			<small class="status-tag not-submitted">Time Not Submitted</small>
			<ul>
			    <s:iterator value="notSubmittedDocs" status="row" >
				<li><a href="<s:property value='#application.url' />switch.action?document_id=<s:property value='id' />&new_employee_id=<s:property value='employee_id' />&action=Change" /><s:property value="employee" /></a>	<s:if test="!#row.last">,</s:if></li>
			    </s:iterator>
			</ul>
		    </s:if>
		    <s:if test="hasNotApprovedMap()">
			<small class="status-tag not-approved">Time Not Approved</small><br /><br /><br />
			   <ul style="list-style-type:none;">
			    <s:iterator value="notApproved" var="one2" >
			    <s:set var="fname" value="#one2.key" />
			    <s:set var="docs" value="#one2.value" />
			    <li><b><s:property value="#fname" /></b> needs to approve hours for
				<s:iterator value="#docs" status="row">
				    <a href="<s:property value='#application.url' />switch.action?document_id=<s:property value='id' />&new_employee_id=<s:property value='employee_id' />&action=Change" /><s:property value="employee" /></a><s:if test="!#row.last">, </s:if>
				</s:iterator></li>
			    </s:iterator>
			  </ul>
		    </s:if>
		</div>
	    </s:if>
	    
	    <s:if test="hasDocuments()">
		<s:iterator var="one" value="documents">
		    <s:if test="hasDaily()">
			<div class="approval-wrapper">
			    <h1>
				<a href="<s:property value='#application.url' />switch.action?document_id=<s:property value='id' />&new_employee_id=<s:property value='employee_id' />&action=Change" />
				<s:property value="employee" />
		    </a>
		    
		    <s:if test="canBeProcessed()">
			<s:if test="isUserCurrentEmployee()">
			    <small class="status-tag approval-ready">
				<input type="checkbox" name="document_ids" value="<s:property value='id' />">Approve Payroll</input>
			    </small>
			    <button type="button" class="quick-approve" data-doc-id="<s:property value='id' />">Quick Approve</button>
			</s:if>
			<s:else>
			    <small class="status-tag approval-ready">Ready for Payroll Approval</small>
			</s:else>
		    </s:if>
		    
		    <s:elseif test="isApproved()">
			<small class="status-tag approved">Payroll Approved</small>
		    </s:elseif>
		    
		    <s:elseif test="!isApproved()">
			<s:if test="!isSubmitted()"><small class="status-tag not-submitted">Time Not Submitted</small></s:if>
			<s:elseif test="!isApproved()"><small class="status-tag not-approved">Time Not Approved</small></s:elseif>
		    </s:elseif>
		    
		    <s:elseif test="isProcessed()">
			<small class="status-tag processed">Payroll Approved</small>
		    </s:elseif>
		    
		    <s:else>
			<small class="status-tag not-submitted">n/a</small>
		    </s:else>
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
					<strong>Weekly Comp Time Earned After Hours: </strong> <s:property value="job.comp_time_weekly_hours" />
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
			    <s:if test="hasLeaveRequests()">
				<h1>Approved Leave in this Pay Period </h1>
				<s:set var="leave_requests" value="leaveRequests" />
				<%@ include file="leaves_approved.jsp" %>
				<br />
			    </s:if>						
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
			</div>
		    </s:if>
		</s:iterator>
	    </s:if>
	    
	    <s:if test="hasNotProcessedEmps()">
		<div class="approval-button-row">
		    
		    <s:if test="isUserCurrentEmployee()">
			<s:submit name="action" type="button" value="Payroll Approve" class="button"/>
		    </s:if>
		</div>
	    </s:if>
	</s:if>
</s:form>
<%@ include file="footer.jsp" %>
