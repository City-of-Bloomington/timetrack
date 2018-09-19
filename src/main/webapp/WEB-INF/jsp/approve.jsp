<%@  include file="header.jsp" %>
<s:form action="approve" id="form_id" method="post" class="internal-page">
	<s:hidden name="action2" id="action2" value="" />
	<h1>Approve Timesheets
		<small><b>Approver:&nbsp;</b><s:property value="employee.full_name" /></small>
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
		  	<s:if test="hasMoreThanOneGroup()">
		  		<b>Pay Period:&nbsp;</b><s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Period" onchange="doRefresh()" />

					<b>Group:&nbsp;</b><s:select name="group_id" value="%{group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" onchange="doRefresh()" />
				</s:if>
			</div>
		</div>

		<div class="calendar-header-controls">
			<div class="button-group">
		    <a href="<s:property value='#application.url' />approve.action?pay_period_id=<s:property value='previousPayPeriod.id' />" class="button hide-text has-icon chevron-left"><span>Backwards</span></a>
		    <a href="<s:property value='#application.url' />approve.action?pay_period_id=<s:property value='currentPayPeriod.id' />" class="button today"><span>Current Pay Period</span></a>
		    <a href="<s:property value='#application.url' />approve.action?pay_period_id=<s:property value='nextPayPeriod.id' />" class="button hide-text has-icon chevron-right"><span>Forwards</span></a>
		  </div>

			<div class="button-group">
		  	<a href="<s:property value='#application.url' />payperiodProcess.action?pay_period_id=<s:property value='pay_period_id' />&department_id=<s:property value='department_id' />&action=Submit" class="button">More Details</a>
		  </div>

		  <s:if test="hasNoDocNorSubmitEmps()">
		  	<div class="button-group">
					<a href="<s:property value='#application.url' />inform.action?employee_ids=<s:iterator value='noDocNorSubmitEmps' status='row'><s:property value='id' /><s:if test='!#row.last'>_</s:if></s:iterator>&type=noSubmit&source=approve&pay_period_id=<s:property value='pay_period_id' />" class="button">Remind Employees</a>
				</div>
			</s:if>

			<div class="form-group">
				<small class="status-tag approval-ready select-all">
					<input type="checkbox" name="check_all" value="y" id="approve_select_all"/>Select All (Approvals)
				</small>
			</div>
		</div>

		<hr />

		<s:set var="week1DateRange" value="payPeriod.week1DateRange" />
		<s:set var="week2DateRange" value="payPeriod.week2DateRange" />

		<s:if test="hasNonDocEmps() || hasNotSubmittedEmps() || hasNotApprovedEmps()">
			<div class="approve-process-header-lists">
			<s:if test="hasNonDocEmps()">
				<div class="emp-no-time-wrapper">
					<strong>Employee(s) with no time entry for this pay period:</strong>
					<s:iterator var="one" value="nonDocEmps" status="row" >
						<a href="<s:property value='#application.url' />timeDetails.action?employee_id=<s:property value='id' />&pay_period_id=<s:property value='pay_period_id' />&source=approve">
							<s:property value="full_name" /></a><s:if test="!#row.last">,&nbsp;</s:if>
					</s:iterator>
				</div>
			</s:if>

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
						<h2>
							<a href="<s:property value='#application.url' />switch.action?document_id=<s:property value='id' />&new_employee_id=<s:property value='employee_id' />&action=Change" />
								<s:property value="employee" />
							</a>
							<!-- this is comment out for testing purpose, we need to change in prod, get back from approve.jsp.old
							-->
							<s:if test="canBeApproved()">
								<s:if test="isUserCurrentEmployee()">
									<small class="status-tag approval-ready">
										<input type="checkbox" name="document_ids" value="<s:property value='id' />">Approve</input>
									</small>
									<button type="button" class="quick-approve" data-doc-id="<s:property value='id' />">Quick Approve</button>
								</s:if>
								<s:else>
									<small class="status-tag approval-ready">Ready for Approval</small>
								</s:else>
							</s:if>
							<s:elseif test="isApproved()">
								<small class="status-tag approved">Approved</small>
							</s:elseif>

							<s:elseif test="!isApproved()">
								<s:if test="!isSubmitted()"><small class="status-tag not-submitted">Time Not Submitted</small></s:if>
								<s:elseif test="!isApproved()"><small class="status-tag not-approved">Time Not Approved</small></s:elseif>
							</s:elseif>
							<s:elseif test="isProcessed()">
								<small class="status-tag processed">Payroll Approved</small>
							</s:elseif>

							<s:else>
								<small class="status-tag not-submitted">Not Submitted</small>
							</s:else>
						</h2>

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
						<strong>Available Accruals</strong> <s:property value="employeeAccrualsShort" /> <br />
						<s:if test="hasJob()">
							<strong>Weekly Standard Work Hrs: </strong> <s:property value="job.weekly_regular_hours" />, <strong>Weekly Compt Time Earned After Hrs: </strong> <s:property value="job.comp_time_weekly_hours" /> <br />
						</s:if>
					</div>
				</s:if>
			</s:iterator>
		</s:if>

		<s:if test="isUserCurrentEmployee()">
			<s:submit name="action" type="submit" value="Approve" class="button"/>
		</s:if>
	</s:if>
</s:form>

<%@  include file="footer.jsp" %>
