<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="timeDetails" id="form_id" method="post" >
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
<table width="100%" border="0">
	<tr>
		<td align="left">
			<b> <s:property value="payPeriod.monthNames" /> <s:property value="payPeriod.startYear" /></b>
		</td>
		<td align="right">
			<b> Employee: </b><s:property value="document.employee" />
		</td>
	</tr>
	<s:if test="!isUserCurrentEmployee()">
		<s:if test="source != ''">
			<tr>
				<td>&nbsp;</td>
				<td align="right">
					<a href="<s:property value='#application.url' /><s:property value='source' />.action">Back to Main Page</a>
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<td>&nbsp;</td>
				<td align="right">
					<a href="<s:property value='#application.url' />switch.action?employee_id=<s:property value='user.employee_id' />&action=Change">Back to Main User</a>
				</td>
			</tr>
		</s:else>
	</s:if>
	<tr>
		<td align="left"><b>Pay Period</b>
			<s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" onchange="doRefresh()" />
		</td>
		<td align="right"><a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='currentPayPeriod.id' />">Current Pay Period</a></td>
	</tr>
</table>
</s:form>
<table width="100%" border="1">
	<tr>
		<td width="14%">Mon</td>
		<td width="14%">Tue</td>
		<td width="14%">Wed</td>
		<td width="14%">Thu</td>
		<td width="14%">Fri</td>
		<td width="14%">Sat</td>
		<td>Sun</td>
	</tr>
	<tr>
		<s:iterator value="document.dailyBlocks" var="block" >
			<s:set var="blockKey" value="#block.key" />
			<s:set var="blockList" value="#block.value" />
			<s:if test="#blockKey == 7">
	       </tr><tr>
			</s:if>
			<s:iterator value="#blockList" status="row" >
				<s:if test="#row.first">
					<td valign="top" style="height:100px;text-align:left">						
						<a href="#" onclick="return popwit('<s:property value='#application.url' />timeBlock?document_id=<s:property value='document_id' />&date=<s:property value='date' />','timeBlock');">
							<s:property value="dayInt" />
						</a>
				</s:if>
				<s:if test="hasData()">
					<table border="0" width="100%">			
						<tr>
							<td align="right">
								<a href="<s:property value='#application.url' />timeBlock?id=<s:property value='id' />&action=Delete">(X)</a>
							</td>
						</tr>
						<tr>
							<td align="left">
								<a href="#" onclick="return popwit('<s:property value='#application.url' />timeBlock?id=<s:property value='id' />','timeBlock');">
									<s:if test="isHourType()">
										<s:property value="hours" />
									</s:if>
									<s:else>
										<s:property value="time_in" /> - <s:property value="time_out" /><br />								
										<s:property value="hours" />
									</s:else>
									<s:property value="hour_code" />
								</a>
							</td>
						</tr>
					</table>												
				</s:if>
			</s:iterator>
					</td>
		</s:iterator>
	</tr>
</table>

<s:set var="daily" value="document.daily" />
<s:set var="week1Total" value="document.week1Total" />
<s:set var="week2Total" value="document.week2Total" />
<s:set var="payPeriodTotal" value="document.payPeriodTotal" />
<%@  include file="dailySummary.jsp" %>

<s:if test="document.hasHourCodeWeek1()">
	<s:set var="weeklyHourCodes" value="document.hourCodeWeek1" />
	<s:set var="weekTotal" value="document.week1Total" />
	<s:set var="weeklyTitle" value="'Hour Codes Week 1 Total'" />
	<%@  include file="weeklySummary.jsp" %>
</s:if>
<s:if test="document.hasHourCodeWeek2()">
	<s:set var="weeklyHourCodes" value="document.hourCodeWeek2" />
	<s:set var="weekTotal" value="document.week2Total" />
	<s:set var="weeklyTitle" value="'Hour Codes Week 2 Total'" />
	<%@  include file="weeklySummary.jsp" %>
</s:if>
<s:if test="document.hasLastWorkflow()" >
	<s:if test="document.lastWorkflow.canSubmit()">
		<s:form action="timeAction" id="form_id" method="post" >
			<input type="hidden" name="source" value="timeDetails" />			
			<s:hidden name="document_id" value="%{document.id}" />
			<s:hidden name="workflow_id" value="%{document.lastWorkflow.next_workflow_id}" />
			<p style="text-align:center">
				<s:submit name="action" type="button" value="Submit for Approval" />
			</p>
		</s:form>
	</s:if>
</s:if>
<s:if test="!document.isSubmitted()">
	<a href="#" onclick="return popwit('<s:property value='#application.url' />timeNote?document_id=<s:property value='%{document.id}' />','Notes');">Add Notes</a>
</s:if>
<s:if test="document.hasAllAccruals()">
	<s:set var="allAccruals" value="document.allAccruals" />
	<%@  include file="accrualSummary.jsp" %>
</s:if>
<s:if test="document.isSubmitted() && document.hasWarnings()">
	<s:set var="warnings" value="document.warnings" />
	<%@  include file="warnings.jsp" %>
</s:if>
<s:if test="document.hasTimeIssues()">
	<s:set var="timeIssuesTitle" value="'Outstanding Issues'" />	
	<s:set var="timeIssues" value="document.timeIssues" />
	<%@  include file="timeIssues.jsp" %>
</s:if>
<s:if test="document.hasTimeNotes()">
	<s:set var="timeNotesTitle" value="'Pay Period Notes'" />	
	<s:set var="timeNotes" value="document.timeNotes" />
	<%@  include file="timeNotes.jsp" %>
</s:if>
<s:if test="document.hasTimeActions()">
	<s:set var="timeActions" value="document.timeActions" />
	<s:set var="timeActionsTitle" value="'Time Action History'" />
	<%@  include file="timeActions.jsp" %>
</s:if>
<s:if test="document.hasLastWorkflow()">
	<s:if test="document.lastWorkflow.hasNextNode()">
		<%@  include file="nextTimeAction.jsp" %>
	</s:if>
</s:if>
<%@ include file="footer.jsp" %>


