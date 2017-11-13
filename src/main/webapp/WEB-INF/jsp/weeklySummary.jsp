<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table width="60%" border="1">
	<caption><s:property value="#weeklyTitle" /></caption>
	<tr>
		<td class="th_text">Hour Code </td>
		<td class="th_text">Total</td>
	</tr>
	<s:iterator value="#weeklyHourCodes" var="one" >
		<s:set var="hourCodeKey" value="#one.key" />
		<s:set var="hourCodeVal" value="#one.value" />
		<tr>
			<td align="left" class="th_text"><s:property value="#hourCodeKey" /></td>
			<td align="right" class="td_text"><s:property value="#hourCodeVal" /></td>
		</tr>
	</s:iterator>
	<tr>
		<td class="th_text">Week Total</td>
		<td align="right" class="td_text"><s:property value="#weekTotal" /></td>
	</tr>
</table>



