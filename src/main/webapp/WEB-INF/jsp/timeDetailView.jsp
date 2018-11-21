<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2018 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="homepage">
	<s:if test="!hasErrors()">
		<s:if test="hasMessages()">
			<s:set var="messages" value="messages" />
			<%@ include file="messages.jsp" %>
		</s:if>
		<div class="viewing-as">
			Time Details View for Employee: <s:property value="%{document.employee}" />
		</div>

		<%@ include file="calendarTopDetails.jsp" %>
		<s:if test="showAllJobs()">
			<s:set var="dailyBlocks" value="mjdoc.dailyBlocks" />
			<s:set var="daily" value="mjdoc.daily" />
			<s:set var="payPeriodTotal" value="mjdoc.payPeriodTotal" />
		</s:if>
		<s:else>		
			<s:set var="dailyBlocks" value="document.dailyBlocks" />
			<s:set var="daily" value="document.daily" />
			<s:set var="payPeriodTotal" value="document.payPeriodTotal" />
		</s:else>
		<%@ include file="calendarFullView.jsp" %>
		
		<div class="container-with-padding">
			<div class="calendar-summary-controls m-b-40">
				<a class="button pay-notes" data-doc-id="<s:property value='%{document.id}' />">Add Pay Period Note</a>
			</div>

			<!--  we need these as global since they will be used in daily -->
			<s:set var="week1DateRange" value="payPeriod.week1DateRange" />
			<s:set var="week2DateRange" value="payPeriod.week2DateRange" />

			<hr class="m-b-40" />



			<s:if test="document.isUnionned()">
			  <s:set var="unionned" value="'true'" />
			  <s:set var="week1Flsa" value="document.week1_flsa" />
			  <s:set var="week2Flsa" value="document.week2_flsa" />
			</s:if>
			<s:else>
			  <s:set var="unionned" value="'false'" />
			</s:else>

			<h1>Pay Period Summary</h1>
			<s:if test="showAllJobs()">
				<s:if test="mjdoc.hasWarnings()">
				<s:set var="warnings" value="mjdoc.warnings" />
					<%@ include file="warnings.jsp" %>
				</s:if>
			</s:if>
			<s:else>
				<s:if test="document.hasWarnings()">
					<s:set var="warnings" value="document.warnings" />
					<%@ include file="warnings.jsp" %>
				</s:if>
			</s:else>
			<%@ include file="dailySummary.jsp" %>

				<div class="d-flex">
					<s:if test="showAllJobs()">					
						<s:if test="mjdoc.hasHourCodeWeek1()">
							<s:set var="weeklyHourCodes" value="mjdoc.hourCodeWeek1" />
							<s:set var="weekTotal" value="mjdoc.week1Total" />
							<s:set var="weeklyTitle" value="'Week 1 (Hour Codes)'" />
							<s:set var="whichWeek" value="'week-one'" />
							<%@ include file="weeklySummary.jsp" %>
						</s:if>
						<s:if test="mjdoc.hasHourCodeWeek2()">
							<s:set var="weeklyHourCodes" value="mjdoc.hourCodeWeek2" />
							<s:set var="weekTotal" value="mjdoc.week2Total" />
							<s:set var="weeklyTitle" value="'Week 2 (Hour Codes)'" />
							<s:set var="whichWeek" value="'week-two'" />
							<%@ include file="weeklySummary.jsp" %>
						</s:if>
					</s:if>
					<s:else>
						<s:if test="document.hasHourCodeWeek1()">
							<s:set var="weeklyHourCodes" value="document.hourCodeWeek1" />
							<s:set var="weekTotal" value="document.week1Total" />
							<s:set var="weeklyTitle" value="'Week 1 (Hour Codes)'" />
							<s:set var="whichWeek" value="'week-one'" />
							<%@ include file="weeklySummary.jsp" %>
						</s:if>
						<s:if test="document.hasHourCodeWeek2()">
							<s:set var="weeklyHourCodes" value="document.hourCodeWeek2" />
							<s:set var="weekTotal" value="document.week2Total" />
							<s:set var="weeklyTitle" value="'Week 2 (Hour Codes)'" />
							<s:set var="whichWeek" value="'week-two'" />
							<%@ include file="weeklySummary.jsp" %>
						</s:if>
					</s:else>
			</div>

			<%@ include file="accrualSummary.jsp" %>
			<%@ include file="timeIssues.jsp" %>

			<%@ include file="timeActions.jsp" %>
			<%@ include file="timeNotes.jsp" %>
			<br />
		</div>
	</s:if>
</div>

<!-- jQuery Dialog Modal: Add Pay Period Note -->
<div class="modal pay-notes" class="timetrack" style="display: none;"></div>

<%@ include file="footer.jsp" %>
