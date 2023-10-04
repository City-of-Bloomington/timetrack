<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="hourcodeExtraCondition" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="hourcodeCondition.id == ''">
	    <h1>New Earn Code Extra Restriction</h1>
	</s:if>
	
	<s:else>
	    <h1>Edit Earn Code Extra Restriction: <s:property value="hourcodeCondition.id" /></h1>
	    <s:hidden name="hourcodeCondition.id" value="%{hourcodeCondition.id}" />
	</s:else>
	<s:if test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />		
	    <%@ include file="errors.jsp" %>
	</s:if>		
	<s:elseif test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />					
	    <%@ include file="messages.jsp" %>
	</s:elseif>
	<div class="form-group">
	    <label>Earn Code</label>
	    <s:select name="hourcodeCondition.hourCode_id" value="%{hourcodeCondition.hourCode_id}" required="true" list="hourcodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Hour Code" />
	</div>
	<div class="form-group">
	    <label># Times Per Day Allowed </label>
	    <s:textfield name="hourcodeCondition.timesPerDay" value="%{hourcodeCondition.timesPerDay}" size="2" maxlength="2" />
	</div>
	<div class="form-group">
	    <label>Max Total ($) per Year </label>
	    <s:textfield name="hourcodeCondition.maxTotalPerYear" value="%{hourcodeCondition.maxTotalPerYear}" size="4" maxlength="4" />
	</div>
	<div class="form-group">
	    <label>Earn Code Association Type:</label>
	    <s:radio name="hourcodeCondition.hourCodeAssociateType" value="%{hourcodeCondition.hourCodeAssociateType}" list="types" />
	</div>
	<div class="form-group">
	    <label>Is Default Monetary Value Fixed? </label>
	    <s:checkbox name="hourcodeCondition.defaultValueFixed" value="%{hourcodeCondition.defaultValueFixed}" /> Yes 
	</div>
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
		<a href="<s:property value='#application.url' />hourcodeExtraCondition.action?" class="button">New Earn Code Extra Restriction</a>
		<s:submit name="action" type="button" value="Save Changes" />				
	    </s:else>
	</div>		
    </s:form>
    <s:if test="hasConditions()">
	<s:set var="hourcodeConditions" value="%{conditions}" />
	<s:set var="hourcodeConditionsTitle" value="hourcodeConditionsTitle" />
	<%@ include file="hourcodeExtraConditions.jsp" %>
    </s:if>
</div>
<%@ include file="footer.jsp" %>
