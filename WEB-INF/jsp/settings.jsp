<%@  include file="header.jsp" %>
<h4>Admin Menu</h4>
<s:if test="hasActionErrors()">
  <div class="errors">
    <s:actionerror/>
  </div>
</s:if>

<s:if test="#session != null && #session.user.isAdmin()">
	<table border="0" width="100%">
		<tr>
			<td valign="top">
				Settings
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
					<li><a href="<s:property value='#application.url'/>jobTask.action">Jobs</a></li>
					<li><a href="<s:property value='#application.url'/>empAccrual.action">Employee Accruals</a></li>
					<li><a href="<s:property value='#application.url'/>holiday.action">Holidays</a></li>
					<li><a href="<s:property value='#application.url'/>benefitGroup.action">Benefit Groups</a></li>
					<li><a href="<s:property value='#application.url'/>codeRef.action">Hour Code Cross Reference</a></li>

				</ul>
			</td>
			<td valign="top">
				Time Clock
				<ul>
					<li><a href="<s:property value='#application.url'/>timeClock.action">Time Clock</a></li>
				</ul>
				Search
				<ul>
					<li><a href="<s:property value='#application.url'/>searchEmployees.action">Employees</a></li>										
					<li><a href="<s:property value='#application.url'/>searchGroups.action">Groups</a></li>										
					<li><a href="<s:property value='#application.url'/>searchJobs.action">Jobs</a></li>
				</ul>
				Employee
				<ul>
					<li><a href="<s:property value='#application.url'/>switch.action">Change Target Employee</a></li>
				</ul>
				
				Timewarp 
				<ul>
					<li><a href="<s:property value='#application.url'/>payperiodProcess.action">Pay Period Process</a></li>
					<li><a href="<s:property value='#application.url'/>reportHand.action">HAND MPO Report</a></li>
					<li><a href="<s:property value='#application.url'/>reportPlan.action">Planning MPO Report</a></li>
					<li><a href="<s:property value='#application.url'/>reportFmla.action">HR FMLA Report</a></li>					
					<li><a href="<s:property value='#application.url'/>accrualSchedule.action">Accrual Schedular</a></li>
					
				</ul>
			</td>
		</tr>
	</table>
</s:if>

<%@  include file="footer.jsp" %>
