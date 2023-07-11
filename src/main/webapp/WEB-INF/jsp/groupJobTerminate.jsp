<%@ include file="header.jsp" %>
<div class="internal-page">
    
    <s:form action="groupJobTerminate" id="form_id" method="post">
	<s:hidden name="group_id" value="%{group_id}" />
	<h1>Group Jobs Termination </h1>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<p>This process will terminate all active jobs for this group</p>
	<s:if test="action == ''">			
	    <ul>
		<li>Enter the expire date from the list that is within last day of work.
		</li>
	    </ul>
	</s:if>
	<div class="width-one-half">
	    <div class="form-group">
		<label>Group</label>
		<a href="<s:property value='#application.url' />group.action?id=<s:property value='group_id' />"> <s:property value="%{group}" /></a>
	    </div>
	    <div class="form-group">
		<label>Expire Date</label>
		<div class="date-range-picker">
		    <div>
			<s:select name="expire_date" value="%{expire_date}" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick Expire Date" /> (End of Pay Period Date)	
		    </div>
		</div>
	    </div>
	    <s:if test="action == ''">
		<s:submit name="action" type="button" value="Submit" class="button"/>
	    </s:if>
	</div>
    </s:form>
    <s:if test="action != null">
	<s:if test="hasGroupId()">
	    <s:if test="term.hasJobs()">
		<s:set var="jobTasks" value="%{term.jobs}" />
		<s:set var="jobTasksTitle" value="'Group Jobs'" />
		<%@ include file="jobTasks.jsp" %>
	    </s:if>
	</s:if>
    </s:if>
</div>
<%@ include file="footer.jsp" %>
