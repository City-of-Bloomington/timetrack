<%@ include file="header.jsp" %>
<div class="internal-page">
    <h1><s:property value="#activeEmployeesTitle" /></h1>	
    <s:form action="activeEmployees" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<p>The list of active employees in your Department/Groups</p>
	<s:if test="hasMoreThanOneDept()">
	    <div class="width-one-half">
		<div class="form-group">		
		    <label for="dept_id"><strong>Department:&nbsp;</strong></label>
		    <s:select name="dept_id" value="%{dept_id}" list="departments" listKey="id" listValue="name" onchange="doRefresh()" />
		</div>
	    </div>
	</s:if>
	<z:if test="hasGroups()">
	    <div class="width-one-half">
		<s:if test="hasMoreThanOneGroup()">
		    <div class="form-group">		
			<label for="group_id"><strong>Group:&nbsp;</strong></label>
			<s:select name="group_id" value="%{group_id}" list="groups" listKey="id" listValue="name" onchange="doRefresh()" />
		    </div>
		</s:if>
		<s:else>
		    <div class="form-group">
			<label>ID</label>
			<s:property value="%{group.id}" />
		    </div>
		    <div class="form-group">
			<label>Name </label>
			<s:property value="%{group.name}" />
		    </div>
		</s:else>
		<s:if test="group.hasActiveJobs()">
		    <table class="width-full">
			<thead>
			    <tr>
				<th>ID</th>
				<th>Position</th>
				<th>Employee</th>				
				<th>Salary Group</th>
				<th>Group</th>
				<th>Effective Date</th>
				<th>Weekly Reg Hrs</th>
				<th>Action </th>
			    </tr>
			</thead>
			<tbody>
			    <s:iterator var="one" value="%{group.activeJobs}">
				<tr>
				    <td><s:property value="id" /></td>
				    <td><s:property value="position" /></td>
				    <td><s:property value="employee" /></td>
				    <td><s:property value="salaryGroup" /></td>
				    <td><s:property value="group" /></a></td>
				    <td><s:property value="effective_date" /></td>
				    <td><s:property value="weekly_regular_hours" /></td>
				    <td><a href="<s:property value='#application.url' />terminateJobs.action?job_id=<s:property value='id' />"> Terminate </a>
				    </td>
				</tr>
			    </s:iterator>
			</tbody>
		    </table>
		</s:if>
	    </div>
	</z:if>
	<s:submit name="action" type="button" value="Submit" class="button"/>			
    </s:form>
</div>

<%@ include file="footer.jsp" %>
