<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<div class="internal-page container clearfix settings">
	<h1>Unscheduled Hours Report</h1>
	<s:form action="reportUnscheduled" id="form_id" method="post" >
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
		<p>Unscheduled Time Off Report, such as PTOUN and SBUUN (sick hours)
				within one year </p>		
		<li>The date below represent the End Date of the Period, the start date will be determined by subtracting one year before the date set below</li>
		
		<div class="width-one-half float-left">
			<div class="form-group">
				<label>End Date: (mm/dd/yyyy)</label>
				<div class="date-range-picker">
					<div>									
						<s:textfield name="report.date" value="%{report.date}" cssClass="date" size="10" />
					</div>
				</div>
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
			</div>
		</div>
	</s:form>
</div>
<s:if test="action != ''">
	<s:if test="report.hasUnscheduleds()">
		<s:set var="reportTitle" value="reportTitle" />
		<s:set var="hasHeaderTitles" value="'true'" />		
		<s:set var="headerTitles" value="report.headerTitles" />
		<s:set var="unscheduleds" value="report.unscheduleds" />
		<%@ include file="../unscheduledTimes.jsp" %>		
	</s:if>
</s:if>
<%@ include file="../footer.jsp" %>


