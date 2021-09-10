<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="codeReasonSchedule" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<h1>Police Code Reason Schedular</h1>
		<s:if test="hasMessages()">
			<s:set var="messages" value="%{messages}" />			
			<%@ include file="messages.jsp" %>
		</s:if>
		<s:elseif test="hasErrors()">
			<s:set var="errors" value="%{errors}" />			
			<%@ include file="errors.jsp" %>
		</s:elseif>		

	  <p>This function is designed to be run only once to schedule creating an xls file for Police department employees work times with earn code and reason. Run these if the 'Next Schedule Date' and 'Previous Schedule Date' are not shown.
		</p>
		<div class="width-one-half">
			<s:if test="hasPrevDates()">
				<div class="form-group">
					<label>Next Schedule Date</label>
					<s:property value="%{next_date}" />
				</div>

				<div class="form-group">
					<label>Previous Schedule Date</label>
					<s:property value="%{prev_date}" />
				</div>
			</s:if>

			<div class="button-group">
				<s:submit name="action" value="Schedule" class="button"/>
			</div>
			<p>
			If you want to create a fresh file of the data or the last scheduled run was not successful, you may click on 'Run Now' to create the file.
			</p>
			<div class="button-group">
				<s:submit name="action" value="Run Now" class="button"/>
			</div>
		</div>
	</s:form>
<%@ include file="footer.jsp" %>
