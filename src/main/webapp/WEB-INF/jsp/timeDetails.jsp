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

	<div class="container-with-padding">
		<%@ include file="warnings.jsp" %>

		<div class="button-group">
			<s:if test="!document.isSubmitted()">
			  <a href="#" class="button" onclick="return popwit('<s:property value='#application.url' />timeNote?document_id=<s:property value='%{document.id}' />','Notes');">Add Notes</a>
			</s:if>

			<s:if test="document.hasLastWorkflow()" >
			  <s:if test="document.lastWorkflow.canSubmit()">
			    <s:form action="timeAction" id="form_id" method="post" >
			      <input type="hidden" name="source" value="timeDetails" />
			      <s:hidden name="document_id" value="%{document.id}" />
			      <s:hidden name="workflow_id" value="%{document.lastWorkflow.next_workflow_id}" />
			      <s:submit name="action" type="button" class="button" value="Submit for Approval" />
			    </s:form>
			  </s:if>
			</s:if>
		</div>

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
		<%@ include file="nextTimeAction.jsp" %>
		<%@ include file="timeActions.jsp" %>
		<%@ include file="timeNotes.jsp" %>
	</div>
</s:if>
<%@ include file="footer.jsp" %>