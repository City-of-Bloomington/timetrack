<%@  include file="header.jsp" %>
<s:form action="approve" id="form_id" method="post" class="internal-page">
	<s:hidden name="action2" id="action2" value="" />
	<h1>Approve Timesheets
		<small><b>Approver:&nbsp;</b><s:property value="employee.full_name" /></small>
	</h1>

	<s:if test="hasActionErrors()">
		<div class="errors">
	    <s:actionerror/>
		</div>
	</s:if>
	<s:elseif test="hasActionMessages()">
		<div class="welcome">
	    <s:actionmessage/>
		</div>
	</s:elseif>

	<s:if test="hasGroups()">
		<div class="calendar-header-controls">
			<div class="button-group">
		    <a href="<s:property value='#application.url' />approve.action?pay_period_id=<s:property value='previousPayPeriod.id' />" class="button hide-text has-icon chevron-left"><span>Backwards</span></a>
		    <a href="<s:property value='#application.url' />approve.action?pay_period_id=<s:property value='currentPayPeriod.id' />" class="button today"><span>Today</span></a>
		    <a href="<s:property value='#application.url' />approve.action?pay_period_id=<s:property value='nextPayPeriod.id' />" class="button hide-text has-icon chevron-right"><span>Forwards</span></a>
		  </div>

		  <div class="pay-period">
		  	<s:if test="hasMoreThanOneGroup()">
					<b>Group:&nbsp;</b><s:select name="group_id" value="%{group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" onchange="doRefresh()" />
				</s:if>

				<b>Pay Period:&nbsp;</b><s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Period" onchange="doRefresh()" />
			</div>
		</div>

		<small class="status-tag approved">
			<input type="checkbox" name="check_all" value="y" id="approve_select_all"/><b>Select All (Approvals)</b>
		</small>

		<s:if test="hasNonDocEmps()">
		<div class="emp-no-time-wrapper">
			<strong>Employee(s) with no time entry for this pay period:</strong>
			<s:iterator var="one" value="nonDocEmps">
				<a href="<s:property value='#application.url' />timeDetails.action?employee_id=<s:property value='id' />&pay_period_id=<s:property value='pay_period_id' />&source=approve">
					<s:property value="full_name" />,</a>&nbsp;
			</s:iterator>
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
								<small class="status-tag approved">
									<input type="checkbox" name="document_ids" value="<s:property value='id' />">Approve</input></small>
							</s:if>
							<s:elseif test="isApproved()">
								<small class="status-tag approved">Approved</small>
							</s:elseif>
							<s:elseif test="isProcessed()">
								<small class="status-tag processed">Processed</small>
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
					</div>
				</s:if>
			</s:iterator>
		</s:if>
		<!-- for testing we comment out this, we need to remove in prod
		<s:if test="isUserCurrentEmployee()">
			<s:submit name="action" type="button" value="Approve" class="fn1-btn"/>
		</s:if>
		-->
		<s:submit name="action" type="submit" value="Approve" class="fn1-btn"/>
	</s:if>
</s:form>

<%@  include file="footer.jsp" %>
