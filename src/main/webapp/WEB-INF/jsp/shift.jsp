<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="shift" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="shift.id == ''">
			<h1>New shift</h1>
		</s:if>
		<s:else>
			<h1>Edit Shift: <s:property value="%{shift.name}" /></h1>
			<s:hidden id="shift.id" name="shift.id" value="%{shift.id}" />
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
			<li>The name could be something like 8 To 5 shift </li>
			<li>Start hour is something like 8, 9, 23 (24 hour format)</li>
			<li>Start minute will be like 0,5,10,15, 30 etc </li>
			<li>Duration is total shift time in minutes, for example 8 hour shift is 480 minutes </li>
			<li>Start minutes window is 0 for no window, or 15 for 15 minutes window </li>
			<li>Time rounding is the multiple of minutes (0 for no rounding), 15 for rounding to 0, 15, 30, 45 etc</li>
			<li>Prefered Earn Time, this is the compensatory method either 'Over Time' or 'Comp Time' if hours worked exceeds shift duration </li>
		</ul>
		<div class="width-one-half">
			<s:if test="shift.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="shift.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Name </label>
				<s:textfield name="shift.name" value="%{shift.name}" size="30" maxlength="70" required="true" />
			</div>

			<div class="form-group">
				<label>Start Hour </label>
				<s:textfield name="shift.startHour" value="%{shift.startHour}" size="2" maxlength="2" required="true" />
			</div>
			<div class="form-group">
				<label>Start Minute </label>
				<s:textfield name="shift.startMinute" value="%{shift.startMinute}" size="2" maxlength="2" required="true" />
			</div>
			<div class="form-group">
				<label>Duration (minutes) </label>
				<s:textfield name="shift.duration" value="%{shift.duration}" size="3" maxlength="3" required="true" />
			</div>
			<div class="form-group">
				<label>Start Minutes Window </label>
				<s:textfield name="shift.startMinuteWindow" value="%{shift.startMinuteWindow}" size="3" maxlength="3" />
			</div>
			<div class="form-group">
				<label>Time Rounding (minutes) </label>
				<s:textfield name="shift.minuteRounding" value="%{shift.minuteRounding}" size="2" maxlength="2" />
			</div>			
			<div class="form-group">
				<label>Prefered Earn Time </label>
				<s:radio name="shift.preferedEarnTime" value="shift.preferedEarnTime" list="#{'CompTime':'Comp Time','OverTime':'Over Time'}" />
			</div>
			<div class="form-group">
				<label>Inactive?</label>
				<s:checkbox name="shift.inactive" value="%{shift.inactive}" fieldValue="true" />Yes
			</div>

			<s:if test="shift.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>
			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url' />groupShift.action?shift_id=<s:property value='shift.id' />" class="button">Add shift to group </a>
					<a href="<s:property value='#application.url'/>shift.action" class="button">New Shift</a><s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>
	<s:if test="shift.id == ''">
		<s:if test="shifts != null">
			<s:set var="shifts" value="shifts" />
			<s:set var="shiftsTitle" value="shiftsTitle" />
			<%@ include file="shifts.jsp" %>
		</s:if>
	</s:if>
	<s:elseif test="hasGroupShifts()">
		<s:set var="groupShifts" value="groupShifts" />
		<s:set var="groupShiftsTitle" value="'Groups Linked to This Shift'" />
		<%@ include file="groupShifts.jsp" %>
	</s:elseif>
</div>
<%@ include file="footer.jsp" %>
