<%@  include file="header.jsp" %>
<div class="internal-page">
	<h1> Email Logs Listing</h1>
	<s:if test="hasErrors()">
		<s:set var="errors" value="errors" />
		<%@ include file="errors.jsp" %>		
	</s:if>
	<div class="width-one-half">
		<s:form action="inform" id="form_id" method="post">
			<div class="form-group">
				<label>Date Range From:</label>
				<div class="date-range-picker">
					<div>
						<s:textfield name="date_from" value="%{date_from}" size="10" maxlength="10" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Date Range To:</label>
				<div class="date-range-picker">
					<div>				
						<s:textfield name="date_to" value="%{date_to}" size="10" maxlength="10" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Page Size (rows):</label>
				<div class="date-range-picker">
					<div>				
						<s:textfield name="pageSize" value="%{pageSize}" size="3" maxlength="3" />
					</div>
				</div>
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Find" />
			</div>
		</s:form>
	</div>
	<s:if test="hasEmailLogs()">
		<table class="email-logs">
			<thead>
				<tr>
					<th>Date Time</th>
					<th>Type</th>
					<th>Performed By</th>
					<th>From</th>
					<th>Recipients</th>
					<th>Subject</th>
					<th>Message</th>
					<th>Status</th>
					<th>Send Errors</th>
				</tr>
			</thead>

			<tbody>
				<s:iterator var="one" value="emailLogs">
					<tr>
						<td><s:property value="dateTime" /></td>
						<td><s:property value="type" /></td>
						<td><s:property value="runBy" /></td>
						<td><s:property value="emailFrom" /></td>
						<td><s:property value="receipiants" /></td>
						<td><s:property value="subject" /></td>
						<td><s:property value="textMessage" /></td>
						<td><s:property value="status" /></td>
						<td><s:property value="sendErrors" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</s:if>

	<s:else>
		<h3> No email logs found </h3>
	</s:else>
</div>
<%@ include file="footer.jsp" %>
