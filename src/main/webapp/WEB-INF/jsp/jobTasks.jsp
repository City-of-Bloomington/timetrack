<h1><s:property value="#jobTasksTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Position</th>
			<th>Salary Group</th>
			<th>Employee</th>
			<th>Group</th>
			<th>Effective Date</th>
			<th>Primary Job</th>
			<th>Clock Time Required</th>
			<th>Weekly Reg Hrs</th>
			<th>Comp Time Weekly Hrs</th>
			<th>Comp Time Muliple Factor</th>
			<th>Holiday Comp Multiple factor</th>
			<th>Expire Date</th>						
			<th>Included in Auto Submit Batch</th>
			<th>Work Irregular Work Days</th>
			<th>Added Date</th>			
			<th>Active?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#jobTasks">
			<tr>
				<td><a href="<s:property value='#application.url' />jobTask.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="position" /></td>
				<td><s:property value="salaryGroup" /></td>
				<td><a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='employee_id' />"> <s:property value="employee" /></a></td>
				<td><a href="<s:property value='#application.url' />group.action?id=<s:property value='group_id' />"> <s:property value="group" /></a></td>					
				<td><s:property value="effective_date" /></td>
				<td><s:if test="primary_flag">Yes</s:if><s:else>&nbsp;</s:else></td>
				<td><s:if test="clock_time_required">Yes</s:if><s:else>&nbsp;</s:else></td>
				<td><s:property value="weekly_regular_hours" /></td>
				<td><s:property value="comp_time_weekly_hours" /></td>
				<td><s:property value="comp_time_factor" /></td>
				<td><s:property value="holiday_comp_factor" /></td>
				<td><s:property value="expire_date" /></td>				
				<td><s:if test="includeInAutoBatch">Yes</s:if><s:else>&nbsp;</s:else></td>
				<td><s:if test="irregularWorkDays">Yes</s:if><s:else>&nbsp;</s:else></td>				
				<td><s:property value="added_date" /></td>								
				<td><s:if test="inactive">No</s:if><s:else>&nbsp;</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
