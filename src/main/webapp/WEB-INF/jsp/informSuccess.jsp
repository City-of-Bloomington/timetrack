<%@ include file="header.jsp" %>

<div>
	<s:if test="hasErrors()">
		<s:set var="errors" value="errors" />
		<%@ include file="errors.jsp" %>		
	</s:if>
	<h1>Email Success</h1>
	<p>Your email was successfully sent.</p>
	
</div>
<%@ include file="footer.jsp" %>
