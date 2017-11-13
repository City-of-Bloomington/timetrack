<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table width="100%" border="1"><caption>Summary</caption>
	<tr>
		<td class="th_text">Week </td>
		<td width="10%" class="th_text">Mon</td>
		<td width="10%" class="th_text">Tue</td>
		<td width="10%" class="th_text">Wed</td>
		<td width="10%" class="th_text">Thu</td>
		<td width="10%" class="th_text">Fri</td>
		<td width="10%" class="th_text">Sat</td>
		<td width="10%" class="th_text">Sun</td>
		<td class="th_text">Total</td>
	</tr>
	<s:iterator value="#daily" var="one" >
		<s:set var="dayKey" value="#one.key" />
		<s:set var="dayVal" value="#one.value" />
		<s:if test="#dayKey == 0">
	    <tr><td class="th_text">Week 1</td>
		</s:if>
		<s:if test="#dayKey == 7">
			<td align="right" class="td_text"><s:property value="#week1Total" /></td>
	    </tr><tr><td class="th_text">Week 2</td>
		</s:if>
		<td align="right" class="td_text"><s:property value="dayVal" /></td>
		<s:if test="#dayKey == 13">
			<td align="right" class="td_text"><s:property value="#week2Total" /></td>
	    </tr>
		</s:if>
	</s:iterator>
	<tr>
		<td class="th_text">Pay Period Total</td>
		<td colspan="7" class="td_text">&nbsp;</td>
		<td align="right" class="td_text"><s:property value="#payPeriodTotal" /></td>
	</tr>
</table>



