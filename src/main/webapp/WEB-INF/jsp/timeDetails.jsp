<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2018 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<script>
var myTimer = setInterval(loginTimer, 7200000); // 2 hours
function loginTimer() {
	// alert("Session Timeout you are going to be redirected to login page ");
  window.location = "<s:property value='#application.url' />Login";
} 
</script>
<div class="homepage">
	<s:if test="hasErrors()">
		<s:set var="errors" value="%{errors}" />
		<%@ include file="errors.jsp" %>
	</s:if>
	<s:if test="!hasErrors()">
	    <s:if test="hasMessages()">
		<s:set var="messages" value="%{messages}" />
		<%@ include file="messages.jsp" %>
	    </s:if>
	    <s:if test="hasAnnuance()">
		<s:if test="annuance.hasUrl()">
		    <b> <a href="<s:property value='annuance.annuanceUrl' />"><s:property value="annuance.annuanceText"/></a></b>
		</s:if>
		<s:else>
		    <b><s:property value="annuance.annuanceText"/></b>
		</s:else>
	    </s:if>
	    <s:if test="document.hasDailyBlocks()">
		<s:set var="dailyBlocks" value="document.dailyBlocks" />
		<s:set var="dayBlocksMap" value="document.dayBlocksMap" />
	    </s:if>
	    <s:if test="isNotEditable()">
		<div class="alert"><p><b>Note:</b> Time Details (View Only)</p></div>
		<%@ include file="calendarTopDetails.jsp" %>
		<%@ include file="calendarFullView.jsp" %>
	    </s:if>
	    <s:else>
		<%@ include file="calendarTopDetails.jsp" %>
		<%@ include file="calendarFullNew.jsp" %>
	    </s:else>

	    <div class="time-details">
		<div class="calendar-summary-controls m-b-40">
		    <a class="button pay-notes" data-doc-id="<s:property value='%{document.id}' />">Add Pay Period Note</a>
		    
		    <s:if test="document.hasLastWorkflow()" >
			<s:if test="document.lastWorkflow.canSubmit()">
			    <s:if test="isUserCurrentEmployee() || user.canApprove() || user.canProcess()">
				<form action="<s:property value='#application.url' />timeAction" id="form_id" class="timesheet-submit" method="post" >
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
				</form>
			    </s:if>
			    <s:else>
				<div>
				    <strong>&nbsp; Not submitted yet&nbsp;</strong>
				</div>
			    </s:else>
			</s:if>
			<s:else>
			    <!--
			    <s:if test="canUserRequestChange()">
				<div>
				    <a class="button" href="<s:property value='#application.url'/>/requestCancel?document_id=<s:property value='%{document.id}' />">Request Entry Change</a>
				</div>
			    </s:if>
			    -->
			    <div class="submitted-on">
				<strong>Submitted on:&nbsp;<s:property value="document.submitTimeAction.action_time" />&nbsp;&nbsp;</strong>
			    </div>
			</s:else>
		    </s:if>
		</div>
		<s:set var="week1DateRange" value="payPeriod.week1DateRange" />
		<s:set var="week2DateRange" value="payPeriod.week2DateRange" />
		
		<hr class="m-b-40" />
		<s:if test="hasMultipleJobs()">
		    <s:set var="payPeriodTotal" value="mjdoc.payPeriodTotal" />
		    <s:set var="payPeriodAmount" value="mjdoc.payPeriodAmount" />
		    <s:set var="daily" value="mjdoc.daily" />
		    <s:set var="week1Total" value="mjdoc.week1Total" />
		    <s:set var="week2Total" value="mjdoc.week2Total" />
		    
		    <s:if test="document.isUnionned()">
			<s:set var="unionned" value="'true'" />
			<s:set var="week1Flsa" value="mjdoc.week1_flsa" />
			<s:set var="week2Flsa" value="mjdoc.week2_flsa" />
		    </s:if>
		    <s:else>
			<s:set var="unionned" value="'false'" />
		    </s:else>
		</s:if>
		<s:else>
		    <s:set var="payPeriodTotal" value="document.payPeriodTotal" />
		    <s:set var="payPeriodAmount" value="document.payPeriodAmount" />
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
		</s:else>
		<s:if test="document.hasLeaveRequests()">
		    <h1>Approved Leave in this Pay Period </h1>
		    <s:set var="leave_requests" value="document.leaveRequests" />
		    <%@ include file="leaves_approved.jsp" %>
		</s:if>
		<h1>Pay Period Summary</h1>
		<s:if test="hasMultipleJobs()">
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
		<div class="monetary-hours-tables">											
		    <s:if test="hasMultipleJobs()">
			<s:if test="showAllJobs()">
			    <s:if test="mjdoc.hasDocuments()">
				<s:iterator var="doc" value="mjdoc.documents">
				    <s:if test="#doc.hasTmwrpRun()">
					<s:if test="#doc.tmwrpRun.hasWeek1Rows()">
					    <s:set var="rows" value="#doc.tmwrpRun.week1Rows" />
					    <s:set var="weeklyTitle" value="'Week 1 (Earn Codes)'" />
					    <s:set var="whichWeek" value="'week-one'" />
					    <%@ include file="weeklyTmwrp.jsp" %>
					</s:if>
					<s:if test="#doc.tmwrpRun.hasWeek2Rows()">
					    <s:set var="rows" value="#doc.tmwrpRun.week2Rows" />
					    <s:set var="weeklyTitle" value="'Week 2 (Earn Codes)'" />
					    <s:set var="whichWeek" value="'week-one'" />
					    <%@ include file="weeklyTmwrp.jsp" %>
					</s:if>
				    </s:if>
				</s:iterator>
			    </s:if>
			</s:if>
			<s:else> <!-- multiple jobs but show one only -->
			    <s:if test="document.hasTmwrpRun()">
				<s:if test="document.tmwrpRun.hasWeek1Rows()">
				    <s:set var="rows" value="document.tmwrpRun.week1Rows" />
				    <s:set var="weeklyTitle" value="'Week 1 (Earn Codes)'" />
				    <s:set var="whichWeek" value="'week-one'" />
				    <%@ include file="weeklyTmwrp.jsp" %>
				</s:if>
				<s:if test="document.tmwrpRun.hasWeek2Rows()">
				    <s:set var="rows" value="document.tmwrpRun.week2Rows" />
				    <s:set var="weeklyTitle" value="'Week 2 (Earn Codes)'" />
				    <s:set var="whichWeek" value="'week-one'" />
				    <%@ include file="weeklyTmwrp.jsp" %>
				</s:if>
			    </s:if>
			    <s:if test="document.hasReasonTotals()">
				<s:if test="document.hasReasonWeek1()">
				    <s:set var="rows" value="document.reasonWeek1" />
				    <s:set var="weeklyTitle" value="'Week 1 (Earn Codes/Reasons)'" />
				    <s:set var="whichWeek" value="'week-one'" />
				    <%@ include file="weeklyReasons.jsp" %>
				</s:if>
				<s:if test="document.hasReasonWeek2()">
				    <s:set var="rows" value="document.reasonWeek2" />
				    <s:set var="weeklyTitle" value="'Week 2 (Earn Codes/Reasons)'" />
				    <s:set var="whichWeek" value="'week-two'" />
				    <%@ include file="weeklyReasons.jsp" %>
				</s:if>
			    </s:if>					
			</s:else>
		    </s:if>
		    <s:else> <!-- one job only -->
			<s:if test="document.hasTmwrpRun()">
			    <s:if test="document.tmwrpRun.hasRows()"> </s:if>
			    <s:if test="document.tmwrpRun.hasWeek1Rows()">
				<s:set var="rows" value="document.tmwrpRun.week1Rows" />
				<s:set var="weeklyTitle" value="'Week 1 (Earn Codes)'" />
				<s:set var="whichWeek" value="'week-one'" />
				<%@ include file="weeklyTmwrp.jsp" %>
			    </s:if>
			    <s:if test="document.tmwrpRun.hasWeek2Rows()">
				<s:set var="rows" value="document.tmwrpRun.week2Rows" />
				<s:set var="weeklyTitle" value="'Week 2 (Earn Codes)'" />
				<s:set var="whichWeek" value="'week-two'" />
				<%@ include file="weeklyTmwrp.jsp" %>
			    </s:if>
			</s:if>
			<s:if test="document.hasReasonTotals()">
			    <div class="monetary-hours-tables">
				<s:if test="document.hasReasonWeek1()">
				    <s:set var="rows" value="document.reasonWeek1" />
				    <s:set var="weeklyTitle" value="'Week 1 (Earn Codes/Reasons)'" />
				    <s:set var="whichWeek" value="'week-one'" />
				    <%@ include file="weeklyReasons.jsp" %>
				</s:if>
				<s:if test="document.hasReasonWeek2()">
				    <s:set var="rows" value="document.reasonWeek2" />
				    <s:set var="weeklyTitle" value="'Week 2 (Earn Codes/Reasons)'" />
				    <s:set var="whichWeek" value="'week-two'" />
				    <%@ include file="weeklyReasons.jsp" %>
				</s:if>
			    </div>
			</s:if>
		    </s:else>
		</div>
		<s:if test="document.isProcessed()">
		    <ul>
			<s:if test="hasAccruals()">
			    <li>
				<strong>Available Accruals</strong> <s:property value="document.employeeAccrualsShort" />
			    </li>
			</s:if>
			<s:if test="document.hasJob()">
			    <li>
				<strong>Weekly Standard Work Hrs: </strong> <s:property value="document.job.weekly_regular_hours" />
			    </li>
			    <li>
				<strong>Weekly Compt Time Earned After Hrs: </strong> <s:property value="document.job.comp_time_weekly_hours" />
			    </li>
			</s:if>
		    </ul>
		</s:if>
		<s:else>
		    <%@ include file="accrualSummary.jsp" %>
		</s:else>
		<%@ include file="timeIssues.jsp" %>
		<s:if test="document.hasUnscheduleds()">
		    <s:set var="unscheduledTitle" value="'Unscheduled Times'" />				
		    <s:set var="unscheduleds" value="document.unscheduleds" />
		    <%@ include file="unscheduledTimes.jsp" %>				
		</s:if>			
		<s:if test="canCancelAction()">
		    <s:set var="canCancelAction" value="'true'" />
		</s:if>
		<%@ include file="timeActions.jsp" %>
		<%@ include file="blockNotes.jsp" %>
		<%@ include file="timeNotes.jsp" %>
		<br />
		<h1>Time Log History</h1>
		<a href="<s:property value='#application.url' />timeBlockLog.action?document_id=<s:property value='document.id' />">(view)</a>
	    </div>
	</s:if>
</div>

<!-- jQuery Dialog Modal: Add Pay Period Note -->
<div class="modal pay-notes" class="timetrack" style="display: none;"></div>

<%@ include file="footer.jsp" %>
