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
					</tr>
				</s:iterator>
			</s:if>
		</s:iterator>
		<tr>
			<td>&nbsp;</td>
			<td><b>Sub Total</b></td>
			<td align="right"><s:property value="#hours" /></td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="2"><b>Total</b></td>
		<td align="right"><s:property value="#totalHours" /></td>
	</tr>
</table>
