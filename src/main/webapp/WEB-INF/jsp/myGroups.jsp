<%@ include file="header.jsp" %>
<div class="internal-page">
    <h1>Jobs & Employees in your group(s)</h1>
    <s:form action="mygroups" id="form_id" method="post" class="internal-page">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />			
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />			
	    <%@ include file="errors.jsp" %>
	</s:elseif>
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
    </s:form>
</div>
<%@ include file="footer.jsp" %>
