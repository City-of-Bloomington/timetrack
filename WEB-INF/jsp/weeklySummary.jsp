<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table width="60%" border="1">
	<caption><s:property value="#weeklyTitle" /></caption>
	<tr>
		<td>Hour Code </td>
		<td>Total</td>
	</tr>
	<s:iterator value="#weeklyHourCodes" var="one" >
		<s:set var="hourCodeKey" value="#one.key" />
		<s:set var="hourCodeVal" value="#one.value" />
		<tr>
			<td align="left"><s:property value="#hourCodeKey" /></td>
			<td align="right"><s:property value="#hourCodeVal" /></td>
		</tr>
	</s:iterator>
	<tr>
		<td>Week Total</td>
		<td align="right"><s:property value="#weekTotal" /></td>
	</tr>
</table>



