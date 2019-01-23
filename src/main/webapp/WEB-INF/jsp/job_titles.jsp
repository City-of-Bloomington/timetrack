<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="jobTitles" id="form_id" method="post">
		<h1>New World Employee Job Titles</h1>
		<p>Enter employee number and click on 'Find'</p>
	  <div class="width-one-half">
			<div class="form-group">
				<label>Employee Number</label>
				<s:textfield name="employeeNumber" value="%{employeeNumber}" size="10" />
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Find" class="button"/>
			</div>
		</div>
	</s:form>
</div>
<s:if test="hasJobTitles()">
	<ul>
		<s:iterator var="one" value="jobTitles">
			<li><s:property /></li>
		</s:iterator>
	</ul>
</s:if>
<%@ include file="footer.jsp" %>
