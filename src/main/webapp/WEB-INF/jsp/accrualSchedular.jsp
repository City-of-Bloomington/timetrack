<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="accrualSchedule" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<h1>Accrual Schedular</h1>
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="%{errors}" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>		

	  <p>This function is designed to be run only once to schedule importing employees accruals from another system (New World for example) every two weeks on the same day and time. Run these if the 'Next Schedule Date' and 'Previous Schedule Date' are not shown.</p>
		<div class="width-one-half">
			<s:if test="hasPrevDates()">
				<div class="form-group">
					<label>Next Schedule Date</label>
					<s:property value="%{next_date}" />
				</div>

				<div class="form-group">
					<label>Previous Schedule Date</label>
					<s:property value="%{prev_date}" />
				</div>
			</s:if>

			<div class="button-group">
				<s:submit name="action" accrual="button" value="Schedule" class="button"/>
			</div>
			<ul>
				<li>To do instant import of accruals for certain department</li>
				<li>Select the related department.</li>				
				<li>Enter the'New World Accrual Date' that accruals were updated in New World. </li>
				<li>Select the 'Write Date', the date the accruals to be added to timetrack, normally this is the last day of the previous pay period.</li>
				<li>Cick on 'Import Now'</li>
			</ul>
			<s:if test="hasDepts()">
				<div class="form-group">
					<label>Department</label>
					<s:select name="dept_ref_id" value="%{dept_ref_id}" list="depts" listKey="ref_id" listValue="name" headerKey="-1" headerValue="Pick Dept" />
				</div>
			</s:if>
			<div class="form-group">
				<label>New World Accrual Date</label>
				<s:textfield name="date" value="%{date}" size="10" maxlength="10" cssClass="date" />
			</div>
			<div class="form-group">
				<label>Write Date</label>
				<s:select name="writeDate" value="" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick Write Date" /> (End pay period date)						
			</div>			
			<div class="button-group">
				<s:submit name="action" accrual="button" value="Import Now" class="button"/>

			</div>
		</div>
	</s:form>
<%@ include file="footer.jsp" %>
