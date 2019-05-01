<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="searchConditions" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<h1>Search Earn Code Restrictions</h1>
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="%{errors}" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>
		<div class="width-one-half">				
			<div class="form-group">
				<label>Department</label>
				<s:select name="dept_id" value="%{dept_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All"/>
			</div>
			<div class="form-group">
				<label>Salary Group</label>
				<s:select name="salary_group_id" value="%{salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="All" />
			</div>
			<div class="form-group">
				<label>Earn Code</label>
				<s:select name="hour_code_id" value="%{hour_code_id}" list="hourCodes" listKey="id" listValue="name" headerKey="-1" headerValue="All" />
			</div>			
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="button"/>
			</div>
		</div>
	</s:form>
	<s:if test="hasConditions()">
		<s:set var="hourcodeConditions" value="conditions" />
		<s:set var="conditionsTitle" value="conditionsTitle" />
		<%@ include file="hourcodeConditions.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
