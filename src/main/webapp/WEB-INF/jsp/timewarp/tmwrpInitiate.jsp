<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page">
	<h1>Timewarp Initiatation Process</h1>
	<p>This process will go through all the employees with entered times
		but no corresponding timewarp data saved. Needed for old pay period
		if we need to move them to the new transition.
	</p>
	<s:form action="tmwrpInitiate" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />
			<%@ include file="../messages.jsp" %>
		</s:if>
		<s:if test="hasErrors()">
			<s:set var="errors" value="%{errors}" />
			<%@ include file="../errors.jsp" %>
		</s:if>
		<div>
			<div class="form-group">
				<label>Pay Period </label>
				<s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" onchange="doRefresh()" /> &nbsp;&nbsp;
				<a href="<s:property value='#application.url' />tmwrpInitiate.action?pay_period_id=<s:property value='currentPayPeriod.id' />">Current Pay Period</a>
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Process" class="fn1-btn"/>
			</div>
		</div>
	</s:form>
	<s:if test="action != ''">
		<div class="internal-timewarp">
			<s:if test="hasEmps()">
				<ul>
				<s:iterator value="emps" var="emp" status="status">
					<li> <s:property value="#status.count" /> - <s:property /> </li>
				</s:iterator>
				</ul>
			</s:if>
		</div>
	</s:if>
</div>

<%@ include file="../footer.jsp" %>
