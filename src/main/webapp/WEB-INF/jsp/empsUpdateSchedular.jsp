<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="empsUpdateSchedule" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h3>Current Employees Update</h3>
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
  <p> This function is designed to run on a schedule once every week to update current employees info and mark inactive ones using data from acitve directory (ldap or AD). <br />
		Run the Schedular if the 'Next Schedule Date'
		and 'Previous Schedule Date' are not shown. <br />
		To verify current employees today click on 'Submit' button, we may need to run this manually if the scheduling fails.
		For new Employees use 'Import AD (ldap) Employee' option in the setting menu. <br />
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
		<dl class="fn1-output-field">
			<dt> </dt>
			<dd>
				<s:submit name="action" accrual="button" value="Submit" class="fn1-btn"/></dd>
		</dl>
	</div>
</s:form>
<%@  include file="footer.jsp" %>


