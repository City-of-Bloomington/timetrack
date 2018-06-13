<h1><s:property value="#empAccrualsTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Accrual</th>
			<th>Related Hour Code</th>
			<th>Employee</th>
			<th>Hours</th>
			<th>Date</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#empAccruals">
			<tr>
				<td><a href="<s:property value='#application.url' />empAccrual.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="accrual" /></td>
				<td><s:property value="hourCode" /></td>
				<td><s:property value="employee" /></td>
				<td><s:property value="hours" /></td>
				<td><s:property value="date" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
