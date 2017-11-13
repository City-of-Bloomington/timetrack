<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="handleIssue" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="timeIssue.id" value="%{timeIssue.id}" />
	<h3>Time Issue <s:property value="timeIssue.id" /></h3>
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
  <p>* Required field <br />
		You must hit 'Save' button to save data.
	</p>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Issue Notes </dt>
			<dd><s:textarea name="timeIssue.issue_notes" value="%{timeIssue.issue_notes}" rows="5" cols="50" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Date & Time </dt>
			<dd><s:property value="%{timeIssue.date}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Reported by </dt>
			<dd><s:property value="%{timeIssue.reporter}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Status </dt>
			<dd><s:property value="%{timeIssue.status}" /> </dd>
		</dl>									
		<s:if test="timeIssue.isClosed()">
			<dl class="fn1-output-field">
				<dt>Closed By </dt>
				<dd><s:property value="%{timeIssue.closed_by}" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Closed Date </dt>
				<dd><s:property value="%{timeIssue.closed_date}" /> </dd>
			</dl>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Close This Issue" class="fn1-btn"/>
		</s:else>
	</div>
</s:form>
<s:if test="hasTimeIssues()">
	<s:set var="timeIssues" value="timeIssues" />
	<s:set var="timeIssuesTitle" value="timeIssuesTitle" />
	<%@  include file="timeIssues.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


