<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:if test="hasMessages()">
		<s:set var="messages" value="%{messages}" />			
		<%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
		<s:set var="errors" value="%{errors}" />			
		<%@ include file="errors.jsp" %>
	</s:elseif>		
	<s:form action="groupEmployee" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="groupEmployee.employee_id" value="%{groupEmployee.employee_id}" />
		<s:if test="groupEmployee.id == ''">
			<h1>Add Employee to a Group</h1>
		</s:if>
		<s:else>
			<h1>Edit/Change Employee Group </h1>
			<ul>
				<li>If you want to change employee group and the related job to
					a new group, pick the new group from the list and hit 'Save Changes'</li>
				<li>If you want to change the employee group to a new group but keep the old group related job then click on 'Change Employee Group' link </li>
			</ul>
			<s:hidden id="groupEmployee.id" name="groupEmployee.id" value="%{groupEmployee.id}" />
			<s:hidden id="groupEmployee.id" name="groupEmployee.group_id" value="%{groupEmployee.group_id}" />
		</s:else>


	  <div class="width-one-half">
			<div class="form-group">
				<label>Employee</label>
				<a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='groupEmployee.employee_id' />" /><s:property value="%{groupEmployee.employee}" /></a>
			</div>
			<div class="form-group">
				<label>Group</label>
				<s:select name="groupEmployee.new_group_id" value="%{groupEmployee.group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Group" />
			</div>
			<div class="form-group">
				<label>Effective Date</label>
					<div class="date-range-picker">
						<div>
							<s:if test="groupEmployee.id == ''">
								<s:select name="groupEmployee.effective_date" value="" list="payPeriods" listKey="startDate" listValue="startDate" headerKey="-1" headerValue="Pick Start Date" /> (Start pay period date)
							</s:if>
							<s:else>
								<s:textfield name="groupEmployee.effective_date" value="%{groupEmployee.effective_date}" size="10" maxlength="10" cssClass="date" required="true" /> (Start pay period date)
							</s:else>
						</div>
					</div>
			</div>
			<div class="form-group">
				<label>Expire Date</label>
				<div class="date-range-picker">
					<div>									
						<s:if test="groupEmployee.hasExpireDate()">
							<s:textfield name="groupEmployee.expire_date" value="%{groupEmployee.expire_date}" size="10" maxlength="10" cssClass="date" /> (End pay period date)
						</s:if>
						<s:else>
							<s:select name="groupEmployee.expire_date" value="" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick End Date" /> (End pay period date)							
						</s:else>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Inactive</label>
				<s:checkbox name="groupEmployee.inactive" value="%{groupEmployee.inactive}" /> Yes (check to disable)
			</div>
			<s:if test="groupEmployee.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>
			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url' />groupEmpChange.action?id=<s:property value='groupEmployee.id' />" class="button"> Change Employee Group</a>
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>

	<s:if test="hasGroupEmployees()">
		<s:set var="groupEmployees" value="%{groupEmployees}" />
		<s:set var="groupEmployeesTitle" value="%{groupEmployeesTitle}" />
		<%@ include file="groupEmployees.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
