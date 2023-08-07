<%@  include file="header.jsp" %>
<div class="internal-page">
    <h1> Search Terminations </h1>
    <s:if test="hasErrors()">
	<s:set var="errors" value="errors" />
	<%@ include file="errors.jsp" %>		
    </s:if>
    <div class="width-one-half">
	<p>You may narrow the list by Department, group and/or date range </p>
	<s:form action="search_terminations" id="form_id" method="post">
	    <div class="form-group">
		<label>Department:</label>
		<div>
		    <s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All"  />

		</div>
	    </div>
	    <div class="form-group">
		<label>Date Range From:</label>
		<div class="date-range-picker">
		    <div>
			<s:textfield name="date_from" value="%{date_from}" size="10" maxlength="10" />(mm/dd/yyyy)
		    </div>
		</div>
	    </div>
	    <div class="form-group">
		<label>Date Range To:</label>
		<div class="date-range-picker">
		    <div>				
			<s:textfield name="date_to" value="%{date_to}" size="10" maxlength="10" />(mm/dd/yyyy)
		    </div>
		</div>
	    </div>
	    <div class="form-group">
		<label>Notification Status:</label>
		<div>
		    <s:select name="status" value="%{status}" list="#{'-1':'All','sent':'Sent','not-sent':'Not Sent'}"  headerKey="-1" headerValue="All"  />
		</div>
	    </div>	    
	    <div class="button-group">
		<s:submit name="action" type="button" value="Submit" />
	    </div>
	</s:form>
    </div>
    <s:if test="hasTerms()">
	<table class="email-logs">
	    <thead>
		<tr>
		    <th>ID </th>
		    <th>Employee</th>
		    <th>Submitted By</th>
		    <th>Last Payperiod Date</th>
		    <th>Department</th>
		    <th>Group</th>
		    <th>Job(s)</th>
		    <th>Notification Status</th>
		</tr>
	    </thead>
	    <tbody>
		<s:iterator var="one" value="terms">
		    <tr>
			<td><s:property value="id" /></td>
			<td><s:property value="submitted_by" /></td>
			<td><s:property value="last_pay_period_date" /></td>
			<td><s:property value="department" /></td>
			<td><s:property value="termination_id" /></td>
			<td><s:property value="group" /></td>
			<td><s:property value="jobsTitles" /></td>
			<td><s:if test="recipients_informed != null">Yes</s:if>
			    <s:else>No</s:else></td>
		    </tr>
		</s:iterator>
	    </tbody>
	</table>
	</s:if>
	
	<s:else>
	    <br /><br />
	    <h3> No Terminations found </h3>
	</s:else>

</div>
<%@ include file="footer.jsp" %>
