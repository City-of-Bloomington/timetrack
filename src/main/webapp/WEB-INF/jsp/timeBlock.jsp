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
			<s:if test="hasActionErrors()">
				<script>
				window.onunload = refreshParent;
				function refreshParent() {
					window.opener.location.reload();
					window.close();
				}
				</script>
			</s:if>
			<s:else>
				<script>
				window.onload = refreshParent;
				function refreshParent() {
					window.opener.location.reload();
					window.close();
				}
				</script>
			</s:else>
		</s:if>
		<s:else>
			<script>
			window.onload = setInputFocus;
			<s:if test="timeBlock.hourCode.record_method == 'Time'">
			function setInputFocus() {
				var inputElement = document.getElementById("time_in");
				inputElement.focus();
				inputElement.addEventListener("blur", function(event){
					if(inputElement.value.length == 0)
						inputElement.focus();
				}); 
			}
			</s:if>
			<s:else>
			function setInputFocus() {
			var inputElement = document.getElementById("hour_change");
				inputElement.focus();
				inputElement.addEventListener("blur", function(event){
					if(inputElement.value.length == 0)
						inputElement.focus();
				}); 
			}
		</s:else>
			</script>		
		</s:else>
		<s:if test="hasEmpAccruals()">		
			<s:iterator var="one" value="empAccruals">		
				<input type="hidden" id="<s:property value='related_hour_code_id' />_Hours" value="<s:property value='hours' />" />
				<input type="hidden" name="timeBlock.accrual_balance" value="<s:property value='related_hour_code_id' />_<s:property value='hours' />" />			
			</s:iterator>
		</s:if>
		<s:if test="timeBlock.id == ''">
			<h4>Add Time Block </h4>
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
		<div class="tbl_wheat">
			<fieldset>
				<table width="100%" border="0">
					<tr><td class="th_text">Date</td><td class="td_text"><s:property value="timeBlock.date" /></td></tr>
					<s:if test="hasMoreThanOneJob()">
						<tr>
							<td class="th_text">Job</td>
							<td class="td_text"><s:select name="timeBlock.job_id" value="%{timeBlock.job_id}" list="jobTasks" listKey="id" listValue="jobTask.position" headerKey="-1" headerValue="Pick Job Assignment" />
							</td>
						</tr>
					</s:if>
					<tr>
				<td class="th_text">Hour Code</td>
				<td class="th_text">
					<s:if test="hasHourCodes()">
						<s:select name="timeBlock.hour_code_id" value="%{timeBlock.id_compound}" list="hourCodes" listKey="id_compound" listValue="codeInfo" id="hour_code_select" tabindex="1" />
					</s:if>
				</td>
					</tr>
					<s:if test="timeBlock.hourCode.record_method == 'Time'">
						<tr id="div_time_in"><td class="th_text">IN</td><td class="td_text"><s:textfield name="timeBlock.time_in" value="%{timeBlock.time_in}" size="8" maxlength="8" id="time_in" tabindex="2" /> (hh:mm AM/PM)</td></tr>
						<tr id="div_time_out"><td class="th_text">OUT</td><td class="td_text"><s:textfield name="timeBlock.time_out" value="%{timeBlock.time_out}" size="8" maxlength="8" id="time_out" tabindex="3" /> (hh:mm AM/PM)</td></tr>
						<tr id="div_overnight"><td class="th_text">&nbsp;</td><td class="th_text"><s:checkbox name="timeBlock.overnight" value="%{timeBlock.overnight}" tabindex="4" id="time_overnight" /> Overnight Shift</td></tr>					
						<tr id="div_hours" style="display:none"><td class="th_text">Hours</td><td class="th_text"><s:textfield name="timeBlock.hours" value="%{timeBlock.hours}" size="5" maxlength="5" id="hour_change" tabindex="-1" />(dd.dd)</td></tr>
					</s:if>
					<s:else>
						<tr id="div_time_in" style="display:none"><td class="th_text">Start Time</td><td class="td_text"><s:textfield name="timeBlock.time_in" value="%{timeBlock.time_in}" size="8" maxlength="8" id="time_in" tabindex="-1" />(hh:mm) AM/PM</td></tr>
						<tr id="div_time_out" style="display:none"><td class="th_text">End Time</td><td class="td_text"><s:textfield name="timeBlock.time_out" value="%{timeBlock.time_out}" size="8" maxlength="8" id="time_out" tabindex="-1" />(hh:mm) AM/PM</td></tr>
						<tr id="div_overnight" style="display:none"><td class="th_text">&nbsp;</td><td class="th_text"><s:checkbox name="timeBlock.overnight" value="%{timeBlock.overnight}" tabindex="4" id="time_overnight" /> <b>Overnight Shift </b></td></tr>			
						<tr id="div_hours"><td class="th_text">Hours</td><td class="th_text"><s:textfield name="timeBlock.hours" value="%{timeBlock.hours}" size="5" maxlength="5" id="hour_change" tabindex="5" />(dd.dd)</td></tr>
					</s:else>
					<tr><td class="th_text">Repeat for the next </td><td class="th_text" align="left"><s:textfield name="timeBlock.repeat_count" value="%{timeBlock.repeat_count}" size="2" maxlength="2" tabindex="6" /> <b>days</b></td></tr>
					<tr><td class="th_text">&nbsp;</td><td class="th_text"><s:checkbox name="timeBlock.include_weekends" value="%{timeBlock.include_weekends}" tabindex="7" /> Include Weekend</td></tr>	
				</table>
				<table width="100%" border="0">
					<tr>
						<td align="center">
							<s:if test="timeBlock.id == ''">
								<s:submit name="action" type="button" value="Save" cssClass="button_link" tabindex="8" />
							</s:if>
							<s:else>
								<s:submit name="action" type="button" value="Save Changes" cssClass="button_link" tabindex="9" />
							</s:else>
						</td>
					</tr>
				</table>
			</fieldset>
		</div>
	</s:form>
	<br />
	<p style="text-align:center">
		<a href="#" onclick="javascript:window.close()" tabindex="10">Close</a>
	</p>
<%@ include file="footer.jsp" %>


