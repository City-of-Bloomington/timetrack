<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:if test="hasErrors()">
    <s:set var="errors" value="errors" />
    <%@ include file="errors.jsp" %>
  </s:if>
  <s:elseif test="hasMessages()">
      <s:set var="messages" value="messages" />
      <%@ include file="messages.jsp" %>
  </s:elseif>
  <s:form action="termRecipient" id="form_id" method="post" >
      <s:hidden name="action2" id="action2" value="" />
      <s:if test="recipient.id == ''">
	  <h1>New Termination Recipient</h1>
      </s:if>
      <s:else>
	  <h1>Edit Termination Recipient: <s:property value="%{recipient.name}" /> </h1>
	  <s:hidden name="recipient.id" value="%{recipient.id}" />
      </s:else>

      <div class="width-one-half">
	  <p><strong>Note:</strong>Enter employee full name of department name in the name field (required)</p>
	  <s:if test="recipient.id != ''">
	      <div class="form-group">
		  <label>ID</label>
		  <s:property value="recipient.id" />
	      </div>
	  </s:if>
	  
	  <div class="form-group">
	      <label>Name</label>
	      <s:textfield name="recipient.name" value="%{recipient.name}" size="50" maxlength="50" requiredLabel="true" required="true" />
	  </div>

	  <div class="form-group">
	      <label>Email</label>
	      <s:textfield name="recipient.email" value="%{recipient.email}" size="50" maxlength="80" required="true"/>
	  </div>
	  
	  <div class="form-group">
	      <s:if test="recipient.id == ''">
		  <s:submit name="action" type="button" value="Save" class="button"/>
	      </s:if>
	      <s:else>
		  <s:submit name="action" type="button" value="Save Changes" class="button"/>
	      </s:else>
	  </div>
      </div>
  </s:form>
  <s:if test="recipients != null">
      <s:set var="recipients" value="recipients" />
      <s:set var="recipientsTitle" value="recipientsTitle" />
      <%@ include file="term_recipients.jsp" %>
  </s:if>
</div>
<%@ include file="footer.jsp" %>
