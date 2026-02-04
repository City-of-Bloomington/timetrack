<h1><s:property value="department" /> Employees</h1>
<s:if test="hasEmpsWithNoEmpNum()">
    <p>The following employees Do not have employee number and will not be imported <br />
	You may add their employees' number and run this process again.
    </p>
    <s:set var="employees" value="empsWithNoEmpNum" />
    <%@ include file="../employees.jsp" %>
</s:if>
<s:if test="hasNoDataEmployees()" >
    <table width="50%">
	<caption>Employees with no time entries</caption>
	<s:iterator value="noDataEmployees" var="one" status="row">
	    <tr><td class="td_text" align="right"><s:property value="#row.index+1" /> - </td><td class="td_text">&nbsp;&nbsp;<s:property /></td></tr>
	</s:iterator>
    </table>
</s:if>
<s:if test="hasDailyBlocks()">
    <table width="100%">
	<tr>
	    <td class="th_text">Employee </td>
	    <td class="th_text">Employee Number</td>
	    <td class="th_text">Date</td>
	    <td class="th_text">NW Earn Code</td>
	    <td class="th_text">Hours</td>
	    <td class="th_text">Amount</td>
	    <td class="th_text">Job Title</td>
	</tr>
	<s:iterator value="dailyBlocks" var="one" >
	    <tr>
		<td class="td_text"><s:property value="empFullName" /></td>
		<td class="td_text"><s:property value="empNumber" /></td>
		<td class="td_text"><s:property value="date" /></td>
		<td class="td_text"><s:property value="nwCode" /></td>
		<td class="td_text" align="right"><s:property value="hours" /></td>
		<td class="td_text" align="right"><s:property value="amount" /></td>
		<td class="td_text"><s:property value="jobTitle" /></td>
	    </tr>
	</s:iterator>
    </table>
</s:if>



