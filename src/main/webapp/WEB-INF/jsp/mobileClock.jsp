<%@ include file="headerMin.jsp" %>
<style>  
li  
{  
	color:white;
	word-wrap: break-word;
	font-size: 3.0em;
}
button
{
	background-color:#4CAF50;
	color: white;
	border: none;
	font-size: 3.0em;
	padding: 15px 32px;
	text-align: center;
}
a:link {
  color: yellow;
}

/* visited link */
a:visited {
  color: #4CAF50;
}

/* mouse over link */
a:hover {
  color: red;
}

</style>  
<br /><br />
<h1 style="font-size:3em">TimeTrack Mobile Login </h1>
<br />
<h1 style="font-size:2em" id="meta">&nbsp;</h1>		
<div class="time-clock">
	<s:if test="hasMessages()">
		<s:set var="messages" value="%{messages}" />		
		<%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
		<h2 class="alert-full"> You may contact ITS for help.(812) 349-3454</h2>
	</s:if>
	<ul>
		<li>TimeTrack Mobile requires you to be at your work location to clock in and out so you must allow location sharing. </li>
		<li>On iPhone you will need to enable Location Services for the browsers you use for TimeTrack. </li>
		<ul>
			<li>Go to Settings > Privacy > Location Services to turn Location Services on.</li>
			<li>Enable Location Services for the browser then scroll down and tap the browser name (Chrome, Edge, Firefox, Safari, etc.) and set the browser to either 'Ask Next Time Or When I Share' or 'While Using the App'.</li>
		</ul>
		<li>On Android (may be different depending on the device and Android version) </li>
		<ul>
			<li>Go to Settings > apps > web browser (Samsung Internet, Chrome, FireFox, etc) > Permissions > Location </li>
			<li>Scroll down and pick 'Allow only when using the app' or 'Ask every time'</li>
		</ul>
		<li>Click on 'Next' to continue</li>
		<li>
			If you encounter any problems, contact your manager and report the problem to ITS support at (812) 349-3454 or 
			<a href="https://help.bloomington.in.gov"> online HelpDesk</a></li>
	</ul>
	<!-- 
			<ul>
				<li>The location must be enabled for this browser in the device you are using. You may be asked to 'Turn on location' you should answer "Yes'</li>
				<li>Next, click on the 'Next' button when you are close enough to the establishment </li>
				<li>It may take some time before your location is found, normally less than one minute </li>
				<li>The geo-location is accepted if it is within reasonable distance from the establishment you will be working at</li>
				<li>If you receive a message saying that you are not close enough, move closer to the establishment and try again</li>
				<li>If your location is accepted you will be directed to the 'Login' screen where you use the City of Bloomington username and password</li>
				<li>A successful login will take you to the welcome screen with 'Clock-in' time or 'Clock-out' time</li>
				<li>If you are using an Iphone and you did not get any response, you have to make location services enabled. Go to your settings->Privacy->Safari->Location Services->Safari Websites: choose 'While Using the App' on the list of available options</li>  
				<li>If you encounter any problem, talk to your manager and report the problem using ITS helpdesk at (812) 349-3454 or online at https://help.bloomington.in.gov</li>
			</ul>
			-->
			<form name="locationForm" id="locationForm" type="post" action="./mobile.action" onsubmit="verifyLocation();return false;">
				<input type="hidden" name="user_lat" value="" id="user_lat" />
				<input type="hidden" name="user_long" value="" id="user_long" />
				<input type="hidden" name="action" value="" id="action_id" />
				<p id="lat_id">&nbsp;</p>
				<p id="lng_id">&nbsp;</p>
				<button id = "mylocation">Next</button>
				<br />
			</form>
		</div>
	</body>
</html>
<%@ include file="footer.jsp" %>
</div>
<script type="text/javascript" src="<s:property value='#application.url' />js/mobile-clock.js"></script>
				
<script type="text/javascript">
<s:if test="hasErrors()">
   window.onload = function(){
		 <s:iterator var="one" value="errors">
     alert("<s:property />");
		 </s:iterator>
	 }
</s:if>
function verifyLocation(){
		
}
function geoFindLocation() {
		const lat_id = document.querySelector('#lat_id');
		const lng_id = document.querySelector('#lng_id');
		const user_lat = document.querySelector('#user_lat');
		const user_long = document.querySelector('#user_long');
		const form_id = document.querySelector('#locationForm');
		const action_id = document.querySelector('#action_id');
		if(!navigator.geolocation){
				alert("Geo Location is not supported or turned off on your device, please turn the location feature on to be able to use this app");
		}
		else{
				// const watchID = navigator.geolocation.watchPosition((position, success, error, options) => {
				// call the geo location js here
				const watchID = navigator.geolocation.getCurrentPosition((position, success, error, options) => {
					checkLocation(position.coords.latitude, position.coords.longitude);
						
				});
			}
		function success(position) {
				updateLocation(position.coords.latitude, position.coords.longitude);
				//
				// after success turn off watch
				//
				// navigator.geolocation.clearWatch(watchID);	
		};
		
		function error() {
				alert('Sorry, no location data available.');
		};
			
		const options = {
				enableHighAccuracy: true,
				maximumAge: 5,
				timeout: 10
		};
		function checkLocation(lat, lng) {
			//alert('your location is at '+lat+","+lng);
			lat_id.textContent = lat;
			lng_id.textContent = lng;
			user_lat.value = lat;
			user_long.value = lng;
			action_id.value = "checkLocation";
			// navigator.geolocation.clearWatch(watchID);
			if(lat > 0){
						// location.replace("http://localhost/geoLocation/next.html");
				form_id.submit();
			}
				// document.querySelector('#myform').submit();
		};
		function updateLocation(lat, lng) {
				checkLocation(lat, lng);
				lat_id.textContent = lat;
				lng_id.textContent = lng;
				// navigator.geolocation.clearWatch(watchID);
				// location.replace("http://localhost/geoLocation/next.html");
		};		
}
document.querySelector('#mylocation').addEventListener('click', geoFindLocation);
setTimeout(() => {
  window.top.location = "mobile.action";
}, 20000);
</script>



