<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="accrualSchedule" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h1>Accrual Schedular</h1>
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
  <p> This function is designed to be run only once to schedule importing
		employees accruals from another system (New World for example) every
		two weeks on the same day and time. Run these if the 'Next Schedule Date'
		and 'Previous Schedule Date' are not shown.
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
			<dt>Date </dt>
			<dd><s:textfield name="date" value="%{date}" size="10" maxlength="10" cssClass="date" /> </dd>
		</dl>
		<s:if test="hasDepts()">
			<dl class="fn1-output-field">
				<dt>Department </dt>
				<dd><s:select name="dept_ref_id" value="%{dept_ref_id}" list="depts" listKey="ref_id" listValue="name" headerKey="-1" headerValue="Pick Dept" /> </dd>
			</dl>
		</s:if>
		<dl class="fn1-output-field">
			<dt> </dt>
			<dd>
				<s:submit name="action" accrual="button" value="Import Now" class="fn1-btn"/></dd>
		</dl>
	</div>
</s:form>
<%@  include file="footer.jsp" %>


