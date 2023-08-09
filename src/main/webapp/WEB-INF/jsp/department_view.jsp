<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:if test="hasErrors()">
	<s:set var="errors" value="errors" />
	<%@ include file="errors.jsp" %>
    </s:if>
    <s:elseif test="hasMessages()">
	<s:set var="messages" value="messages" />
	<%@ include file="messages.jsp" %>
    </s:elseif>   
    <h1><s:property value="%{department.name}" /></h1>
    <p><strong>Note:</strong> Reference ID is New World app ID for the specified department.<br /> Ldap/AD name is the department name in ldap, needed for data import.
    </p>
    <div class="width-one-half">
	<div class="form-group">
	    <label>ID</label>
	    <s:property value="%{department.id}" />
	</div>
	<div class="form-group">
	    <label>Ldap/AD Name</label>
	    <s:property value="%{department.ldap_name}"/>
	</div>
	<div class="form-group">
	    <label>Description</label>
	    <s:property value="%{department.description}"/>
	</div>
	<div class="form-group">
	    <label>Referance ID(s)</label>
	    <s:property value="%{department.ref_id}" />
	</div>
	<div class="form-group">
	    <label>Allow Pending Accrual?</label>
	    <s:if test="department.allowPendingAccrual">Yes</s:if><s:else>No</s:else>
	</div>
	<div class="form-group">
	    <label>Inactive?</label>
	    <s:if test="department.inactive">Yes</s:if><s:else>No</s:else>
	</div>
	<div class="button-group">
	    <a href="<s:property value='#application.url'/>department.action?id=<s:property value='id' />&action=Edit" class="button">Edit</a>
	</div>
    </div>
    <s:if test="groups != null">
	<s:set var="groups" value="groups" />
	<s:set var="groupsTitle" value="'Groups in this Department'" />
	<%@ include file="groups.jsp" %>
    </s:if>
    <s:if test="hasDepartmentEmployees()">
	<s:set var="departmentEmployees" value="departmentEmployees" />
	<s:set var="departmentEmployeesTitle" value="'Employees in this Department'" />
	<%@ include file="departmentEmployees.jsp" %>
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
