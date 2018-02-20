<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption class="cap_left"><s:property value="#timeNotesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>Reported By</b></th>			
			<th align="center"><b>Date & Time</b></th>
			<th align="center"><b>Notes</b></th>						
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#timeNotes">
			<tr>
				<td><s:property value="reporter" /></td>				
				<td><s:property value="date" /></td>
				<td><s:property value="notes" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
