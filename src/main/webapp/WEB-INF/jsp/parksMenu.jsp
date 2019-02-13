<%@  include file="header.jsp" %>

<div class="internal-page container clearfix settings">
	<h1>Parks Menu Options</h1>
	<s:if test="hasErrors()">
	  <div class="errors">
			<s:set var="errors" value="errors" />
			<%@ include file="errors.jsp" %>
	  </div>
	</s:if>
	<s:if test="#session != null && #session.user.canRunParksReport()">
		<div class="width-one-half float-left">
			<h2>Target Employee</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>switch.action">Change Target Employee</a></li>
			</ul>
			<h2>Change & Update</h2>			
				<li><a href="<s:property value='#application.url'/>jobTitles.action">Parks Employee Jobs Need Intervention</a></li>				
			</ul>						
			<h2>Reports</h2>
			<ul>
				<li><a href="<s:property value='#application.url'/>parksJobReport.action">Parks Current Employee Jobs</a></li>
			</ul>
		</div>
	</s:if>
</div>

<%@  include file="footer.jsp" %>
