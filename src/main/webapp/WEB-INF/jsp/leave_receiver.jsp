<%@  include file="header.jsp" %>
<div class="internal-page">
    <div>
	<h1>Approved Leave Notification Receiver</h1>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="errors" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<s:elseif test="hasMessages()">
	    <s:set var="messages" value="messages" />
	    <%@ include file="messages.jsp" %>
	</s:elseif>
	<br />
	<s:form action="leave_receiver" id="form_id" method="post" >
	    <s:hidden name="action2" id="action2" value="" />
	    <s:if test="id != ''">
		<s:hidden name="id" id="r_id" value="%{id}" />
	    </s:if>
	    <s:if test="!hasDepartment()">
		<b>Pick a Department</b><br />
		<table style="border:none;spacing:none;">
		    <tr style="background-color:gainsboro;border:none">
			<td style="border:none"><b>Department</b></td>
			<td>
			    <s:select name="dept_id" value="%{dept_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick One" />
			</td>		
		    </tr>
		</table>
		<div class="button-group">
		    <s:submit name="action" type="button" value="Next" class="button" style="height:26px;width:150px" />
		</div>
	    </s:if>
	    <s:else>
		<s:if test="id == ''">
		    <b>New Notification Receiver</b><br />
		</s:if>
		<s:else>
		    <b>Change Notification Receiver</b><br />
		</s:else>
		<table style="border:none;spacing:none;">
		    <tr style="background-color:gainsboro;border:none">
			<td style="border:none"><b>Department</b></td>
			<td style="border:none"><b>Group</b></td>
			<td style="border:none"><b>Employee</b></td>
		    </tr>
		    <tr style="background-color:gainsboro;border:none;spacing:none;">
			<td style="border:none;padding-bottom:none;">
			    <s:if test="showAllDepts()">
				<s:select name="dept_id" value="%{dept_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick One" id="department_id_change" />
			    </s:if>
			    <s:else>
				<s:property value="department" />
			    </s:else>
			</td>			
			<td style="border:none;padding-bottom:none;">
			    <s:if test="hasGroups()">
				<s:select name="group_id" value="%{group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" id="group_id_set" />
			    </s:if>
			    <s:else>
				<select name="group_id" id="group_id_set"  disabled="disabled" >
				    <option value="-1">Pick A Group</option>
				</select>
			    </s:else>
			</td>
			<td style="border:none;padding-bottom:none;">
			    <s:if test="hasEmployees()">							<s:select name="emp_id" list="employees" listKey="id" listValue="full_name" headerKey="-1" headerValue="All" style="height:26px;width:150px" />		    
			    </s:if>
			    <s:else>
				&nbsp;&nbsp;
			    </s:else>
			</td>			
		    </tr>
		</table>
		<div class="button-group">
		    <s:if test="id == ''">		
			<s:submit name="action" type="button" value="Add" class="button" style="height:26px;width:150px" />
		    </s:if>
		    <s:else>
			<s:submit name="action" type="button" value="Save Changes" class="button" style="height:26px;width:150px" />
		    </s:else>
		</div>
	    </s:else>
	</s:form>
    </div>
    <br />
    <s:if test="hasReceivers()">
	<table>
	    <caption>Current Approved Leave Receivers</caption>
	    <tr>
		<th>&nbsp;</th>
		<th>Id </th>
		<th>Group </th>
		<th>Employee </th>
		<th>email</th>
		<th>Action</th>
	    </tr>
	    <s:iterator var="one" value="receivers" status="rowStatus">
		<s:if test="#rowStatus.count%2 == 0">
		    <tr style="background-color:#efefef">		
		</s:if>
		<s:else>
		    <tr>
		</s:else>	
		<td>&nbsp;</td>
		<td> <a href="<s:property value='#application.url' />leave_receiver.action?id=<s:property value='id' />">Edit</a>&nbsp;</td>
		<td><s:property value="groupName" /></td>		
		<td><s:property value="employeeName" /></td>
		<td><s:property value="email" /></td>		
		<td> <a href="<s:property value='#application.url' />leave_receiver.action?id=<s:property value='id' />&action=Delete">Delete</a></td>
		    </tr>
	    </s:iterator>
	</table>
    </s:if>
</div>
<%@  include file="footer.jsp" %>
