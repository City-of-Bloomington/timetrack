<s:set var="allAccruals" value="document.allAccruals" />
<h1>Accrual Summary</h1>
<s:if test="document.hasAllAccruals()">
	<table class="accruals-summary width-full">
		<tr>
			<th width="20%">Accrual Category</th>
			<th width="20%">Carry Over Balance</th>
			<th width="20%">Earned</th>
			<th width="20%">Usage</th>
			<th>Available Balance</th>
		</tr>
		<s:iterator value="document.allAccruals" var="one" >
			<s:set var="key" value="#one.key" />
			<s:set var="list" value="#one.value" />
			<tr>
				<td><s:property value="#key" /></td>
				<s:iterator value="#list" status="row">
					<td><s:property /></td>
				</s:iterator>
		</tr>
		</s:iterator>
	</table>
</s:if>
<s:else>
	<div class="m-b-40">
		<p><strong>Note:</strong> Accrual / Benefits information is not yet available for this pay period.</p>
	</div>
</s:else>
