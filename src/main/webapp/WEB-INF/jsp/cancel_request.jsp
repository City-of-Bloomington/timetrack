<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="requestCancel" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="requst.document_id" value="%{requst.document_id}" />
	<s:hidden name="requst.requestBy_id" value="%{requst.requestBy_id}" />	
	<s:if test="request.id == ''">
	    <h1>Time Entry Change Request</h1>
	</s:if>
	<s:else>
	    <h1>Entry Change Request: <s:property value="%{request.id}" />
		- <s:property value="requst.requestDate" />
	    </h1>
	    <s:hidden name="requst.id" value="%{requst.id}" />
	    <s:hidden name="requst.requestDate" value="%{requst.requestDate}" />	    
	</s:else>
	<ul>
	    <li>Please enter the date and times of the timesheet that you
	    are requesting to edit in the 'Request Details' below. </li>
	    <li>Your supervisor will receive and email about your request to cancel his/her approval so that you can change your time entries.  </li>
	    <li>Your supervisor(s) may agree with your request and perform cancel the approval.</li>
	    <li>They may contact you for further detials</li>
	</ul>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="messages" />		
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
	    <s:set var="errors" value="errors" />		
	    <%@ include file="errors.jsp" %>
	</s:elseif>
	<div class="width-one-half">
	    <s:if test="requst.id != ''">
		<div class="form-group">
		    <label>ID: 
			<s:property value="requst.id" />
		    </label>
		</div>
	    </s:if>
	    <div class="form-group">
		<label>
		    <a href="<s:property value='#application.url' />timeDetails.action?document_id=<s:property value='requst.document_id' />">Back to Time details page.</a></label>
	    </div>	    
	    <div class="form-group">
		<label>Request Details </label>
		<s:textarea
		    name="requst.requestReason"
		    value="%{requst.requestReason}"
		    rows="5"
		    cols="50"
		    wrap="wrap"
		    required="true" />		
	    </div>
	    <s:if test="requst.id == ''">
		<s:submit name="action" accrual="button" value="Submit" class="button"/>
	    </s:if>
	</div>
    </s:form>
</div>
<%@ include file="footer.jsp" %>
