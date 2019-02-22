<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page container clearfix settings">
	<h1>Employees and Jobs Report</h1>
	<div class="width-one-half float-left">
		<ul>
			<li>From this list you can create a csv file</li>
			<li>Remove all the rows that will not have any change</li>
			<li>Under the 'Change' column use the following key words only:</li>
				<ul>
					<li>'New' for new hire</li>
					<li>'Remove' to remove a job but keep other employee's other jobs</li>
					<li>'Add' to a add a new position for the employee. </li>
					<li>'Change' to change a wrong position and group to another job and group without losing time data</li>
				</ul>
				<li>User name is the username used to login</li>
				<li>ID card #, is the number on the badge given to the employee during training</li>
				<li>Employee NW #, is the New World employee number (check with HR)</li>
				<li>Effective date should 'ALWAYS' be the first day of a pay period</li>
		</ul>
		<s:form action="jobsReport" id="form_id" method="post" >
			<s:if test="hasDepartment()">
				<s:hidden name="department_id" value="%{department_id}" />
			</s:if>
			<s:if test="hasMessages()">
				<s:set var="messages" value="messages" />			
				<%@ include file="../messages.jsp" %>
			</s:if>
			<s:elseif test="hasErrors()">
				<s:set var="errors" value="errors" />			
				<%@ include file="../errors.jsp" %>
			</s:elseif>
			<div class="form-group">
				<label>Department</label>
				<s:if test="hasDepartment()">
					<s:property value="department" />
				</s:if>
				<s:else>
					<s:select name="department_id" value="%{department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" />					
				</s:else>
			</div>
			<div class="form-group">
				<label>Employment Type</label>
				<s:select name="employmentType" value="%{employmentType}" list="#{'-1':'All','Temp':'Temp Employee Only','Non Temp':'All Others'}" />
			</div>
			<div class="form-group">			
				<label>Output Type:</label>				
				<s:radio name="outputType" value="%{outputType}" list="#{'html':'Web page format','csv':'CSV format'}" />
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit"/>
			</div>
		</s:form>
	</div>		
	<s:if test="action != ''">
		<s:if test="hasJobs()">
			<s:set var="jobs" value="jobs" />
			<s:set var="jobsTitle" value="reportTitle" />
			<%@  include file="../jobsShort.jsp" %>
		</s:if>
	</s:if>
	<%@ include file="../footer.jsp" %>
</div>

