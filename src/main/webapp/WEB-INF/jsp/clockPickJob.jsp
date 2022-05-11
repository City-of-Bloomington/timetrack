<%@ include file="headerMin.jsp" %>
<div class="time-clock job-select">
	<h1>Time Clock <small id="meta"></small></h1>
	<h2>Please Pick a Job <small>Hi, <s:property value="%{timeClock.employee}" /></small></h2>
	<s:if test="hasErrors()">
		<s:set var="errors" value="%{errors}" />		
		<%@ include file="errors.jsp" %>
	</s:if>
	<s:if test="hasMessages()">
		<s:set var="messages" value="%{messages}" />
		<%@ include file="messages.jsp" %>
	</s:if>

	<div>
		<s:form action="timeClock" id="form_id" class="clock-pick-job-form" method="post" >
			<s:hidden name="timeClock.id_code" value="%{timeClock.id_code}" />
			<s:hidden name="timeClock.time" value="%{timeClock.time}" id="time_clock_id2" />

			<div class="job-cards">
				<s:iterator var="one" value="%{timeClock.jobs}">
					<div class="card">
						<input type="radio"
									 class="job-radios"
									 name="timeClock.job_id"
								   id="<s:property value='%{#one.name}' />"
								   value="<s:property value='%{#one.id}' />" />

						<label for="<s:property value='%{#one.name}' />">
							<s:property value="%{#one.name}" />
						</label>
					</div>
				</s:iterator>
			</div>

			<s:submit name="action" type="submit" value="Clock In" />
		</s:form>
	</div>
</div>
<%@ include file="footer.jsp" %>
<script type="text/javascript" src="<s:property value='#application.url' />js/time-clock.js"></script>
