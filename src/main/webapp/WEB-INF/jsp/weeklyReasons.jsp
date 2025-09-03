<div class="width-one-half">

    <table class="monetary-hours-summary-total ${whichWeek}">
	<caption style="text-align:left;font-weight:bold">
	    <s:property value="#weeklyTitle" />
	</caption>	    
	<tr>
	    <td>Earn Code & Reason</td>
	    <td>Hours</th>
	    <td>Amount</th>
	</tr>
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
    </table>
</div>
