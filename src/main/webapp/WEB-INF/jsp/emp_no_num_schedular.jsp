<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="emp_no_num_schedular" id="form_id" method="post" >
	    <s:hidden name="action2" id="action2" value="" />
	    <h1>Employee w/No Employee Number Schedular</h1>
	    <s:if test="hasMessages()">
		<s:set var="messages" value="%{messages}" />			
		<%@ include file="messages.jsp" %>
	    </s:if>
	    <s:elseif test="hasErrors()">
		<s:set var="errors" value="%{errors}" />			
		<%@ include file="errors.jsp" %>
	    </s:elseif>
	    <ul>
		<li>This function is designed to be run only once to schedule finding new employees with no employee number in timetrack using employees info from NW to add employee number.</li>
		<li>It runs one every two weeks on Sunday Morning before the new pay period.</li>
		<li>
		    If you need to run the update right away use the 'New Employee no number' Report.
		</li>
	    </ul>
	    <div class="width-one-half">
		<s:if test="hasPrevDates()">
		    <div class="form-group">
			<label>Next Scheduled Date</label>
			<s:property value="%{next_date}" />
		    </div>
		    <div class="form-group">
			<label>Previous Scheduled Date</label>
			<s:property value="%{prev_date}" />
		    </div>
		</s:if>
		<div class="button-group">
		    <s:submit name="action" accrual="button" value="Schedule" class="button"/>
		</div>
	    </div>
	</s:form>
</div>
<%@ include file="footer.jsp" %>
