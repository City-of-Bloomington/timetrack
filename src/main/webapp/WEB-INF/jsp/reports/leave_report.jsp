<%@  include file="../header.jsp" %>
<div class="internal-page">
    <div>
	<h1>Departmental Leave Report </h1>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="errors" />
	    <%@ include file="../errors.jsp" %>
	</s:if>
	<s:elseif test="hasMessages()">
	    <s:set var="messages" value="messages" />
	    <%@ include file="../messages.jsp" %>
	</s:elseif>
	<br />
	<s:form action="leave_report" id="form_id" method="post" >
	    <s:hidden name="action2" id="action2" value="" />	    
	    <table style="border:none;spacing:none;">
		<tr style="background-color:gainsboro;border:none">
		    <td style="border:none"><b>Department</b></td>
		    <td style="border:none"><b>Group</b></td>
		    <td style="border:none"><b>Employee</b></td>
		</tr>
		<tr style="background-color:gainsboro;border:none;spacing:none;">
		    <td style="border:none;padding-bottom:none;">
			<s:if test="showAllDepts()">
			    <s:select name="dept_id" value="%{dept_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" id="department_id_change" />
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
		    <td style="border:0px"><b>Pay Period</b></td>
		    <td style="border:none"><b>Date from</b></td>
		    <td style="border:none"><b>Date to</b></td>
		</tr>
		<tr style="background-color:gainsboro;border:none">
		    <td style="border:none;padding-bottom:none;">
        	    <s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="All" />		    
		    </td>
		    <td style="border:none;padding-bottom:none;"><s:textfield name="date_from" value="%{date_from}" type="date" pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" placeholder="MM/DD/YYYY" id="date_from" />
		    </td>
		    <td style="border:none;padding-bottom:none;"><s:textfield name="date_to" value="%{date_to}" type="date" pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" placeholder="MM/DD/YYYY" />
			
		    </td>
		</tr>
	    </table>
	     <div class="button-group">
		    <s:submit name="action" type="button" value="Submit" class="button" style="height:26px;width:150px" />
	     </div>
	</s:form>
    </div>
    <br />
    <s:if test="hasLeaves()">
	<table>
	    <tr>
		<th>&nbsp;</th>
		<th>Request Date</th>
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
		<td><s:property value="requestDate" /></td>		
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
<%@  include file="../footer.jsp" %>
