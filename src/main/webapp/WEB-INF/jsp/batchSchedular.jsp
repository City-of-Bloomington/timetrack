<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="batchSchedule" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h1>Batch Submission Schedular</h1>
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
  <p> This function is designed to be run only once to schedule batch submission for punch clock employees who do not submit their work times. This is designed to run on Monday morning at 7:30am the day next to the last day of previous pay period.<br />
		Run the schedule if the 'Next Schedule Date' and 'Previous Schedule Date' are not shown.
	</p>
	<div class="tt-row-container">
		<s:if test="hasPrevDates()">
			<dl class="fn1-output-field">
				<dt>Next Schedule Date </dt>
				<dd><s:property value="%{next_date}" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Previous Schedule Date </dt>
				<dd><s:property value="%{prev_date}" /> </dd>
			</dl>			
		</s:if>
		<dl class="fn1-output-field">
			<dt> </dt>
			<dd><s:submit name="action" accrual="button" value="Schedule" class="fn1-btn"/></dd>
			</dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt> </dt>
			<dd>To submit right now, pick pay period and click on 'Submit Now' </dd>
		</dl>
		<s:if test="hasPeriods()">
			<dl class="fn1-output-field">
				<dt>Pay Period </dt>
				<dd><s:select name="pay_period_id" value="%{pay_period_id}" list="periods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Pay Period" /> </dd>
			</dl>
		</s:if>
		<dl class="fn1-output-field">
			<dt> </dt>
			<dd>
				<s:submit name="action" accrual="button" value="Submit Now" class="fn1-btn"/></dd>
		</dl>
	</div>
</s:form>
<%@  include file="footer.jsp" %>


