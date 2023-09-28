<%@ include file="headerMin.jsp" %>

<div class="time-block">
     	<s:form action="timeBlock" id="form_id" class="time-block-form" method="post"	>
		<s:hidden id="document_id" name="timeBlock.document_id" value="%{timeBlock.document_id}" />
		<s:hidden id="salary_group_id" name="timeBlock.salary_group_id" value="%{timeBlock.salary_group_id}" />
		<s:hidden id="group_id" name="timeBlock.group_id" value="%{timeBlock.group_id}" />
		<s:if test="timeBlock.id == ''">
			<input type="hidden" name="action" value="Save">
		</s:if>
		<s:else>
			<s:hidden id="timeBlock_id" name="timeBlock.id" value="%{timeBlock.id}" />
			<s:hidden name="timeBlock.date" value="%{timeBlock.date}" />
			<s:hidden name="timeBlock.order_index" value="%{timeBlock.order_index}" />
			<s:hidden name="timeBlock.clock_in" value="%{timeBlock.clock_in}" />
			<s:hidden name="timeBlock.clock_out" value="%{timeBlock.clock_out}" />
			<s:hidden name="timeBlock.hour_code_id_old" value="%{timeBlock.hour_code_id}" />
			<input type="hidden" id="<s:property value='timeBlock.hour_code_id' />_Hours_old" name="timeBlock.hours_old"
				     value="<s:property value='timeBlock.hours' />" />
			<input type="hidden" name="action" value="Save Changes">
		</s:else>
		<s:if test="hasEmpAccruals()">
		    <s:iterator var="one" value="empAccruals">
			<input type="hidden" id="<s:property value='related_hour_code_id' />_Hours"
			       value="<s:property value='hours' />" />
			<input type="hidden" name="timeBlock.accrual_balance"
			       value="<s:property value='related_hour_code_id' />_<s:property value='hours' />" />
		    </s:iterator>
		</s:if>
		<s:if test="hasMonetaryHourCodes()">
		    <s:iterator var="one" value="monetaryHourCodes">
			<input type="hidden" id="<s:property value='id' />_Monetary"
			       value="<s:property value='defaultMonetaryAmount' />" />
			</s:iterator>
		</s:if>
		<h1>
		    <s:if test="timeBlock.id == ''">
			Add Time Block
		    </s:if>
		    <s:else>
			Edit Time Block
		    </s:else>
		    <small>Date:
			<s:property value="timeBlock.date" /></small>
		</h1>
		<div class="alert">
			<p></p>
		</div>
		<s:if test="timeBlock.id == ''">
		    <div class="form-group" style="border-bottom: none;">
			<label>Date Range</label>
			<div class="date-range-picker">
			    <div>
				<label for="timeBlock.start_date">From</label>
				<s:textfield name="timeBlock.start_date" type="date" value="%{timeBlock.start_date}"
					     pattern="[0-9]{2}-[0-9]{2}-[0-9]{4}" placeholder="MM-DD-YYYY" id="start" />
			    </div>
			    <div>
				<label for="timeBlock.end_date">To</label>
				<s:textfield name="timeBlock.end_date" type="date" value="%{timeBlock.end_date}"
					     pattern="[0-9]{2}-[0-9]{2}-[0-9]{4}" placeholder="MM-DD-YYYY" id="end" />
			    </div>
			</div>
		    </div>
		    <div class="form-group">
			<label>&nbsp;</label>
			<s:checkbox name="timeBlock.include_weekends" value="%{timeBlock.include_weekends}" /> Include Weekends
			</div>
		</s:if>
		<div class="form-group">
		    <label>Earn Code</label>
		    <s:if test="hasHourCodes()">
			<s:select name="timeBlock.hour_code_id" value="%{timeBlock.id_compound}" list="hourCodes" listKey="id_compound"
					listValue="codeInfo" id="hour_code_select" />
		    </s:if>
		</div>
		<s:if test="timeBlock.hasEarnReasons()">
		    <div class="form-group" id="reason_div_id">
			<label>Earn Reason</label>
			<s:select name="timeBlock.earn_code_reason_id" value="%{timeBlock.earn_code_reason_id}"
					list="timeBlock.earnReasons" listKey="id" listValue="description" id="select_reason_id" />
		    </div>
		</s:if>
		<s:else>
		    <div class="form-group" id="reason_div_id" style="display:none;">
			<label>Earn Reason</label>
			<select name="timeBlock.earn_code_reason_id" value="" id="select_reason_id" disabled="disabled">
			    <option value="-1">Select a Reason</option>
			</select>
		    </div>
		</s:else>
		<s:if test="timeBlock.hourCode.record_method == 'Time'">
			<div id="div_time_in" class="form-group">
				<label>Time In</label>
				
				<s:textfield name="timeBlock.time_in" value="%{timeBlock.time_in}" maxlength="8" id="time_in"
					placeholder="(hhmm or hh:mm AM/PM)" />

				<button tabindex="10" type="button" class="time-now" onclick="getTimeAndInsert('time_in')">Now</button>
			</div>
			<div id="div_time_out" class="form-group">
				<label>Time Out</label>
				
				<s:textfield name="timeBlock.time_out" value="%{timeBlock.time_out}" maxlength="8" id="time_out"
					placeholder="(hhmm or hh:mm AM/PM)" />

					<button tabindex="10" type="button" class="time-now" onclick="getTimeAndInsert('time_out')">Now</button>
			</div>
			<div id="div_overnight" class="form-group">
				<label>Overnight Shift Options</label>
				<s:select tabindex="10" name="timeBlock.overnightOption" value="%{timeBlock.overnightOption}" id="time_overnight"
					list="#{'-1':'None','arrived before 12am':'Regular Overnight Shift (arrived before 12am)','arrived after 12am':'Irregular Overnight Shift (arrived after 12am)'}" />
			</div>
			<div id="div_hours" style="display:none;" class="form-group">
				<label>Hours</label>
				<s:textfield name="timeBlock.hours" value="%{timeBlock.hoursStr}" maxlength="5" id="hour_change"
					placeholder="(dd.dd)" />
			</div>
			<div id="div_amount" style="display:none;" class="form-group">
				<label>Amount $</label>
				<s:textfield name="timeBlock.amount" value="%{timeBlock.amountStr}" maxlength="6" id="amount_change"
					placeholder="(ddd.dd)" />
			</div>
		</s:if>
		<s:elseif test="timeBlock.isHourType()">
			<div id="div_time_in" style="display:none;" class="form-group">
				<label>Start Time</label>
				<s:textfield name="timeBlock.time_in" value="%{timeBlock.time_in}" maxlength="8" id="time_in"
					placeholder="(hhmm or hh:mm AM/PM)" />
			</div>
			<div id="div_time_out" style="display:none;" class="form-group">
				<label>End Time</label>
				<s:textfield name="timeBlock.time_out" value="%{timeBlock.time_out}" maxlength="8" id="time_out"
					placeholder="(hhmm or hh:mm AM/PM)" />
			</div>
			<div id="div_overnight" style="display:none;" class="form-group">
				<label>Overnight Shift Options</label>
				<s:select name="timeBlock.overnightOption" value="%{timeBlock.overnightOption}" id="time_overnight"
					list="#{'-1':'None','arrived before 12am':'Regular Overnight Shift (arrived before 12am)','arrived after 12am':'Irregular Overnight Shift (arrived after 12am)'}" />
			</div>
			<div id="div_hours" class="form-group">
				<label>Hours</label>
				<s:textfield name="timeBlock.hours" value="%{timeBlock.hours}" maxlength="5" id="hour_change"
					placeholder="(dd.dd)" />
			</div>
			<div id="div_amount" style="display:none;" class="form-group">
			    <label>Amount $</label>
			    <s:textfield name="timeBlock.amount" value="%{timeBlock.amountStr}" maxlength="6" id="amount_change"
					       placeholder="(ddd.dd)" />
			</div>
		</s:elseif>
		<s:else>
			<div id="div_time_in" style="display:none;" class="form-group">
				<label>Start Time</label>
				<s:textfield name="timeBlock.time_in" value="%{timeBlock.time_in}" maxlength="8" id="time_in"
					placeholder="(hhmm or hh:mm AM/PM)" />
			</div>
			<div id="div_time_out" style="display:none;" class="form-group">
				<label>End Time</label>
				<s:textfield name="timeBlock.time_out" value="%{timeBlock.time_out}" maxlength="8" id="time_out"
					placeholder="(hhmm or hh:mm AM/PM)" />
			</div>
			<div id="div_overnight" style="display:none;" class="form-group">
				<label>Overnight Shift Options</label>
				<s:select name="timeBlock.overnightOption" value="%{timeBlock.overnightOption}" id="time_overnight"
					list="#{'-1':'None','arrived before 12am':'Regular Overnight Shift (arrived before 12am)','arrived after 12am':'Irregular Overnight Shift (arrived after 12am)'}" />
			</div>
			<div id="div_hours" style="display:none" class="form-group">
				<label>Hours</label>
				<s:textfield name="timeBlock.hours" value="%{timeBlock.hours}" maxlength="5" id="hour_change"
					placeholder="(dd.dd)" />
			</div>
			<div id="div_amount" class="form-group">
			    <label>Amount $</label>
			    <s:textfield name="timeBlock.amount" value="%{timeBlock.amountStr}" maxlength="6" id="amount_change"
					       placeholder="(ddd.dd)" />
			</div>
		</s:else>
		<div id="div_notes" style="display:inline;" class="form-group">
		    <label>Notes</label>
		    <s:textarea maxlength="512"
			name="timeBlock.notes"
			      value="%{timeBlock.notes}"
		    />
		</div>
	</s:form>

	<script>
		function getTimeAndInsert(element) {
			let timeNow = new Date().toLocaleTimeString('en-US', {
				timeZone: 'America/Indiana/Indianapolis',
				hour: '2-digit',
				minute: '2-digit'
			});

			document.getElementById(element).value = timeNow;
		}
	</script>
</div>
<%@ include file="footer.jsp" %>
