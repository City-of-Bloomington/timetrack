<%@ include file="header.jsp" %>
<div class="internal-page">
<s:form action="altPayPeriodGroup" id="form_id" method="post" >
    <s:hidden name="action2" id="action2" value="" />
    <h1>New Alt Pay Period Group Assignment</h1>
    <s:if test="hasMessages()">
	<s:set var="messages" value="messages" />		
	<%@ include file="messages.jsp" %>
    </s:if>
    <s:elseif test="hasErrors()">
	<s:set var="errors" value="errors" />		
	<%@ include file="errors.jsp" %>
    </s:elseif>
    <p>Add New Group to Alt PayPeriod Groups <br />
	These groups pay period starts on Sundays and ends on Saturday
	Mostly for Police Dispatch groups 
    </p>
    <div class="width-one-half">
	<div class="form-group">
	    <label>Department</label>
	    <s:select name="department_id" value="" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" id="department_id_change" />
	</div>
	<div class="form-group">
	    <label>Group</label>
	    <select name="altGroup.group_id" value="" id="group_id_set"  disabled="disabled"/>
	    <option value="-1">Pick a group</option>
				   </select>(To pick a group you need to pick a department first)
	</div>
	<div class="button-group">
	    <s:submit name="action" type="button" value="Save" class="button"/>
	</div>
    </div>
</s:form>
<s:if test="hasAltGroups()">
    <table class="width-full">
	<caption>Current Alt Pay Period Groups</caption>
	<thead>
	    <tr>
		<th>ID</th>
		<th>Department</th>		
		<th>Group Id</th>
		<th>Group </th>
		<th>Action</th>
	    </tr>
	</thead>
	<tbody>
	    <s:iterator var="one" value="altGroups">
		<tr>
		    <td><s:property value="id" /></td>
		    <td><s:property value="deptName" /></td>
		    <td><s:property value="group_id" /></td>
		    <td><s:property value="groupName" /></td>
		    <td><a href="<s:property value='#application.url' />altPayPeriodGroup.action?id=<s:property value='id' />&action=Delete">Delete</a></td>
		</tr>
	    </s:iterator>
	</tbody>
    </table>
</s:if>
<%@ include file="footer.jsp" %>
