<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table width="100%" border="1"><caption>Accruals Summary</caption>
	<tr>
		<td width="25%" class="th_text">Accrual Category</td>
		<td width="25%" class="th_text">Carry Over Balance</td>
		<td width="25%" class="th_text">Usage</td>
		<td class="th_text">Available Balance</td>
	</tr>
	<s:iterator value="#allAccruals" var="one" >
		<s:set var="key" value="#one.key" />
		<s:set var="list" value="#one.value" />
		<tr>
			<td class="th_text"><s:property value="#key" /></td>
				<s:iterator value="#list">
					<td align="right" class="td_text"><s:property /></td>
				</s:iterator>
		</tr>
	</s:iterator>
</table>




