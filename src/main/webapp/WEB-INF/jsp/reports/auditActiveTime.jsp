<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page container clearfix settings">
	<h1>Active Time Entries Audit</h1>
	<s:form action="auditTime" id="form_id" method="post" >
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
			<li>You can search for the list of users who used timetrack on certain dates</li>
			<li>At least one date is required </li>
			<li>The output may include email only or name and email</li>
		</ul>
		<div class="width-one-half float-left">
			<div class="form-group">
				<label>First Date: </label>
				<div class="date-range-picker">
					<div>
					<s:textfield name="audit.start_date" value="%{audit.start_date}" cssClass="date" size="10" required="true" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Second Date: </label>
				<div class="date-range-picker">
					<div>
					<s:textfield name="audit.end_date" value="%{audit.end_date}" cssClass="date" size="10" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label>Include Employee Name: <s:checkbox name="audit.includeName" value="%{audit.includeName}" /> Yes </label>
			</div>
			<div class="button-group">
				<s:submit name="action" type="button" value="Submit" class="button"/>
			</div>
		</div>
	</s:form>
</div>
<div>
	<s:if test="action != ''">
		<s:if test="hasEntries()">
			<table>
				<s:iterator var="one" value="entries">
					<tr>
						<s:iterator var="two" value="#one">
							<td><s:property /></td>
						</s:iterator>
					</tr>
					</s:iterator>
			</table>
		</s:if>
	</s:if>
</div>			
<%@ include file="../footer.jsp" %>


