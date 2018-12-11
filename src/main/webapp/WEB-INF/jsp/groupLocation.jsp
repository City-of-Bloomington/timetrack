<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="groupLocation" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="groupLocation.group_id != ''">
			<s:hidden name="groupLocation.group_id" value="%{groupLocation.group_id}" />			
		</s:if>
		<s:if test="groupLocation.id == ''">
			<h1>Add location to group </h1>
		</s:if>
		<s:else>
			<h1>Edit Group Location: <s:property value="%{groupLocation.id}" /></h1>
		</s:else>
	  <%@ include file="messages.jsp" %>
		<div class="width-one-half">
			<s:if test="groupLocation.group_id != ''">			
				<div class="form-group">
					<label>Group </label>
					<s:property value="%{groupLocation.group}" />
				</div>
			</s:if>
			<s:else>
				<div class="form-group">
					<label>Department</label>
					<s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Dept" required="true" id="department_id_change" />
				</div>
				<div class="form-group">
					<label>Group</label>
					<select name="groupLocation.group_id" value="" id="group_id_set"  disabled="disabled" >
						<option value="-1">Pick a Group</option>
					</select><br />
					(To pick a group you need to pick a department first)
				</div>
			</s:else>
			<div class="form-group">			
				<label>Locations </label>
				<s:select name="groupLocation.location_id" value="%{groupLocation.location_id}" list="locations" listKey="id" listValue="info" headerKey="-1" headerValue="Pick Location" required="true" />
			</div>
			<s:if test="groupLocation.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>
			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url' />groupLocation.action?group_id=<s:property value='groupLocation.group_id' />" class="button">Assign another locations to group </a>					
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>
	<s:if test="groupLocation.id != ''">
		<s:if test="groupLocation.group.hasGroupLocations()">
			<s:set var="groupLocations" value="%{groupLocation.group.groupLocations}" />
			<s:set var="groupLocationsTitle" value="'Group Locations'" />
			<%@ include file="groupLocations.jsp" %>
		</s:if>

		<s:if test="hasGroupManagers()">
			<s:set var="groupManagers" value="%{groupManagers}" />
			<s:set var="groupManagersTitle" value="'Group Managers'" />
			<%@ include file="groupManagers.jsp" %>
		</s:if>
	</s:if>
	<s:else>
		<s:if test="groupLocations != null">
			<s:set var="groupLocations" value="groupLocations" />
			<s:set var="groupLocationsTitle" value="groupLocationsTitle" />
			<%@ include file="groupLocations.jsp" %>
		</s:if>
	</s:else>
</div>
<%@ include file="footer.jsp" %>
