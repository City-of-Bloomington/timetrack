<%@ include file="header.jsp" %>
<div class="internal-page">

	<s:if test="hasMessages()">
		<s:set var="messages" value="%{messages}" />
		<%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
		<s:set var="errors" value="%{errors}" />
		<%@ include file="errors.jsp" %>
	</s:if>
	<h1>Pay Periods</h1>
	<s:form action="payperiod" id="form_id" method="post">
		<div class="width-one-half">
			<div class="form-group">
				<label>Year:</label>
				<s:select name="year" value="%{year}" list="years" headerKey="-1" headerValue="Pick Year" />
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="button"/>
			</div>
		</div>
	</s:form>
	<br />
	Current Pay Period <s:property value="currentPayPeriod.dateRange" />
	<br />
	<s:if test="action != '' && hasPayPeriods()">
		<table class="groups width-one-half">
			<thead>
				<tr>
					<th>ID</th>
					<th>Date Range</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator var="one" value="payPeriods">
					<tr>
						<td><s:property value="id" /></td>
						<td><s:property value="dateRange" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
