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
		<input type="hidden" name="department_id" id="department_id"
			value="<s:property value='department_id' />"  />
		<s:if test="hasErrors()">
			<s:set var="errors" value="errors" />
			<%@ include file="../errors.jsp" %>
		</s:if>
		<s:if test="hasMessages()">
			<s:set var="messages" value="messages" />
			<%@ include file="../messages.jsp" %>
		</s:if>
	<ul>
		<li>You can query time details by choosing either
			<ul>
				<li>Pay period,</li>
				<li>Year and quarter, </li>
				<li>Or by date range.</li>
			</ul>
		</li>
		<li>For output type, we suggest that you run the 'Web page HTML' type first so that you get an idea about the numbers. If you are OK with these numbers then you choose the 'CSV' type.
		</li>
	</ul>
	<div class="width-one-half float-left">
				<div class="form-group">
			<label>Department</label>
			<s:select name="dept_id"
								value="%{dept_id}"
								list="departments"
								listKey="id" listValue="name" headerKey="-1"
								headerValue="Pick Department"
								id="department_id_change" />
		</div>
		<div class="form-group">
			<label>Group</label>
			<s:if test="hasDept()">
				<s:select
					name="group_id"
					id="group_id_set"
					value="%{group_id}" list="groups"
					listKey="id"
					listValue="name"
					headerKey="-1"
					headerValue="All" />
			</s:if>
			<s:else>
				<select name="group_id" id="group_id_set"
					value="<s:property value='group_id' />"
					disabled="disabled">
					<option value="-1">All</option>
				</select>(To pick a group you need to pick a department first)
			</s:else>
		</div>		
		<div class="form-group">
			<label>Salary Group</label>
			<s:select name="salaryGroup_id" value="%{salaryGroup_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="All" />
		</div>
		<div class="form-group">
      <label>Pay Period</label>
        <s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="All" />
		</div>
		<div class="form-group">
			<label>Quarter Selection: </label>
					<s:select name="quarter" value="%{quarter}" list="#{'-1':'Pick quarter','1':'First','2':'Second','3':'Third','4':'Forth'}" /> Year:<s:select name="year" value="%{year}" list="years" headerKey="-1" headerValue="Pick Year" />
		</div>
		<div class="form-group">
			<label>Date, from: </label>
			<s:textfield name="date_from" value="%{date_from}" cssClass="date" size="10" /> to:
			<s:textfield name="date_to" value="%{date_to}" cssClass="date" size="10" />
		</div>
		<div class="form-group">
			<label>Output Type:</label>
			<s:radio name="outputType" value="%{outputType}" list="#{'html':'Web page HTML','csv':'CSV format'}" />
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


