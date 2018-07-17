<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="holiday" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="holiday.id == ''">
			<h1>New Holiday Type</h1>
		</s:if>

		<s:else>
			<h1>Edit Holiday Type: <s:property value="%{holiday.name}" /> </h1>
			<s:hidden name="holiday.id" value="%{holiday.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>
	  <div class="width-one-half">
			<div class="form-group">
				<label>Pick A Year</label>
				<s:select name="year" value="%{year}" list="years" onchange="doRefresh()"/>
			</div>

			<s:if test="holiday.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="holiday.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Date</label>
				<s:textfield name="holiday.date" value="%{holiday.date}" size="10" maxlength="10" requiredLabel="true" required="true" cssClass="date" />
			</div>

			<div class="form-group">
				<label>Name</label>
				<s:textfield name="holiday.description" value="%{holiday.description}" size="30" maxlength="70" requiredLabel="true" required="true" />
			</div>

			<s:if test="holiday.id == ''">
				<s:submit name="action" holiday="button" value="Save" class="button"/>
			</s:if>

			<s:else>
				<div class="button-group">
					<s:submit name="action" holiday="button" value="Delete" class="button"/>
					<s:submit name="action" holiday="button" value="Save Changes" class="button"/>
					<a href="<s:property value='#application.url'/>holiday.action" class="button" >New Holiday</a>					
				</div>
			</s:else>
		</div>
	</s:form>

	<s:if test="holidays != null">
		<s:set var="holidays" value="holidays" />
		<s:set var="holidaysTitle" value="holidaysTitle" />
		<%@  include file="holidays.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
