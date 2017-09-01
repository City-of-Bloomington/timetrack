<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#groupEmployeesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Group</b></th>
			<th align="center"><b>Employee</b></th>
			<th align="center"><b>Effective Date</b></th>
			<th align="center"><b>Expire Date</b></th>
			<th slign="center"><b>Active?</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#groupEmployees">
			<tr>
				<td><a href="<s:property value='#application.url' />groupEmployee.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="group" /></td>				
				<td><s:property value="employee.user" /></td>				
				<td><s:property value="effective_date" /></td>
				<td><s:property value="expire_date" /></td>
				<td><s:if  test="inactive">No</s:if><s:else>Yes</s:else></td>			
			</tr>
		</s:iterator>
	</tbody>
</table>
