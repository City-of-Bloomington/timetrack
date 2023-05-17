<h1><s:property value="#jobTasksTitle" /></h1>
<table class="width-full">
    <thead>
	<tr>
	    <th>ID</th>
	    <th>Position</th>
	    <th>Salary Group</th>
	    <th>Employee</th>
	    <s:if test="#skipGroupName == null">
		<th>Group</th>
	    </s:if>
	    <th>Effective Date</th>
	    <th>Weekly Reg Hrs</th>
	    <th>Comp Time Weekly Hrs</th>
	    <th>Comp Time Muliple Factor</th>
	    <th>Holiday Comp Multiple factor</th>
	    <th>Expire Date</th>
	    <th>Action</th>	    
	    <th>Added Date</th>
	</tr>
	</thead>
	<tbody>
	    <s:iterator var="one" value="#jobTasks">
		<tr>
		    <td><a href="<s:property value='#application.url' />jobTask.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
		    <td><s:property value="position" /></td>
		    <td><s:property value="salaryGroup" /></td>
		    <td><a href="<s:property value='#application.url' />employee.action?emp_id=<s:property value='employee_id' />"> <s:property value="employee" /></a></td>
		    <s:if test="#skipGroupName == null">
			<td><a href="<s:property value='#application.url' />group.action?id=<s:property value='group_id' />"> <s:property value="group" /></a></td>
		    </s:if>		
		    <td><s:property value="effective_date" /></td>
		    <td><s:property value="weekly_regular_hours" /></td>
		    <td><s:property value="comp_time_weekly_hours" /></td>
		    <td><s:property value="comp_time_factor" /></td>
		    <td><s:property value="holiday_comp_factor" /></td>
		    <td><s:property value="expire_date" /></td>
		    <td>
			<s:if test="isActive()">
			    <a href="<s:property value='#application.url' />jobTerminate.action?job_id=<s:property value='id' />"> Terminate </a>
			</s:if>
			<s:else>
			    <a href="<s:property value='#application.url' />jobTask.action?id=<s:property value='id' />&action=Reactivate"> Re-Activate </a>
			</s:else>
		    </td>
		    <td><s:property value="added_date" /></td>
		</tr>
	    </s:iterator>
	</tbody>
</table>
