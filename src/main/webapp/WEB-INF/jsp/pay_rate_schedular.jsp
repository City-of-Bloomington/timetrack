<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="payRateSchedule" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<h1>Employee Weekly Pay Rate Schedular</h1>
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="%{errors}" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>		

	  <p>This function is designed to be run only once to schedule importing employees weekly hourly pay rate from another system (New World for example) every week on the Sunday at certain time. Run the schedular to start once and it will continue forever. <br />You can run an import if you want to update or add new rates to certain pay period.</p>
		<div class="width-one-half">
			<div class="button-group">
				<s:submit name="action" accrual="button" value="Schedule" class="button"/>
			</div>
			<ul>
			    <li>To do instant import of pay rates for certain department</li>
			    <li>Select the related department.</li>				
			    <li>Enter Effective Date </li>
			    <li>Cick on 'Import Now'</li>
			</ul>
			<s:if test="hasDepts()">
			    <div class="form-group">
				<label for="dept_id">Department</label>
				<s:select name="dept_ref_id" value="%{dept_ref_id}" list="depts" listKey="ref_id" listValue="name" headerKey="-1" headerValue="All" id="dept_id" />
			    </div>
			</s:if>
			<div class="form-group">			
			    <label for="date_id">Effective Date</label>
			    <s:textfield name="rate_date" value="%{rate_date}" size="10" id="date_id" />
			</div>
			<div class="button-group">
				<s:submit name="action" value="Initial Start" class="button"/>

			</div>
			<div class="button-group">
			    <s:submit name="action" value="Fill Previous Records" class="button"/>

			</div>			
			<div class="button-group">
				<s:submit name="action" value="Import Now" class="button"/>

			</div>
		</div>
	</s:form>
<%@ include file="footer.jsp" %>
