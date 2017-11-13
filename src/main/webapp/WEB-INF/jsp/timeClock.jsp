<%@  include file="headerMin.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="timeClock" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="action == ''">
		<h4>Swipe your ID Card</h4>
	</s:if>
	<s:else>
		<h4>Time Clock</h4>
	</s:else>
  <s:if test="hasActionErrors()">
		<div class="errors">
      <s:actionerror/>
		</div>
  </s:if>
  <s:elseif test="hasActionMessages()">
		<div class="welcome">
      <s:actionmessage/>
		</div>
  </s:elseif>
	<div class="tt-row-container">	
	<s:if test="action == ''">
		<dl class="fn1-output-field">
			<dt>Employee ID Code </dt>
			<dd><s:textfield name="timeClock.id_code" value="" size="10" maxlength="10" requiredLabel="true" required="true" id="emp_id_code" autofocus="autofocus" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Time </dt>
			<dd><s:textfield name="timeClock.time" value="%{timeClock.time}" size="10" maxlength="10" requiredLabel="true" required="true" id="time_clock_id" />* </dd>
		</dl>		
		<s:submit name="action" type="button" value="Submit" class="fn1-btn"/></dd>
	</s:if>
	<s:else>
		<script type="text/javascript">
    setTimeout(function(){window.top.location="<s:property value='#application.url' />timeClock.action"} , 5000); // wait 5 seconds
    </script>
		<dl class="fn1-output-field">
			<dt>Welcome </dt>
			<dd><s:property value="timeClock.timeBlock.document.employee" /> </dd>
		</dl>
		<s:if test="timeClock.timeBlock.isClockOut()">
			<dl class="fn1-output-field">
				<dt>Time-In,Time-Out, hours </dt>
				<dd><s:property value="timeClock.timeBlock.timeInfo" /> </dd>
			</dl>
		</s:if>
		<s:else>
			<dl class="fn1-output-field">
				<dt>Clock-In </dt>
				<dd><s:property value="timeClock.timeBlock.time_in" /> </dd>
			</dl>
		</s:else>
		<s:if test="hasTimeBlocks()">
				<dl class="fn1-output-field">
					<dt>In/Out Hours </dt>
					<dd>
					<s:iterator var="one" value="timeBlocks" status="row">			
						<s:property value="timeInfo" />
						<s:if test="!#row.last">, </s:if>
					</s:iterator>
					</dd>
				</dl>
				<dl class="fn1-output-field">
					<dt>Day Total </dt>
					<dd><s:property value="totalHours" /> (hrs) </dd>
				</dl>				
		</s:if>
		Note: if you want to report and issue related to your click-in or out such as missing punch click  <a href="<s:property value='#application.url' />timeIssue.action?time_block_id=<s:property value='timeClock.timeBlock.id' />&reported_by=<s:property value='timeClock.timeBlock.document.employee_id' />" class="fn1-btn">Here </a> to report <br />		
		<a href="<s:property value='#application.url' />timeClock.action" class="fn1-btn">Done </a>
	</s:else>
	</div>
</s:form>
<%@  include file="footer.jsp" %>
<s:if test="action == ''">
	<script type="text/javascript">
	window.onload = setCurTime('time_clock_id');
	</script>		
</s:if>

