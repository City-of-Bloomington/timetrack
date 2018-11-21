<div class="calendar-header-controls">
  <s:form id="form_id" method="get">
    <s:hidden name="action2" id="action2" value="" />
    <s:hidden name="source" value="source" />

      <s:if test="hasMultipleJobs()">
        <div class="pay-period">
          <div>
            <label for="pay_period_id"><b>Pay Period:</b></label><br>
            <s:select
              tabindex="3"
              name="pay_period_id"
              value="%{pay_period_id}"
              list="payPeriods"
              listKey="id"
              listValue="dateRange"
              onchange="this.form.submit()" />
          </div>

          <div class="button-group">
            <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='previousPayPeriod.id' />"
               tabindex="3"
               class="button hide-text has-icon chevron-left"
               alt="Previous Pay Period"
               title="Previous Pay Period">
               <span>Backwards</span>
            </a>

            <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='currentPayPeriod.id' />"
               tabindex="3"
               class="button today"
               alt="Current Pay Period"
               title="Current Pay Period">
               <span>Current Pay Period</span>
            </a>

            <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='nextPayPeriod.id' />"
               tabindex="3"
               class="button hide-text has-icon chevron-right"
               alt="Forward Pay Period"
               title="Forward Pay Period">
               <span>Forwards</span>
            </a>
          </div>
        </div>

        <h1 class="month-year">
          <s:property value="payPeriod.monthNames" /> <s:property value="payPeriod.startYear" />
        </h1>

        <div class="job-select">
          <label for="job_id"><b>Job:</b></label><br>
					<s:if test="hasJobTypes()">
						<s:select
							tabindex="4"
							name="job_id"
							value="%{job_id}"
							list="jobTypes"
							listKey="id"
							listValue="name"
							onchange="this.form.submit()" />
					</s:if>
        </div>
      </s:if>

      <s:else>
        <div class="button-group">
          <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='previousPayPeriod.id' />"
             tabindex="3"
             class="button hide-text has-icon chevron-left"
             alt="Previous Pay Period"
             title="Previous Pay Period">
             <span>Backwards</span>
          </a>

          <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='currentPayPeriod.id' />"
             tabindex="3"
             class="button today"
             alt="Current Pay Period"
             title="Current Pay Period">
             <span>Current Pay Period</span>
          </a>

          <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='nextPayPeriod.id' />"
             tabindex="3"
             class="button hide-text has-icon chevron-right"
             alt="Forward Pay Period"
             title="Forward Pay Period">
             <span>Forwards</span>
          </a>
        </div>

        <h1 class="month-year">
          <s:property value="payPeriod.monthNames" /> <s:property value="payPeriod.startYear" />
        </h1>

        <div>
          <label for="pay_period_id"><b>Pay Period:</b></label><br>
          <s:select
            tabindex="3"
            name="pay_period_id"
            value="%{pay_period_id}"
            list="payPeriods"
            listKey="id"
            listValue="dateRange"
            onchange="this.form.submit()" />
        </div>
      </s:else>

    <button type="submit">Submit</button>
  </s:form>
</div>

<s:if test="hasErrors()">
  <div class="alert">
    <s:hidden name="errors" value="<s:property value='errorsAll' />" id="id_errors"/>
  </div>
</s:if>
