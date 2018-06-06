<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2018 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
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

<s:if test="!hasActionErrors()">
	<%@ include file="calendarTopDetails.jsp" %>

	<%@ include file="calendarFullNew.jsp" %>

	<s:if test="document.hasWarnings()">
		<h1>butcherad testing: hasWarnings</h1>
		<s:set var="warnings" value="document.warnings" />
		<%@ include file="warnings.jsp" %>
	</s:if>

	<!-- Is this being used?
		<s:set var="daily" value="document.daily" /> -->

	<s:if test="document.isUnionned()">
		<h1>butcherad testing: isUnionned</h1>
		<s:set var="unionned" value="'true'" />
		<s:set var="week1Flsa" value="document.week1_flsa" />
		<s:set var="week2Flsa" value="document.week2_flsa" />
	</s:if>
	<s:else>
		<h1>butcherad testing: !isUnionned</h1>
		<s:set var="unionned" value="'false'" />
	</s:else>

	<s:set var="payPeriodTotal" value="document.payPeriodTotal" />
	<h1>butcherad testing: dailySummary</h1>
	<%@ include file="dailySummary.jsp" %>

	<s:if test="document.hasHourCodeWeek1()">
		<h1>butcherad testing: hasHourCodeWeek1</h1>
		<s:set var="weeklyHourCodes" value="document.hourCodeWeek1" />
		<s:set var="week1Total" value="document.week1Total" />
		<s:set var="weekTotal" value="document.week1Total" />
		<s:set var="weeklyTitle" value="'Hour Codes Week 1 Total'" />
		<%@ include file="weeklySummary.jsp" %>
	</s:if>

	<s:if test="document.hasHourCodeWeek2()">
		<h1>butcherad testing: hasHourCodeWeek2</h1>
		<s:set var="weeklyHourCodes" value="document.hourCodeWeek2" />
		<s:set var="week2Total" value="document.week2Total" />
		<s:set var="weekTotal" value="document.week2Total" />
		<s:set var="weeklyTitle" value="'Hour Codes Week 2 Total'" />
		<%@ include file="weeklySummary.jsp" %>
	</s:if>

	<s:if test="document.hasLastWorkflow()" >
		<h1>butcherad testing: hasLastWorkflow</h1>
		<s:if test="document.lastWorkflow.canSubmit()">
			<s:form action="timeAction" id="form_id" method="post" >
				<input type="hidden" name="source" value="timeDetails" />
				<s:hidden name="document_id" value="%{document.id}" />
				<s:hidden name="workflow_id" value="%{document.lastWorkflow.next_workflow_id}" />
				<s:submit name="action" type="button" value="Submit for Approval" cssClass="button_link" />
			</s:form>
		</s:if>
	</s:if>

	<s:if test="!document.isSubmitted()">
		<h1>butcherad testing: isSubmitted</h1>
		<a href="#" onclick="return popwit('<s:property value='#application.url' />timeNote?document_id=<s:property value='%{document.id}' />','Notes');">Add Notes</a>
	</s:if>

	<s:if test="document.hasAllAccruals()">
		<h1>butcherad testing: hasAllAccruals</h1>
		<s:set var="allAccruals" value="document.allAccruals" />
		<%@ include file="accrualSummary.jsp" %>
	</s:if>

	<s:if test="document.hasTimeIssues()">
		<h1>butcherad testing: hasTimeIssues</h1>
		<s:set var="timeIssuesTitle" value="'Outstanding Issues'" />
		<s:set var="timeIssues" value="document.timeIssues" />
		<%@ include file="timeIssues.jsp" %>
	</s:if>

	<s:if test="document.hasTimeNotes()">
		<h1>butcherad testing: hasTimeNotes</h1>
		<s:set var="timeNotesTitle" value="'Pay Period Notes'" />
		<s:set var="timeNotes" value="document.timeNotes" />
		<%@ include file="timeNotes.jsp" %>
	</s:if>

	<s:if test="document.hasTimeActions()">
		<h1>butcherad testing: hasTimeActions</h1>
		<s:set var="timeActions" value="document.timeActions" />
		<s:set var="timeActionsTitle" value="'Action History'" />
		<%@ include file="timeActions.jsp" %>
	</s:if>

	<s:if test="document.hasLastWorkflow()">
		<h1>butcherad testing: hasLastWorkflow</h1>
		<s:if test="document.lastWorkflow.hasNextNode()">
			<%@ include file="nextTimeAction.jsp" %>
		</s:if>
	</s:if>
</s:if>
<%@ include file="footer.jsp" %>