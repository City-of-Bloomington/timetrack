<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#empAccrualsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Accrual</b></th>
			<th align="center"><b>Related Hour Code</b></th>
			<th align="center"><b>Employee</b></th>
			<th align="center"><b>Hours</b></th>
			<th align="center"><b>Date</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#empAccruals">
			<tr>
				<td><a href="<s:property value='#application.url' />empAccrual.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="accrual" /></td>				
				<td><s:property value="hourCode" /></td>
				<td><s:property value="employee.user" /></td>
				<td><s:property value="hours" /></td>
				<td><s:property value="date" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
