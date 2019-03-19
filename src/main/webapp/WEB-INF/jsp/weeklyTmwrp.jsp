<div class="width-one-half">
	<h1><s:property value="#weeklyTitle" /></h1>
	<table class="code-hours-summary-total ${whichWeek}">
		<tr>
			<th>Earn Code</th>
			<th>Hours</th>
			<th>Amount</th>
		</tr>
		<s:iterator value="#rows" var="one" >
			<tr>
				<s:iterator value="#one">
					<td><s:property/></td>
				</s:iterator>						
			</tr>
		</s:iterator>
	</table>
</div>
