<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="searchJobs" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<h1>Search Jobs</h1>
		<s:if test="hasMessages()">
			<s:set var="messages" value="messages" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="errors" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>

	  <p>For employee field you can use key words of first name or last name to pick from the auto complete list.</p>

		<div class="width-one-half">
			<div class="form-group">
				<label>Employee Name</label>
				<s:textfield name="joblst.employee_name" value="%{joblst.employee_name}" id="employee_name2" size="20" /><br />
				ID:<s:textfield name="joblst.employee_id" value="%{joblst.employee_id}" id="employee_id" size="6" maxlength="6" />
			</div>
			<div class="form-group">
				<label>Department</label>
				<s:select name="joblst.department_id" value="%{joblst.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" id="department_id_change"/>
			</div>
			<div class="form-group">
				<label>Group</label>
				<s:if test="joblst.hasGroups()">
					<s:select name="joblst.group_id" value="%{joblst.group_id}" id="group_id_set"  list="%{joblst.groups}" listKey="id" listValue="name" headerKey="-1" headerValue="All" />
				</s:if>
				<s:else>
					<select name="joblst.group_id" value="" id="group_id_set"  disabled="disabled">
						<option value="-1">All</option>
					</select><br />
				</s:else>
			</div>
			<div class="form-group">
				<label>Job ID</label>
				<s:textfield name="joblst.id" value="%{joblst.id}" size="5" />
			</div>

			<div class="form-group">
				<label>Position</label>
				<s:select name="joblst.position_id" value="%{joblst.position_id}" list="positions" listKey="id" listValue="name" headerKey="-1" headerValue="All" required="true" />*
			</div>

			<div class="form-group">
				<label>Salary Group</label>
				<s:select name="joblst.salary_group_id" value="%{joblst.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="All" required="true" />*
			</div>

			<div class="form-group">
				<label>Date, from: (mm/dd/yyyy)</label>
				<div class="date-range-picker">
					<div>					
						<s:textfield name="joblst.date_from" value="%{joblst.date_from}" size="10" cssClass="date" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Date, to: (mm/dd/yyyy)</label>			
				<div class="date-range-picker">
					<div>			
						<s:textfield name="joblst.date_to" value="%{joblst.date_to}" size="10" cssClass="date" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Date Type:</label>
				<s:radio name="joblst.which_date" value="%{joblst.which_date}" list="#{'j.effective_date':'Effective Date','j.expire_date':'Expire Date'}" />
			</div>

			<div class="form-group">
				<label>Required Punch Clock?</label>
				<s:radio name="joblst.clock_status" value="%{joblst.clock_status}" list="#{'-1':'All','y':'Yes','n':'No'}" />
			</div>

			<div class="form-group">
				<label>Active ?</label>
				<s:radio name="joblst.active_status" value="%{joblst.active_status}" list="#{'-1':'All','Active':'Yes','Inactive':'No'}" />
			</div>

			<div class="button-group">
				<a href="<s:property value='#application.url' />jobTask.action" class="button">New Job</a>
				<s:submit name="action" type="button" value="Submit" class="button"/>
			</div>
		</div>
	</s:form>
	<s:if test="hasJobs()">
		<s:set var="jobTasks" value="jobs" />
		<s:set var="jobsTitle" value="jobsTitle" />
		<%@ include file="jobTasks.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
