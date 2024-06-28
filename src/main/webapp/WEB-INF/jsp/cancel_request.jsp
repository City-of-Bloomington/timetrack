<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="requestCancel" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="request.document_id" value="%{request.document_id}" />
	<s:hidden name="request.requestBy_id" value="%{request.requstBy_id}" />	
	<s:if test="request.id == ''">
	    <h1>New Cancel Request</h1>
	</s:if>
	<s:else>
	    <h1>Cancel Request: <s:property value="%{request.id}" />
		- <s:property value="request.requestDate" />
	    </h1>
	    <s:hidden name="request.id" value="%{request.id}" />
	    <s:hidden name="request.requestDate" value="%{request.requstDate}" />	    
	</s:else>
	<p>Please enter the date and times of the timesheet that you
	    are requesting to edit. The reason(s) for change. </p>
	<p>Your supervisor will receive and email about your request to cancel his/her approval so that you can edit your times </p>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="messages" />		
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
	    <s:set var="errors" value="errors" />		
	    <%@ include file="errors.jsp" %>
	</s:elseif>
	<div class="width-one-half">
	    <s:if test="request.id != ''">
		<div class="form-group">
		    <label>ID</label>
		    <s:property value="request.id" />
		</div>
	    </s:if>
	    <div class="form-group">
		<label>IP network address</label>
		<s:textarea name="request.request_reason" value="%{request.request_reason}" rows="10" cols="50" wrap="wrap" /> *
	    </div>
	    <s:if test="request.id == ''">
		<s:submit name="action" accrual="button" value="Submint" class="button"/>
	    </s:if>
	</div>
    </s:form>
</div>
<%@ include file="footer.jsp" %>
