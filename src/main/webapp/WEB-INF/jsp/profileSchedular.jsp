<%@ include file="header.jsp" %>
<div class="internal-page">
	<h1>Employees' Profile Schedular</h1>
		<s:if test="hasMessages()">
			<s:set var="messages" value="messages" />			
			<%@ include file="messages.jsp" %>
		</s:if>	
	<s:form action="profileSchedule" id="form_id" method="post" >
	  <p>This function is designed to be run only once to schedule importing employees profiles from another system (New World for example) every two weeks on the same day and time. Run these if the 'Next Schedule Date' and 'Previous Schedule Date' are not shown.</p>

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
			<div class="form-group">
				<label>Date</label>
				<s:textfield name="date" value="%{date}" size="10" maxlength="10" cssClass="date" />
			</div>

			<s:if test="hasDepts()">
				<div class="form-group">
					<label>Department</label>
					<s:select name="dept_ref_id" value="%{dept_ref_id}" list="depts" listKey="ref_id" listValue="name" headerKey="-1" headerValue="Pick Dept" />
				</div>
			</s:if>

			<div class="button-group">
				<s:submit name="action" accrual="button" value="Update Now" class="button"/>
				<s:submit name="action" accrual="button" value="Find Employee (Test)" class="button"/>				
				<s:submit name="action" accrual="button" value="Schedule" class="button"/>
			</div>
		</div>
	</s:form>
<%@ include file="footer.jsp" %>
