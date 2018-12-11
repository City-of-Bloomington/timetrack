<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="ipaddress" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="ipaddress.id == ''">
			<h1>New IP Address</h1>
		</s:if>
		<s:else>
			<h1>Edit IP Address: <s:property value="%{ipaddress.ip_address}" /></h1>
			<s:hidden name="ipaddress.id" value="%{ipaddress.id}" />
		</s:else>

		<p>These ip addresses will be needed for employees using punch clock.</p>

	  <%@ include file="strutMessages.jsp" %>

		<div class="width-one-half">
			<s:if test="ipaddress.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="ipaddress.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>IP</label>
				<s:textfield name="ipaddress.ip_address" value="%{ipaddress.ip_address}" size="15" maxlength="15" requiredLabel="true" required="true" />
			</div>
			<div class="form-group">
				<label>Location</label>
				<s:textfield name="ipaddress.location" value="%{ipaddress.location}" rows="30" maxlength="128" />
			</div>
			<s:if test="ipaddress.id == ''">
				<s:submit name="action" accrual="button" value="Save" class="button"/>
			</s:if>
			<s:else>
				<s:submit name="action" accrual="button" value="Save Changes" class="button"/>
				<s:submit name="action" accrual="button" value="Delete" class="button"/>				
			</s:else>
		</div>
	</s:form>

	<s:if test="ipaddresses != null">
		<s:set var="ipaddresses" value="ipaddresses" />
		<s:set var="ipaddressesTitle" value="ipaddressesTitle" />
		<%@ include file="ipaddresses.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
