<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page container clearfix settings">
	<h1>Parks Employees and Jobs Report</h1>
	<div class="width-one-half float-left">
		
		<s:form action="parksJobReport" id="form_id" method="post" >
			<s:if test="hasMessages()">
				<s:set var="messages" value="messages" />			
				<%@ include file="../messages.jsp" %>
			</s:if>
			<s:elseif test="hasErrors()">
				<s:set var="errors" value="errors" />			
				<%@ include file="../errors.jsp" %>
			</s:elseif>
			<table class="width-full">
				<tr>
					<td align="center">
						<table width="90%" border="0">
							<tr>
								<td>Employment Type </td>
								<td><s:select name="employmentType" value="%{employmentType}" list="#{'-1':'All','Temp':'Temp Employee Only','Non Temp':'All Others'}" /> </td>
							</tr>
							<tr>
								<td>Output Type:</td>				
								<td><s:radio name="outputType" value="%{outputType}" list="#{'html':'Web page format','csv':'CSV format'}" /></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td><s:submit name="action" type="button" value="Submit"/></td>
				</tr>
			</table>
		</s:form>
	</div>		
	<s:if test="action != ''">
		<s:if test="hasJobs()">
			<s:set var="jobs" value="jobs" />
			<s:set var="jobsTitle" value="reportTitle" />
			<%@  include file="../jobsShort.jsp" %>
		</s:if>
	</s:if>
	<%@ include file="../footer.jsp" %>
</div>

