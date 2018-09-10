<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="department" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="department.id == ''">
			<h1>New Department</h1>
		</s:if>

		<s:else>
			<h1>Edit <s:property value="%{department.name}" /></h1>
			<s:hidden name="department.id" value="%{department.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

	  <p><strong>Note:</strong> Reference ID is New World app ID for the specified department.<br /> Ldap/AD name is the department name in ldap, needed for data import.
		</p>

		<div class="width-one-half">
			<s:if test="department.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="department.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Name</label>
				<s:textfield name="department.name" value="%{department.name}" size="30" maxlength="70" requiredLabel="true" required="true" />
			</div>

			<div class="form-group">
				<label>Ldap/AD Name</label>
				<s:textfield name="department.ldap_name" value="%{department.ldap_name}" size="30" maxlength="70" requiredLabel="true" required="true" />
			</div>

			<div class="form-group">
				<label>Description</label>
				<s:textarea name="department.description" value="%{department.description}" rows="5" maxlength="50" />
			</div>

			<div class="form-group">
				<label>Referance ID(s)</label>
				<s:textfield name="department.ref_id" value="%{department.ref_id}" size="10" maxlength="30" /><br />(for multiple ID's, use comma in between)
			</div>

			<div class="form-group">
				<label>Inactive?</label>
				<s:checkbox name="department.inactive" value="%{department.inactive}" fieldValue="true" />Yes
			</div>

			<s:if test="department.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>

			<s:else>
				<div class="button-group">
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
					<a href="<s:property value='#application.url'/>department.action" class="button">New Department</a>
				</div>
			</s:else>
		</div>
	</s:form>

	<s:if test="department.id != ''">
		<s:if test="groups != null">
			<s:set var="groups" value="groups" />
			<s:set var="groupsTitle" value="'Groups in this Department'" />
			<%@ include file="groups.jsp" %>
		</s:if>

		<s:if test="hasEmployees()">
			<s:set var="employees" value="employees" />
			<s:set var="employeesTitle" value="'Employees in this Department'" />
			<%@ include file="employees.jsp" %>
		</s:if>
	</s:if>

	<s:else>
		<s:if test="departments != null">
			<s:set var="departments" value="departments" />
			<s:set var="departmentsTitle" value="deptsTitle" />
			<%@ include file="departments.jsp" %>
		</s:if>
	</s:else>
</div>
<%@ include file="footer.jsp" %>
