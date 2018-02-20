
<table width="100%" border="0" class="tbl_gray">
	<caption class="cap_left"><s:property value="#timeActionsTitle" /></caption>	
	<tr>
		<td align="left" class="th_text">Date/time</td>
		<td align="left" class="th_text">By</td>
		<td align="left" class="th_text">Action</td>
	</tr>
	<s:iterator var="one" value="#timeActions">
		<tr>
			<td class="td_text"><s:property value="action_time" /></td>				
			<td class="td_text"><s:property value="actioner" /></td>
			<td class="td_text"><s:property value="workflow.node.annotation" /></td>
		</tr>
	</s:iterator>
</table>
