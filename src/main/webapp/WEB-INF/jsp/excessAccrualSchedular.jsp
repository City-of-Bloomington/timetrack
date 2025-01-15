<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="excess_accruals" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h1>Excess Accrual Warning Schedular</h1>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />			
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />			
	    <%@ include file="errors.jsp" %>
	</s:elseif>		
	<p>This function is designed to be run only once to schedule looking for excess accrual of certain type of accrual hour code (we are looking for comp time accrual) that exceeds certain threshold (40 hours). The process will run once every two weeks around 7:30 am on Fridays</p>
	<div class="width-one-half">
	    <div class="button-group">
		<s:submit name="action" accrual="button" value="Schedule" class="button"/>
	    </div>
	    <ul>
		<li>You may want to run this process now (for example to test)</li>
		<li>Cick on 'Run Now'</li>
	    </ul>
	    <div class="form-group">
		<label>Process Date</label>
		<s:textfield name="date" value="%{date}" size="10" maxlength="10" cssClass="date" />
	    </div>
	    <div class="button-group">
		<s:submit name="action" accrual="button" value="Run Now" class="button"/>
		
	    </div>
	</div>
    </s:form>
    <s:if test="hasLogs()">
	<s:set var="excess_logs" value="logs" />
	<%@ include file="excess_accrual_email_logs.jsp" %>	
    </s:if>
<%@ include file="footer.jsp" %>
