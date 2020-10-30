
<h1>Action History</h1>
	<table class="width-full action-history">
		<tr>
			<th width="200">By</th>
			<th width="200">Date/time</th>
			<th>Action</th>
			<th>Is Cancelled</th>
			<th>Cancel Info</th>
		</tr>
		<s:iterator var="one" value="timeActions">
		<tr>
			<td><s:property value="actioner" /></td>
			<td><s:property value="action_time" /></td>
			<td><s:property value="workflow.node.annotation" /></td>
			<td><s:if test="isCancelled()">Yes</s:if>&nbsp;</td>
			<td><s:property value="cancelInfo" /></td>
		</tr>
		</s:iterator>
	</table>

