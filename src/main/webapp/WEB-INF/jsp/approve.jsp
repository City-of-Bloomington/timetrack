<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="approve" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
<h4>Approve Employees' Times</h4>
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
		<tr><td align="left" class="th_text">
			<b>Pay Period </b><s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Period" onchange="doRefresh()" /> </td>
			<td align="right" class="td_text"><a href="<s:property value='#application.url' />dataEntry.action?pay_period_id=<s:property value='currentPayPeriod.id' />">Current Pay Period</a></td>
		</tr>
		<tr>
			<td class="th_text">
				<s:if test="hasMoreThanOneGroup()">
					<b>Group: </b>
					<s:select name="group_id" value="%{group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" onchange="doRefresh()" />
				</s:if>
			</td>
			<td align="right" class="td_text"><b> Approver: </b><s:property value="employee.full_name" /></td>
		</tr>
	</table>
	<s:if test="hasDocuments()">
		<table width="100%">				
			<s:iterator var="one" value="documents">				
				<s:if test="hasDaily()">
					<tr>
						<td valign="center" width="13%" class="th_text">
							<s:property value="employee" /><br />
							<a href="<s:property value='#application.url' />timeDetails.action?document_id=<s:property value='id' />&source=approve" />Details</a>
						</td>
						<td valign="center">
							<s:set var="daily" value="daily" />
							<s:set var="week1Total" value="week1Total" />
							<s:set var="week2Total" value="week2Total" />
							<s:set var="payPeriodTotal" value="payPeriodTotal" />
							<%@  include file="dailySummary.jsp" %>
						</td>
						<td valign="center" width="12%" align="right" class="td_text">
							<s:if test="canBeApproved()">
								<s:if test="isUserCurrentEmployee()">										
									<input type="checkbox" name="document_ids" value="<s:property value='id' />">Approve</input>
								</s:if>
								<s:else>
									Ready for Approval
								</s:else>
							</s:if>									
							<s:elseif test="isApproved()">Approved</s:elseif>
							<s:elseif test="isProcessed()">Processed</s:elseif>
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
		<table width="80%" border="0">
			<caption>Employee(s) with no time entry for this pay period</caption>
			<s:iterator var="one" value="nonDocEmps">
				<tr>
					<td width="20%" class="th_text">&nbsp;</td>
					<td class="td_text"><a href="<s:property value='#application.url' />timeDetails.action?employee_id=<s:property value='id' />&pay_period_id=<s:property value='pay_period_id' />&source=approve"> <s:property value="full_name" /></a></td>
				</tr>
			</s:iterator>
		</table>
	</s:if>
	<s:if test="isUserCurrentEmployee()">
		<s:submit name="action" type="button" value="Approve" class="fn1-btn"/>
	</s:if>
	<s:else> 
		Only authorized can approve
	</s:else>			
</s:if>
</s:form>

<%@  include file="footer.jsp" %>


