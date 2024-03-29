<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="group" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="group.id == ''">
	    <h1>New group</h1>
	</s:if>
	<s:else>
	    <h1>Edit Group: <s:property value="%{group.name}" /></h1>
	    <s:hidden id="group.id" name="group.id" value="%{group.id}" />
	</s:else>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />			
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />			
	    <%@ include file="errors.jsp" %>
	</s:elseif>
	<ul>
	    <li>Excess Hours Earn Type is the compensatory type either 'Earn Time' such as 'CE1.0 Comp Time' or Monetary such as 'OT1.0 Overtime' for certain groups such as union, if hours worked exceeds weekly, daily or pay period hours. The Donation is for groups that do not earn either. </li>
	</ul>
	<div class="width-one-half">
	    <s:if test="group.id != ''">
		<div class="form-group">
		    <label>ID</label>
		    <s:property value="group.id" />
		</div>
	    </s:if>
	    
	    <div class="form-group">
		<label>Name </label>
		<s:textfield name="group.name" value="%{group.name}" size="30" maxlength="70" required="true" />
	    </div>
	    
	    <div class="form-group">
		<label>Department</label>
		<s:select name="group.department_id" value="%{group.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Dept" required="true" />
	    </div>
	    
	    <div class="form-group">
		<label>Description</label>
		<s:textarea name="group.description" value="%{group.description}" rows="5" cols="50" />
	    </div>
	    <div class="form-group">
		<label>Excess Hours Earn Type</label>			
		<s:radio name="group.excessHoursEarnType" value="%{group.excessHoursEarnType}" list="excessHoursEarnTypes" />
	    </div>
	    <div class="form-group">
		<label>Clock Time Required?</label>
		<s:checkbox name="group.clock_time_required" value="%{group.clock_time_required}" fieldValue="true" id="clock_required_id" />Yes (Employees who are required to use punch clock)
	    </div>
	    
	    <div class="form-group">
		<label>Allow Pending Accrual?</label>
		<s:checkbox name="group.allowPendingAccrual" value="%{group.allowPendingAccrual}" fieldValue="true" />Yes
	    </div>
	    <div class="form-group">
		<label>Include in Auto Submit Batch?</label>
		<s:checkbox name="group.includeInAutoBatch" value="%{group.includeInAutoBatch}" fieldValue="true" id="include_in_auto_batch" />Yes (The system will auto submit timesheet for approval for the whole group)
	    </div>			
	    
	    <div class="form-group">
		<label>Inactive?</label>
		<s:checkbox name="group.inactive" value="%{group.inactive}" fieldValue="true" />Yes
	    </div>
	    
	    <s:if test="group.id == ''">
		<s:submit name="action" type="button" value="Save" class="button"/>
	    </s:if>
	    <s:else>
		<div class="button-group">
		    <a href="<s:property value='#application.url' />groupManagerAdd.action?group_id=<s:property value='group.id' />" class="button">Assign managers to group </a>
		    <a href="<s:property value='#application.url' />groupJobTerminate.action?group_id=<s:property value='group.id' />" class="button">Terminate group jobs</a>	  
		    <a href="<s:property value='#application.url'/>group.action" class="button">New Group</a><s:submit name="action" type="button" value="Save Changes" class="button"/>
		    <s:if test="group.hasNoShift()">
			<a href="<s:property value='#application.url'/>groupShift.action?group_id=<s:property value='group.id' />" class="button">Add Time Rounding</a>		
		    </s:if>
		</div>
	    </s:else>
	</div>
    </s:form>

    <s:if test="group.id != ''">
	<s:if test="group.hasEmployees()">
	    <s:set var="employees" value="%{group.employees}" />
	    <s:set var="employeesTitle" value="'Group Members'" />
	    <%@ include file="employees.jsp" %>
	</s:if>
	<s:if test="group.hasJobs()">
	    <s:set var="jobTasks" value="%{group.jobs}" />
	    <s:set var="jobTasksTitle" value="'Group Jobs'" />
	    <s:set var="skipGroupName" value="'true'" />	    
	    <%@ include file="jobTasks.jsp" %>
	</s:if>	
	<s:if test="group.hasGroupShifts()">
	    <s:set var="groupShifts" value="%{group.groupShifts}" />
	    <s:set var="groupShiftsTitle" value="'Group Shifts'" />
	    <%@ include file="groupShifts.jsp" %>
	</s:if>
	<s:if test="group.hasLocation()">
	    <s:set var="location" value="%{group.location}" />
	    <s:set var="LocationTitle" value="'Group Location'" />
	    <%@ include file="location_row.jsp" %>
	</s:if>	
	<s:if test="hasGroupManagers()">
	    <s:set var="groupManagers" value="%{groupManagers}" />
	    <s:set var="groupManagersTitle" value="'Group Managers'" />
	    <s:set var="skipGroupName" value="'true'" />		    
	    <%@ include file="groupManagers.jsp" %>
	</s:if>
    </s:if>
    
    <s:else>
	<s:if test="groups != null">
	    <s:set var="groups" value="groups" />
	    <s:set var="groupsTitle" value="groupsTitle" />
	    <%@ include file="groups.jsp" %>
	</s:if>
    </s:else>
</div>
<%@ include file="footer.jsp" %>
