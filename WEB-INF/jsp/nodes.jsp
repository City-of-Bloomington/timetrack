<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#nodesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Name</b></th>
			<th align="center"><b>Description</b></th>
			<th align="center"><b>Managers Only</b></th>
			<th align="center"><b>Annotation</b></th>
			<th align="center"><b>Active?</b></th>						
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#nodes">
			<tr>
				<td><a href="<s:property value='#application.url' />node.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="description" /></td>				
				<td><s:if test="managers_only">Yes</s:if><s:else>No</s:else></td>
				<td><s:property value="annotation" /></td>				
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
