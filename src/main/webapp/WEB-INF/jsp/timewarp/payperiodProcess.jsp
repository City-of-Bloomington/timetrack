<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="payperiodProcess" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="source" value="source" />
<s:if test="hasActionErrors()">
	<div class="errors">
    <s:actionerror/>
	</div>
</s:if>
<s:elseif test="hasActionMessages()">
	<div class="welcome">
    <s:actionmessage/>
	</div>
</s:elseif>
<table width="100%" border="1">
	<tr><td align="center">
		<table width="90%" border="0">
			<tr>
				<td align="right" class="td_text">Pay Period </td>
				<td align="left" class="td_text">&nbsp;&nbsp;<s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" onchange="doRefresh()" />
				</td>
				<td align="right" class="td_text"><a href="<s:property value='#application.url' />paypriodProcess.action?pay_period_id=<s:property value='currentPayPeriod.id' />">Current Pay Period</a></td>
			</tr>
			<tr>
				<td align="right" class="td_text">Department </td>
				<td align="left" class="td_text">&nbsp;&nbsp;<s:select name="department_id" valuw="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" onchange="doRefresh()" /></td>
			</tr>
			<tr>
				<td align="right" class="td_text">Output format </td>
				<td align="left" class="td_text">&nbsp;&nbsp;<s:radio name="outputType" value="%{outputType}" list="#{'html':'html','csv':'csv'}" /></td>
			</tr>
			<tr>
				<td class="th_text">&nbsp;</td>
				<td class="th_text">&nbsp;</td>
				<td class="th_text"><s:submit name="action" type="button" value="Submit" class="fn1-btn"/></td>
			</tr>
		</table>
	</td>
	</tr>
</table>
</s:form>
<s:if test="action != ''">
	<div style="text-align:center">
		<h4><s:property value="department" /> Employees</h4>			
	</div> 		
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
</s:if>
<%@ include file="../footer.jsp" %>


