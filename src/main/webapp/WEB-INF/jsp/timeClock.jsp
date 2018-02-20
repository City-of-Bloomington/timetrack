<%@  include file="headerMin.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="timeClock" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
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
	<div class="tbl_wheat">
		<fieldset>
			<legend>Time Clock</legend>
			<s:if test="action == ''">
			<h4>Swipe your ID Card</h4>
			<s:hidden name="timeClock.time" value="%{timeClock.time}" id="time_clock_id2" />		
			<div style="font-size:250%;display:inline-block">Time:<span id="time_clock_id"><s:property value="%{timeClock.time}" /></span></div><br />
			<table widht="75%" border="0">
				<tr>
					<th align="right">Employee ID: </th>
					<td align="left"><s:textfield name="timeClock.id_code" value="" size="10" maxlength="10" requiredLabel="true" required="true" id="emp_id_code" autofocus="autofocus" />* </td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
						<s:submit name="action" type="button" value="Submit" cssClass="button_link" /></dd>
					</td>
				</tr>
			</table>
	</s:if>
	<s:else>
		<h4>Time Clock</h4>		
		<script type="text/javascript">
    setTimeout(function(){window.top.location="<s:property value='#application.url' />timeClock.action"} , 5000); // wait 5 seconds
    </script>
		<table width="75%" border="0">
		<tr>
			<td colspan="2" align="center">Welcome <s:property value="timeClock.timeBlock.document.employee" /> 
			</td>
		</tr>
		<tr><td width="30%">&nbsp;</td><td></td></tr>
		<s:if test="timeClock.timeBlock.isClockOut()">
			<tr>
				<th align="right"">Time-In,Time-Out, hours </th>
				<td align="left"><s:property value="timeClock.timeBlock.timeInfo" />
					<s:if test="timeClock.timeBlock.hasNextLine()">
						&nbsp;&nbsp;<s:property value="timeClock.timeBlock.timeInfoNextLine" />
					</s:if>
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<th align="right">Clock-In </th>
				<td align="left"><s:property value="timeClock.timeBlock.time_in" /> </td>
			</tr>
		</s:else>
		<s:if test="hasTimeBlocks()">
				<tr>
					<th align="right" valign="top">In/Out Hours </th>
					<td>
					<s:iterator var="one" value="timeBlocks" status="row">			
						<s:property value="timeInfo" />
						<s:if test="!#row.last">, </s:if>
					</s:iterator>
					</td>
				</tr>
				<tr>
					<th align="right"> Day Total </th>
					<td align="left"><s:property value="totalHours" /> (hrs) </td>
				</tr>				
		</s:if>
		<tr><td colspan="2" align="left">
			Note: if you want to report and issue related to your click-in or out such as missing punch click  <a href="<s:property value='#application.url' />timeIssue.action?time_block_id=<s:property value='timeClock.timeBlock.id' />&reported_by=<s:property value='timeClock.timeBlock.document.employee_id' />" cssClass="button_link">Here </a> to report <br /></td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<a href="<s:property value='#application.url' />timeClock.action" cssClass="button_link">Done </a></td>
		</tr>
		</table>
	</s:else>
		</fieldset>
	</div>
</s:form>
<p>IP Address: <s:property value="ipaddr" /><br />
	<s:if test="!isAccepted()">
		Note: punch card will not be accepted from this IP address.<br />
		Contact ITS for help (812) 349-3454.
	</s:if>
</p>
<s:if test="action == ''">
	<script type="text/javascript">
	window.onload = setCurTime('time_clock_id');
	var inputElement = document.getElementById("emp_id_code");
	inputElement.focus();
	inputElement.addEventListener("blur", function(event){
		if(inputElement.value.length == 0)
			inputElement.focus();
	}); 
	</script>		
</s:if>
</main>
</body>
</html>

