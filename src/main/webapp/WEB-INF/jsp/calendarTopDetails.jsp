<s:form id="form_id" method="get" class="calendar-header-controls">
  <s:hidden name="action2" id="action2" value="" />
  <s:hidden name="source" value="source" />

  <div class="button-group">
    <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='previousPayPeriod.id' />" class="button hide-text has-icon chevron-left"><span>Backwards</span></a>
    <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='currentPayPeriod.id' />" class="button today"><span>Current Pay Period</span></a>
    <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='nextPayPeriod.id' />" class="button hide-text has-icon chevron-right"><span>Forwards</span></a>
  </div>

  <h1 class="month-year">
    <s:property value="payPeriod.monthNames" /> <s:property value="payPeriod.startYear" />
  </h1>

  <div class="pay-period">
    <b>Pay Period:</b>
    <s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" onchange="this.form.submit()" />
  </div>
  <button type="submit">Submit</button>
</s:form>
