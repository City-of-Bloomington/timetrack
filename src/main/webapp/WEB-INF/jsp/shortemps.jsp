<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#employeesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>Full Name</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#employees">
			<tr>
				<td><a href="<s:property value='#application.url' />employee.action?id=<s:property value='id' />"> <s:property value="user.full_name" /></a></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
