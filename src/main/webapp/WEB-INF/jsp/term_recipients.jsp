
<h1><s:property value="#recipientsTitle" /></h1>
<table class="width-full">
    <thead>
	<tr>
	    <th>ID</th>
	    <th>Name</th>
	    <th>Email</th>
	    <th>Action</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#recipients">
	    <tr>
		<td><a href="<s:property value='#application.url' />termRecipient.action?id=<s:property value='id' />">Edit</a></td>
		<td><s:property value="name" /></td>
		<td><s:property value="email" /></td>
		<td><a href="<s:property value='#application.url' />termRecipient.action?action=Remove&id=<s:property value='id' />">Remove</a></td>		
	    </tr>
	</s:iterator>
    </tbody>
</table>
