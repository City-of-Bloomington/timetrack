<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="hourcode" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="hourcode.id == ''">
			<h1>New Earn Code</h1>
		</s:if>
		<s:else>
			<h1>Edit Earn Code: <s:property value="hourcode.name" /></h1>
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
				<s:radio name="hourcode.record_method" value="%{hourcode.record_method}" list="#{'Time':'Time','Hours':'Hours','Monetary':'Monetary'}" />
			</div>

			<div class="form-group">
				<label>Related Accrual:</label>
				<s:select name="hourcode.accrual_id" value="%{hourcode.accrual_id}" list="accruals" listKey="id" listValue="name" headerKey="-1" headerValue="Pick related accrual" />
			</div>
			<div class="form-group">
				<label>Default Regular?:</label>
				<s:checkbox name="hourcode.reg_default" value="%{hourcode.reg_default}" /> Yes	(each salary group need only one default regular)
			</div>
			<div class="form-group">
				<label>Type:</label>
				<s:radio name="hourcode.type" value="%{hourcode.type}" list="types" />
			</div>
			<div class="form-group">
				<label>Default Monetary Amount:</label>
				$<s:textfield name="hourcode.defaultMonetaryAmount" value="%{hourcode.defaultMonetaryAmount}" size="8" maxlength="8" /> (for fixed amount hour codes such as ONCALL)
			</div>			
			<s:if test="hourcode.id != ''">
				<div class="form-group">
					<label>Inactive?:</label>
					<s:checkbox name="hourcode.inactive" value="%{hourcode.inactive}" /> Yes (check to dissable)
				</div>
			</s:if>
		</div>
		<div class="button-group">
			<s:if test="hourcode.id == ''">
				<s:submit name="action" type="button" value="Save" />
			</s:if><br />
			<s:else>
				<a href="<s:property value='#application.url' />hourcode.action?" class="button">New Hour Code </a>
				<s:submit name="action" type="button" value="Save Changes" />
			</s:else>
		</div>
	</s:form>
	<s:if test="hasHourcodes()">
		<s:set var="hourcodes" value="%{hourcodes}" />
		<s:set var="hourcodesTitle" value="hourcodesTitle" />
		<%@ include file="hourcodes.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
