<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="annuance" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="annuance.id == ''">
	    <h1>New Annuancement</h1>
	</s:if>
	<s:else>
	    <h1>Edit Annuancement: <s:property value="%{annuance.id}" /> </h1>
	    <s:hidden name="annuance.id" value="%{annuance.id}" />
	</s:else>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />			
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />			
	    <%@ include file="errors.jsp" %>
	</s:elseif>
	<div class="width-one-half">
	    <p><strong>Note:</strong> Annunacement Text and Expire Date fields are required </p>
	    <div class="form-group">
		<label>Annuancement Text*</label>
		<s:textfield name="annuance.annuanceText" value="%{annuance.annuanceText}" size="30" maxlength="160" requiredLabel="true" required="true" />
	    </div>
	    <div class="form-group">
		<label>Annuancement Url</label>
		<s:textfield name="annuance.annuanceUrl" value="%{annuance.annuanceUrl}" size="30" maxlength="160" />(example: https://bloomington.in.gov)
	    </div>
	    <div class="form-group">
		<label>Start Date</label>
		<s:textfield name="annuance.startDate" value="%{annuance.startDate}" size="10" maxlength="10" />(mm/dd/yyyy, The start date of the annuancement to show up)
	    </div>
	    <div class="form-group">
		<label>Expire Date*</label>
		<s:textfield name="annuance.expireDate" value="%{annuance.expireDate}" size="10" maxlength="10" requiredField="true" required="true" />(mm/dd/yyyy)
	    </div>
	    <div>
		<s:if test="annuance.id == ''">
		    <s:submit name="action" accrual="button" value="Save" class="button"/>
		</s:if>
		<s:else>
		    <s:submit name="action" accrual="button" value="Save Changes" class="button"/>
		</s:else>
	    </div>
	</div>	    
    </s:form>
    <s:if test="annuancements != null">
	<s:set var="annuancements" value="annuancements" />
	<s:set var="annuancementsTitle" value="annuancementsTitle" />
	<%@ include file="annuancements.jsp" %>
    </s:if>
    <%@ include file="footer.jsp" %>
