<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page">
	<h1>Timewarp</h1>
	<s:form action="timewarp" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="source" value="source" />
		<s:hidden name="department_id" value="%{department_id}" />
		<input type="hidden" name="type" value="single" />	
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />
			<%@ include file="../messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="%{errors}" />
			<%@ include file="../errors.jsp" %>
		</s:elseif>
		<div class="width-one-half">
			<div class="form-group">
				<label>Pay Period </label>
				<s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" onchange="doRefresh()" />&nbsp;&nbsp; <a href="<s:property value='#application.url' />timewarp.action?type=single&pay_period_id=<s:property value='currentPayPeriod.id' />&department_id=<s:property value='department_id' />&group_id=<s:property value='group_id' />&action=Submit">Current Pay Period</a>
			</div>
			<div class="form-group">			
				<label>Department </label>
					<s:property value="department" />
			</div>
			<s:if test="hasGroups()">
				<div class="form-group">
					<label>Group </label>
					<s:select name="group_id" valuw="%{group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All"  onchange="doRefresh()" />
				</div>
			</s:if>
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
			</div>
		</div>
		<hr />
	</s:form>
</div>
<s:if test="action != ''">
	<%@  include file="timewarpDetails.jsp" %>	
</s:if>
<%@ include file="../footer.jsp" %>


