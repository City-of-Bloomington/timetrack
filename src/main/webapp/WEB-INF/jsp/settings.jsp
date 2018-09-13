<%@  include file="header.jsp" %>

<div class="internal-page container clearfix">
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
				<li><a href="<s:property value='#application.url'/>hourcode.action">Hour Codes</a></li>
				<li><a href="<s:property value='#application.url'/>hourcodeCondition.action">Hour Code Restrictions</a></li>
				<li><a href="<s:property value='#application.url'/>type.action?type_name=position">Positions</a></li>
				<li><a href="<s:property value='#application.url'/>salaryGroup.action">Salary Groups</a></li>
				<li><a href="<s:property value='#application.url'/>payperiod.action">Pay Periods</a></li>
				<li><a href="<s:property value='#application.url'/>department.action">Departments</a></li>
				<li><a href="<s:property value='#application.url'/>node.action">Workflow Actions</a></li>
				<li><a href="<s:property value='#application.url'/>accrual.action"">Accrual Types</a></li>
				<li><a href="<s:property value='#application.url'/>accrualWarning.action">Accrual Warnings </a></li>
				<li><a href="<s:property value='#application.url'/>group.action">Groups</a></li>
				<li><a href="<s:property value='#application.url'/>employee.action">Employees</a></li>
				<li><a href="<s:property value='#application.url'/>importEmployees.action">Import AD (ldap) Employees</a></li>

				<li><a href="<s:property value='#application.url'/>jobTask.action">Jobs</a></li>
				<li><a href="<s:property value='#application.url'/>empAccrual.action">Employee Accruals</a></li>
				<li><a href="<s:property value='#application.url'/>holiday.action">Holidays</a></li>
				<li><a href="<s:property value='#application.url'/>benefitGroup.action">Benefit Groups</a></li>
				<li><a href="<s:property value='#application.url'/>codeRef.action">Hour Code Cross Reference</a></li>
				<li><a href="<s:property value='#application.url'/>ipaddress.action">Allowed IP Addresses</a></li>
			</ul>
			<h2>Audit</h2>
			<ul>			
				<li><a href="<s:property value='#application.url'/>inform.action?action=logs">Approvers/Payroll Email Logs</a></li>
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
				<li><a href="<s:property value='#application.url'/>searchGroups.action">Groups</a></li>
				<li><a href="<s:property value='#application.url'/>searchJobs.action">Jobs</a></li>
			</ul>
			<h2>Target Employee</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>switch.action">Change Target Employee</a></li>
			</ul>
			<h2>Timewarp</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>payperiodProcess.action">Pay Period Process</a></li>
				<li><a href="<s:property value='#application.url'/>reportHand.action">HAND MPO Report</a></li>
				<li><a href="<s:property value='#application.url'/>reportPlan.action">Planning MPO Report</a></li>
				<li><a href="<s:property value='#application.url'/>reportFmla.action">HR FMLA Report</a></li>
			</ul>
			<h2>Data Import</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>empImport.action">Employees Data Import (CSV file)</a></li>
			</ul>			
			<h2>Schedulars</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>accrualSchedule.action">Employees' accrual</a></li>
				<li><a href="<s:property value='#application.url'/>notificationSchedule.action">Employees reminder notification</a></li>
				<li><a href="<s:property value='#application.url'/>batchSchedule.action">Punch clock batch submission </a></li>
				<li><a href="<s:property value='#application.url'/>empsUpdateSchedule.action">Update Employees (including active status)</a></li>
			</ul>
		</div>
	</s:if>
</div>

<%@  include file="footer.jsp" %>
