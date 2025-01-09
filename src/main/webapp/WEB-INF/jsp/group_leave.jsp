<%@ include file="header.jsp" %>
<div class="internal-page">
  <s:form action="group_leave" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" /> <s:if test="hourcodeCondition.id == ''">
	    <h1>Group Leave Configuration</h1>
	</s:if>
	
	<s:else>
	    <h1>Edit Group Leave: <s:property value="groupLeave.name" /></h1>
	    <s:hidden name="groupLeave.id" value="%{groupLeave.id}" />
	</s:else>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />		
	    <%@ include file="errors.jsp" %>
	</s:if>		
	<s:elseif test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />					
	    <%@ include file="messages.jsp" %>
	</s:elseif>
	<s:if test="groupLeave.id != ''">
	    <div class="form-group">
		<label>ID</label>
		<s:property value="%{groupLeave.id}" />
	    </div>
	</s:if>
	<div class="form-group">
	    <label>Salary Group</label>
	    <s:select name="groupLeave.salary_group_id" value="%{groupLeave.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick salary group" required="true" />
	</div>
	<div class="form-group">
	    <label>Department</label>
	    <s:select name="groupLeave.department_id" value="%{groupLeave.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" id="department_id_change" headerValue="All" />
	</div>
	<s:if test="groupLeave.id != ''">
	    <div class="form-group">
		<label>Group</label>
		<s:select name="groupLeave.group_id" value="%{groupLeave.group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" /> (to pick a group pick a department first) <br />
	    </div>
	</s:if>
	<s:else>
	    <div class="form-group">
		<label>Group</label>			
		<select name="groupLeave.group_id" id="group_id_set"  disabled="disabled" >
		    <option value="-1">All</option>
		</select>
	    </div>
	</s:else>
	<s:if test="groupLeave.id != ''">
	    <div class="form-group">
		<label>Inactive ?</label>
		<s:checkbox name="groupLeave.inactive" value="%{groupLeave.inactive}" /> Yes (check to dissable)
	    </div>
	</s:if>
	<div class="button-group">
	    <s:if test="groupLeave.id == ''">
		<s:submit name="action" type="button" value="Save" />
	    </s:if>
	    <s:else>
		<a href="<s:property value='#application.url' />group_leave.action?" class="button">New Group Leave</a>
		<s:submit name="action" type="button" value="Save Changes" />
	    </s:else>
	</div>		
    </s:form>
    <s:if test="hasGroupLeaves()">
	<s:set var="groupLeaves" value="%{groupLeaves}" />
	<s:set var="groupLeavesTitle" value="groupLeavesTitle" />
	<%@ include file="group_leaves.jsp" %>
    </s:if>
</div>

<%@ include file="footer.jsp" %>
