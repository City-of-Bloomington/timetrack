<div class="width-one-half">
	<h1><s:property value="#weeklyTitle" /></h1>
	<table class="code-hours-summary-total ${whichWeek}">
		<tr>
			<th>Earn Code</th>
			<th>Hours</th>
			<th>Amount</th>
		</tr>
		<s:iterator value="#weeklyHourCodes" var="one" >
			<s:set var="hourCodeKey" value="#one.key" />
			<s:set var="hourCodeVal" value="#one.value" />
			<tr>
				<td><s:property value="#hourCodeKey" /></td>
				<td><s:property value="#hourCodeVal" /></td>
				<td>&nbsp;</td>
			</tr>
		</s:iterator>
		<s:if test="#weeklyAmountCodes != null">
			<s:iterator value="#weeklyAmountCodes" var="one" >
				<s:set var="hourCodeKey" value="#one.key" />
				<s:set var="hourCodeVal" value="#one.value" />
				<tr>
					<td><s:property value="#hourCodeKey" /></td>
					<td>&nbsp;</td>					
					<td>$<s:property value="#hourCodeVal" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<tr class="totals-row">
			<td><strong>Week Total</strong></td>
			<td><strong><s:property value="#weekHourTotal" /></strong></td>
			<td><s:if test="#weekAmountTotal > 0.0"><strong>$<s:property value="#weekAmountTotal" /></strong></s:if><s:else>&nbsp;</s:else></td>
		</tr>
	</table>
</div>
