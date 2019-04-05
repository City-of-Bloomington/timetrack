<h1><s:property value="#employeesTitle" /></h1>
<table class="employees width-full">
	<caption>Most Recent Employees Update Runs</caption>
	<thead>
		<tr>
			<th>Date & Time</th>
			<th style="text-align:left">Employees Changed to Inactive</th>
			<th>Status</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#employeesLogs">
			<tr>
				<td><s:property value="date" /></td>
				<td style="text-align:left"><s:property value="empsIdSet" /></td>
				<td><s:if test="hasErrors()">Error: <s:property value="errors" /></s:if><s:else>Success</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
