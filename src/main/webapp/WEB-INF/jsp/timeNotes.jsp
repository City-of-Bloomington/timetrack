<s:if test="document.hasTimeNotes()">
    <s:set var="timeNotesTitle" value="'Pay Period Notes'" />
    <s:set var="timeNotes" value="document.timeNotes" />

    <table class="pay-period-notes width-full">
	<caption style="text-align:left;font-weight:bold">
	    <s:property value="#timeNotesTitle" />
	</caption>
	    <tr>
	    <th width="20%">By</th>
	    <th>&nbsp;</th>
	    <th width="20%">Date &amp; Time</th>
	    <th>Notes</th>
	    <th>Action</th>
	</tr>
	<s:iterator var="one" value="#timeNotes">
	    <tr>
		<td><s:property value="reporter" /></td>
		<td>&nbsp;</td>
		<td><s:property value="date" /></td>
		<td><s:property value="notes" /></td>
		<td><a href="<s:property value='#application.url' />timeNote.action?id=<s:property value='id' />&action=Delete&document_id=<s:property value='document_id'/>">Delete</a></td>
	    </tr>
	</s:iterator>
    </table>
</s:if>
