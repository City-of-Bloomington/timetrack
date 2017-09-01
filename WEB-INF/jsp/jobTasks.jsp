<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#jobTasksTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Position</b></th>
			<th align="center"><b>Salary Group</b></th>
			<th align="center"><b>Employee</b></th>
			<th align="center"><b>Effective Date</b></th>
			<th align="center"><b>Expire Date</b></th>
			<th align="center"><b>Primary Job</b></th>
			<th align="center"><b>Weekly Reg Hrs</b></th>
			<th align="center"><b>Comp Time Weekly Hrs</b></th>
			<th align="center"><b>Comp Time Muliple Factor</b></th>
			<th align="center"><b>Holiday Comp Multiple factor</b></th>
			<th align="center"><b>Active?</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#jobTasks">
			<tr>
				<td><a href="<s:property value='#application.url' />jobTask.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="position" /></td>				
				<td><s:property value="salaryGroup" /></td>
				<td><s:property value="employee.user" /></td>
				<td><s:property value="effective_date" /></td>
				<td><s:property value="expire_date" /></td>
				<td><s:if test="primary_flag">Yes</s:if><s:else>No</s:else></td>
				<td><s:property value="weekly_regular_hours" /></td>
				<td><s:property value="comp_time_weekly_hours" /></td>
				<td><s:property value="comp_time_factor" /></td>
				<td><s:property value="holiday_comp_factor" /></td>				
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>				
			</tr>
		</s:iterator>
	</tbody>
</table>
