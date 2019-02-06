<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="jobTitles" id="form_id" method="post">
		<h1>Parks Employee Job Titles</h1>
		<p>Some of the following job titles are not found in NW for some
			reason, such as typo, or missing portion of job title.</p>
		<p>We have three categories of employees and their jobs as follows</p>
		<ul>
			<li> 1-Jobs that can be deleted </li>
			<li> 2-Jobs that we recommend to be expired, since not used any more and
				not in New World</li>
			<li> 3-Employees and their jobs are not found in New World, we can
				expire the employees record and their jobs </li>
		</ul>
		<p>For Each category (if any) mark the checkbox in front of the job
			or employee and hit the 'Submint' button</p>
	  <div class="full-width">
			<s:if test="hasEmpJobCanDelete()">
				<table>
					<caption>1-Employee Jobs that can be deleted</caption>
					<tr><th>Employee</th><th>Job ID</th><th>Job Title</tr>
					<s:iterator var="block" value="empJobCanDelete">
						<s:set var="blockKey" value="#block.key" />
						<s:set var="blockSet" value="#block.value" />
						<s:iterator var="two" value="#blockSet">
							<tr>
								<td><s:property value="#blockKey" /> </td>								
								<td><input type="checkbox" name="del_jobs" value='<s:property value="#two.id" />' /> <s:property value="#two.id" /> </td>
								<td><s:property value="#two.name" /></td>								
							</tr>
						</s:iterator>
					</s:iterator>
				</table>
			</s:if>
			<s:if test="hasEmpJobNeedUpdate()">
				<table>
					<caption>2-Employee Jobs Need to be Updated</caption>
					<tr><th>Employee</th><th>Job ID</th><th>Job Title</th></tr>
					<s:iterator var="block" value="empJobNeedUpdate">
						<s:set var="blockKey" value="#block.key" />
						<s:set var="blockSet" value="#block.value" />						
						<s:iterator var="two" value="#blockSet">
							<tr>
								<td><s:property value="#blockKey" /> </td>
								<td><input type="checkbox" name="exp_jobs" value='<s:property value="#two.id" />' /> <s:property value="#two.id" /> </td>
								<td><s:property value="#two.name" /></td>								
							</tr>
						</s:iterator>
					</s:iterator>
				</table>
			</s:if>
			<s:if test="hasEmpNotFoundNewWorld()">
				<table>
					<caption>3-Employees that are not in New World, These employees and their jobs can be expired</caption>
					<tr><th>Employee</th><th>Job Titles</th></tr>
					<s:iterator var="block" value="empNotFoundNewWorld">
						<s:set var="blockKey" value="#block.key" />
						<s:set var="blockSet" value="#block.value" />
						<tr>
							<td><input type="checkbox" name="exp_emps" value='<s:property value="#blockKey.id" />' /> <s:property value="#blockKey" /> </td>
							<td>
								<s:iterator var="two" value="#blockSet" status="status" >
									<s:property value="#two.name" /> <s:if test="!#status.last" >,</s:if>
								</s:iterator>
							</td>
						</tr>
					</s:iterator>
				</table>
			</s:if>			
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="button"/>
			</div>
		</div>
	</s:form>
</div>

<%@ include file="footer.jsp" %>
