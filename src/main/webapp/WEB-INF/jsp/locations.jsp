<h1><s:property value="#locationsTitle" /></h1>
<table class="width-full">
    <thead>
	<tr>
	    <th>ID</th>
	    <th>IP Address</th>
	    <th>Location Name</th>
	    <th>Address</th>
	    <th>Latitude</th>
	    <th>Longitude</th>
	    <th>Radius</th>
	    <th>Used Count(14 days)</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="#locations">
	    <tr>
		<td><a href="<s:property value='#application.url' />location.action?id=<s:property value='id' />">Edit</a></td>
		<td><s:property value="ip_address" /></td>
		<td><s:property value="name" /></td>
		<td><s:property value="street_address" /></td>
		<td><s:property value="latitude" /></td>
		<td><s:property value="longitude" /></td>
		<td><s:property value="radius" /></td>
		<td><s:property value="usedCount" /></td>		
	    </tr>
	</s:iterator>
    </tbody>
</table>
