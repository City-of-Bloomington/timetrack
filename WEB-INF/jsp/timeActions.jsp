<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#timeActionsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>Date/time</b></th>
			<th align="center"><b>Taken by</b></th>
			<th align="center"><b>Action Taken</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#timeActions">
			<tr>
				<td><s:property value="action_time" /></td>				
				<td><s:property value="actioner.user" /></td>
				<td><s:property value="workflow.node.annotation" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
