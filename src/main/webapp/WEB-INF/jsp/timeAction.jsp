<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<h3>View Time Action: <s:property value="%{timeAction.id}" /></h3>
<s:if test="hasErrors()">
	<div class="errors">
    <s:actionerror/>
	</div>
</s:if>
<s:elseif test="hasMessages()">
	<div class="welcome">
    <s:actionmessage/>
	</div>
</s:elseif>
<div class="tt-row-container">
	<dl class="fn1-output-field">
		<dt>Document ID </dt>
		<dd><s:property value="%{timeAction.document_id}" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Workflow ID </dt>
		<dd><s:property value="%{timeAction.workflow_id}" /> </dd>
	</dl>				
	<dl class="fn1-output-field">
		<dt>Action By </dt>
		<dd><s:property value="%{timeAction.action_by}" /> </dd>
	</dl>		
	<a href="<s:property value='#application.url' />timeDetails.action?document_id=<s:property value='timeAction.document_id' />" class="fn1-btn">Time Details </a>					
</div>
<%@  include file="footer.jsp" %>


