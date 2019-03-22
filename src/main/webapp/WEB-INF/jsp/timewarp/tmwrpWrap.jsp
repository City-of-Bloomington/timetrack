<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page">
	<h1>Timewarp</h1>
	<s:form action="tmwrpWrap" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="source" value="source" />
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
				<a href="<s:property value='#application.url' />timewarp.action?pay_period_id=<s:property value='currentPayPeriod.id' />&action=Submit">Current Pay Period</a>
			</div>
			<div class="form-group">
				<label>Department </label>
				<s:select name="department_id" valuw="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" onchange="doRefresh()" />
			</div>
			<s:if test="hasGroups()">
				<div class="form-group">
					<label>Group </label>
					<s:select name="group_id" valuw="%{group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" onchange="doRefresh()" />
				</div>
			</s:if>
			<div class="form-group">
				<label>Output format </label>
				<s:radio name="outputType" value="%{outputType}" list="#{'html':'html','csv':'csv'}" />
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
			</div>
		</div>
	</s:form>

	<s:if test="action != ''">
		<div class="internal-timewarp">
			<%@ include file="tmwrpRuns.jsp" %>
		</div>
	</s:if>
</div>

<%@ include file="../footer.jsp" %>