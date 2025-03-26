<h1><s:property value="#benefitGroupRefsTitle" /></h1>
<table class="width-full">
    <thead>
	<tr>
	    <th>ID</th>
	    <th>Benefit Name</th>
	    <th>Salary Group</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#benefitGroupRefs">
	    <tr>
		<td><a href="<s:property value='#application.url' />benefitGroupRef.action?id=<s:property value='id' />">Edit</a></td>
		<td><s:property value="name" /></td>
		<td><s:property value="salaryGroupName" /></td>
	    </tr>
	</s:iterator>
    </tbody>
</table>
