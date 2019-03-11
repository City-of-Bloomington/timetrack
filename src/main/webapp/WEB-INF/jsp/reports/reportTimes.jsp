<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<div class="internal-page container clearfix settings">
	<h1>Employees Time Details</h1>
	<s:form action="reportTimes" id="form_id" method="post" >
		<s:if test="report.hasDepartment()">
			<s:hidden name="report.department_id" value="%{report.department_id}" />
		</s:if>
	<s:if test="hasErrors()">
		<s:set var="errors" value="errors" />
		<%@ include file="../errors.jsp" %>
	</s:if>
	<s:elseif test="hasMessages()">
		<s:set var="messages" value="messages" />
		<%@ include file="../messages.jsp" %>
	</s:elseif>
	<ul>
		<li>You can quary time details by choosing either the year and quarter or by entering date range.</li>
		<li>For output type, we suggest that you run the 'Web page HTML' type first so that you get an idea about the numbers. If you are OK with these numbers then you choose the 'CSV' type.</li>
	</ul>
	<div class="width-one-half float-left">	
		<div class="form-group">
			<label>Department: </label>
			<s:if test="report.hasDepartment()">
				<s:property value="%{report.department}" />
			</s:if>
			<s:else>
				<s:select name="report.department_id" value="%{report.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" />
			</s:else>
		</div>
		<div class="form-group">
			<label>Employment Type</label>
			<s:select name="report.employmentType" value="%{report.employmentType}" list="#{'-1':'All','Temp':'Temp Employee Only','All Other':'All Others'}" />
		</div>					

		<div class="form-group">
			<label>Quarter Selection: </label>
					<s:select name="report.quarter" value="%{report.quarter}" list="#{'-1':'Pick quarter','1':'First','2':'Second','3':'Third','4':'Forth'}" /> Year:<s:select name="report.year" value="%{report.year}" list="years" headerKey="-1" headerValue="Pick Year" />
		</div>
		<div class="form-group">
			<label>Date, from: </label>
			<s:textfield name="report.date_from" value="%{report.date_from}" cssClass="date" size="10" /> to:  
							<s:textfield name="report.date_to" value="%{report.date_to}" cssClass="date" size="10" />
		</div>

		<div class="form-group">
			<label>Output Type:</label>			
			<s:radio name="report.type" value="%{report.type}" list="#{'html':'Web page HTML','csv':'CSV format'}" />
		</div>
		<div class="button-group">
			<s:submit name="action" type="button" value="Submit" class="fn1-btn"/></td>
		</div>
	</div>
	</s:form>
</div>
<s:if test="action != '' && hasData()">
	<table><caption>Employees Time Details</caption>
		<s:iterator var="one" value="arrAll">
			<tr>
				<s:iterator var="two" value="#one">
					<td><s:property  /></td>
				</s:iterator>
			</tr>
		</s:iterator>
	</table>
</s:if>

<%@ include file="../footer.jsp" %>

