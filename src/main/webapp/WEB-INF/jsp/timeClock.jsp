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
				<div id="current-time">
					<span class="time"></span>
					<span class="a"></span>
				</div>
			</s:if>

			<s:else>
				<div class="log-info">
					<h1>
						<s:if test="timeClock.timeBlock.isClockOut()">Bye,</s:if>
						<s:elseif test="timeClock.timeBlock.isClockIn()">Hi,</s:elseif>

						<s:property value="timeClock.timeBlock.document.employee" />
					</h1><br/>

					<h2>Current Log:</h2>
					<h3>
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
		    	}, 6000); // wait 6 seconds
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
	function timeUpdate() {
		var btownTime = moment().tz("America/Indiana/Indianapolis");
		var now = moment();
		var exp = moment(btownTime);
		var bigTime = document.getElementsByClassName("time")[0];
		var bigTimeAmPm = document.getElementsByClassName("a")[0];
		var beforeNoon = btownTime.clone().hour(12).minute(0).second(0);
		var isBeforeNoon = moment(btownTime).isBefore(beforeNoon);

		bigTime.innerHTML = exp.format('h:mm');
		bigTimeAmPm.innerHTML = exp.format('a').toUpperCase();
		isBeforeNoon ? bigTimeAmPm.classList.add("am") : bigTimeAmPm.classList.add("pm");
	}
	setInterval(function() { timeUpdate(); }, 10);
	timeUpdate();

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
