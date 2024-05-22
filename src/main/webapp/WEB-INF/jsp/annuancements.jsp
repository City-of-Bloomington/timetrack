<h1><s:property value="#annuancementsTitle" /></h1>
<table class="width-full">
    <thead>
	<tr>
	    <th>ID</th>
	    <th>Annuancement Text</th>
	    <th>Annuancement Url</th>
	    <th>Start Date</th>
	    <th>Expire Date</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#annuancements">
	    <tr>
		<td><a href="<s:property value='#application.url' />annuance.action?id=<s:property value='id' />">Edit</a></td>
		<td><s:property value="annuanceText" /></td>
		<td><s:property value="annuanceUrl" /></td>
		<td><s:property value="startDate" /></td>
		<td><s:property value="expireDate" /></td>
	    </tr>
	</s:iterator>
    </tbody>
</table>
