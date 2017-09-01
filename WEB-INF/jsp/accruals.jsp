<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#accrualsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Name</b></th>
			<th align="center"><b>Description</b></th>
			<th align="center"><b>Pref Max Level</b></th>
			<th align="center"><b>Inactive?</b></th>						
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#accruals">
			<tr>
				<td><a href="<s:property value='#application.url' />accrual.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="description" /></td>
				<td><s:property value="pref_max_level" /></td>
				<td><s:if test="inactive">Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
