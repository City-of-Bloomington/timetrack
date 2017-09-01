<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="payrollProcess" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h3>Payroll Process Approval</h3>
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
	<s:if test="hasGroups()">	
	<table width="100%" border="0">
		<tr><td align="left">
			<b>Pay Period </b><s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Period" onchange="doRefresh()" /> </td>
			<td align="right"><a href="<s:property value='#application.url' />approve.action?pay_period_id=<s:property value='currentPayPeriod.id' />">Current Pay Period</a></td>
		</tr>
		<tr>
			<td><b>Group: </b>
				<s:select name="group_id" value="%{group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" onchange="doRefresh()" /> </td>
			<td align="right"><b> Payroll Processor: </b><s:property value="employee" /></td>
		</tr>
	</table>
	<s:if test="hasDocuments()">
		<table width="100%">				
					<s:iterator var="one" value="documents">				
						<s:if test="hasDaily()">
							<tr>
								<td valign="center" width="10%">
									<s:property value="employee" />
									<a href="<s:property value='#application.url' />timeDetails.action?document_id=<s:property value='id' />&source=payrollProcess" />Details</a>
								</td>
								<td valign="center">
									<s:set var="daily" value="daily" />
									<s:set var="week1Total" value="week1Total" />
									<s:set var="week2Total" value="week2Total" />
									<s:set var="payPeriodTotal" value="payPeriodTotal" />
									<%@  include file="dailySummary.jsp" %>
								</td>
								<td valign="center" width="10%" align="right">
									<s:if test="canBeProcessed()">
										<s:if test="isUserCurrentEmployee()">										
											<input type="checkbox" name="document_ids" value="<s:property value='id' />">Payroll Process Approve</input>
										</s:if>
										<s:else>
											Ready to Payroll Process
										</s:else>
									</s:if>									
									<s:elseif test="!isApproved()">Not Approved</s:elseif>
									<s:else>
										Not Submitted 
									</s:else>
								</td>
							</tr>
							<s:if test="hasTimeIssues()">
								<s:set var="timeIssuesTitle" value="'Outstanding Issues'" />	
								<s:set var="timeIssues" value="timeIssues" />
								<tr>
									<td>&nbsp;</td>
									<td>							
										<%@  include file="timeIssues.jsp" %>
									</td>
									<td>&nbsp;</td>
								</tr>									
							</s:if>							
							<s:if test="isSubmitted() && hasWarnings()">
								<tr>
									<td>&nbsp;</td>
									<td>
										<s:set var="warnings" value="warnings" />
										<%@  include file="warnings.jsp" %>
									</td>
									<td>&nbsp;</td>
								</tr>
							</s:if>
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
			<s:if test="isUserCurrentEmployee()">
				<s:submit name="action" type="button" value="Payroll Process Approve" class="fn1-btn"/>
			</s:if>
			<s:else> 
				Only authorized can payroll process
			</s:else>
	</s:if>
</s:form>

<%@  include file="footer.jsp" %>


