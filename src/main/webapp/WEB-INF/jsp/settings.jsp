<%@  include file="header.jsp" %>

<div class="internal-page container clearfix settings">
	<h1>Admin Menu</h1>

	<s:if test="hasErrors()">
	  <div class="errors">
			<s:set var="errors" value="errors" />
			<%@ include file="errors.jsp" %>
	  </div>
	</s:if>
	<s:if test="#session != null && #session.user.isAdmin()">
		<div class="width-one-half float-left">
			<h2>Settings</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>accrual.action"">Accrual Types</a></li>
				<li><a href="<s:property value='#application.url'/>accrualWarning.action">Accrual Warnings </a></li>
				<li><a href="<s:property value='#application.url'/>location.action">Locations</a></li>				
				<li><a href="<s:property value='#application.url'/>benefitGroup.action">Benefit Groups</a></li>
				<li><a href="<s:property value='#application.url'/>department.action">Departments</a></li>				
				<li><a href="<s:property value='#application.url'/>employee.action">Employees</a></li>
				<li><a href="<s:property value='#application.url'/>empWizard.action">Add Employee Wizard</a></li>
				<li><a href="<s:property value='#application.url'/>empTimeChange.action">Employee Job Times Change</a></li>				
				<li><a href="<s:property value='#application.url'/>empAccrual.action">Employee Accruals</a></li>
				<li><a href="<s:property value='#application.url'/>groupLocation.action">Group Locations</a></li>				
				<li><a href="<s:property value='#application.url'/>hourcode.action">Earn Codes</a></li>
				
				<li><a href="<s:property value='#application.url'/>hourcodeCondition.action">Earn Code Restrictions</a></li>
				<li><a href="<s:property value='#application.url'/>codeRef.action">Earn Code Cross Reference</a></li>
				<li><a href="<s:property value='#application.url'/>reasonCategory.action">Code Reason Categories (Police)</a></li>				
				<li><a href="<s:property value='#application.url'/>codeReason.action">Earn Code Reasons (Police)</a></li>
				<li><a href="<s:property value='#application.url'/>codeReasonCondition.action">Code Reason Restrictions (Police)</a></li>				
				<li><a href="<s:property value='#application.url'/>contribute.action"">Earn Code Accraul Contributers</a></li>				
				<li><a href="<s:property value='#application.url'/>holiday.action">Holidays</a></li>				
				<li><a href="<s:property value='#application.url'/>group.action">Groups</a></li>
				<li><a href="<s:property value='#application.url'/>payperiod.action">Pay Periods</a></li>				
				<li><a href="<s:property value='#application.url'/>position.action">Positions</a></li>
				<li><a href="<s:property value='#application.url'/>salaryGroup.action">Salary Groups</a></li>
				<li><a href="<s:property value='#application.url'/>serviceKey.action">Service Keys</a></li>				
				<li><a href="<s:property value='#application.url'/>shift.action">Shifts</a></li>				
				<li><a href="<s:property value='#application.url'/>node.action">Workflow Actions</a></li>
			</ul>
			
			<h2>Shift Times (Fire)</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>shiftTime.action">Employee Shift Times</a></li>
			</ul>
			
			<h2>Employee Data Import</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>empImport.action">Employees Data Import (CSV file)</a></li>
			</ul>
			
			<h2>Audit</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>notificationSchedule.action?action=logs">Recent Automated Notification logs</a></li>				
				<li><a href="<s:property value='#application.url'/>inform.action?action=logs">Email Logs (By Group Managers)</a></li>
				<li><a href="<s:property value='#application.url'/>timeBlockLog.action">Time Entry History</a></li>
				<li><a href="<s:property value='#application.url'/>auditTime.action">Time Active Users (Email list)</a></li>				
			</ul>
		</div>
		<div class="width-one-half float-right">
			<h2>Time Clock</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>timeClock.action">Time Clock</a></li>
			</ul>
			<h2>Search</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>searchEmployees.action">Employees</a></li>
				<li><a href="<s:property value='#application.url'/>searchDeptEmployees.action">Department Employees</a></li>				
				<li><a href="<s:property value='#application.url'/>searchGroups.action">Groups</a></li>
				<li><a href="<s:property value='#application.url'/>searchJobs.action">Jobs</a></li>
				<li><a href="<s:property value='#application.url'/>searchConditions.action">Earn Code Restrictions</a></li>
				<li><a href="<s:property value='#application.url'/>searchReasonConditions.action">Code Reason Restrictions</a></li>				
			</ul>
			<h2>Target Employee</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>switch.action">Change Target Employee</a></li>
			</ul>
			<h2>Timewarp</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>tmwrpWrap.action">Timewarp </a></li>								
				<li><a href="<s:property value='#application.url'/>tmwrpInitiate.action">Timewarp Initiatation Process</a></li>				
			</ul>

			<h2>Reports</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>activeEmployees.action">Active Employees</a></li>								
				<li><a href="<s:property value='#application.url'/>reportHand.action">HAND MPO</a></li>
				<li><a href="<s:property value='#application.url'/>reportPlan.action">Planning MPO</a></li>
				<li><a href="<s:property value='#application.url'/>reportFmla.action">HR FMLA</a></li>
				<li><a href="<s:property value='#application.url'/>jobsReport.action">Employee Jobs</a></li>
				<li><a href="<s:property value='#application.url'/>jobIntervention.action">Parks Jobs Need Intervention</a></li>
				<li><a href="<s:property value='#application.url'/>reportTimes.action">Employees Time Details</a></li>
				<li><a href="<s:property value='#application.url'/>reportReason.action">Police Code Reasons</a></li>
				<li><a href="<s:property value='#application.url'/>reportPublicWorks.action">Asset Management (Public Works)</a></li>
				<li><a href="<s:property value='#application.url'/>reportUnscheduled.action">Unscheduled Times</a></li>				
			</ul>			
			<h2>Schedulars</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>accrualSchedule.action">Employees' accrual</a></li>
				<li><a href="<s:property value='#application.url'/>profileSchedule.action">Employees Profile Update</a></li>
				<li><a href="<s:property value='#application.url'/>empsUpdateSchedule.action">Employees' Update from AD (ldap)</a></li>				
				<li><a href="<s:property value='#application.url'/>notificationSchedule.action">Employees reminder notification</a></li>
				<li><a href="<s:property value='#application.url'/>batchSchedule.action">Punch Clock Batch Auto Submission </a></li>
			</ul>
		</div>
	</s:if>
</div>

<%@  include file="footer.jsp" %>
