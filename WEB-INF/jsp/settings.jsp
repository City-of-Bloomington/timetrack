<%@  include file="header.jsp" %>
<h3>Settings</h3>
<s:if test="hasActionErrors()">
  <div class="errors">
    <s:actionerror/>
  </div>
</s:if>
<ul>
	<s:if test="#session != null && #session.user.isAdmin()">
		<li><a href="<s:property value='#application.url'/>timeClock.action">Time Clock</a></li>		
		<li><a href="<s:property value='#application.url'/>switch.action">Change Target Employee</a></li>
		<li><a href="<s:property value='#application.url'/>hourcode.action">Hour Codes</a></li>
		<li><a href="<s:property value='#application.url'/>hourcodeCondition.action">Hour Code Restrictions</a></li>
		<li><a href="<s:property value='#application.url'/>type.action?type_name=position">Positions</a></li>
		<li><a href="<s:property value='#application.url'/>salaryGroup.action">Salary Groups</a></li>
		<li><a href="<s:property value='#application.url'/>payperiod.action">Pay Periods</a></li>
		<li><a href="<s:property value='#application.url'/>type.action?type_name=department">Departments</a></li>
		<li><a href="<s:property value='#application.url'/>node.action">Workflow Actions</a></li>
		<li><a href="<s:property value='#application.url'/>accrual.action"">Accrual Types</a></li>
		<li><a href="<s:property value='#application.url'/>accrualWarning.action">Accrual Warnings </a></li>		
		<li><a href="<s:property value='#application.url'/>group.action">Employee Groups</a></li>
		<li><a href="<s:property value='#application.url'/>employee.action">Employees</a></li>
		<li><a href="<s:property value='#application.url'/>jobTask.action">Jobs</a></li>
		<li><a href="<s:property value='#application.url'/>empAccrual.action">Employee Accruals</a></li>		
	</s:if>
</ul>

<%@  include file="footer.jsp" %>
