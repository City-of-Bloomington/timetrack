<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="hourcode" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="hourcode.id == ''">
			<h1>New Hour Code</h1>
		</s:if>
		<s:else>
			<h1>Edit Hour Code: <s:property value="hourcode.name" /></h1>
			<s:hidden name="hourcode.id" value="%{hourcode.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

		<div class="width-one-half">
			<s:if test="hourcode.id != ''">
				<div class="form-group">
					<label>ID:</label>
					<s:property value="%{hourcode.id}" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Name:</label>
				<s:textfield name="hourcode.name" value="%{hourcode.name}" required="true" size="20" maxlength="20" />
			</div>

			<div class="form-group">
				<label>Description:</label>
				<s:textfield name="hourcode.description" value="%{hourcode.description}" size="30" maxlength="80" />
			</div>

			<div class="form-group">
				<label>Recording Method:</label>
				<s:radio name="hourcode.record_method" value="%{hourcode.record_method}" list="#{'Time':'Time','Hours':'Hours'}" />
			</div>

			<div class="form-group">
				<label>Related Accrual:</label>
				<s:select name="hourcode.accrual_id" value="%{hourcode.accrual_id}" list="accruals" listKey="id" listValue="name" headerKey="-1" headerValue="Pick related accrual" />
			</div>

			<div class="form-group">
				<label>Count as regular pay?:</label>
				<s:checkbox name="hourcode.count_as_regular_pay" value="%{hourcode.count_as_regular_pay}" /> Yes
			</div>

			<div class="form-group">
				<label>Default Regular?:</label>
				<s:radio name="hourcode.reg_default" value="%{hourcode.reg_default}" list="#{'0':'Yes','1':'No'}"/> (each salary group need only one default regular)
			</div>

			<s:if test="hourcode.id != ''">
				<div class="form-group">
					<label>Inactive?:</label>
					<s:checkbox name="hourcode.inactive" value="%{hourcode.inactive}" /> Yes (check to dissable)
				</div>
			</s:if>
		</div>

		<s:if test="hourcode.id == ''">
			<s:submit name="action" type="button" value="Save" />
		</s:if><br />

		<s:else>
			<div class="button-group">
				<a href="<s:property value='#application.url' />hourcode.action?" class="button">New Hour Code </a>
				<s:submit name="action" type="button" value="Save Changes" />
			</div>
		</s:else>
	</s:form>

	<s:if test="hasHourcodes()">
		<s:set var="hourcodes" value="%{hourcodes}" />
		<s:set var="hourcodesTitle" value="hourcodesTitle" />
		<%@ include file="hourcodes.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>