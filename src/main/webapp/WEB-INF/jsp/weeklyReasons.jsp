<div class="width-one-half">
	<h2><s:property value="#weeklyTitle" /></h2>
	<table class="monetary-hours-summary-total ${whichWeek}">
		<thead>
			<tr>
				<th>Earn Code & Reason</th>
				<th>Hours</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="#rows" var="one">
				<s:set var="key" value="#one.key" />
				<s:set var="keyVal" value="#one.value" />
				<tr data-job-id="<s:property value='job.id' />">
					<td><s:property value="#key" /></td>
					<td><s:property value="#keyVal" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>
