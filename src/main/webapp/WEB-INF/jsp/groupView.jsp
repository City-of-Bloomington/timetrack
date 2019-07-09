<%@ include file="header.jsp" %>
<div class="internal-page">
	<h1>Group: <s:property value="%{group.name}" /></h1>
	<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />			
		<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="%{errors}" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>
		<div class="width-one-half">
			<div class="form-group">
				<label>ID</label>
				<s:property value="group.id" />
			</div>

			<div class="form-group">
				<label>Name </label>
				<s:property value="%{group.name}" />
			</div>

			<div class="form-group">
				<label>Department</label>
				<s:property value="%{group.department}" />
			</div>
			<div class="form-group">
				<label>Description</label>
				<s:property value="%{group.description}" />
			</div>
			<div class="form-group">
				<label>Excess Hours Earn Type</label>			
				<s:property value="%{group.excessHoursEarnType}" />
			</div>
			<div class="form-group">
				<label>Allow Pending Accrual?</label>
				<s:if test="group.allowPendingAccrual">Yes</s:if><s:else>No</s:else>
			</div>
			<div class="form-group">
				<label>Active Status?</label>
				<s:if test="group.inactive">Inactive</s:if><s:else>Active</s:else>
			</div>
			<div class="button-group">
				<a href="<s:property value='#application.url' />group.action?id=<s:property value='group.id' />&action=Edit" class="button">Edit </a>
				<a href="<s:property value='#application.url' />groupManagerAdd.action?group_id=<s:property value='group.id' />" class="button">Assign managers to group </a>
			</div>
		</div>
		<s:if test="group.hasGroupEmployees()">
			<s:set var="groupEmployees" value="%{group.groupEmployees}" />
			<s:set var="groupEmployeesTitle" value="'Group Members'" />
			<%@ include file="groupEmployees.jsp" %>
		</s:if>
		<s:if test="group.hasGroupShifts()">
			<s:set var="groupShifts" value="%{group.groupShifts}" />
			<s:set var="groupShiftsTitle" value="'Group Shifts'" />
			<%@ include file="groupShifts.jsp" %>
		</s:if>
		<s:if test="hasGroupManagers()">
			<s:set var="groupManagers" value="%{groupManagers}" />
			<s:set var="groupManagersTitle" value="'Group Managers'" />
			<%@ include file="groupManagers.jsp" %>
		</s:if>
</div>
<%@ include file="footer.jsp" %>
