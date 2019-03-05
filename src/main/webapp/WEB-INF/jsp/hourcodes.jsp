<h1><s:property value="#hourcodesTitle" /></h1>
<table class="hour-codes">
	<thead>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Description</th>
			<th>Recoding Method</th>
			<th>Related Accrual</th>
			<th>Type</th>
			<th>Default Monetary Amount</th>
			<th>Default Regular</th>
			<th>Active?</th>
		</tr>
	<tbody>
		<s:iterator var="one" value="#hourcodes">
			<tr>
				<td><a href="<s:property value='#application.url' />hourcode.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="description" /></td>
				<td><s:property value="record_method" /></td>
				<td><s:if test="accrual_id != ''"><s:property value="accrual" /></s:if><s:else>&nbsp;</s:else></td>
				<td><s:property value="type" /></td>
				<td>$<s:property value="defaultMonetaryAmount" /></td>
				<td><s:if test="reg_default">Yes</s:if><s:else>No</s:else></td>
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
