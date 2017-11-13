<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="hourcodeCondition" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="hourcodeCondition.id == ''">
		<h3>New Hour Code Restriction</h3>
	</s:if>
	<s:else>
		<h3>Edit Hour Code Restriction: <s:property value="hourcodeCondition.name" /></h3>
		<s:hidden name="hourcodeCondition.id" value="%{hourcodeCondition.id}" />
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
	<s:if test="hourcodeCondition.id != ''">
		<dl class="fn1-output-field">
			<dt>ID</dt>
			<dd><s:property value="%{hourcodeCondition.id}" /></dd>
		</dl>
	</s:if>
	<dl class="fn1-output-field">
		<dt>Hour Code</dt>
		<dd><s:select name="hourcodeCondition.hour_code_id" value="%{hourcodeCondition.hour_code_id}" required="true" list="hourcodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Hour Code" /></dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>Department </dt>
		<dd><s:select name="hourcodeCondition.department_id" value="%{hourcodeCondition.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Salary Group </dt>
		<dd><s:select name="hourcodeCondition.salary_group_id" value="%{hourcodeCondition.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick salary group" required="true" /> </dd>
	</dl>
	<s:if test="hourcodeCondition.id != ''">
		<dl class="fn1-output-field">
			<dt>Inactive ?</dt>
			<dd><s:checkbox name="hourcodeCondition.inactive" value="%{hourcodeCondition.inactive}" /> Yes (check to dissable)
			</dd>
		</dl>
	</s:if>
	<s:if test="hourcodeCondition.id == ''">
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
	</s:if>
	<s:else>
		<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		<a href="<s:property value='#application.url' />hourcodeCondition.action?" class="fn1-btn">New Hour Code Restiction</a>						
	</s:else>
</s:form>
<s:if test="hasConditions()">
	<s:set var="hourcodeConditions" value="%{conditions}" />
	<s:set var="hourcodeConditionsTitle" value="hourcodeConditionsTitle" />
	<%@  include file="hourcodeConditions.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


