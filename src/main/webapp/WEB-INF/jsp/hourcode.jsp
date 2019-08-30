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
		<s:if test="hasErrors()">
			<s:set var="errors" value="%{errors}" />		
			<%@ include file="errors.jsp" %>
		</s:if>		
		<s:elseif test="hasMessages()">
			<s:set var="messages" value="%{messages}" />					
			<%@ include file="messages.jsp" %>
		</s:elseif>
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
				<label> <s:checkbox name="hourcode.reg_default" value="%{hourcode.reg_default}" />Default Regular </label> (each salary group need only one default regular)
			</div>
			<div class="form-group">
				<label> <s:checkbox name="hourcode.holidayRelated" value="%{hourcode.holidayRelated}" />Holiday Related </label> (can be used on holidays only)
			</div>			
			<div class="form-group">
				<label>Type:</label>
				<s:radio name="hourcode.type" value="%{hourcode.type}" list="types" />
			</div>
			<div class="form-group">
				<label>Default Monetary Amount ($):</label>
				<div class="date-range-picker">
					<div>			
						<s:textfield name="hourcode.defaultMonetaryAmount" value="%{hourcode.defaultMonetaryAmount}" size="6" maxlength="6" placeholder="ddd.dd"/>
					</div>
					<div>
						(for fixed amount earn codes such as ONCALL)
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Earn Factor:</label>
				<div class="date-range-picker">
					<div>			
						<s:textfield name="hourcode.earnFactor" value="%{hourcode.earnFactor}" size="6" maxlength="6" placeholder="dd.dd"/>
					</div>
					<div>
						(for Earn Codes only, such as CE1.0, CE1.5, HCE1.5, etc)
					</div>
				</div>
			</div>			
			<s:if test="hourcode.id != ''">
				<div class="form-group">
					<label>
						<s:checkbox name="hourcode.inactive" value="%{hourcode.inactive}" /> Inactive</label> (check to dissable)
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
