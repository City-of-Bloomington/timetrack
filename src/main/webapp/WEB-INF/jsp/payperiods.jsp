<%@ include file="header.jsp" %>
<div class="internal-page">
	<%@ include file="strutMessages.jsp" %>
	<h1>Current Pay Period: <s:property value="currentPayPeriod.dateRange" /></h1>
	<table class="width-full">
		<thead>
			<tr>
				<th>ID</th>
				<th>Start Date</th>
				<th>End Date</th>
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
</div>
<%@ include file="footer.jsp" %>