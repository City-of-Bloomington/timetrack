<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table width="100%" border="1"><caption>Summary</caption>
	<tr>
		<td>Week </td>
		<td width="10%">Mon</td>
		<td width="10%">Tue</td>
		<td width="10%">Wed</td>
		<td width="10%">Thu</td>
		<td width="10%">Fri</td>
		<td width="10%">Sat</td>
		<td width="10%">Sun</td>
		<td>Total</td>
	</tr>
	<s:iterator value="#daily" var="one" >
		<s:set var="dayKey" value="#one.key" />
		<s:set var="dayVal" value="#one.value" />
		<s:if test="#dayKey == 0">
	    <tr><td>Week 1</td>
		</s:if>
		<s:if test="#dayKey == 7">
			<td align="right"><s:property value="#week1Total" /></td>
	    </tr><tr><td>Week 2</td>
		</s:if>
		<td align="right"><s:property value="dayVal" /></td>
		<s:if test="#dayKey == 13">
			<td align="right"><s:property value="#week2Total" /></td>
	    </tr>
		</s:if>
	</s:iterator>
	<tr>
		<td>Pay Period Total</td>
		<td colspan="7">&nbsp;</td>
		<td align="right"><s:property value="#payPeriodTotal" /></td>
	</tr>
</table>



