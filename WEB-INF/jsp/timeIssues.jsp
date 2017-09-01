<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#timeIssuesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Time block ID</b></th>
			<th align="center"><b>Reported By</b></th>			
			<th align="center"><b>Date & Time</b></th>
			<th align="center"><b>Issue</b></th>
			<th align="center"><b>Status</b></th>
			<th align="center"><b>Closed Date</b></th>
			<th align="center"><b>Closed By</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#timeIssues">
			<tr>
				<s:if test="isOpen()">
					<td><a href="<s:property value='#application.url' />handleIssue.action?id=<s:property value='id' />">Handle Issue</a></td>
					<td><a href="#" onclick="return popwit('<s:property value='#application.url' />timeBlock?id=<s:property value='time_block_id' />','timeBlock');">Edit Clock-In/Clock-Out Times</a></td>					
				</s:if>
				<s:else>
					<td><s:property value='id' /></td>					
					<td><s:property value="time_block_id" /></td>
				</s:else>
				<td><s:property value="reporter" /></td>				
				<td><s:property value="date" /></td>
				<td><s:property value="issue_notes" /></td>
				<td><s:property value="status" /></td>
				<td><s:property value="closed_date" /></td>
				<td><s:property value="closed_by" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
