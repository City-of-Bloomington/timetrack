<s:form action="timeDetails" id="form_id" method="post" style="background: red; margin: 20px 0;" >
  <s:hidden name="action2" id="action2" value="" />
  <s:hidden name="source" value="source" />
  <b><s:property value="payPeriod.monthNames" /> <s:property value="payPeriod.startYear" /></b>
  <b>Employee: </b><s:property value="document.employee" />

  <s:if test="!isUserCurrentEmployee()">
    <s:if test="source != ''">
      <a href="<s:property value='#application.url' /><s:property value='source' />.action">Back to Main Page</a>
    </s:if>

    <s:else>
      <a href="<s:property value='#application.url' />timeDetails.action?employee_id=<s:property value='user.id' />&action=Change">Back to Main User</a>
    </s:else>
  </s:if>

  <div>
    <b>Pay Period</b>
    <s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" onchange="doRefresh()" />
  </div>

  <s:if test="!isCurrentPayPeriod()">
    <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='currentPayPeriod.id' />">Current Pay Period</a>
  </s:if>
</s:form>