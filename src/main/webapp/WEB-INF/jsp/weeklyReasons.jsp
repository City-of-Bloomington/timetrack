<div class="width-one-half">
	<h2><s:property value="#weeklyTitle" /></h2>
	<table class="monetary-hours-summary-total ${whichWeek}">
	    <thead>
		<tr>
		    <th>Earn Code & Reason</th>
		    <th>Hours</th>
		    <th>Amount</th>
		</tr>
	    </thead>
	    <tbody>
		<s:iterator value="#rows" var="one">
		    <s:set var="key" value="#one.key" />
		    <s:set var="list" value="#one.value" />
		    <tr data-job-id="<s:property value='job.id' />">
			<td><s:property value="#key" /></td>
			<s:iterator value="#list" status="row">
			    <td><s:property /></td>
			</s:iterator>
		    </tr>		    
		</s:iterator>
	    </tbody>
	</table>
</div>
