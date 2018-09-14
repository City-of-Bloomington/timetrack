<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2018 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:if test="!hasErrors()">
	<%@ include file="calendarTopDetails.jsp" %>
	<%@ include file="strutMessages.jsp" %>
	<s:if test="document.isProcessed()">
		<%@ include file="calendarFullView.jsp" %>
	</s:if>
	<s:else>
		<%@ include file="calendarFullNew.jsp" %>
	</s:else>
	<div class="container-with-padding">

		<div class="calendar-summary-controls m-b-40">
			<a class="button pay-notes" data-doc-id="<s:property value='%{document.id}' />">Add Pay Period Note</a>

			<s:if test="document.hasLastWorkflow()" >
			  <s:if test="document.lastWorkflow.canSubmit()">
					<!-- To-Do:
						The submit button should be disabled unless the
						amount of hours for each week are suitable for submit. -->
					<s:form action="timeAction" id="form_id" class="timesheet-submit" method="post" >
			      <input type="hidden" name="source" value="timeDetails" />
			      <s:hidden name="document_id" value="%{document.id}" />
			      <s:hidden name="workflow_id" value="%{document.lastWorkflow.next_workflow_id}" />
			      <s:submit
			      	 name="action"
			      	 type="submit"
			      	 class="button"
			      	 value="Submit for Approval"
			      	 data-week-one-total="%{document.week1Total}"
			      	 data-week-two-total="%{document.week2Total}" />
			    </s:form>
			  </s:if>
			  <s:else>
			  	<strong>Submitted on: <s:property value="document.submitTimeAction.action_time" /></strong>
				</s:else>
			</s:if>
		</div>

		<!--  we need these as global since they will be used in daily -->
		<s:set var="week1DateRange" value="payPeriod.week1DateRange" />
		<s:set var="week2DateRange" value="payPeriod.week2DateRange" />		

		<hr class="m-b-40" />

		<s:set var="payPeriodTotal" value="document.payPeriodTotal" />
		<s:set var="daily" value="document.daily" />
		<s:set var="week1Total" value="document.week1Total" />
		<s:set var="week2Total" value="document.week2Total" />
		<s:if test="document.isUnionned()">
		  <s:set var="unionned" value="'true'" />
		  <s:set var="week1Flsa" value="document.week1_flsa" />
		  <s:set var="week2Flsa" value="document.week2_flsa" />
		</s:if>
		<s:else>
		  <s:set var="unionned" value="'false'" />
		</s:else>

		<h1>Pay Period Summary</h1>
		<s:if test="document.hasWarnings()">
			<s:set var="warnings" value="document.warnings" />
			<%@ include file="warnings.jsp" %>
		</s:if>
		<%@ include file="dailySummary.jsp" %>


			<div class="d-flex">
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
		</div>

		<%@ include file="accrualSummary.jsp" %>
		<%@ include file="timeIssues.jsp" %>

		<%@ include file="timeActions.jsp" %>
		<%@ include file="timeNotes.jsp" %>
		<br />
		<li><a href="<s:property value='#application.url' />timeBlockLog.action?document_id=<s:property value='document.id' />"> Time block history</a></li>
		<br />
	</div>
</s:if>

<!-- jQuery Dialog Modal: Add Pay Period Note -->
<div class="modal pay-notes" class="timetrack" style="display: none;"></div>

<%@ include file="footer.jsp" %>
