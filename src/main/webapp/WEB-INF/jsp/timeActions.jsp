
<s:set var="timeActions" value="document.timeActions" />
<h1>Action History</h1>
<%@ include file="nextTimeAction.jsp" %>
<s:if test="document.hasTimeActions()">
	<table class="width-full action-history">
		<tr>
			<th width="200">By</th>
			<th width="200">Date/time</th>
			<th>Action</th>
			<th>Is Cancelled</th>
			<th>Cancel Info</th>
			<s:if test="#canCancelAction">
				<th>Cancel Action</th>
			</s:if>
		</tr>
		<s:iterator var="one" value="document.timeActions">
		<tr>
			<td><s:property value="actioner" /></td>
			<td><s:property value="action_time" /></td>
			<td><s:property value="workflow.node.annotation" /></td>
			<td><s:if test="isCancelled()">Yes</s:if>&nbsp;</td>
			<td><s:property value="cancelInfo" /></td>
			<s:if test="#canCancelAction">
				<td>
					<s:if test="canBeCancelled()">
						<a href="<s:property value='#application.url' />timeAction.action?id=<s:property value='id' />&action=Cancel&document_id=<s:property value='document_id'/>">Cancel</a>
					</s:if> 
				</td>
			</s:if>			
		</tr>
		</s:iterator>
	</table>
</s:if>
