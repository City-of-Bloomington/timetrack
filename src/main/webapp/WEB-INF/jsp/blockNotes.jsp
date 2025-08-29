<s:if test="document.hasTimeBlockWithNotes()">
    <s:set var="timeBlockNotesTitle" value="'Time Block Notes'" />
    <s:set var="blockNotes" value="document.timeBlockWithNotes" />
    <table class="pay-period-notes width-full">
	<caption style="text-align:left;font-weight:bold">
	    <s:property value="#timeBlockNotesTitle" />
	</caption>
	<tr>
	    <th>Date</th>
	    <th>&nbsp;</th>
	    <th>Hour Code</th>	    
	    <th>Start Time</th>
	    <th>End Time</th>	    
	    <th>Hours</th>
	    <th>Amount ($)</th>
	    <th>Notes</th>
	</tr>
	<s:iterator var="one" value="#blockNotes">
	    <tr>
		<td><s:property value="date" /></td>
		<td>&nbsp;</td>
		<td><s:property value="hourCode" /></td>
		<td><s:if test="showBeginTime()"><s:property value="beginHourMinute" /></s:if>&nbsp;</td>
		<td><s:if test="showEndTime()"><s:property value="endHourMinute" /></s:if>&nbsp;</td>
		<td><s:if test="!isClockInOnly()"><s:property value="hours" /></s:if>&nbsp;</td>
		<td><s:property value="amountStr" /></td>
		<td><s:property value="notes" /></td>
	    </tr>
	</s:iterator>
    </table>
</s:if>
