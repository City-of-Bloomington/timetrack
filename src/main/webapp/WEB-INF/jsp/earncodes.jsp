<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#earncodesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Name</b></th>
			<th align="center"><b>Description</b></th>
			<th align="center"><b>Recoding Method</b></th>
			<th align="center"><b>Related Accrual</b></th>
			<th align="center"><b>Count as Regular</b></th>
			<th align="center"><b>Active?</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#earnCodes">
			<tr>
				<td><a href="<s:property value='#application.url' />earncode.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="name" /></td>				
				<td><s:property value="description" /></td>
				<td><s:property value="record_method" /></td>
				<td><s:if test="accrual_id != ''"><s:property value="accrual" /></s:if><s:else>&nbsp;</s:else></td>
				<td><s:if test="count_as_regular_pay">Yes</s:if><s:else>No</s:else></td>
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>				
			</tr>
		</s:iterator>
	</tbody>
</table>
