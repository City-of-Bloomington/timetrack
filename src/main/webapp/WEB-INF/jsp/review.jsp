<%@  include file="header.jsp" %>
<s:form action="review" id="form_id" method="post" class="internal-page">
	<s:hidden name="action2" id="action2" value="" />
	<h1>Review Timesheets
		<small><b>Reviewer:&nbsp;</b><s:property value="user.full_name" /></small>
	</h1>
	<s:if test="hasErrors()">
		<s:set var="errors" value="errors" />
		<div class="errors">
			<%@  include file="errors.jsp" %>
		</div>
	</s:if>
	<s:elseif test="hasMessages()">
		<s:set var="messages" value="messages" />
		<div class="welcome">
			<%@  include file="messages.jsp" %>
		</div>
	</s:elseif>
	<s:if test="hasGroups()">
		<div class="calendar-header-controls">
			<div class="pay-period">
				<b>Pay Period:&nbsp;</b><s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Period" onchange="doRefresh()" />

		  	<s:if test="hasMoreThanOneGroup()">
					<b>Group:&nbsp;</b><s:select name="group_id" value="%{group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" onchange="doRefresh()" />
				</s:if>
			</div>
		</div>

		<div class="calendar-header-controls">
			<div class="button-group">
		    <a href="<s:property value='#application.url' />review.action?pay_period_id=<s:property value='previousPayPeriod.id' />" class="button hide-text has-icon chevron-left"><span>Backwards</span></a>
		    <a href="<s:property value='#application.url' />review.action?pay_period_id=<s:property value='currentPayPeriod.id' />" class="button today"><span>Current Pay Period</span></a>
		    <a href="<s:property value='#application.url' />review.action?pay_period_id=<s:property value='nextPayPeriod.id' />" class="button hide-text has-icon chevron-right"><span>Forwards</span></a>
		  </div>
		</div>

		<hr />

		<!--  we need these as global since they will be used multiple times -->
		<s:set var="week1DateRange" value="payPeriod.week1DateRange" />
		<s:set var="week2DateRange" value="payPeriod.week2DateRange" />

		<s:if test="hasNonDocEmps() || hasNotSubmittedEmps() || hasNotApprovedEmps()">
			<div class="approve-process-header-lists">
			<s:if test="hasNonDocEmps()">
				<div class="emp-no-time-wrapper">
					<strong>Employee(s) with no time entry for this pay period:</strong>
					<s:iterator var="one" value="nonDocEmps" status="row" >
						<s:property value="full_name" /></a><s:if test="!#row.last">,&nbsp;</s:if>
					</s:iterator>
				</div>
			</s:if>
			<!-- these ifs below should only display if there are users within -->
			<s:if test="hasNotSubmittedEmps()">
				<small class="status-tag not-submitted">Time Not Submitted</small>
				<ul>
					<s:iterator value="notSubmittedEmps" status="row" >
						<li><s:property value="full_name" /><s:if test="!#row.last">,</s:if></li>
					</s:iterator>
				</ul>
			</s:if>
			<s:if test="hasNotApprovedEmps()">
				<small class="status-tag not-approved">Time Not Approved</small>
				<ul>
					<s:iterator value="notApprovedEmps" status="row">
						<li><s:property value="full_name" /><s:if test="!#row.last">,</li></s:if></li>
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
							<a href="<s:property value='#application.url' />timeDetails.action?document_id=<s:property value='id' />&action=View" />
								<s:property value="employee" />
							</a>

							<!-- <s:if test="hasNotSubmittedEmps()">
								<small class="status-tag not-submitted">Time Not Submitted</small>
							</s:if>

							<s:if test="hasNotApprovedEmps()">
								<small class="status-tag not-approved">Time Not Approved</small>
								</ul>
							</s:if> -->
						</h1>

						<ul>
							<li>
								<strong>Available Accruals</strong> <s:property value="employeeAccrualsShort" />
							</li>
							<s:if test="hasJob()">
								<li>
									<strong>Weekly Standard Work Hrs: </strong> <s:property value="job.weekly_regular_hours" />
								</li>
								<li>
									<strong>Weekly Compt Time Earned After Hrs: </strong> <s:property value="job.comp_time_weekly_hours" />
								</li>
							</s:if>
						</ul>

						<s:if test="hasWarnings()">
							<s:set var="warnings" value="warnings" />
							<%@ include file="warnings.jsp" %>
						</s:if>

						<s:set var="daily" value="daily" />
						<s:set var="week1Total" value="week1Total" />
						<s:set var="week2Total" value="week2Total" />
						<s:set var="payPeriodTotal" value="payPeriodTotal" />
						<div class="m-b-40"><%@ include file="dailySummary.jsp" %></div>
						<s:if test="hasTimeIssues()">
							<s:set var="timeIssuesTitle" value="'Outstanding Issues'" />
							<s:set var="timeIssues" value="timeIssues" />
							<%@ include file="timeIssues.jsp" %>
						</s:if>
						<div class="d-flex">
							<s:if test="hasHourCodeWeek1()">
								<s:set var="weeklyHourCodes" value="hourCodeWeek1" />
								<s:set var="weekTotal" value="week1Total" />
								<s:set var="weeklyTitle" value="'Week 1 (Hour Codes)'" />
								<s:set var="whichWeek" value="'week-one'" />
								<%@ include file="weeklySummary.jsp" %>
							</s:if>
							<s:if test="hasHourCodeWeek2()">
								<s:set var="weeklyHourCodes" value="hourCodeWeek2" />
								<s:set var="weekTotal" value="week2Total" />
								<s:set var="weeklyTitle" value="'Week 2 (Hour Codes)'" />
								<s:set var="whichWeek" value="'week-two'" />
								<%@ include file="weeklySummary.jsp" %>
							</s:if>
						</div>
					</div>
				</s:if>
			</s:iterator>
		</s:if>

	</s:if>
</s:form>

<%@  include file="footer.jsp" %>
