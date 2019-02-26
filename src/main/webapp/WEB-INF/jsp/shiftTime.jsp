<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="shiftTime" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="hasDepartment()">
			<s:hidden name="department_id" value="%{department_id}" />
		</s:if>
		<s:if test="shift.id == ''">
			<h1>New Shift Times</h1>
		</s:if>
		<s:else>
			<h1>Edit Shift Times: <s:property value="%{shift.id}" /></h1>
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
			<li>Pick the department, if not already set</li>
			<li>Pick the group from the list</li>
			<li>Pick pay period</li>
			<li>Set start time and end time in hh:mm format using 24 hours </li>
			<li>For example if shift start time is 7 AM you would enter 07:00 in Start Time box</li>
			<li>For shift end time in the after noon such as 4:30 pm, you enter 16:30 in End Time box.</li>
			<li>If shift end time is 7 AM next day you would enter 31:00 in End Time box</li>
			<li>Add dates one at a time, the dates will copied to the lower textbox </li>
			<li>If you mistakenly added a date, removed from textbox below, just make sure to keep a comma in between dates</li>
			<li>Save or Save Changes</li>
			<li>After you 'Save' or 'Save Changes' make sure there is not any error reported in top of the page</li>
			<li>If there is any error, please fix it and click on 'Save' or 'Save Changes' again</li>
			<li>If there is no errors you can click on 'Process' to add these dates and times for each employee in the group</li>
		</ul>
		<div class="width-one-half">
			<s:if test="shift.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="shift.id" />
				</div>
			</s:if>
			<s:if test="shift.id == ''">
				<div class="form-group">
					<label>Department </label>
					<s:if test="hasDepartment()">
						<s:property value="department" />
					</s:if>
					<s:else>
						<s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" id="department_id_change" />
					</s:else>
				</div>
			</s:if>
			<div class="form-group">
				<label>Group </label>
				<s:if test="hasGroups()">
					<s:select name="shift.group_id" value="%{shift.group_id}" id="group_id_set"  list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick a group" />
				</s:if>
				<s:else>
					<select name="shift.group_id" value="" id="group_id_set"  disabled="disabled">
						<option value="-1">Pick a group</option>
					</select>(To pick a group you need to pick a department first)
				</s:else>
			</div>
			<div class="form-group">
				<label>Pay Period </label>
					<s:select name="shift.pay_period_id" value="%{shift.pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Pay Period" />
			</div>			
			<div class="form-group">
				<label>Default Hour Code </label>
					<s:select name="shift.default_hour_code_id" value="%{shift.default_hour_code_id}" list="hourCodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Hour Code" />
			</div>
			<div class="form-group">
				<label>Start Time </label>
				<s:textfield name="shift.startTime" value="%{shift.startTime}" size="5" maxlength="5" required="true" /> (hh:mm)
			</div>
			<div class="form-group">
				<label>End Time </label>
				<s:textfield name="shift.endTime" value="%{shift.endTime}" size="5" maxlength="5" required="true" /> (hh:mm)
			</div>			
			<div class="form-group">
				<label>Date </label>
				<s:textfield name="date" type="date" value="%{date}" pattern="[0-9]{2}-[0-9]{2}-[0-9]{4}" placeholder="MM/DD/YYYY" id="start_date_id" />(add one at a time)				
			</div>
			<div class="form-group">
				<label>Dates </label>
				<s:textarea name="shift.dates" value="%{shift.dates}" rows="5" cols="70" id="all_dates_id" />
			</div>
			<s:if test="shift.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>
			<s:else>
				<div class="form-group">
					<label>Added on </label>
					<s:property value="%{shift.addedTime}" /> By <s:property value="shift.addedBy" />
				</div>
				<div class="form-group">
					<label>Processed? </label>
					<s:if test="shift.processed">Yes</s:if><s:else>No</s:else>
				</div>
				
				<div class="button-group">
					<s:if test="!shift.processed">					
						<s:submit name="action" type="button" value="Save Changes" class="button"/>
						<s:submit name="action" type="button" value="Process" class="button"/>
					</s:if>
					<s:if test="hasDepartment()">
						<a href="<s:property value='#application.url'/>shiftTime.action?department_id=<s:property value='department_id' />" class="button">New Shift Times</a>
					</s:if>
					<s:else>
						<a href="<s:property value='#application.url'/>shiftTime.action" class="button">New Shift Times</a>
					</s:else>
				</div>
			</s:else>
		</div>
	</s:form>
	<s:if test="shift.id == ''">
		<s:if test="shifts != null">
			<s:set var="shifts" value="shifts" />
			<s:set var="shiftsTitle" value="shiftsTitle" />
			<%@ include file="shiftTimes.jsp" %>
		</s:if>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
