<%@  include file="headerMin.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="timeBlock" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="action != ''" >
		<script>
    window.onunload = refreshParent;
    function refreshParent() {
      window.opener.location.reload();
			window.close();
    }
		</script>
	</s:if>
	<s:if test="hasEmpAccruals()">		
		<s:iterator var="one" value="empAccruals">		
			<input type="hidden" id="<s:property value='related_hour_code_id' />_Hours" value="<s:property value='hours' />" />
			<input type="hidden" name="timeBlock.accrual_balance" value="<s:property value='related_hour_code_id' />_<s:property value='hours' />" />			
		</s:iterator>
	</s:if>
	<s:if test="timeBlock.id == ''">
		<h4>New Time Block </h4>
	</s:if>
	<s:else>
		<h4>Edit Time Block </h4>
		<s:hidden id="timeBlock_id" name="timeBlock.id" value="%{timeBlock.id}" />
		
	</s:else>
	<s:hidden id="document_id" name="timeBlock.document_id" value="%{timeBlock.document_id}" />
	<s:hidden name="timeBlock.clock_in" value="%{timeBlock.clock_in}" />
	<s:hidden name="timeBlock.clock_out" value="%{timeBlock.clock_out}" />	
	<s:hidden name="timeBlock.date" value="%{timeBlock.date}" />
	<s:hidden name="timeBlock.order_index" value="%{timeBlock.order_index}" />	
	<s:if test="hasOneJobOnly()">
		<s:hidden name="timeBlock.job_id" value="%{selected_job_id}" />
	</s:if>
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
	<table width="100%" border="0">
	<tr><td>Date</td><td><s:property value="timeBlock.date" /></td></tr>
	<s:if test="hasMoreThanOneJob()">
		<tr>
			<td>Job</td>
			<td><s:select name="timeBlock.job_id" value="%{timeBlock.job_id}" list="jobTasks" listKey="id" listValue="jobTask.position" headerKey="-1" headerValue="Pick Job Assignment" />
			</td>
		</tr>
	</s:if>
	<tr>
		<td>Hour Code</td>
		<td>
			<s:if test="hasHourCodes()">
				<s:select name="timeBlock.hour_code_id" value="%{timeBlock.id_compound}" list="hourCodes" listKey="id_compound" listValue="name" id="hour_code_select" />
			</s:if>
		</td>
	</tr>
	<s:if test="timeBlock.hourCode.record_method == 'Time'">
		<tr id="div_time_in"><td>IN</td><td><s:textfield name="timeBlock.time_in" value="%{timeBlock.time_in}" size="8" maxlength="8" id="time_in" />(hh:mm AM/PM)</td></tr>
		<tr id="div_time_out"><td>OUT</td><td><s:textfield name="timeBlock.time_out" value="%{timeBlock.time_out}" size="8" maxlength="8" id="time_out" />(hh:mm AM/PM)</td></tr>
		<tr id="div_hours" style="display:none"><td>Hours</td><td><s:textfield name="timeBlock.hours" value="%{timeBlock.hours}" size="5" maxlength="5" id="div_hour_change" />(dd.dd)</td></tr>
	</s:if>
	<s:else>
		<tr id="div_time_in" style="display:none"><td>Start Time</td><td><s:textfield name="timeBlock.time_in" value="%{timeBlock.time_in}" size="8" maxlength="8" id="time_in" />(hh:mm) AM/PM</td></tr>
		<tr id="div_time_out" style="display:none"><td>End Time</td><td><s:textfield name="timeBlock.time_out" value="%{timeBlock.time_out}" size="8" maxlength="8" id="time_out" />(hh:mm) AM/PM</td></tr>		
		<tr id="div_hours"><td>Hours</td><td><s:textfield name="timeBlock.hours" value="%{timeBlock.hours}" size="5" maxlength="5" id="div_hour_change" />(dd.dd)</td></tr>
	</s:else>
	<tr><td colspan="2">Repeat for the next <s:textfield name="timeBlock.repeat_count" value="%{timeBlock.repeat_count}" size="2" maxlength="2" /> days</td></tr>
	<tr><td>&nbsp;</td><td><s:checkbox name="timeBlock.include_weekends" value="%{timeBlock.include_weekends}" /> Include Weekend</td></tr>	
</table>
<s:if test="timeBlock.id == ''">
	<s:submit name="action" type="button" value="Save" cssClass="popit_btn" />
</s:if>
<s:else>
	<s:submit name="action" type="button" value="Save Changes" cssClass="popit_btn" />
</s:else>
</s:form>
<s href="#" onclick="javascript:window.close()">Close</a>
<%@ include file="footer.jsp" %>


