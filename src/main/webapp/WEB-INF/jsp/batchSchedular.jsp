<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="batchSchedule" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<h1>Batch Submission Schedular</h1>

	  <%@ include file="strutMessages.jsp" %>

	  <p>This function is designed to be run only once to schedule batch submission for punch clock employees who do not submit their work times. This is designed to run on Monday morning at 7:30am the day next to the last day of previous pay period.</p>
		<p>Run the schedule if the 'Next Schedule Date' and 'Previous Schedule Date' are not shown.</p>
		<p>To submit right now, pick pay period and click on 'Submit Now'.</p>

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

			<s:if test="hasPeriods()">
				<div class="form-group">
					<label>Pay Period</label>
					<s:select name="pay_period_id" value="%{pay_period_id}" list="periods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Pay Period" />
				</div>
			</s:if>

			<div class="button-group">
				<s:submit name="action" accrual="button" value="Schedule" class="button"/>
				<s:submit name="action" accrual="button" value="Submit Now" class="button"/>
			</div>
		</div>
	</s:form>
</div>
<%@ include file="footer.jsp" %>