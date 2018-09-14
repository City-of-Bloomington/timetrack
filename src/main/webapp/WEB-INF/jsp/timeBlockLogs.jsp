<%@  include file="header.jsp" %>

<h1>Time Block History</h1>
<s:if test="hasTimeBlockLogs()">
	<table class="width-full">
		<tr>
			<th>Related Time Block</th>			
			<th>Hour Code</th>
			<th>Date</th>
			<th>Start Time</th>
			<th>End Time</th>
			<th>Hours</th>
			<th>Action Type</th>
			<th>Action By</th>						
			<th>Action Date-Time</th>
		</tr>
		<s:iterator var="one" value="timeBlockLogs">
		<tr>
			<td><s:property value="time_block_id" /></td>
			<td><s:property value="hourCode" /></td>
			<td><s:property value="date" /></td>
			<td><s:if test="showBeginTime()"><s:property value="beginHourMinute" /></s:if>&nbsp;</td>
			<td><s:if test="showEndTime()"><s:property value="endHourMinute" /></s:if>&nbsp;</td>
			<td><s:if test="!isClockInOut()"><s:property value="hours" /></s:if>&nbsp;</td>
			<td><s:property value="action_type" /></td>
			<td><s:property value="action_by" /></td>
			<td><s:property value="action_time" /></td>
		</tr>
		</s:iterator>
	</table>
</s:if>
<s:else>
	<p>No data entry found </p>
</s:else>
<%@ include file="footer.jsp" %>
