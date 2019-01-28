<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="reportPlan" id="form_id" method="post" >
	<h4>Planning MPO Report</h4>
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
		<tr>
			<td align="center">
				<table width="90%" border="0">
					<tr>
						<td colspan="2" class="td_text">You can quary time details by choosing either the year and quarter or by entering date range.</td>
					</tr>
					<tr>
						<td align="right" class="th_text">Quarter Selection: </td>
						<td align="left" class="td_text">&nbsp;&nbsp;<s:select name="report.quarter" value="%{report.quarter}" list="#{'-1':'Pick quarter','1':'First','2':'Second','3':'Third','4':'Forth'}" /> Uear:<s:select name="report.year" value="%{report.year}" list="years" headerKey="-1" headerValue="Pick Year" /></td>
					</tr>
					<tr>
						<td align="right" class="th_text">Date, from: </td>
						<td align="left" class="td_text"><s:textfield name="report.date_from" value="%{report.date_from}" cssClass="date" size="10" /> to:  
							<s:textfield name="report.date_to" value="%{report.date_to}" cssClass="date" size="10" />
						</td>
					</tr>
					<tr>
						<td colspan="2" class="td_text"><br />For output type, we suggest that you run the 'Web page HTML' type first so that you get an idea about the numbers. If you are OK with these numbers then you choose the 'CSV' type.</td>
					</tr>				
					<tr>
						<td align="right" class="th_text">Output Type:</td>				
						<td align="left" class="td_text"><s:radio name="report.type" value="%{report.type}" list="#{'html':'Web page HTML','csv':'CSV format'}" /></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="th_text" align="center"><s:submit name="action" type="button" value="Submit" class="fn1-btn"/></td>
		</tr>
	</table>
</s:form>		
<s:if test="action != ''">
	<s:if test="report.hasEntries()">
		<s:set var="mapEntries" value="report.mapEntries" />
		<s:set var="hoursSums" value="report.hoursSums" />
		<s:set var="amountsSums" value="report.amountsSums" />
		<s:set var="totalHours" value="report.totalHours" />
		<s:set var="totalAmount" value="report.totalAmount" />
		<s:set var="reportTitle" value="reportTitle" />
		<%@  include file="mpoReport.jsp" %>
	</s:if>
</s:if>
<%@ include file="../footer.jsp" %>


