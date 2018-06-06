<s:form action="timeDetails" id="form_id" method="post" class="calendar-header-controls">
  <s:hidden name="action2" id="action2" value="" />
  <s:hidden name="source" value="source" />

  <div class="button-group">
    <a href="" class="button hide-text has-icon chevron-left">
      <span>Backwards</span>
    </a>

    <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='currentPayPeriod.id' />" class="button today"><span>Today</span></a>

    <a href="" class="button hide-text has-icon chevron-right">
      <span>Forwards</span>
    </a>
  </div>

  <div class="month-year">
    <s:property value="payPeriod.monthNames" /> <s:property value="payPeriod.startYear" />
  </div>

  <div class="pay-period">
    <b>Pay Period:</b>
    <s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" onchange="doRefresh()" />
  </div>

</s:form>