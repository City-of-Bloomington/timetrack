<s:form action="timeDetails" id="form_id" method="post" style="background: red; margin: 20px 0;" >
    <s:hidden name="action2" id="action2" value="" />
    <s:hidden name="source" value="source" />
    <s:if test="!isUserCurrentEmployee()">
      <!-- not sure we need this here, it's below and does the same action -->
      <!-- <h4>You are viewing/Entity <s:property value="document.employee" /></h4> -->
    </s:if>

    <table width="100%" border="0">
      <tr>
        <td align="left" class="th_text">
          <b> <s:property value="payPeriod.monthNames" /> <s:property value="payPeriod.startYear" /></b>
        </td>

        <td align="right" class="td_text">
          <b> Employee: </b><s:property value="document.employee" />
        </td>
      </tr>

      <s:if test="!isUserCurrentEmployee()">
        <s:if test="source != ''">
          <tr>
            <td class="th_text">&nbsp;</td>
            <td align="right" class="td_text">
              <a href="<s:property value='#application.url' /><s:property value='source' />.action">Back to Main Page</a>
            </td>
          </tr>
        </s:if>

        <s:else>
          <tr>
            <td class="th_text">&nbsp;</td>
            <td align="right" class="td_text">
              <a href="<s:property value='#application.url' />timeDetails.action?employee_id=<s:property value='user.id' />&action=Change">Back to Main User</a>
            </td>
          </tr>
        </s:else>
      </s:if>

      <tr>
        <td align="left" class="td_text"><b>Pay Period</b>
          <s:select name="pay_period_id" value="%{pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" onchange="doRefresh()" />
        </td>

        <td align="right" class="td_text">
          <s:if test="!isCurrentPayPeriod()">
            <a href="<s:property value='#application.url' />timeDetails.action?pay_period_id=<s:property value='currentPayPeriod.id' />">Current Pay Period</a>
          </s:if>
        </td>
      </tr>
    </table>
  </s:form>