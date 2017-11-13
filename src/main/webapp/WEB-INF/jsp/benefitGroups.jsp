<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#benefitGroupsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Group Code</b></th>			
			<th align="center"><b>Full Time?</b></th>
			<th align="center"><b>Exempt?</b></th>
			<th align="center"><b>Unioned?</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#benefitGroups">
			<tr>
				<td><a href="<s:property value='#application.url' />benefitGroup.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>				
				<td><s:if test="fullTime" >Yes</s:if><s:else>No</s:else></td>
				<td><s:if test="exempt" >Yes</s:if><s:else>No</s:else></td>
				<td><s:if test="unioned" >Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
