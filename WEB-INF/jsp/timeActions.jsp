<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table width="100%" border="1">
	<caption><s:property value="#timeActionsTitle" /></caption>
	<tr>
		<td align="center" class="th_text">Date/time</td>
		<td align="center" class="th_text">Taken by</td>
		<td align="center" class="th_text">Action Taken</td>
	</tr>
	<s:iterator var="one" value="#timeActions">
		<tr>
			<td class="td_text"><s:property value="action_time" /></td>				
			<td class="td_text"><s:property value="actioner.user" /></td>
			<td class="td_text"><s:property value="workflow.node.annotation" /></td>
		</tr>
	</s:iterator>
</table>
