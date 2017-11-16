<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<h4><s:property value="#reportTitle" /></h4>

<table border="1" width="40%">
	<caption>Hours Classified by Hour Codes</caption>
	<tr>
		<td align="center"><b>Hour Code</b></td>
		<td align="center"><b>Hours</b></td>
	</tr>
	<s:iterator var="row" value="#hoursSums">
		<s:set var="code" value="#row.key" />
		<s:set var="hours" value="#row.value" />
		<tr>
			<td><s:property value="#code" /></td>
			<td align="right"><s:property value="#hours" /></td>
		</tr>
	</s:iterator>
	<tr>
		<td><b>Total</b></td><td align="right"><s:property value="#totalHours" /></td>
	</tr>
</table>

<table width="80%" border="1">
	<caption>Hours Classified by Employees & Hour Codes</caption>
	<tr>
		<td>Employee</td>
		<td>Hour Code</td>
		<td>Hours</td>
		<td>Hourly Rate</td>
		<td>Amount</td>
	</tr>
	<s:iterator var="row" value="#hoursSums">
		<s:set var="code" value="#row.key" />
		<s:set var="hours" value="#row.value" />
		<s:iterator var="row2" value="#mapEntries">
			<s:set var="code2" value="#row2.key" />
			<s:set var="entries" value="#row2.value" />
			<s:if test="#code == #code2">
				<s:iterator var="entry" value="#entries">				
					<tr>
						<td><s:property value="#entry.fullname" /></td>
						<td><s:property value="#entry.code" /></td>
						<td align="right"><s:property value="#entry.hoursStr" /></td>
						<td align="right">$<s:property value="#entry.hourlyRate" /></td>
						<td align="right">$<s:property value="#entry.amountStr" /></td>
					</tr>
				</s:iterator>
			</s:if>
		</s:iterator>
		<tr>
			<td>&nbsp;</td>
			<td><b>Sub Total</b></td>
			<td align="right"><s:property value="#hours" /></td>
			<td>&nbsp;</td>
			<s:iterator var="row3" value="#amountsSums">
				<s:set var="code3" value="#row3.key" />
				<s:set var="amount" value="#row3.value" />
				<s:if test="#code == #code3">
					<td align="right">$<s:property value="#amount" /></td>
				</s:if>
			</s:iterator>
		</tr>
		<tr>
			<td colspan="5">&nbsp;</td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="2"><b>Total</b></td>
		<td align="right"><s:property value="#totalHours" /></td>
		<td>&nbsp;</td>
		<td align="right">$<s:property value="#totalAmount" /></td>
	</tr>
</table>