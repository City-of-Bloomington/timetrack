<h1>
	<s:property value="department" /> Employees
</h1>

<s:if test="hasEmpsWithNoEmpNum()">
	<p>The following employees Do not have employee number and will not be imported <br />
		You may add their employees' number and run this process again.
	</p>
	<s:set var="employees" value="empsWithNoEmpNum" />
	<%@ include file="../employees.jsp" %>
</s:if>

<s:if test="hasNoDataEmployees()" >
	<h2>Employees with no time entries</h2>
	<ul class="no-data">
		<s:iterator value="noDataEmployees" var="one" status="row">
			<li><s:property />,</li>
		</s:iterator>
	</ul>
</s:if>

<s:if test="hasEmployeeRuns()">
	<s:iterator value="employeeRuns" var="empRun" >
		<s:set var="emp" value="#empRun.key" />
		<s:set var="tmwrpRuns" value="#empRun.value" />

		<s:iterator value="#tmwrpRuns" var="tmwrpRun" >

			<div class="emp-runs">
				<div class="title-row">
					<h1>
						<s:property value="#emp" /> (<s:property value="#emp.employee_number " />)
					</h1>

					<h2>
						Job: <s:property value="#tmwrpRun.document.job" />
					</h2>
				</div>

				<div class="weeks-run">
					<s:if test="#tmwrpRun.hasWeek1Rows()">
						<table>
							<tr>
								<th>Earn Code</th>
								<th>Hours</th>
								<th>Amount</th>
							</tr>

							<tr>
								<td colspan="3" class="th_text">Week 1</td>
							</tr>

							<s:iterator value="#tmwrpRun.week1Rows" var="row" >
								<tr>
									<s:iterator value="#row" var="col" status="status" >
										<s:if test="#status.first">
											<td><s:property /></td>
										</s:if>
										<s:else>
											<td align="right"><s:property /></td>
										</s:else>
									</s:iterator>
								</tr>
							</s:iterator>
						</table>
					</s:if>

					<s:if test="#tmwrpRun.hasWeek2Rows()">
						<table>
							<tr>
								<th>Earn Code</th>
								<th>Hours</th>
								<th>Amount</th>
							</tr>

							<tr>
								<td colspan="3" class="th_text">Week 2</td>
							</tr>

							<s:iterator value="#tmwrpRun.week2Rows" var="row" >
								<tr>
									<s:iterator value="#row" var="col" status="status">
										<s:if test="#status.first">
											<td><s:property /></td>
										</s:if>
										<s:else>
											<td align="right"><s:property /></td>
										</s:else>
									</s:iterator>
								</tr>
							</s:iterator>

							<s:if test="#tmwrpRun.hasCycleRow()">
								<tr>
									<s:iterator value="#tmwrpRun.cycleRow" status="status">
										<s:if test="#status.first">
											<td>
												<strong><s:property /></strong>
											</td>
										</s:if>
										<s:else>
											<td style="text-align: right;">
												<strong><s:property /></strong>
											</td>
										</s:else>
									</s:iterator>
								</tr>
							</s:if>
						</table>
					</s:if>
				</div>

				<s:if test="#tmwrpRun.document.hasTimeActions()">
					<s:set var="timeActions" value="#tmwrpRun.document.timeActions" />
					<s:set var="timeActionsTitle" value="' '" />
					<%@  include file="../timeActions.jsp" %>
				</s:if>

				<table class="last-workflow">
					<s:if test="#tmwrpRun.document.hasLastWorkflow()">
						<tr>
							<td class="td_text" colspan="2">
								<b>Status:</b> <s:property value="#tmwrpRun.document.LastWorkflow.node.annotation" />
							</td>
						</tr>
					</s:if>

					<s:if test="#tmwrpRun.document.hasAllAccruals()">
						<tr>
							<td colspan="2" class="td_text"><b> Available Accruals: </b>
								<s:property value="#tmwrpRun.document.employeeAccrualsShort" />
							</td>
						</tr>
					</s:if>

					<tr>
						<td colspan="2" class="td_text">
							<b>Weekly Standard Work Hours:</b> <s:property value="#tmwrpRun.document.job.weekly_regular_hours" />
						</td>
					</tr>

					<tr>
						<td colspan="2" class="td_text">
							<b>Weekly Comp Time Earned After Hours:</b> <s:property value="#tmwrpRun.document.job.comp_time_weekly_hours" />
						</td>
					</tr>
				</table>
			</div>
		</s:iterator>
	</s:iterator>
</s:if>