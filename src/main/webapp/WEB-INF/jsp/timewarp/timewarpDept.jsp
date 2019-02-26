<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="timewarp" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="source" value="source" />
	<s:hidden name="type" value="single" />	
	<s:hidden name="department_id" value="%{department_id}" />
	<input type="hidden" name="type" value="single" />	
	<s:if test="hasActionErrors()">
		<div class="errors">
	    <s:actionerror/>
		</div>
	</s:if>
	<s:elseif test="hasActionMessages()">
		<div class="welcome">
	    <s:actionmessage/>
		</div>
	</s:elseif>
	<table width="100%" border="1">
		<tr><td align="center">
			<table width="90%" border="0">
				<tr>
					<td align="right" class="td_text">Pay Period </td>
					<td align="left" class="td_text">&nbsp;&nbsp;<s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" onchange="doRefresh()" />
					</td>
					<td align="right" class="td_text"><a href="<s:property value='#application.url' />timewarp.action?type=single&pay_period_id=<s:property value='currentPayPeriod.id' />">Current Pay Period</a></td>
				</tr>
				<tr>
					<td align="right" class="td_text">Department </td>
					<td align="left" class="td_text">&nbsp;&nbsp;<s:property value="department" /></td>
				</tr>
				<tr>
					<td class="th_text">&nbsp;</td>
					<td class="th_text">&nbsp;</td>
					<td class="th_text"><s:submit name="action" type="button" value="Submit" class="fn1-btn"/></td>
				</tr>
			</table>
		</td>
		</tr>
	</table>
</s:form>
<s:if test="action != ''">
	<%@  include file="timewarpDetails.jsp" %>	
</s:if>
<%@ include file="../footer.jsp" %>


