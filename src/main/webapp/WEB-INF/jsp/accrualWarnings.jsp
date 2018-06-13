<h1 class="cap_left"><s:property value="#accrualWarningsTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Hour Code</th>
			<th>Min Hours</th>
			<th>Step Hour</th>
			<th>Related Accrual Max Level</th>
			<th>Step Warning</th>
			<th>Min Warning</th>
			<th>Excess Warning</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#accrualWarnings">
			<tr>
				<td><a href="<s:property value='#application.url' />accrualWarning.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="hourCode" /></td>
				<td><s:property value="min_hrs" /></td>
				<td><s:property value="step_hrs" /></td>
				<td><s:property value="related_accrual_max_level" /></td>
				<td><s:property value="step_warning_text" /></td>
				<td><s:property value="min_warning_text" /></td>
				<td><s:property value="excess_warning_text" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
