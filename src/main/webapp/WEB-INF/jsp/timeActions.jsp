<s:if test="document.hasTimeActions()">
	<s:set var="timeActions" value="document.timeActions" />
	<s:set var="timeActionsTitle" value="'Action History'" />

	<h1><s:property value="#timeActionsTitle" /></h1>
	<table class="width-full action-history">
		<tr>
			<th width="200">By</th>
			<th width="200">Date/time</th>
			<th>Action</th>
		</tr>
		<s:iterator var="one" value="#timeActions">
			<tr>
				<td><s:property value="actioner" /></td>
				<td><s:property value="action_time" /></td>
				<td><s:property value="workflow.node.annotation" /></td>
			</tr>
		</s:iterator>
	</table>
</s:if>