<table>
    <tr>
	<th>Email Date</th>
	<th>From</th>
	<th>To </th>
	<th>CC </th>
	<th>Email Message</th>
	<th>Sent Status</th>
	<th>Error Msg</th>
    </tr>
    <s:iterator var="one" value="email_logs">
	<tr>
	    <td><s:property value="date" /></td>
	    <td><s:property value="emailfrom" /></td>	    
	    <td><s:property value="emailTo" /></td>
	    <td><s:property value="emailCc" /></td>
	    <td><s:property value="textMessage" /></td>
	    <td><s:property value="status" /></td>
	    <td><s:property value="sentErrors" /></td>
	</tr>
    </s:iterator>
</table>

