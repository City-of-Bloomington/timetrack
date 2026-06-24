<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="shiftDifferential" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h1>Employee Shift Differentail (Police)</h1>
	<s:if test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />			
	    <%@ include file="messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
	    <s:set var="errors" value="%{errors}" />			
	    <%@ include file="errors.jsp" %>
	</s:elseif>		
	<div class="width-one-half">
	    <div class="button-group">
		<s:submit name="action" accrual="button" value="Schedule" class="button"/>
	    </div>
	    <ul>
		<li>To run the process now </li>
		<li>Select the related department.</li>				
		<li>Enter Effective Date </li>
		<li>Cick on 'Submit'</li>
	    </ul>
	    <s:if test="hasDepts()">
		<div class="form-group">
		    <label for="dept_id">Department</label>
		    <s:select name="dept_ref_id" value="%{dept_ref_id}" list="depts" listKey="ref_id" listValue="name" headerKey="-1" headerValue="All" id="dept_id" />
		</div>
	    </s:if>
	    <div class="form-group">			
		<label for="date_id">Effective Date</label>
		<s:textfield name="effectiveDate" value="%{effecitveDate}" size="10" id="date_id" />
	    </div>
	    <div class="button-group">
		<s:submit name="action" value="Submit" class="button"/>
		
	    </div>
	</div>
    </s:form>
</div>
<%@ include file="footer.jsp" %>
