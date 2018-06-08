<div class="width-one-half">
	<h1><s:property value="#weeklyTitle" /></h1>
	<table class="code-hours-summary-total ${whichWeek}">
		<tr>
			<th>Hour Code</th>
			<th>Total</th>
		</tr>
		<s:iterator value="#weeklyHourCodes" var="one" >
			<s:set var="hourCodeKey" value="#one.key" />
			<s:set var="hourCodeVal" value="#one.value" />
			<tr>
				<td><s:property value="#hourCodeKey" /></td>
				<td><s:property value="#hourCodeVal" /></td>
			</tr>
		</s:iterator>
		<tr class="totals-row">
			<td><strong>Week Total</strong></td>
			<td><strong><s:property value="#weekTotal" /></strong></td>
		</tr>
	</table>
</div>