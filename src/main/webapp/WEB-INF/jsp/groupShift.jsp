<%@ include file="header.jsp" %>
<div class="internal-page">
	
	<s:form action="groupShift" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="groupShift.group_id != ''">
			<s:hidden name="groupShift.group_id" value="%{groupShift.group_id}" />			
		</s:if>
		<s:if test="groupShift.id == ''">
			<h1>Add shift to group </h1>
		</s:if>
		<s:else>
			<s:hidden name="groupShift.id" value="%{groupShift.id}" />						
			<h1>Edit Group Shift: <s:property value="%{groupShift.id}" /></h1>
		</s:else>
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="%{errors}" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>
		Note:Each group can have only one active shift <br />
		<div class="width-one-half">
			<s:if test="groupShift.group_id != ''">			
				<div class="form-group">
					<label>Group </label>
					<s:property value="%{groupShift.group}" />
				</div>
			</s:if>
			<s:else>
				<div class="form-group">
					<label>Department</label>
					<s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Dept" required="true" id="department_id_change" />
				</div>
				<div class="form-group">
					<label>Group</label>
					<select name="groupShift.group_id" value="" id="group_id_set"  disabled="disabled" >
						<option value="-1">Pick a Group</option>
					</select><br />
					(To pick a group you need to pick a department first)
				</div>
			</s:else>
			<div class="form-group">			
				<label>Shifts </label>
				<s:select name="groupShift.shift_id" value="%{groupShift.shift_id}" list="shifts" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Shift" required="true" />
			</div>
			<div class="form-group">
				<label>Start Date </label>
				<s:textfield name="groupShift.startDate" value="%{groupShift.startDate}" size="10" maxlength="10" class="date" />
			</div>
			<div class="form-group">
				<label>Expire Date </label>
				<s:textfield name="groupShift.expireDate" value="%{groupShift.expireDate}" size="10" maxlength="10" class="date" />
			</div>
			<div class="form-group">
				<label>Inactive?</label>
				<s:checkbox name="groupShift.inactive" value="%{groupShift.inactive}" fieldValue="true" />Yes
			</div>			
			<s:if test="groupShift.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>
			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url' />groupShift.action" class="button">Assign another shift to a group </a>					
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>
	<s:if test="groupShifts != null">
		<s:set var="groupShifts" value="groupShifts" />
		<s:set var="groupShiftsTitle" value="groupShiftsTitle" />
		<%@ include file="groupShifts.jsp" %>
	</s:if>

</div>
<%@ include file="footer.jsp" %>
