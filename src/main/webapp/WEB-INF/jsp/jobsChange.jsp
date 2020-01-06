<%@ include file="header.jsp" %>
<div class="internal-page">

<h1>Employee jobs</h1>
<s:if test="hasMessages()">
	<s:set var="messages" value="%{messages}" />			
	<%@ include file="messages.jsp" %>
</s:if>
<s:elseif test="hasErrors()">
	<s:set var="errors" value="%{errors}" />			
	<%@ include file="errors.jsp" %>
</s:elseif>
`	<div class="width-one-half">
		<label>Employee: </label>
		<a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='%{relatedEmployee.id}' />"> <s:property value="%{relatedEmployee}" /></a>
</div>
<s:if test="hasJobs()">
	<s:set var="jobTasks" value="jobs" />
	<s:set var="jobTasksTitle" value="'All Employee Jobs'" />
	<%@ include file="jobTasks.jsp" %>	
</s:if>
<%@ include file="footer.jsp" %>
