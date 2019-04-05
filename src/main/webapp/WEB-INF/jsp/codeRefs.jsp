<h1><s:property value="#codeRefsTitle" /></h1>
<table class="width-full">
	<tr>
		<th>ID</th>
		<th>Code ID</th>
		<th>Earn Code</th>
		<th>NW Code</th>
		<th>GL String</th>
		<th>PTO Ratio</th>
	</tr>
	<s:iterator var="one" value="#codeRefs">
		<tr>
			<td><a href="<s:property value='#application.url' />codeRef.action?id=<s:property value='id' />">Edit</a></td>
			<td><s:property value="code_id" /></td>
			<td><s:property value="code" /></td>
			<td><s:property value="nw_code" /></td>
			<td><s:property value="gl_value" /></td>
			<td><s:property value="pto_ratio" /></td>
		</tr>
	</s:iterator>
</table>
