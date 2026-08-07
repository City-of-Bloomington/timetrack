<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="notificationTempSchedule" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h1>Notification Temp/Seasonal Employee Limit Schedular</h1>
	
	<%@ include file="strutMessages.jsp" %>
	
	<p>This function is designed to be run only once to schedule email notification for supervisors whose employees exceeded max employment limit. Temp and Seasonal employees.
	</p>
	<p>Run the schedule if the 'Next Schedule Date' and 'Previous Schedule Date' are not shown.</p>
	<p>To notify supervisors right now, click on 'Submit' also for testing</p>
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
		<s:submit name="action" accrual="button" value="Submit" class="button"/>		
	    </div>
	</div>
    </s:form>
</div>
<%@ include file="footer.jsp" %>
