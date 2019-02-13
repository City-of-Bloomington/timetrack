<h1><s:property value="department" /> Employees</h1>
<s:if test="hasEmpsWithNoEmpNum()">
	<p>The following employees Do not have employee number and will not be imported <br />
		You may add their employees' number and run this process again.
	</p>
	<s:set var="employees" value="empsWithNoEmpNum" />
	<%@ include file="../employees.jsp" %>
</s:if>
<s:if test="hasProcesses()">
	<table width="100%">
		<tr>
			<td class="th_text">Name</td>
			<td class="th_text">Actions</td>
			<td align="center" class="th_text">Interpreted Hours Break Down</td>
		</tr>
		<s:iterator value="processes" var="one" >
			<tr>
				<td valign="top" class="td_text" id="<s:property value='#one.employee.id' />"> <s:property value="#one.employee" /> (<s:property value="#one.employee.employee_number " />)</td>
				<td valign="top">
					<s:if test="document.hasTimeActions()">
						<s:set var="timeActions" value="document.timeActions" />
						<s:set var="timeActionsTitle" value="' '" />
						<%@  include file="../timeActions.jsp" %>
					</s:if>
				</td>
				<td valign="top">
					<table width="100%">
						<tr><td class="th_text">Hours</td><td class="th_text">W1 (gross)</td><td class="th_text">W2 (gross)</td><td class="th_text">Total</td></tr>
						<tr>
							<td class="td_text"><s:property value="firstEntry.name" /></td>
							<td class="td_text"><s:property value="firstEntry.val" /></td>
							<td class="td_text"><s:property value="firstEntry.val2" /></td>
							<td class="td_text" align="right"><s:property value="firstEntry.val3" /></td>
						</tr>
						<s:if test="hasEntries()">
							<s:iterator value="entries" var="row" status="rrow">
								<s:set var="rowKey" value="#row.key" />
								<s:set var="rowList" value="#row.value" />
								<s:iterator value="#rowList" >
									<tr>
										<td class="td_text"><s:property value="name" /></td>
										<td class="td_text"><s:property value="val" /></td>
										<td class="td_text"><s:property value="val2" /></td>
										<td class="td_text" align="right""><s:property value="val3" /></td>
									</tr>
								</s:iterator>
							</s:iterator>
						</s:if>
						<tr>
							<td class="td_text">-------</td>
							<td class="td_text">-------</td>
							<td class="td_text">-------</td>
							<td class="td_text" align="right">-------</td>
						</tr>
						<tr>
							<td class="td_text"><s:property value="lastEntry.name" /></td>
							<td class="td_text"><s:property value="lastEntry.val" /></td>
							<td class="td_text"><s:property value="lastEntry.val2" /></td>
							<td class="td_text" align="right"><s:property value="lastEntry.val3" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<s:if test="document.hasLastWorkflow()">
				<tr>
					<td class="td_text" colspan="2">
						<b>Status:</b> <s:property value="document.LastWorkflow.node.annotation" />
					</td>
				</tr>
			</s:if>
			<s:if test="document.hasAllAccruals()">
				<tr>
					<td colspan="2" class="td_text"><b> Available Accruals: </b>
						<s:property value="document.employeeAccrualsShort" />
					</td>
				</tr>
			</s:if>
			<tr>
				<td colspan="2" class="td_text"><b>Weekly Standard Work Hrs:</b> <s:property value="profile.stWeeklyHrs" />, <b>Weekly Compt Time Earned After Hrs:</b> <s:property value="profile.compTimeAfter" />
				</td>
			</tr>
			<s:if test="document.hasWarnings()">
				<tr>
					<td colspan="2" class="td_text"><br />
						<s:set var="warnings" value="document.warnings" />
						<%@  include file="../warnings.jsp" %>
					</td>
				</tr>
			</s:if>
			<tr><td colspan="3"><hr /></td></tr>
		</s:iterator>
	</table>
</s:if>
<s:if test="hasNoDataEmployees()" >
	<table width="50%">
		<caption>Employees with no time entries</caption>
		<s:iterator value="noDataEmployees" var="one" status="row">
			<tr><td class="td_text" align="right"><s:property value="#row.index+1" /> - </td><td class="td_text">&nbsp;&nbsp;<s:property /></td></tr>
		</s:iterator>
	</table>
</s:if>


