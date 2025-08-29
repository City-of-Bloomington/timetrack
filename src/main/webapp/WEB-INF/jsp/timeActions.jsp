
<s:set var="timeActions" value="document.timeActions" />


<s:if test="document.hasTimeActions()">
    <table class="width-full action-history">
	<caption style="text-align:left;font-weight:bold">
	    Action History
	</caption>
	<tr>
	    <td colspan="7">
		<%@ include file="nextTimeAction.jsp" %>		
	    </td>
	</tr>
	<tr>
		<th width="20%">By</th>
		<th>&nbsp;</th>
		<th width="20%">Date/time</th>
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
		    <td>&nbsp;</td>
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

