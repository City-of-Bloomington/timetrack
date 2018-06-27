<%@ include file="headerMin.jsp" %>

<div class="time-clock">

	<h1>Time Clock <small id="meta"></small></h1>
	<h2>Swipe your ID Card</h2>

	<%@ include file="strutMessages.jsp" %>

  <s:if test="!isAccepted()">
		<h2 class="alert-full">ID card's will not be accepted from this IP address.<br /><br />
		Contact ITS for help.<br /><br />(812) 349-3454</h2>
	</s:if>

	<s:if test="isAccepted()">
		<div class="clock-wrapper">
			<div class="actions">
				<s:if test="timeClock.timeBlock.isClockOut()">
					<div class="in">In</div>
					<div class="out active">Out</div>
				</s:if>

				<s:elseif test="timeClock.timeBlock.isClockIn()">
					<div class="in active">In</div>
					<div class="out">Out</div>
				</s:elseif>

				<s:else>
					<div class="in">In</div>
					<div class="out">Out</div>
				</s:else>
			</div>

			<s:if test="action == ''">
				<script>
					// hack window refresh for updated time value
					setInterval(function() {
					  window.location.reload();
					}, 6000); // 60 seconds || every minute for new minute update
				</script>
				<div id="current-time"></div>
			</s:if>

			<s:else>
				<div class="log-info">
					<h1>Hi, <s:property value="timeClock.timeBlock.document.employee" />!</h1><br/>

					<h2>Current Log:</h2>
					<h3>
						<!-- this displays AM value if logged time is PM -->
						- <s:property value="timeClock.timeBlock.timeInfo" />
						<s:if test="timeClock.timeBlock.hasNextLine()">
							-- <s:property value="timeClock.timeBlock.timeInfoNextLine" /><br>
						</s:if>
					</h3>
				</div>
			</s:else>
		</div>

		<s:form action="timeClock" id="form_id" method="post" >
			<s:hidden name="action2" id="action2" value="" />
			<s:if test="action == ''">
				<s:hidden name="timeClock.time" value="%{timeClock.time}" id="time_clock_id2" />
				<s:textfield name="timeClock.id_code" value="" size="10" maxlength="10" requiredLabel="true" required="true" id="emp_id_code" autofocus="autofocus" placeholder="Employee ID" />
				<s:submit name="action" type="button" value="Submit" cssClass="button_link" />
			</s:if>

			<s:else>
				<script type="text/javascript">
		    	setTimeout(function(){
		    		window.top.location = "<s:property value='#application.url' />timeClock.action"
		    	}, 10000); // wait 10 seconds
		    </script>
		  </s:else>
		</s:form>
	</s:if>

	<p class="testing"></p>
	<p class="ip">IP Address: <s:property value="ipaddr" /></p>

	<s:if test="action == ''">
		<script type="text/javascript">
			var inputElement = document.getElementById("emp_id_code");
			inputElement.focus();
			inputElement.addEventListener("blur", function(event){
				if(inputElement.value.length == 0)
					inputElement.focus();
			});
		</script>
	</s:if>
</div>

<%@ include file="footer.jsp" %>
<script type="text/javascript">

	// (function getServerTime() {
	//   var xhr = $.ajax({
	//     url: APPLICATION_URL + 'timeClock.action',
	//     success: function(data, status) {
	//     	var testingTime = document.querySelector('.testing');
	// 			testingTime.innerHTML = new Date(xhr.getResponseHeader('Date'));
	//     },
	//     complete: function() {
	//       setTimeout(getServerTime, 10000);
	//     }
	//   });
	// })();

	function displayTime() {
		var displayTime = document.getElementById('time_clock_id2').value;

		displayTime = displayTime.split(':');

		var displayHours = Number(displayTime[0]);
		var displayMinutes = Number(displayTime[1]);

		var displayTimeValue;

		if (displayHours > 0 && displayHours <= 12) {
		  displayTimeValue = "" + displayHours;
		} else if (displayHours > 12) {
		  displayTimeValue = "" + (displayHours - 12);
		} else if (displayHours == 0) {
		  displayTimeValue = "12";
		}

		displayTimeValue += (displayMinutes < 10) ? ":0" + displayMinutes : ":" + displayMinutes;
		displayTimeValue += (displayHours >= 12) ? " <span class='pm'>PM</span>" : " <span class='am'>AM</span>";

		var paintTime = document.getElementById("current-time");
		paintTime.innerHTML = displayTimeValue;
	}
	displayTime();

	var headingMetaElm = document.getElementById('meta');
	var dayMoment = moment().format('dddd');
	var dateMoment = moment().format('MMMM Do YYYY');
	var newHeadingMetaElm = dayMoment + "  -  " + dateMoment;
	headingMetaElm.innerHTML = newHeadingMetaElm;
</script>
