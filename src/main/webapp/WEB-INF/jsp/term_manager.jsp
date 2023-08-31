<%@ include file="header.jsp" %>
<div class="internal-page">
    <div class="width-one-half">
	
	<s:form action="termManager" id="form_id" method="post" >
  	    <input type="hidden" name="action2" id="action2" value="" />
  	    <input type="hidden" name="department_id" id="department_id"
		   value="<s:property value='department_id' />"  />	    
  	    <input type="hidden" name="type" id="type" value="activeOnly" />
	    <s:if test="manager.id == ''">
  		<h1>New Termination Manager</h1>
	    </s:if>
	    <s:else>
  		<s:hidden name="manager.id" value="%{manager.id}" />
		<h1>Edit Termination Manager</h1>
	    </s:else>
	    <s:if test="hasErrors()">
		<s:set var="errors" value="errors" />
		<div class="errors">
		    <%@  include file="errors.jsp" %>
		</div>
	    </s:if>
	    <s:elseif test="hasMessages()">
		<s:set var="messages" value="messages" />
		<div class="welcome">
		    <%@  include file="messages.jsp" %>
		</div>
	    </s:elseif>
  	    <p><strong>Note:</strong> start typing the first name or last name of the employee in the employee field then pick from a list.</p>
  	    <div class="form-group">
  		<label>Employee</label>
		<s:textfield name="manager.empName" value="%{manager.empName}" id="employee_name" size="20" />
  	    </div>
	    <div class="form-group">
		<label>Employee ID</label>
		<s:textfield name="manager.employee_id" value="%{manager.employee_id}" id="employee_id" size="5" />
	    </div>
	    <div class="form-group">
		<label>Department </label>
		<s:select name="manager.department_id" value="%{manager.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Dept" required="true" />
	    </div>
	    <s:if test="manager.id == ''">
  		<s:submit name="action" type="button" value="Save" class="button"/>
	    </s:if>
	    <s:else>
		<div class="form-group">
		    <label>Inactive? </label>
		    <s:checkbox name="manager.inactive" value="%{manager.inactive}" />
		    Yes (check to disable)
		</div>		
  		<s:submit name="action" type="button" value="Save Changes" class="button"/>
	    </s:else>
	</s:form>	    
    </div>
</div>
<s:if test="hasManagers()">
<table class="groups width-full">
	<thead>
	    <tr>
		<th>ID</th>
		<th>Employee </th>
		<th>Department</th>
		<th>Active?</th>
	    </tr>
	</thead>
	<tbody>
	    <s:iterator var="one" value="managers">
		<tr>
		    <td><a href="<s:property value='#application.url' />termManager.action?id=<s:property value='id' />">Edit</a></td>
		    <td><s:property value="empName" /></td>
		    <td><s:property value="deptName" /></td>
		    <td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>
		</tr>
	    </s:iterator>
	</tbody>
</table>

</s:if>
<%@ include file="footer.jsp" %>
