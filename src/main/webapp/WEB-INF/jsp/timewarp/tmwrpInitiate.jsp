<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page">
	<h1>Timewarp Initiatation Process</h1>
	<p>1 - This process will go through all the employees with entered times
		but no corresponding timewarp data saved. Needed for old pay period
		if we need to move them to the new transition.
	</p>
	<p>2 - (Optional) You may run this process for certain employee only by start typing the employee name to pick from a list</p>
	<p>3 - (Optional) You may run this process for whole department or only certain group in the department</p>	
	
	<s:form action="tmwrpInitiate" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<input type="hidden" name="department_id" id="department_id"
			value="<s:property value='department_id' />"  />
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />
			<%@ include file="../messages.jsp" %>
		</s:if>
		<s:if test="hasErrors()">
			<s:set var="errors" value="%{errors}" />
			<%@ include file="../errors.jsp" %>
		</s:if>
		<div class="width-one-half">
			<div class="form-group">
				<label>Pay Period </label>
				<s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" /> &nbsp;&nbsp;
				<a href="<s:property value='#application.url' />tmwrpInitiate.action?pay_period_id=<s:property value='currentPayPeriod.id' />">Current Pay Period</a>
			</div>
  		<div class="form-group">
  			<label>Employee</label>
        <s:textfield name="employee_name" value="" id="employee_name" size="20" />
  		</div>
      <div class="form-group">
        <label>Employee ID</label>
        <s:textfield name="new_employee_id" value="" id="employee_id" size="5" />
      </div>
  		<div class="form-group">
  			<label>Department</label>
				<s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" id="department_id_change"/>				
  		</div>
  		<div class="form-group">
  			<label>Group</label>
				<select name="group_id" value="" id="group_id_set"  disabled="disabled" >
					<option value="-1">Pick a Group </option>
				</select><br />
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
