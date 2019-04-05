<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="contribute" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="contribute.id == ''">
			<h1>Accrual Contributing Earn Code</h1>
		</s:if>
		<s:else>
			<h1>Edit Accrual Contribute: <s:property value="contribute.name" /></h1>
			<s:hidden name="contribute.id" value="%{contribute.id}" />
		</s:else>
		<s:if test="hasMessages()">
			<s:set var="messages" value="messages" />
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="errors" />
			<%@ include file="errors.jsp" %>
		</s:elseif>
		<ul>
			<li>Select the earn code that will contribute to related accrual,</li>
			<li>Next select the related accrual</li>
			<li>for example earn code CE1.0 contribute to accrual CUA </li>			
			<li>Factor is the multiple factor that will be used to multiply the number
				of hours by</li>
			<li>for example factor (1.0) will one hour per hour worked</li>
			<li>the factor 1.5 will 1.5 multiplied hy the hours worked,
				so 2 hours worked
				will become 3 hours contributed to accruals.</li>
			<li>Currently you can use Comp time and holiday comp time for contribution</li>
		</ul>
		<div class="width-one-half">
			<s:if test="contribute.id != ''">
				<div class="form-group">
					<label>ID:</label>
					<s:property value="%{contribute.id}" />
				</div>
			</s:if>
			<div class="form-group">
				<label>Name:</label>
				<s:textfield name="contribute.name" value="%{contribute.name}" required="true" size="30" maxlength="80" />
			</div>
			<div class="form-group">
				<label>Earn Code Contributer:</label>
				<s:select name="contribute.hourCode_id" value="%{contribute.hourCode_id}" list="hourCodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick earn code" />
			</div>
			<div class="form-group">
				<label>Related Accrual:</label>
				<s:select name="contribute.accrual_id" value="%{contribute.accrual_id}" list="accruals" listKey="id" listValue="name" headerKey="-1" headerValue="Pick related accrual" />
			</div>
			<div class="form-group">
				<label>Multiple Factor:</label>
				<s:textfield name="contribute.factor" value="%{contribute.factor}" required="true" size="3" maxlength="3" />(1, 1.5 or 2)
			</div>
		</div>
		<s:if test="contribute.id == ''">
			<s:submit name="action" type="button" value="Save" />
		</s:if><br />
		<s:else>
			<div class="button-group">
				<a href="<s:property value='#application.url' />contribute.action?" class="button">New Accrual Contributer </a>
				<s:submit name="action" type="button" value="Save Changes" />
			</div>
		</s:else>
	</s:form>
	<s:if test="hasContributes()">
		<s:set var="contributes" value="%{contributes}" />
		<s:set var="contributesTitle" value="contributesTitle" />
		<%@ include file="accrual_contributes.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
