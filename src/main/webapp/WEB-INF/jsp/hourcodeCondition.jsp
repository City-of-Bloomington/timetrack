<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="hourcodeCondition" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="hourcodeCondition.id == ''">
			<h1>New Earn Code Restriction</h1>
		</s:if>

		<s:else>
			<h1>Edit Earn Code Restriction: <s:property value="hourcodeCondition.name" /></h1>
			<s:hidden name="hourcodeCondition.id" value="%{hourcodeCondition.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

		<s:if test="hourcodeCondition.id != ''">
			<div class="form-group">
				<label>ID</label>
				<s:property value="%{hourcodeCondition.id}" />
			</div>
		</s:if>

		<div class="form-group">
			<label>Earn Code</label>
			<s:select name="hourcodeCondition.hour_code_id" value="%{hourcodeCondition.hour_code_id}" required="true" list="hourcodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Hour Code" />
		</div>
		<div class="form-group">
			<label>Salary Group</label>
			<s:select name="hourcodeCondition.salary_group_id" value="%{hourcodeCondition.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick salary group" required="true" />
		</div>
		<div class="form-group">
			<label>Department</label>
			<s:select name="hourcodeCondition.department_id" value="%{hourcodeCondition.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" />
		</div>
		<div class="form-group">
			<label>Group Department</label>
			<s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All" id="department_id_change"/>
		</div>		
		<s:if test="hourcodeCondition.id != ''">
			<div class="form-group">
				<label>Group</label>
				<s:select name="hourcodeCondition.group_id" value="%{hourcodeCondition.group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="All" /> (to pick a group pick a group department first) <br />
			</div>
		</s:if>
		<s:else>
			<div class="form-group">
				<label>Group</label>			
				<select name="hourcodeCondition.group_id" id="group_id_set"  disabled="disabled" >
					<option value="-1">All</option>
				</select>
			</div>
		</s:else>
		<s:if test="hourcodeCondition.id != ''">
			<div class="form-group">
				<label>Inactive ?</label>
				<s:checkbox name="hourcodeCondition.inactive" value="%{hourcodeCondition.inactive}" /> Yes (check to dissable)
			</div>
		</s:if>
		<div class="button-group">
			<s:if test="hourcodeCondition.id == ''">
				<s:submit name="action" type="button" value="Save" />
			</s:if>
			<s:else>
				<a href="<s:property value='#application.url' />hourcodeCondition.action?" class="button">New Earn Code Restriction</a>
				<s:submit name="action" type="button" value="Save Changes" />				
			</s:else>
			<a href="<s:property value='#application.url' />searchConditions.action?" class="button">Search Earn Code Restrictions</a>
		</div>		
	</s:form>
	<s:if test="hasConditions()">
		<s:set var="hourcodeConditions" value="%{conditions}" />
		<s:set var="hourcodeConditionsTitle" value="hourcodeConditionsTitle" />
		<%@ include file="hourcodeConditions.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
