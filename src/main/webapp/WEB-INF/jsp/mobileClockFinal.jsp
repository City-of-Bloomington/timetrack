<%@ include file="headerMin.jsp" %>

<div class="time-clock">

	<h1>Mobile Time Clock<small id="meta"></small></h1>
	<h2>
		<s:if test="!hasErrors()">
			<small>Hi, <s:property value="employee" /></small>
		</s:if>
	</h2>
	<s:if test="isAccepted()">
		<div class="clock-wrapper">
			<div class="actions">
				<s:if test="!hasErrors()">
					<s:if test="mobileClock.timeBlock.isClockOut()">
						<div class="in">In</div>
						<div class="out active">Out</div>
					</s:if>
					<s:elseif test="mobileClock.timeBlock.isClockIn()">
						<div class="in active">In</div>
						<div class="out">Out</div>
					</s:elseif>
					<s:else>
						<div class="in">In</div>
						<div class="out">Out</div>
					</s:else>
				</s:if>
			</div>
			<s:if test="!hasErrors()">
				<div class="log-info">
					<h1>
						<s:if test="mobileClock.timeBlock.isClockOut()">Bye,</s:if>
						<s:elseif test="mobileClock.timeBlock.isClockIn()">Hi,</s:elseif>
						<s:property value="employee" />
					</h1><br/>
					
					<h2>Current Log:</h2>
					<h3>
						- <s:property value="mobileClock.timeBlock.timeInfo" />
						<s:if test="mobileClock.timeBlock.hasNextLine()">
							<small>(<s:property value="mobileClock.timeBlock.timeInfoNextLine" /> )</small><br>
						</s:if>
					</h3>
				</div>
			</s:if>
		</div>
		<script type="text/javascript">
		setTimeout(function(){
		  window.top.location = "<s:property value='#application.url' />mobile.action?action="
		}, 10000);
		</script>
	</s:if>
</div>

<%@ include file="footer.jsp" %>
<script type="text/javascript" src="<s:property value='#application.url' />js/mobile-clock.js"></script>
