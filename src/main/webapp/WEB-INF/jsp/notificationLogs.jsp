<%@ include file="header.jsp" %>
<div class="internal-page">
	<h1>Most Recent Automated Notification Logs</h1>	
	<s:if test="hasLogs()">
	<s:set var="logs" value="logs" />
	<%@ include file="notification_logs.jsp" %>
</s:if>
<%@ include file="footer.jsp" %>
