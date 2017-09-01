<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:if test="hasActionErrors()">
	<div class="errors">
    <s:actionerror/>
	</div>
</s:if>
<p style="text-align:center">
	Current Pay Period: <s:property value="currentPayPeriod.dateRange" />
</p>
<table class="fn1-table">
	<caption><s:property value="payPeriodsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Start Date</b></th>
			<th align="center"><b>End Date</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="payPeriods">
			<tr>
				<td><s:property value="id" /></td>
				<td><s:property value="start_date" /></td>
				<td><s:property value="end_date" /></td>				
			</tr>
		</s:iterator>
	</tbody>
</table>

<%@  include file="footer.jsp" %>
