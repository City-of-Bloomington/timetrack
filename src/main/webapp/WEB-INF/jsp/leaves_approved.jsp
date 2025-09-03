<table class="width-full">
    <caption style="text-align:left;font-weight:bold">
	<s:property value="#leaves_title" />
    </caption>
    <tr>
	<td>&nbsp;</td>
	<td>Request Date</td>
	<td>Date Range</td>
	<td>Hour Code(s)</td>	    
	<td>Total Hours</td>
	<td>Review Status</td>
	<td>Reviewer</td>
    </tr>
    <s:iterator var="one" value="#leave_requests">
	<tr>
	    <td>&nbsp;</td>
	    <td><s:property value="requestDate" /></td>
	    <td><s:property value="date_range" /></td>
	    <td><s:property value="earnCodes" /></td>
	    <td><s:property value="totalHours" /></td>
	    <td style="background-color:#CCFFCD">
		<s:property value="reviewStatus" />
	    </td>
	    <td><s:property value="reviewer" /></td>
	</tr>
    </s:iterator>
</table>
