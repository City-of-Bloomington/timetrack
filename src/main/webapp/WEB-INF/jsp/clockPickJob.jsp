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
<script type="text/javascript">
	// wait 15 seconds for select & submit action ...
	// if no action, refresh
	setTimeout(function(){
  	window.top.location = "/timetrack/timeClock.action"
  }, 15000);

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

	function showNextButton() {
		var processButton = document.querySelectorAll('#form_id_action')[0];
		var radioElms 	  = document.querySelectorAll('input[type=radio]');
		var radioCount    = radioElms.length;

		function sendData() {
	    var XHR = new XMLHttpRequest();
	    var FD  = new FormData();

	    XHR.addEventListener("error", function(event) {
	      alert('Oops! Something went wrong, please try again.');
	    });

	    XHR.open("POST", "/timetrack/timeClock.action");
	    XHR.send(FD);
	  }

		for (var i = 0; i < radioCount; i++) {
  		radioElms[i].onclick = function(){
  			processButton.classList.add("active");
  		};
		}

		processButton.onclick = function(e) {
			var selectedJob = document.querySelectorAll('input[type=radio]:checked')[0];

			if(!selectedJob){
				e.preventDefault();
				alert('Please select a job and try again.')
			} else {
				sendData();
			}
		}
	}
	showNextButton();
</script>