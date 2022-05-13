<%@  include file="header.jsp" %>
<div class="internal-page">
	<h1>Time Entry History</h1>
	<s:if test="hasTimeBlockLogs()">
		<table class="width-full">
			<tr>
				<th>Related Time Block</th>
				<th>Hour Code</th>
				<th>Earn Reason</th>
				<th>Date</th>
				<th>Start Time</th>
				<th>End Time</th>
				<th>Hours</th>
				<th>Amount ($)</th>
				<th>Action Type</th>
				<th>Action By</th>
				<th>Action Date-Time</th>
				<th>Location</th>
			</tr>
			<s:iterator var="one" value="timeBlockLogs">
			<tr>
				<td><s:property value="time_block_id" /></td>
				<td><s:property value="hourCode" /></td>
				<td><s:property value="earnCodeReason" /></td>
				<td><s:property value="date" /></td>
				<td><s:if test="showBeginTime()"><s:property value="beginHourMinute" /></s:if>&nbsp;</td>
				<td><s:if test="showEndTime()"><s:property value="endHourMinute" /></s:if>&nbsp;</td>
				<td><s:if test="!isClockInOnly()"><s:property value="hours" /></s:if>&nbsp;</td>
				<td><s:property value="amountStr" /></td>				
				<td><s:property value="action_type" /></td>
				<td><s:property value="action_by" /></td>
				<td><s:property value="action_time" /></td>
				<td><s:if test="hasLocation()"><s:property value="location" /></s:if>&nbsp;</td>
			</tr>
			</s:iterator>
		</table>
	</s:if>
	<s:else>
		<p>No data entry found </p>
	</s:else>
</div>
<%@ include file="footer.jsp" %>
