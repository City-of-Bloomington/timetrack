<s:form id="form_id" method="get" class="calendar-header-controls">
  <s:hidden name="action2" id="action2" value="" />
  <s:hidden name="source" value="source" />

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

  <div class="pay-period">
    <b>Pay Period:</b>
    <s:select
      tabindex="3"
      name="pay_period_id"
      value="%{pay_period_id}"
      list="payPeriods"
      listKey="id"
      listValue="dateRange"
      onchange="this.form.submit()" />
  </div>
  <button type="submit">Submit</button>
</s:form>

<s:if test="hasErrors()">
  <div class="alert">
    <s:hidden name="errors" value="<s:property value='errorsAll' />" id="id_errors"/>
  </div>
</s:if>