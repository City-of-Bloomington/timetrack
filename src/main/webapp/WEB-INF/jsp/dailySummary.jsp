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
		<s:set var="jobKey" value="#one.key" />		
		<s:set var="jobVal" value="#one.value" />
		<s:iterator value="#jobVal" var="day" >		
			<s:set var="dayKey" value="#day.key" />
			<s:set var="dayVal" value="#day.value" />			
			<s:if test="#dayKey == 0">
				<tr><td>Week 1 - <small>(<s:property value="#week1DateRange" />) <s:property value="#jobKey" /></small> </td>
			</s:if>
			<s:if test="#dayKey == 8">
				<s:if test="#unionned">
					<td><s:property value="#week1Flsa" /></td>
				</s:if>
				</tr>
				<tr><td>Week 2 - <small>(<s:property value="#week2DateRange" />) <s:property value="#jobKey" /></small></td>
			</s:if>
			<td><s:property value="dayVal" /></td>
			<s:if test="#dayKey == 15">
				<s:if test="#unionned">
					<td><s:property value="#week2Flsa" /></td>
				</s:if>
				</tr>
			</s:if>
		</s:iterator>
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
