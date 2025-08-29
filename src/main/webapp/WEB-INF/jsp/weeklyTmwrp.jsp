<div class="width-one-half">

	<table class="monetary-hours-summary-total ${whichWeek}">
	    <caption style="text-align:left;font-weight:bold">
		<s:property value="#weeklyTitle" />
	    </caption>
	    <thead>
		<tr>
		    <th>Earn Code</th>
		    <th>&nbsp;</th>		    
		    <th>Hours</th>
		    <th>Amount</th>
		</tr>
	    </thead>
	    <tbody>
		<s:iterator value="#rows" var="one">
		    <tr data-job-id="<s:property value='job.id' />">
			<s:iterator value="#one" status="col">
			    <s:if test="#col.index == 1">
				<td>&nbsp;</td>
			    </s:if>
			    <td><s:property/></td>
			</s:iterator>
		    </tr>
		</s:iterator>
	    </tbody>
	</table>
</div>
