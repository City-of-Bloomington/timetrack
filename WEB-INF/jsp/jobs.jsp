<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#jobsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Position</b></th>
			<th align="center"><b>Salary Group</b></th>
			<th align="center"><b>Employee</b></th>
			<th align="center"><b>Effective Date</b></th>
			<th align="center"><b>Expire Date</b></th>
			<th align="center"><b>Primary Job</b></th>
			<th align="center"><b>Active?</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#jobs">
			<tr>
				<td><a href="<s:property value='#application.url' />job.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="position" /></td>				
				<td><s:property value="salaryGroup" /></td>
				<td><s:property value="user.full_name" /></td>
				<td><s:property value="effective_date" /></td>
				<td><s:property value="expire_date" /></td>
				<td><s:if test="primary_flag">Yes</s:if><s:else>No</s:else></td>
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>				
			</tr>
		</s:iterator>
	</tbody>
</table>
