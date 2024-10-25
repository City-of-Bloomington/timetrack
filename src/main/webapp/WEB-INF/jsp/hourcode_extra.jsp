<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="hourcode_extra" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="extra.id == ''">
	    <h1>New Hour Code Additional Restriction</h1>
	</s:if>
	
	<s:else>
	    <h1>Edit Hour Code Additional Restriction: <s:property value="extra.id" /></h1>
	    <s:hidden name="extra.id" value="%{extra.id}" />
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
	    <label># Times Per Day Allowed </label>
	    <s:textfield name="extra.timesPerDay" value="%{extra.timesPerDay}" size="2" maxlength="2" />
	</div>
	<div class="form-group">
	    <label>Max Total ($) per Year </label>
	    <s:textfield name="extra.maxTotalPerYear" value="%{extra.maxTotalPerYear}" size="4" maxlength="4" />
	</div>
	<div class="form-group">
	    <label>Earn Code Association Type:</label>
	    <s:radio name="extra.hourCodeAssociateType" value="%{extra.hourCodeAssociateType}" list="types" />
	</div>
	<div class="form-group">
	    <label>Is Default Monetary Value Fixed? </label>
	    <s:checkbox name="extra.defaultValueFixed" value="%{extra.defaultValueFixed}" /> Yes 
	</div>
	<s:if test="extra.id != ''">
	    <div class="form-group">
		<label>Add Hour Code </label>
		<s:select name="extra.hour_code_id" value="%{extra.hour_code_id}" list="hourCodes" listKey="id" listValue="codeInfo" headerKey="-1" headerValue="Select one to add" />
	    </div>
	</s:if>
	<s:if test="extra.id != ''">
	    <div class="form-group">
		<label>Inactive ?</label>
		<s:checkbox name="extra.inactive" value="%{extra.inactive}" /> Yes (check to dissable)
	    </div>
	    <s:if test="extra.hasHourCodes()">
		<label>Included Hour Codes (select to delete)</label>
		<ul>
		<s:iterator var="one" value="extra.hourCodes">		
		    <li><input type="checkbox" name="extra.delete_code_id" value="<s:property value='id' />"><s:property value="codeInfo" /></input></li>
		</s:iterator>
		</ul>
	    </s:if>
	</s:if>
	
	<div class="button-group">
	    <s:if test="extra.id == ''">
		<s:submit name="action" type="button" value="Save" />
	    </s:if>
	    <s:else>
		<s:submit name="action" type="button" value="Save Changes" />
	    </s:else>
	</div>		
    </s:form>
    <s:if test="hasExtras()">
	<s:set var="hourcode_extras" value="%{extras}" />
	<s:set var="hourcode_extras_title" value="extrasTitle" />
	<%@ include file="hourcode_extras.jsp" %>
    </s:if>
</div>
<%@ include file="footer.jsp" %>
