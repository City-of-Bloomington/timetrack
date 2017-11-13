<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="searchJobs" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h3>Search Jobs</h3>
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
  <p> For employee field you can use key words of first name or last name to pick from the auto complete list <br />
	</p>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Job ID </dt>
			<dd>
				<s:textfield name="joblst.id" value="%{joblst.id}" size="5" />
			</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Position </dt>
			<dd><s:select name="joblst.position_id" value="%{joblst.position_id}" list="positions" listKey="id" listValue="name" headerKey="-1" headerValue="All" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Salary Group </dt>
			<dd><s:select name="joblst.salary_group_id" value="%{joblst.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="All" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Employee </dt>
			<dd>
				<s:textfield name="joblst.employee_name" value="%{joblst.employee_name}" id="employee_name" size="20" />(key words) ID:
				<s:textfield name="joblst.employee_id" value="%{joblst.employee_id}" id="employee_id" size="5" />
			</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Department </dt>
			<dd><s:select name="joblst.department_id" value="%{joblst.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" /> </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Date, from:</dt>
			<dd>
				<s:textfield name="joblst.date_from" value="%{joblst.date_from}" size="10" cssClass="date" /> to:
				<s:textfield name="joblst.date_to" value="%{joblst.date_to}" size="10" cssClass="date" />
			</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Date Type:</dt>
			<dd><s:radio name="joblst.which_date" value="%{joblst.which_date}" list="#{'j.effective_date':'Effective Date','j.expire_date':'Expire Date'}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Active ?</dt>
			<dd><s:radio name="joblst.active_status" value="%{joblst.active_status}" list="#{'-1':'All','Active':'Yes','Inactive':'No'}" /> </dd>
		</dl>
		<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
		<a href="<s:property value='#application.url' />jobTask.action" class="fn1-btn">New Job</a>				
	</div>
</s:form>
<s:if test="hasJobs()">
	<s:set var="jobTasks" value="jobs" />
	<s:set var="jobsTitle" value="jobsTitle" />
	<%@  include file="jobTasks.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


