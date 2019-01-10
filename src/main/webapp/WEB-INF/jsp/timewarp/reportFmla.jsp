<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="reportFmla" id="form_id" method="post" >
	<h4>FMLA Report</h4>
	<s:if test="hasErrors()">
		<div class="errors">
			<s:set var="errors" value="%{errors}" />			
			<%@ include file="../errors.jsp" %>
		</div>
	</s:if>
	<s:elseif test="hasMessages()">
			<s:set var="messages" value="%{messages}" />			
			<%@ include file="../messages.jsp" %>
	</s:elseif>
	<table width="100%" border="1">
		<tr>
			<td align="center">
				<table width="90%" border="0">
					<s:if test="hasDepts()">
						<tr>
							<td align="right" class="th_text">Department </td>
							<td align="left" class="td_text"><s:select name="report.department_id" value="%{report.department_id}" list="depts" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Dept" required="true" /> </td>
						</tr>
					</s:if>					
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
		<s:set var="hasEntries" value="'true'" />
		<s:set var="reportTitle" value="reportTitle" />		
		<s:set var="mapEntries" value="report.mapEntries" />
		<s:set var="hoursSums" value="report.hoursSums" />
		<s:set var="totalHours" value="report.totalHours" />
	</s:if>
	<s:if test="report.hasDailyEntries()">
		<s:set var="hasDaily" value="'true'" />
		<s:set var="dailyEntries" value="report.dailyEntries" />		
	</s:if>
	<s:if test="report.hasAnyEntries()">
		<%@  include file="fmlaReport.jsp" %>
	</s:if>
</s:if>
<%@ include file="../footer.jsp" %>


