<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table width="100%" border="1">
	<caption><s:property value="#codeRefsTitle" /></caption>
	<tr>
		<th align="center"><b>ID</b></th>
		<th align="center"><b>Code ID</b></th>
		<th align="center"><b>Code</b></th>
		<th align="center"><b>NW Code</b></th>
		<th align="center"><b>GL String</b></th>
		<th align="center"><b>PTO Ratio</b></th>
	</tr>
	<s:iterator var="one" value="#codeRefs">
		<tr>
			<td><a href="<s:property value='#application.url' />codeRef.action?id=<s:property value='id' />">Edit</a></td>
			<td><s:property value="code_id" /></td>
			<td><s:property value="code" /></td>
			<td><s:property value="nw_code" /></td>
			<td><s:property value="gl_value" /></td>
			<td><s:property value="pto_ratio" /></td>
		</tr>
	</s:iterator>
</table>
