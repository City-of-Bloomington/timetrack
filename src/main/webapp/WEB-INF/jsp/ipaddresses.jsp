<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#ipaddressesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>IP</b></th>
			<th align="center"><b>Description</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#ipaddresses">
			<tr>
				<td><a href="<s:property value='#application.url' />ipaddress.action?id=<s:property value='id' />">Edit</a></td>
				<td><s:property value="ip_address" /></td>
				<td><s:property value="description" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
