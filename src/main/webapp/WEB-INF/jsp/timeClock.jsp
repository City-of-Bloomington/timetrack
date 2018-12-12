<%@ include file="headerMin.jsp" %>

<div class="time-clock">

	<h1>Time Clock <small id="meta"></small></h1>
	<h2>
		Swipe your ID Card
		<s:if test="%{hasErrors() && timeClock.timeBlock.document.employee}">
			<small>Hi, <s:property value="timeClock.timeBlock.document.employee" /></small>
		</s:if>
	</h2>

	<%@ include file="strutMessages.jsp" %>

  <s:if test="!isAccepted()">
		<h2 class="alert-full">ID card's will not be accepted from this IP address.<br /><br />
		Contact ITS for help.<br /><br />(812) 349-3454</h2>
	</s:if>

	<s:if test="isAccepted()">

		<div class="clock-wrapper">
			<div class="actions">
				<s:if test="!hasErrors()">
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
				</s:if>

				<s:else>
					<div class="error-icon">
						<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512">
						  <path fill="rgba(255,255,255,0.5)" d="M569.517 440.013C587.975 472.007 564.806 512 527.94 512H48.054c-36.937 0-59.999-40.054-41.577-71.987L246.423 23.985c18.467-32.009 64.72-31.952 83.154 0l239.94 416.028zm-27.658 15.991l-240-416c-6.16-10.678-21.583-10.634-27.718 0l-240 416C27.983 466.678 35.731 480 48 480h480c12.323 0 19.99-13.369 13.859-23.996zM288 372c-15.464 0-28 12.536-28 28s12.536 28 28 28 28-12.536 28-28-12.536-28-28-28zm-11.49-212h22.979c6.823 0 12.274 5.682 11.99 12.5l-7 168c-.268 6.428-5.556 11.5-11.99 11.5h-8.979c-6.433 0-11.722-5.073-11.99-11.5l-7-168c-.283-6.818 5.167-12.5 11.99-12.5zM288 372c-15.464 0-28 12.536-28 28s12.536 28 28 28 28-12.536 28-28-12.536-28-28-28z" class="">
						  </path>
						</svg>
					</div>
				</s:else>
			</div>

			<s:if test="action == '' || hasErrors()">
				<div id="current-time">
					<span class="time"></span>
					<span class="a"></span>
				</div>
			</s:if>

			<s:else>
				<s:if test="!hasErrors()">
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
							<small>(<s:property value="timeClock.timeBlock.timeInfoNextLine" /> )</small><br>
							</s:if>
						</h3>
					</div>
				</s:if>
			</s:else>
		</div>

		<s:form action="timeClock" id="form_id" class="time-clock-form" method="post" >
			<s:hidden name="action2" id="action2" value="" />
			<s:if test="action == ''">
				<s:hidden name="timeClock.time" value="%{timeClock.time}" id="time_clock_id2" />
				<s:textfield name="timeClock.id_code" size="10" maxlength="10" requiredLabel="true" required="true" id="emp_id_code" autofocus="autofocus" placeholder="Employee ID" />
				<s:submit name="action" type="button" value="Submit" cssClass="button_link" />
			</s:if>

			<s:else>

				<script type="text/javascript">
		    	setTimeout(function(){
		    		window.top.location = "<s:property value='#application.url' />timeClock.action"
		    	}, 3000); // wait 3 seconds
		    </script>

		    <s:hidden name="timeClock.time" value="%{timeClock.time}" id="time_clock_id2" />
				<s:textfield name="timeClock.id_code" size="10" maxlength="10" requiredLabel="true" required="true" id="emp_id_code" autofocus="autofocus" placeholder="Employee ID" />
				<s:submit name="action" type="button" value="Submit" cssClass="button_link" />
		  </s:else>
		</s:form>
	</s:if>

	<p class="testing"></p>
	<p class="ip">IP Address: <s:property value="ipaddr" /></p>


</div>

<%@ include file="footer.jsp" %>
<script type="text/javascript">
	const timeClockForm 		= document.getElementById("form_id");

	var inputElement = document.getElementById("emp_id_code");

	if(inputElement != null || inputElement != undefined) {
		inputElement.value = "";
		inputElement.focus();
		inputElement.addEventListener("blur", function(event){
			if(inputElement.value.length == 0)
				inputElement.focus();
		});
	}

	timeClockForm.addEventListener("submit", function(e) {
		// console.log("clicked!!");
		// return false;

		e.preventDefault();

		const request = new XMLHttpRequest();
    const url = APPLICATION_URL + "timeClock.action";

    request.open("POST", url, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    request.onreadystatechange = function () {
      if (request.readyState === 4 && request.status === 200) {
      	// document.body.innerHTML = request.response;
      	timeClockIdCodeElem.value = "";
      	document.querySelector('main').innerHTML = request.response;


      	var processButton = document.querySelectorAll('#form_id_action')[0];
				var radioElms 	  = document.querySelectorAll('input[type=radio]');
				var radioCount    = radioElms.length;


      	function showNextButton() {
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

				if(radioCount >= 2) {
					// alert("TimeClock has radios");
					setTimeout(function(){
						window.top.location = APPLICATION_URL + "timeClock.action"
					}, 10000); // wait 10 seconds
					showNextButton();
				} else {
					setTimeout(function(){
						// alert('run the refresh');
						window.top.location = APPLICATION_URL + "timeClock.action"
					}, 3000); // wait 3 seconds
				}


      	var inputElement = document.getElementById("emp_id_code");
				if(inputElement != null || inputElement != undefined) {
					inputElement.value = "";
					inputElement.focus();
					inputElement.addEventListener("blur", function(event){
						if(inputElement.value.length == 0)
							inputElement.focus();
					});
				}

        // console.log(request.response);
      }
    };

    const timeClockFormData = new FormData();

    var action   				    = document.getElementById("form_id_action").value;
    // const action2 			    = document.getElementById("action2").value;
    const timeClockTime     = document.getElementById("time_clock_id2").value;
    let timeClockIdCodeVal   = document.getElementById("emp_id_code").value;
    let timeClockIdCodeElem   = document.getElementById("emp_id_code");

    timeClockFormData.append('action', action);
    // timeClockFormData.append('action2', action2);
    timeClockFormData.append('timeClock.time', timeClockTime);
    timeClockFormData.append('timeClock.id_code', timeClockIdCodeVal);

		// for (let pair of timeClockFormData.entries()) {
  //   	console.log(pair[0]+ ', ' + pair[1]);
		// }

		const queryString = new URLSearchParams(timeClockFormData).toString()

		// console.log(queryString);

		request.send(queryString);


	});

	// console.log(timeClockForm);

	function timeUpdate() {
		var btownTime = moment().tz("America/Indiana/Indianapolis");
		var now = moment();
		var exp = moment(btownTime);
		var bigTime = document.getElementsByClassName("time")[0];
		var bigTimeAmPm = document.getElementsByClassName("a")[0];
		var beforeNoon = btownTime.clone().hour(12).minute(0).second(0);
		var isBeforeNoon = moment(btownTime).isBefore(beforeNoon);

		if(bigTime != null || bigTime != undefined) {
			bigTime.innerHTML = exp.format('h:mm');
			bigTimeAmPm.innerHTML = exp.format('a').toUpperCase();
			isBeforeNoon ? bigTimeAmPm.classList.add("am") : bigTimeAmPm.classList.add("pm");
		}
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
