<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="location" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="location.id == ''">
			<h1>New Location</h1>
		</s:if>
		<s:else>
			<h1>Edit Location: <s:property value="%{location.ip_address}" /></h1>
			<s:hidden name="location.id" value="%{location.id}" />
		</s:else>

		<p>These ip addresses locations are needed for employees using punch clock.</p>

	  <%@ include file="strutMessages.jsp" %>

		<div class="width-one-half">
			<s:if test="location.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="location.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>IP network address</label>
				<s:textfield name="location.ip_address" value="%{location.ip_address}" size="15" maxlength="15" requiredLabel="true" required="true" />(xxx.xxx.xxx.xxx)
			</div>
			<div class="form-group">
				<label>Location Name</label>
				<s:textfield name="location.name" value="%{location.name}" rows="30" maxlength="128" required="true" />
			</div>
			<s:if test="location.id == ''">
				<s:submit name="action" accrual="button" value="Save" class="button"/>
			</s:if>
			<s:else>
				<s:submit name="action" accrual="button" value="Save Changes" class="button"/>
				<s:submit name="action" accrual="button" value="Delete" class="button"/>				
			</s:else>
		</div>
	</s:form>

	<s:if test="locations != null">
		<s:set var="locations" value="locations" />
		<s:set var="locationsTitle" value="locationsTitle" />
		<%@ include file="locations.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
