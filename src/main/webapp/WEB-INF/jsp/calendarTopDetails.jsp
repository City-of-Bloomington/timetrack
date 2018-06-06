<s:form action="timeDetails" id="form_id" method="post" class="calendar-header-controls">
  <s:hidden name="action2" id="action2" value="" />
  <s:hidden name="source" value="source" />

  <div class="button-group">
    <a href="" class="button backwards">
      <svg xmlns="http://www.w3.org/2000/svg"
           viewBox="0 0 320 512"
           height="10"
           width="10"
           fill="#4a4a4a">
            <path d="M34.52 239.03L228.87 44.69c9.37-9.37 24.57-9.37 33.94 0l22.67 22.67c9.36 9.36 9.37 24.52.04 33.9L131.49 256l154.02 154.75c9.34 9.38 9.32 24.54-.04 33.9l-22.67 22.67c-9.37 9.37-24.57 9.37-33.94 0L34.52 272.97c-9.37-9.37-9.37-24.57 0-33.94z"/>
      </svg>
    </a>

    <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='currentPayPeriod.id' />" class="button today">Today</a>

    <a href="" class="button forward">
      <svg xmlns="http://www.w3.org/2000/svg"
           viewBox="0 0 320 512"
           height="10"
           width="10"
           fill="#4a4a4a">
           <path d="M285.476 272.971L91.132 467.314c-9.373 9.373-24.569 9.373-33.941 0l-22.667-22.667c-9.357-9.357-9.375-24.522-.04-33.901L188.505 256 34.484 101.255c-9.335-9.379-9.317-24.544.04-33.901l22.667-22.667c9.373-9.373 24.569-9.373 33.941 0L285.475 239.03c9.373 9.372 9.373 24.568.001 33.941z"/>
      </svg>
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