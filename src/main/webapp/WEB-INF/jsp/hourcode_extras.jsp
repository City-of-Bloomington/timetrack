<h1><s:property value="#hourcode_extras_itle" /></h1>
<table class="hour-codes width-full">
    <thead>
	<tr>
	    <th>ID</th>
	    <th># Times Per Day</th>			
	    <th>Max Total ($) per Year</th>
	    <th>Earn Code Associtation Type</th>
	    <th>Is Default Monetary Value Fixed </th>
	    <th>Active?</th>
	</tr>
    </thead>
	<tbody>
	    <s:iterator var="one" value="#hourcode_extras">
		<tr>
		    <td><a href="<s:property value='#application.url' />hourcode_extra.action?id=<s:property value='id' />"><s:property value="id" /></a></td>
		    <td><s:property value="timesPerDay" /></td>				
		    <td><s:property value="maxTotalPerYear" /></td>
		    <td><s:property value="hourCodeAssociateType" /></td>
		    <td><s:if test="defaultValueFixed">Yes</s:if><s:else>No</s:else></td>
		    <td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
		</tr>
	    </s:iterator>
	</tbody>
</table>
