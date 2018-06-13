<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="importEmployees" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:hidden name="dept_name" id="dept_name_id" value="%{dept_name}" />
		<h1>Import employees data from Ldap (AD)</h1>

	  <%@ include file="strutMessages.jsp" %>

	  <div class="width-one-half">
			<p>To import employees data from ldap or AD, pick the department, you may pick a certain group if you do not want to import only certain group.</p>

			<div class="form-group">
				<label>Department</label>
				<s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" id="department_id_change" />
			</div>

			<div class="form-group">
				<label>Group</label>
					<s:if test="hasGroups()">
						<s:select name="group_name" value="%{group_name}" list="groups" listKey="name" listValue="name" id="group_name_set" headerKey="-1" headerValue="Pick a group"/>
					</s:if>

					<s:else>
						<select name="group_name" value=""  id="group_name_set" disabled="disabled" ></select><br />
						(optional)
					</s:else>
				</dd>
			</div>

			<div class="form-group">
				<label>Effective Date</label>
				<s:textfield name="effective_date" value="" size="10" maxlength="10" cssClass="date" />
			</div>

			<s:submit name="action" type="button" value="Import" class="button"/>
		</div>
	</s:form>
</div>
<%@ include file="footer.jsp" %>