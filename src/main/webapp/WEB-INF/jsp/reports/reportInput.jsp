<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<div class="internal-page container clearfix settings">
    <h1>Report</h1>
    <s:form action="report" id="form_id" method="post" >
	<s:if test="hasErrors()">
	    <div class="errors">
		<s:set var="errors" value="%{errors}" />
		<%@ include file="../errors.jsp" %>
	    </div>
	</s:if>
	<s:elseif test="hasMessages()">
	    <s:set var="messages" value="%{messages}" />
	    <%@ include file="../messages.jsp" %>
	</s:elseif>
	<ul>
	    <li>You can specify salary group</li>	    
	    <li>You can query time details by choosing either the year and quarter or by entering date range.</li>
	    <li>Pick from the available 'Earn Codes' in the left pane and click 'Add', continue adding one by one</li>
	    <li>If you chose an earn code by mistake, click on it in the right pange and click on 'Remove'</li>
	    <li>For output type, we suggest that you run the 'Web page HTML' type first so that you get an idea about the numbers. If you are OK with these numbers then you choose the 'CSV' type.</li>
	</ul>
	<div class="width-one-half float-left">
	    <s:if test="hasSalaryGroups()">
		<div class="form-group">
		    <label>Salary Groups</label>
		    <s:select name="report.salary_group_id" value="%{report.salary_group_id}" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="All" />
		</div>
	    </s:if>	    
	    <s:if test="hasDepts()">
		<div class="form-group">
		    <label>Department</label>
		    <s:select name="report.department_id" value="%{report.department_id}" list="depts" listKey="id" listValue="name" headerKey="-1" headerValue="All" />
		</div>
	    </s:if>
	    <div class="form-group">
		<label>Quarter Selection: </label>
		<s:select name="report.quarter" value="%{report.quarter}" list="#{'-1':'Pick quarter','1':'First','2':'Second','3':'Third','4':'Forth'}" /> Year:<s:select name="report.year" value="%{report.year}" list="years" headerKey="-1" headerValue="Pick Year" />
	    </div>
	    <div class="form-group">
		<label>Date, from: (mm/dd/yyyy)</label>
		<div class="date-range-picker">
		    <div>									
			<s:textfield name="report.date_from" value="%{report.date_from}" cssClass="date" size="10" />
		    </div>
		</div>
	    </div>
	    <div class="form-group">
		<label>Date, to: (mm/dd/yyyy)</label>
		<div class="date-range-picker">
		    <div>									
			<s:textfield name="report.date_to" value="%{report.date_to}" cssClass="date" size="10" />
		    </div>
		</div>
	    </div>
	    <div>
		<table>
		    <tr>
			<td>
			    <div class="form-group">
				<label>Earn Codes</label>
				<s:if test="hasAvailableCodes()">
				    
				    <s:select name="addCode" value="%{addCode}" list="availableCodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick to Add" size="10" />
				</s:if>
				<s:else>
				    <select name="addCode" size="10">
					<option value="-1">No Code Available</option>
				    </select>
				</s:else>
			    </div>
			</td>
			<td>
			    <input type='submit' name='action' value='Add >>' />
			     <br />
			    <input type='submit' name='action' value='Remove <<'/>
			    <br />
			</td>
			<td>
			    <div class="form-group">
				<label>Selected Earn Codes</label>
				<s:if test="hasSelectedCodes()">
				    <s:select name="removeCode" value="%{removeCode}" list="selectedCodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick to Remove" size="10" />
				</s:if>
				<s:else>
				    <select name="removeCode" size="10">
					<option value="-1">No Code Available</option>
				    </select>
				</s:else>
			    </div>
			</td>
		    </tr>
		</table>
	    </div>
	    <div class="form-group">
		<label>Output Type:</label>
		<s:radio name="report.type" value="%{report.type}" list="#{'html':'Web page HTML','csv':'CSV format'}" />
	    </div>
	    <div class="button-group">
		<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
	    </div>
	</div>
    </s:form>
</div>
<s:if test="action != ''">
    <s:if test="report.hasEntries()">
	<s:set var="hasEntries" value="'true'" />
	<s:set var="reportTitle" value="reportTitle" />
	<s:set var="mapEntries" value="report.mapEntries" />
	<s:set var="hoursSums" value="report.hoursSums" />
	<s:set var="totalHours" value="report.totalHours" />
    </s:if>
    <s:if test="report.hasDailyEntries()">
	<s:set var="hasDaily" value="'true'" />
	<s:set var="dailyEntries" value="report.dailyEntries" />
    </s:if>
    <s:if test="report.hasAnyEntries()">
	<%@  include file="asaReport.jsp" %>
    </s:if>
</s:if>
<%@ include file="../footer.jsp" %>




