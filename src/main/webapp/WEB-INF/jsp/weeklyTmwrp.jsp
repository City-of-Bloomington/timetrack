<div class="width-one-half">
	<h1><s:property value="#weeklyTitle" /></h1>
	<table class="code-hours-summary-total ${whichWeek}">
		<tr>
			<th>Earn Code</th>
			<th>Hours</th>
			<th>Amount</th>
		</tr>
		<s:iterator value="#tmrpRows" var="one" >
			<s:set var="key" value="#one.key" />
			<s:set var="vals" value="#one.value" />
			<tr>
				<td><s:property value="#key" /></td>			
				<s:iterator value="#vals">
					<td><s:property/></td>
				</s:iterator>						
			</tr>
		</s:iterator>
	</table>
</div>
