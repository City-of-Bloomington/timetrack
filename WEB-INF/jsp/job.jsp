<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="jobTask" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="jobTask.id == ''">
		<h3>New jobTask</h3>
	</s:if>
	<s:else>
		<h3>Edit JobTask: <s:property value="%{jobTask.name}" /></h3>
		<s:hidden id="jobTask.id" name="jobTask.id" value="%{jobTask.id}" />
	</s:else>
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
		<s:if test="id != ''">
			If you make any change, please hit the 'Save Changes' button
		</s:if>
		<s:else>
			You must hit 'Save' button to save data.
		</s:else>
	</p>
	<div class="tt-row-container">
		<s:if test="jobTask.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="jobTask.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Position </dt>
			<dd><s:select name="jobTask.position_id" value="%{jobTask.position_id}" list="positions" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Position" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Salary Group </dt>
			<dd><s:select name="jobTask.salary_group_id" value="%{jobTask.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Salary Group" required="true" />* </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Employee </dt>
			<dd><s:select name="jobTask.employee_id" value="%{jobTask.employee_id}" list="employees" listKey="id" listValue="user.full_name" headerKey="-1" headerValue="Pick Employee" required="true" />* </dd>
		</dl>				
		<dl class="fn1-output-field">
			<dt>Effective Date </dt>
			<dd><s:textfield name="jobTask.effective_date" value="%{jobTask.effective_date}" size="10" cssClass="date" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Expire Date </dt>
			<dd><s:textfield name="jobTask.expire_date" value="%{jobTask.expire_date}" size="10" cssClass="date" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Primary JobTask?</dt>
			<dd><s:checkbox name="jobTask.primary_flag" value="%{jobTask.primary_flag}" />Yes </dd>
		</dl>
		<s:if test="jobTask.id != ''">
			<dl class="fn1-output-field">
				<dt>Inactive?</dt>
				<dd><s:checkbox name="jobTask.inactive" value="%{jobTask.inactive}" fieldValue="true" />Yes </dd>
			</dl>
		</s:if>
		<s:if test="jobTask.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>

		</s:else>
	</div>
</s:form>
<s:if test="hasJobTasks()">
	<s:set var="jobTasks" value="jobTasks" />
	<s:set var="jobTasksTitle" value="jobTasksTitle" />
	<%@  include file="jobTasks.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


