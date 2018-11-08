<%@ include file="headerMin.jsp" %>
<div class="time-clock job-select">
	<h1>Time Clock <small id="meta"></small></h1>
	<h2>Please Pick a Job</h2>

	<h3>Welcome <s:property value="%{timeClock.employee}" />

	<s:if test="hasMessages()">
		<s:set var="messages" value="messages" />
		<%@ include file="messages.jsp" %>
	</s:if>

	<div>
		<s:form action="timeClock" id="form_id" method="post" >
			<s:hidden name="timeClock.id_code" value="%{timeClock.id_code}" />
			<s:hidden name="timeClock.time" value="%{timeClock.time}" id="time_clock_id2" />

			<div class="job-cards">
				<s:iterator var="one" value="%{timeClock.jobs}">
					<div class="card">
						<input type="radio"
								   name="timeClock.job_id"
								   value="<s:property value='%{#one.id}' />"
								   checked="<s:if test="%{#one.isPrimary()}">checked</s:if>" />

						<label for="%{timeClock.job_id}">
							<s:property value="%{#one.name}" />
						</label>
					</div>
				</s:iterator>
			</div>

			<s:submit name="action" type="submit" value="Process" />
		</s:form>
	</div>
</div>
<%@ include file="footer.jsp" %>
<script type="text/javascript">
	function topTime() {
		var btownTime = moment().tz("America/Indiana/Indianapolis");
		var now = moment();
		var exp = moment(btownTime);
		var headingMetaElm = document.getElementById('meta');
		var btownTime = moment().tz("America/Indiana/Indianapolis");
		headingMetaElm.innerHTML = exp.format('MMMM Do YYYY, h:mm:ss a');
	}
	setInterval(function() { topTime(); }, 10);
	topTime();
</script>