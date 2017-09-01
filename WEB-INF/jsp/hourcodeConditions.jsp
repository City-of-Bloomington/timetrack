<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#hourcodeConditionsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Hour Code</b></th>
			<th align="center"><b>Department</b></th>
			<th align="center"><b>Salary Group</b></th>
			<th align="center"><b>Date</b></th>
			<th align="center"><b>Active?</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#hourcodeConditions">
			<tr>
				<td><a href="<s:property value='#application.url' />hourcodeCondition.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="hourCode" /></td>				
				<td><s:property value="department" /></td>
				<td><s:property value="salaryGroup" /></td>
				<td><s:property value="date" /></td>		
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>				
			</tr>
		</s:iterator>
	</tbody>
</table>
