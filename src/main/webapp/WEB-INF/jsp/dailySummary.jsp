<table class="pay-period-summary-total width-full">
	<tr>
		<th>Week</th>
		<th width="10%">Mon</th>
		<th width="10%">Tue</th>
		<th width="10%">Wed</th>
		<th width="10%">Thu</th>
		<th width="10%">Fri</th>
		<th width="10%">Sat</th>
		<th width="10%">Sun</th>

		<s:if test="#unionned">
			<th width="10%">Total</th>
			<th>FLSA Total</th>
		</s:if>

		<s:else>
			<th>Total</th>
		</s:else>
	</tr>

	<s:iterator value="#daily" var="one" >
		<s:set var="dayKey" value="#one.key" />
		<s:set var="dayVal" value="#one.value" />
		<s:if test="#dayKey == 0">
	    <tr><td>Week 1</td>
		</s:if>

		<s:if test="#dayKey == 7">
			<td><s:property value="#week1Total" /></td>
			<s:if test="#unionned">
				<td><s:property value="#week1Flsa" /></td>
			</s:if>
	    </tr>
			<tr><td>Week 2</td>
		</s:if>

		<td><s:property value="dayVal" /></td>
		<s:if test="#dayKey == 13">
			<td><s:property value="#week2Total" /></td>
			<s:if test="#unionned">
				<td><s:property value="#week2Flsa" /></td>
			</s:if>
	    </tr>
		</s:if>
	</s:iterator>

	<tr class="totals-row">
		<td><strong>Pay Period Total</strong></td>
		<td colspan="7">&nbsp;</td>
		<td>
			<strong><s:property value="#payPeriodTotal" /></strong>
		</td>
		<s:if test="#unionned">
			<td>&nbsp;</td>
		</s:if>
	</tr>
</table>