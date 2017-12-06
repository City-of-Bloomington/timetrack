<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#departmentEmployeesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Employee</b></th>
			<th align="center"><b>Department</b></th>
			<th align="center"><b>Seconday Department</b></th>			
			<th align="center"><b>Start Date</b></th>
			<th align="center"><b>Expire Date</b></th>
			<th align="center"><b>Active</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#departmentEmployees">
			<tr>
				<td><a href="<s:property value='#application.url' />departmentEmployee.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="employee.user" /></td>				
				<td><s:property value="department" /></td>
				<td><s:if test="hasSecondaryDept()"><s:property value="department2" /></s:if>&nbsp;</td>
				<td><s:property value="effective_date" /></td>
				<td><s:property value="expire_date" /></td>
				<td><s:if test="active">Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
