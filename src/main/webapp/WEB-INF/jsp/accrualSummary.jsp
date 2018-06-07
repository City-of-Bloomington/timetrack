<s:if test="document.hasAllAccruals()">
	<s:set var="allAccruals" value="document.allAccruals" />
	<s:set var="accrualsTitle" value="'Accruals Summary'" />

	<h1><s:property value="#accrualsTitle" /></h1>
	<table class="accruals-summary width-full">
		<tr>
			<th width="25%">Accrual Category</th>
			<th width="25%">Carry Over Balance</th>
			<th width="25%">Usage</th>
			<th>Available Balance</th>
		</tr>
		<s:iterator value="#allAccruals" var="one" >
			<s:set var="key" value="#one.key" />
			<s:set var="list" value="#one.value" />
			<tr>
				<td><s:property value="#key" /></td>
					<s:iterator value="#list">
						<td><s:property /></td>
					</s:iterator>
			</tr>
		</s:iterator>
	</table>
</s:if>