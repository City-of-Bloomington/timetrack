<%@ include file="headerMin.jsp" %>
	<s:form action="timeBlock" id="form_id" method="post">
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

		<s:elseif test="timeBlock.hourCode.record_method == 'Time'">
			<script>
				window.onload = setInputFocus;
				function setInputFocus() {
					var inputElement = document.getElementById("time_in");
					inputElement.focus();
					inputElement.addEventListener("blur", function(event){
						if(inputElement.value.length == 0)
							inputElement.focus();
					});
					console.log("Time In: " + inputElement.value);
				}
			</script>
		</s:elseif>

		<s:else>
			<script>
				window.onload = setInputFocus;
				function setInputFocus() {
					var inputElement = document.getElementById("hour_change");
					inputElement.focus();
					inputElement.addEventListener("blur", function(event){
						if(inputElement.value == 0.0)
							inputElement.focus();
					});
					console.log("Hour Change: " + inputElement.value);
				}
			</script>
		</s:else>

		<s:if test="hasEmpAccruals()">
			<s:iterator var="one" value="empAccruals">
				<input type="hidden" id="<s:property value='related_hour_code_id' />_Hours" value="<s:property value='hours' />" />
				<input type="hidden" name="timeBlock.accrual_balance" value="<s:property value='related_hour_code_id' />_<s:property value='hours' />" />
			</s:iterator>
		</s:if>
		<s:hidden id="document_id" name="timeBlock.document_id" value="%{timeBlock.document_id}" />
		<s:hidden name="timeBlock.clock_in" value="%{timeBlock.clock_in}" />
		<s:hidden name="timeBlock.clock_out" value="%{timeBlock.clock_out}" />
		<s:hidden name="timeBlock.date" value="%{timeBlock.date}" />
		<s:hidden name="timeBlock.order_index" value="%{timeBlock.order_index}" />

		<s:if test="hasOneJobOnly()">
			<s:hidden name="timeBlock.job_id" value="%{selected_job_id}" />
		</s:if>

		<h1>
			<s:if test="timeBlock.id == ''">
				Add Time Block
			</s:if>
			<s:else>
				Edit Time Block
				<s:hidden id="timeBlock_id" name="timeBlock.id" value="%{timeBlock.id}" />
			</s:else>
			<small><s:property value="timeBlock.date" /></small>
		</h1>

		<%@ include file="strutMessages.jsp" %>

		<s:if test="hasMoreThanOneJob()">
			<div class="form-group">
				<label>Job</label>
				<s:select name="timeBlock.job_id" value="%{timeBlock.job_id}" list="jobTasks" listKey="id" listValue="jobTask.position" headerKey="-1" headerValue="Pick Job Assignment" />
			</div>
		</s:if>

		<div class="form-group">
			<label>Hour Code</label>
			<s:if test="hasHourCodes()">
				<s:select name="timeBlock.hour_code_id" value="%{timeBlock.id_compound}" list="hourCodes" listKey="id_compound" listValue="codeInfo" id="hour_code_select" tabindex="1" />
			</s:if>
		</div>

		<s:if test="timeBlock.hourCode.record_method == 'Time'">
			<div id="div_time_in" class="form-group">
				<label>Time In</label>
				<s:textfield name="timeBlock.time_in" value="%{timeBlock.time_in}" maxlength="8" id="time_in" tabindex="2" placeholder="(hh:mm AM/PM)" />
			</div>

			<div id="div_time_out" class="form-group">
				<label>Time Out</label>
				<s:textfield name="timeBlock.time_out" value="%{timeBlock.time_out}" maxlength="8" id="time_out" tabindex="3" placeholder="(hh:mm AM/PM)" />
			</div>

			<div id="div_overnight" class="form-group">
				<label>Overnight Shift</label>
				<s:checkbox name="timeBlock.overnight" value="%{timeBlock.overnight}" tabindex="4" id="time_overnight" />
			</div>

			<div id="div_hours" style="display:none" class="form-group">
				<label>Hours</label>
				<s:textfield name="timeBlock.hours" value="%{timeBlock.hours}" maxlength="5" id="hour_change" tabindex="-1" placeholder="(dd.dd)" />
			</div>
		</s:if>

		<s:else>
			<div id="div_time_in" style="display:none" class="form-group">
				<label>Start Time</label>
				<s:textfield name="timeBlock.time_in" value="%{timeBlock.time_in}" maxlength="8" id="time_in" tabindex="-1" />(hh:mm) AM/PM
			</div>

			<div id="div_time_out" style="display:none" class="form-group">
				<label>End Time</label>
				<s:textfield name="timeBlock.time_out" value="%{timeBlock.time_out}" maxlength="8" id="time_out" tabindex="-1" />(hh:mm) AM/PM
			</div>

			<div id="div_overnight" style="display:none" class="form-group">
				<label>Overnight Shift</label>
				<s:checkbox name="timeBlock.overnight" value="%{timeBlock.overnight}" tabindex="4" id="time_overnight" />yes
			</div>

			<div id="div_hours" class="form-group">
				<label>Hours</label>
				<s:textfield name="timeBlock.hours" value="%{timeBlock.hours}" maxlength="5" id="hour_change" tabindex="5" />(dd.dd)
			</div>
		</s:else>

		<div class="form-group">
			<label>Repeat Amount</label>
			<s:textfield name="timeBlock.repeat_count" value="%{timeBlock.repeat_count}" placeholder="Number of days" maxlength="2" tabindex="6" />
		</div>

		<div class="form-group">
			<label>Include Weekend</label>
			<s:checkbox name="timeBlock.include_weekends" value="%{timeBlock.include_weekends}" tabindex="7" />
		</div>

		<div class="button-group">
			<s:if test="timeBlock.id == ''">
				<s:submit name="action" type="submit" value="Save" class="button" tabindex="8" />
			</s:if>
			<s:else>
				<s:submit name="action" type="submit" value="Save Changes" class="button" tabindex="9" />
			</s:else>
			<a class="button" href="#" onclick="javascript:window.close()" tabindex="10">Cancel</a>
		</div>
	</s:form>
<%@ include file="footer.jsp" %>