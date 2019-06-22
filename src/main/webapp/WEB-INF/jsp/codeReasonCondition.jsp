<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="codeReasonCondition" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="condition.id == ''">
			<h1>New Code Reason Restriction</h1>
		</s:if>
		<s:else>
			<h1>Edit Code Reason Restriction: <s:property value="condition.id" /></h1>
			<s:hidden name="condition.id" value="%{condition.id}" />
		</s:else>
		<s:if test="hasErrors()">
			<s:set var="errors" value="%{errors}" />		
			<%@ include file="errors.jsp" %>
		</s:if>		
		<s:elseif test="hasMessages()">
			<s:set var="messages" value="%{messages}" />					
			<%@ include file="messages.jsp" %>
		</s:elseif>
		<s:if test="condition.id != ''">
			<div class="form-group">
				<label>ID</label>
				<s:property value="%{condition.id}" />
			</div>
		</s:if>
		<div class="form-group">
			<label>Code Reason</label>
			<s:select name="condition.reason_id" value="%{condition.reason_id}" required="true" list="reasons" listKey="id" listValue="description" headerKey="-1" headerValue="Pick Code Reason" />
		</div>		
		<div class="form-group">
			<label>Earn Code</label>
			<s:select name="condition.hour_code_id" value="%{condition.hour_code_id}" required="true" list="hourcodes" listKey="id" listValue="codeInfo" headerKey="-1" headerValue="Pick Earn Code" />
		</div>
		<div class="form-group">
			<label>Salary Group</label>
			<s:select name="condition.salary_group_id" value="%{condition.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="All" required="true" />
		</div>
		<div class="form-group">
			<label>Department</label>+``````````````````````````
			<s:select name="condition.department_id" value="%{condition.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" id="department_id_change" />
		</div>
		<s:if test="condition.id != ''">
			<div class="form-group">
				<label>Group</label>
				<s:select name="condition.group_id" value="%{condition.group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" /> (to pick a group pick a group department first) <br />
			</div>
		</s:if>
		<s:else>
			<div class="form-group">
				<label>Group</label>			
				<select name="condition.group_id" id="group_id_set"  disabled="disabled" >
					<option value="-1">All</option>
				</select>
			</div>
		</s:else>
		<s:if test="condition.id != ''">
			<div class="form-group">
				<label>Inactive ?</label>
				<s:checkbox name="condition.inactive" value="%{condition.inactive}" /> Yes (check to dissable)
			</div>
		</s:if>
		<div class="button-group">
			<s:if test="condition.id == ''">
				<s:submit name="action" type="button" value="Save" />
			</s:if>
			<s:else>
				<a href="<s:property value='#application.url' />codeReasonCondition.action?" class="button">New Code Reason Restriction</a>
				<s:submit name="action" type="button" value="Save Changes" />
				<s:submit name="action" type="button" value="Delete" />				
			</s:else>
		</div>		
	</s:form>
	<s:if test="hasConditions()">
		<s:set var="conditions" value="%{conditions}" />
		<s:set var="conditionsTitle" value="conditionsTitle" />
		<%@ include file="codeReasonConditions.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
