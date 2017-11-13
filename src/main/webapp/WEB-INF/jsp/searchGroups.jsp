<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="searchGroups" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<h3>Group Search</h3>
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
	<dl class="fn1-output-field">
		<dt>Group ID</dt>
		<dd><s:textfield name="grplst.id" value="%{grplst.id}" size="10" id="group_id" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Name </dt>
		<dd><s:textfield name="grplst.name" value="%{grplst.name}" size="30" id="group_name" />(key words) </dd>
	</dl>

	<dl class="fn1-output-field">
		<dt>Department</dt>
		<dd><s:select name="grplst.department_id" value="%{grplst.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All"  /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Active?</dt>
		<dd><s:radio name="grplst.active_status" value="%{grplst.active_status}" list="#{'-1':'All','Active':'Active only','Inactive':'Inactive only'}"/>
		</dd>
	</dl>	
	<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
	<a href="<s:property value='#application.url' />group.action" class="fn1-btn">New Group</a>		
</s:form>
<s:if test="groups != null && groups.size() > 0">
	<s:set var="groups" value="%{groups}" />
	<s:set var="groupsTitle" value="groupsTitle" />
	<%@  include file="groups.jsp" %>
</s:if>

<%@  include file="footer.jsp" %>


