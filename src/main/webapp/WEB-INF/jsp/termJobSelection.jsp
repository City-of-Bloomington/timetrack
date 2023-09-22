<%@ include file="header.jsp" %>
<div class="internal-page">
    
    <s:form action="terminateJobs" id="form_id" method="post">
	<h1>Job Termination Selection</h1>
	<input type="hidden" name="job_id" id="job_id"
	       value="<s:property value='job_id' />"  />
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />
	    <%@ include file="errors.jsp" %>
	</s:if>
	<ul>
	    <li>Select the last day of pay period </li>
	    <li>Select the jobs you want to terminate. </li>
	    <li>Click Next to terminate the selected job(s).</li>
	    <li>If you  want to to do final tremination click on 'Final Termination' to go to the temination form.</li>
	</ul>
	<div>
	    <div class="form-group">
		<label>Employee</label>
		<s:property value="job.employee" />
	    </div>
	    <s:if test="hasOneJobOnly()">
		<div class="form-group">
		    <label>Job</label>
		    <s:property value="job" />
		</div>
	    </s:if>
	    <div class="form-group">
		<label>Last Pay Period Ending Date</label>
		<s:select name="last_pay_period_date" value="%{last_pay_period_date}" list="payPeriods" listKey="endDate" listValue="endDate" headerKey="-1" headerValue="Pick End Date" required="required" />
	    </div>	    
	</div>
	<s:if test="hasJobs()">
	    <table class="width-full">
		<thead>
		    <tr>
			<th>ID</th>
			<th>Position</th>
			<th>Salary Group</th>
			<th>Employee</th>
			<th>Group</th>
			<th>Effective Date</th>
			<th>Weekly Reg Hrs</th>
		    </tr>
		</thead>
		<tbody>
		    <s:iterator var="one" value="%{jobs}">
			<tr>
			    <td><input type="checkbox" name="selected_job_id" value="<s:property value='id' />" /></td>
			    <td><s:property value="position" /></td>
			    <td><s:property value="salaryGroup" /></td>
			    <td><s:property value="employee" /></td>
			    <td><s:property value="group" /></a></td>
			    <td><s:property value="effective_date" /></td>
			    <td><s:property value="weekly_regular_hours" /></td>
			</tr>
		    </s:iterator>
		</tbody>
	    </table>
	    <div class="button-group">
		<s:submit name="action" type="button" value="Next" class="button"/>
	    </div>
	    <div class="button-group">
		<s:submit name="action" type="button" value="Final Termination" class="button"/> (all jobs)
	    </div>	    
	</s:if>
    </s:form>
</div>
<%@ include file="footer.jsp" %>
