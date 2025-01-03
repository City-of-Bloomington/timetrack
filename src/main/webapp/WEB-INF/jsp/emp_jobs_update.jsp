<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="emp_jobs_update" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	
	<p>Employee Jobs Update</p>
	<%@ include file="strutMessages.jsp" %>
	<div class="width-one-half">
	    <p> Employee Jobs will be updated according to NW job configuration</p>
	    <p> Enter the New World date that the job configurations are active in the field below </p>
	    <div class="form-group">
		<label>Date </label>
		<s:textfield name="date" value="%{date}" size="10" maxlength="10" required="true" />
	    </div>	    
	    <s:submit name="action" accrual="button" value="Run" class="button"/>
	</div>
    </s:form>

</div>
<%@ include file="footer.jsp" %>
