<div class="width-one-half">
	<h2><s:property value="#weeklyTitle" /></h2>
	<table class="monetary-hours-summary-total ${whichWeek}">
		<thead>
			<tr>
				<th>Earn Code</th>
				<th>Hours</th>
				<th>Amount</th>
			</tr>
		</thead>

		<tbody>
			<s:iterator value="#rows" var="one">
				<tr data-job-id="<s:property value='job.id' />">
					<s:iterator value="#one">
						<td><s:property/></td>
					</s:iterator>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>
