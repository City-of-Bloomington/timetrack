<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="searchEmployees" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<h3>Employee Search</h3>
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
		<dt>ID</dt>
		<dd><s:textfield name="emplst.id" value="%{emplst.id}" size="10" id="employee_id" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Name </dt>
		<dd><s:textfield name="emplst.name" value="%{emplst.name}" size="30" id="employee_name" />(key words) </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>ID Code # </dt>
		<dd><s:textfield name="emplst.id_code" value="%{emplst.id_code}" size="10" maxlength="10" />(The number on City ID) </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Employee # </dt>
		<dd><s:textfield name="emplst.employee_number" value="%{emplst.employee_number}" size="15" maxlength="15" />(from new world) </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Department</dt>
		<dd><s:select name="emplst.department_id" value="%{emplst.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" id="department_id_change" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Group</dt>
		<s:if test="emplst.hasGroups()">
			<dd>
				<s:select name="emplst.group_id" value="%{emplst.group_id}" id="group_id_set"  list="%{emplst.groups}" listKey="id" listValue="name" headerKey="-1" headerValue="All" />
			</dd>
		</s:if>
		<s:else>
			<dd>					
				<select name="emplst.group_id" value="" id="group_id_set"  disabled="disabled"/>
				<option value="-1">All</option>
			</select>(To pick a group you need to pick a department first)
			</dd>			
		</s:else>
	</dl>		
	<dl class="fn1-output-field">
		<dt>Active?</dt>
		<dd><s:radio name="emplst.active_status" value="%{emplst.active_status}" list="#{'-1':'All','Active':'Active only','Inactive':'Inactive only'}"/>
		</dd>
	</dl>	
	<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
	<a href="<s:property value='#application.url' />employee.action" class="fn1-btn">New Employee</a>		
</s:form>
<s:if test="employees != null && employees.size() > 0">
	<s:set var="employees" value="%{employees}" />
	<s:set var="employeesTitle" value="employeesTitle" />
	<%@  include file="employees.jsp" %>
</s:if>

<%@  include file="footer.jsp" %>


