<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="termNotificationSchedule" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h1>Managers Termination Notification Reminder</h1>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="messages" />			
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
	    <s:set var="errors" value="errors" />			
	    <%@ include file="errors.jsp" %>
	</s:elseif>
	
	<p>This function is designed to run once a day around 6:45 am from Nonday to Friday (during working week days)</p>
	<p>The process will lookup all the recent terminations that are not complete by the managers or forgot to click on 'Send Notification' button to inform the related departments of the employee termination(s) </p> 
	<p>You may run this process right away if needed by clicking on 'Submit'</p>
	<p>For first time, need to click 'Schedule'</p>
	<div class="button-group">
	    <s:submit name="action" accrual="button" value="Schedule" class="button"/>
	    <s:submit name="action" accrual="button" value="Submit" class="button"/>
	</div>
    </s:form>
</div>

<%@ include file="footer.jsp" %>
