<%@ include file="header.jsp" %>
<div class="internal-page">
	<div class="width-one-half">	
		<s:form action="empAccrual" id="form_id" method="post">
			<s:hidden name="employee_selected_id" id="action2" value="%{employee_selected_id}" />
			<s:hidden name="empAccrual.employee_id" id="action2" value="%{employee_selected_id}" />
			<s:if test="empAccrual.id == ''">
				<h1>New Employee Accrual</h1>
			</s:if>
			<s:else>
				<h1>Edit Employee Accrual: <s:property value="empAccrual.id" /></h1>
				<s:hidden name="empAccrual.id" value="%{empAccrual.id}" />
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
			<div class="form-group">
				<label>Employee</label>
				<s:property value="empAccrual.employee" />
			</div>
			<s:if test="empAccrual.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="%{empAccrual.id}" />
				</div>
			</s:if>
			<s:if test="hasAccruals()">
				<div class="form-group">
					<label>Accrual</label>
					<s:select name="empAccrual.accrual_id" value="%{empAccrual.accrual_id}" list="accruals" listKey="id" listValue="name" headerKey="-1" headerValue="Pick accrual" />*
				</div>
			</s:if>
			
			<div class="form-group">
				<label>Total Hours</label>
				<s:textfield name="empAccrual.hours" value="%{empAccrual.hours}" size="5" maxlength="5" />
			</div>
			
			<div class="form-group">
				<label>Date</label>
				<s:textfield name="empAccrual.date" value="%{empAccrual.date}" size="10" maxlength="10" cssClass="date" />
			</div>
				
			<s:if test="empAccrual.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/>
			</s:if>
				
			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url' />empAccrual.action?employee_selected_id=<s:property value='employee_selected_id' />" class="button">New Employee Accrual</a>
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</s:form>			
	</div>
	<s:if test="hasAccruals()">
		<s:set var="empAccruals" value="%{empAccruals}" />
		<s:set var="empAccrualsTitle" value="empAccrualsTitle" />
		<%@ include file="empAccruals.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
