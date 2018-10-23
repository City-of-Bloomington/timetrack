<%@ include file="headerMin.jsp" %>

<div>
	
	<h1>Time Clock <small id="meta"></small></h1>
	<h3>Welcome <s:property value="%{timeClock.employee}" />
	<h2>Please Pick a Job</h2>
	<s:if test="hasMessages()">
		<s:set var="messages" value="messages" />
		<%@ include file="messages.jsp" %>
	</s:if>
	<div>
		<s:form action="timeClock" id="form_id" method="post" >
			<s:hidden name="timeClock.id_code" value="%{timeClock.id_code}" />
			<s:hidden name="timeClock.time" value="%{timeClock.time}" id="time_clock_id2" />
			<s:iterator var="one" value="%{timeClock.jobs}">			
				<li> <input type="radio" name="timeClock.job_id" value="<s:property value='%{#one.id}' />" <s:if test="%{#one.isPrimary()}"> checked="checked" </s:if> /><s:property value="%{#one.name}" /> </li>
			</s:iterator>
			<s:submit name="action" type="submit" value="Process" />
		</s:form>
	</div>
</div>

<%@ include file="footer.jsp" %>

