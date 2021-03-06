<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="searchEmployees" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
  	<input type="hidden" name="department_id" id="department_id"
							value="<s:property value='department_id' />"  />
		<h1>Employee Search</h1>
		<s:if test="hasMessages()">
			<s:set var="messages" value="messages" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="errors" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>
	  <div class="width-one-half">			
			<div class="form-group">
				<label>ID</label>
				<s:textfield name="emplst.id" value="%{emplst.id}" size="10" id="employee_id" />
			</div>
			<div class="form-group">
				<label>Full Name</label>
				<s:textfield name="emplst.name" value="%{emplst.name}" size="30" id="employee_name" /><br />
				(start typing to pick from list)
			</div>

			<div class="form-group">
				<label>ID Code #</label>
				<s:textfield name="emplst.id_code" value="%{emplst.id_code}" size="10" maxlength="10" /><br />
				(The number on City ID)
			</div>
			
			<div class="form-group">
				<label>Employee #</label>
				<s:textfield name="emplst.employee_number" value="%{emplst.employee_number}" size="15" maxlength="15" /><br />
				(from new world)
			</div>
			<div class="form-group">
				<label>AD Sid</label>
				<s:textfield name="emplst.ad_sid" value="%{emplst.ad_sid}" size="10" maxlength="10" /><br />
			</div>			
			<div class="form-group">
				<label>Department</label>
				<s:select name="emplst.department_id" value="%{emplst.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" id="department_id_change" />
			</div>

			<div class="form-group">
				<label>Group</label>
				<s:if test="emplst.hasGroups()">
					<s:select name="emplst.group_id" value="%{emplst.group_id}" id="group_id_set"  list="%{emplst.groups}" listKey="id" listValue="name" headerKey="-1" headerValue="All" />
				</s:if>
				<s:else>
					<select name="emplst.group_id" id="group_id_set"  disabled="disabled" >
						<option value="-1">All</option>
					</select><br />
				(To pick a group you need to pick a department first)
				</s:else>
			</div>
			<div class="form-group">
				<label>Added Date</label>
				<s:radio name="emplst.added_status" value="%{emplst.added_status}" list="#{'-1':'All','Recent':'Recent (Within 14 days)','Old':'Old (More than 14 days)'}"/>				
			</div>
			<div class="form-group">
				<label>Active?</label>
				<s:radio name="emplst.activeStatus" value="%{emplst.activeStatus}" list="#{'-1':'All','Active':'Active only','Inactive':'Inactive only'}"/>
			</div>
			
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="button"/>
				<a href="<s:property value='#application.url' />employee.action" class="button">New Employee</a>
			</div>
		</div>
	</s:form>
</div>
<s:if test="employees != null && employees.size() > 0">
	<s:set var="employees" value="%{employees}" />
	<s:set var="employeesTitle" value="employeesTitle" />
	<%@ include file="employees.jsp" %>
</s:if>
<%@ include file="footer.jsp" %>

