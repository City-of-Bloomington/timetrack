<%@  include file="header.jsp" %>
<div class="internal-page">
    <div>
	<h1>Leave Advance Search </h1>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="errors" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<s:elseif test="hasMessages()">
	    <s:set var="messages" value="messages" />
	    <%@ include file="messages.jsp" %>
	</s:elseif>
	<br />
	<s:form action="leave_history" id="form_id" method="post" >
	    <s:hidden name="action2" id="action2" value="" />	    
	    <table style="border:none;spacing:none;">
		<tr style="background-color:gainsboro;border:none">
		    <td style="border:0px"><label>&nbsp;&nbsp;</label></td>
		    <td style="border:none"><label>Department</label></td>
		    <td style="border:none"><label>Group</label></td>
		    <td style="border:none"><label>Employee</label></td>
		</tr>
		<tr style="background-color:gainsboro;border:none;spacing:none;">
		    <td style="border:none;padding-bottom:none;"><label>Filter by</label></td>
		    <td style="border:none;padding-bottom:none;">
			<s:if test="showAllDepts()">
			    <s:select name="dept_id" value="%{dept_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" id="department_id_change" />
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
				<option value="-1">All</option>
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
		<tr style="background-color:gainsboro;border:none">
		    <td style="border:0px"><label>&nbsp;&nbsp;</label></td>
		    <td style="border:none"><label>Date from</label></td>
		    <td style="border:none"><label>Date to</label></td>
		    <td style="border:0px"><label>&nbsp;&nbsp;</label></td>
		</tr>
		<tr style="background-color:gainsboro;border:none">
		    <td style="border:0px"><label>&nbsp;&nbsp;</label></td>
		    <td style="border:none;padding-bottom:none;"><s:textfield name="date_from" value="%{date_from}" type="date" pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" placeholder="MM/DD/YYYY" id="date_from" />
		    </td>
		    <td style="border:none;padding-bottom:none;"><s:textfield name="date_to" value="%{date_to}" type="date" pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" placeholder="MM/DD/YYYY" />
			
		    </td>
		    <td style="border:0px"><label>&nbsp;&nbsp;</label></td>
		</tr>
		<tr style="background-color:gainsboro;border:none">		
		    <td style="border:none;padding-bottom:none;"><s:submit name="action" type="button" value="Submit" class="button" style="height:26px;width:150px" />
		    </td>
		    <td style="border:0px" colspan="3">&nbsp;</td>
		</tr>
	    </table>
	</s:form>
    </div>
    <s:if test="hasLeaves()">
	<table>
	    <tr>
		<th>&nbsp;</th>
		<th>Employee </th>
		<th>Job Title</th>
		<th>Leave Date Range </th>
		<th>Total Hours</th>
		<th>Reviewer</th>	
	    </tr>
	    <s:iterator var="one" value="leaves" status="rowStatus">
		<s:if test="#rowStatus.count%2 == 0">
		    <tr style="background-color:#efefef">		
		</s:if>
		<s:else>
		    <tr>
		</s:else>	
		<td>&nbsp;</td>
		<td><s:property value="employee" /></td>
		<td><s:property value="jobTitle" /></td>
		<td><s:property value="date_range" /></td>
		<td><s:property value="totalHours" /></td>
		<td><s:property value="reviewer" /></td>	    
		    </tr>
	    </s:iterator>
	</table>
    </s:if>
</div>
<%@  include file="footer.jsp" %>
