<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#accrualWarningsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Hour Code</b></th>
			<th align="center"><b>Min Hours</b></th>
			<th align="center"><b>Step Hour</b></th>
			<th align="center"><b>Related Accrual Max Level</b></th>
			<th align="center"><b>Step Warning</b></th>
			<th align="center"><b>Min Warning</b></th>
			<th align="center"><b>Excess Warning</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#accrualWarnings">
			<tr>
				<td><a href="<s:property value='#application.url' />accrualWarning.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="hourCode" /></td>				
				<td><s:property value="min_hrs" /></td>
				<td><s:property value="step_hrs" /></td>
				<td><s:property value="related_accrual_max_level" /></td>
				<td><s:property value="step_warning_text" /></td>
				<td><s:property value="min_warning_text" /></td>
				<td><s:property value="excess_warning_text" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
