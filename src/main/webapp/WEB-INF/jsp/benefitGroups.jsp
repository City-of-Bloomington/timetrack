<h1><s:property value="#benefitGroupsTitle" /></h1>
<table class="width-full">
	<thead>
		<tr>
			<th>ID</th>
			<th>Group Code</th>
			<th>Full Time?</th>
			<th>Exempt?</th>
			<th>Unioned?</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#benefitGroups">
			<tr>
				<td><a href="<s:property value='#application.url' />benefitGroup.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="name" /></td>
				<td><s:if test="fullTime" >Yes</s:if><s:else>No</s:else></td>
				<td><s:if test="exempt" >Yes</s:if><s:else>No</s:else></td>
				<td><s:if test="unioned" >Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
