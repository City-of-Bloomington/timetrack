<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="dataEntry" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h1>Data Entry</h1>

	<p>Intended for data entry of time keeping by certain authorized employees for other employees of certain employee groups (such as Utilities T&D group).</p>

	<%@ include file="strutMessages.jsp" %>

	<s:if test="hasGroups()">
		<table width="100%" border="0">
			<tr><td align="left">
				<b>Pay Period </b><s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Period" onchange="doRefresh()" /> </td>
				<td align="right"><a href="<s:property value='#application.url' />dataEntry.action?pay_period_id=<s:property value='currentPayPeriod.id' />">Current Pay Period</a></td>
			</tr>
			<tr>
				<td><b>Group: </b>
					<s:select name="group_id" value="%{group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" onchange="doRefresh()" /> </td>
				<td align="right"><b> Approver: </b><s:property value="employee" /></td>
			</tr>
		</table>

		<s:if test="hasDocuments()">
			<table width="100%">
				<s:iterator var="one" value="documents">
					<s:if test="hasDaily()">
						<tr>
							<td valign="center" width="10%">
								<s:property value="employee.user" />
								<a href="<s:property value='#application.url' />timeDetails.action?document_id=<s:property value='id' />&source=approve" />Details</a>
							</td>
							<td valign="center">
								<s:set var="daily" value="daily" />
								<s:set var="week1Total" value="week1Total" />
								<s:set var="week2Total" value="week2Total" />
								<s:set var="payPeriodTotal" value="payPeriodTotal" />
								<%@  include file="dailySummary.jsp" %>
							</td>
							<td valign="center" width="10%" align="right">
								<s:if test="canBeApproved()">
									Ready for Approval
								</s:if>
								<s:elseif test="isApproved()">Approved</s:elseif>
								<s:elseif test="isProcessed()">Processed</s:elseif>
								<s:else>
									Not Submitted
								</s:else>
							</td>
						</tr>
					</s:if>
				</s:iterator>
			</table>
		</s:if>

				<s:if test="hasNonDocEmps()">
					<table width="50%" border="0">
						<caption>Employee(s) with no time entry for this pay period</caption>
						<s:iterator var="one" value="nonDocEmps">
							<tr>
								<td width="20%">&nbsp;</td>
								<td><a href="<s:property value='#application.url' />timeDetails.action?employee_id=<s:property value='id' />&pay_period_id=<s:property value='pay_period_id' />&source=approve"> <s:property value="user.full_name" /></a></td>
							</tr>
						</s:iterator>
					</table>
				</s:if>
		</s:if>
	</s:form>
</div>
<%@ include file="footer.jsp" %>