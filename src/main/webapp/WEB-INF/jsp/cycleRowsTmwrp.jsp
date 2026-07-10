<div class="width-one-half">

    <table class="monetary-hours-summary-total">
	<caption style="text-align:left;font-weight:bold">
	    Pay Period Totals
	</caption>
	<thead>
	    <tr>
		<th>Earn Code</th>
		<th>Hours</th>
		<th>Amount($)</th>
	    </tr>
	</thead>
	<tbody>
	    <s:iterator value="#rows" var="row">
		<tr>
		    <td><s:property value="#row[0]" /> </td>
		    <td><s:property value="#row[1]" /> </td>
		    <td><s:property value="#row[2]" /> </td>
		</tr>
	    </s:iterator>		
	</tbody>
    </table>
</div>
