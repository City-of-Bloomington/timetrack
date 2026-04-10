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
				<li>Select the pay period </li>
				<li>Select the week (1, or 2)</li>
				<li>Cick on 'Import Now'</li>
			</ul>
			<s:if test="hasDepts()">
			    <div class="form-group">
				<label for="dept_id">Department</label>
				<s:select name="dept_ref_id" value="%{dept_ref_id}" list="depts" listKey="ref_id" listValue="name" headerKey="-1" headerValue="Pick Dept" id="dept_id" />
			    </div>
			</s:if>
			<div class="form-group">			
			    <label for="pay_period_id">Pay Period</label>
			    <s:select name="payPeriod_id" value="%{payPeriod_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Pick Pay Period" id="pay_period_id" />
			</div>
			<div class="form-group">			
			    <label for="week_no">Week Number </label>
			    <s:select name="weekNo" value="%{weekNo}" list="{'1','2'}"  headerKey="-1" headerValue="Pick Week #" id="week_no" />
			</div>			
		
			<div class="button-group">
				<s:submit name="action" accrual="button" value="Import Now" class="button"/>

			</div>
		</div>
	</s:form>
<%@ include file="footer.jsp" %>
