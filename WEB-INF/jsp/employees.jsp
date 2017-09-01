<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#employeesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Username</b></th>
			<th align="center"><b>First Name</b></th>
			<th align="center"><b>Last Name</b></th>
			<th align="center"><b>ID Code #</b></th>
			<th align="center"><b>Employee #</b></th>
			<th align="center"><b>Role</b></th>
			<th align="center"><b>Active?</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#employees">
			<tr>
				<td><a href="<s:property value='#application.url' />employee.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="user.username" /></td>				
				<td><s:property value="user.first_name" /></td>
				<td><s:property value="user.last_name" /></td>
				<td><s:property value="id_code" /></td>
				<td><s:property value="employee_number" /></td>
				<td><s:property value="user.role" /></td>
				<td><s:if  test="inactive">No</s:if><s:else>Yes</s:else></td>				
			</tr>
		</s:iterator>
	</tbody>
</table>
