<table width="100%" border="1" class="tbl_wheat">
  <tr>
    <td width="14%" class="th_text">Mon</td>
    <td width="14%" class="th_text">Tue</td>
    <td width="14%" class="th_text">Wed</td>
    <td width="14%" class="th_text">Thu</td>
    <td width="14%" class="th_text">Fri</td>
    <td width="14%" class="th_text tbl_weekend">Sat</td>
    <td class="th_text tbl_weekend">Sun</td>
  </tr>

  <tr>
    <s:iterator value="document.dailyBlocks" var="block" >
      <s:set var="blockKey" value="#block.key" />
      <s:set var="blockList" value="#block.value" />

      <!-- this breaks into a new row -->
      <s:if test="#blockKey == 7">
         </tr><tr>
      </s:if>

      <s:iterator value="#blockList" status="row" >
        <s:if test="#row.first">
          <!-- this <td> is for for the weekend(s) -->
          <s:if test="#blockKey==5 || #blockKey==6 || #blockKey==12 || #blockKey==13">
            <td valign="top" style="height:100px; color: blue; text-align:left" class="th_text tbl_weekend">
          </s:if>

          <!-- this <td> is for each day -->
          <s:else>
            <td valign="top" style="height:100px; color: red; text-align:left" class="th_text">
          </s:else>

          <a href="#"
             class="hr_cell"
             onclick="return popwit('<s:property value='#application.url' />timeBlock?document_id=<s:property value='document_id' />&date=<s:property value='date' />&order_index=<s:property value='#blockKey' />','timeBlock');">
            <s:property value="dayInt" />
          </a>
        </s:if>

        <s:if test="hasData()">
          <table border="0" width="100%" class="TESTCLASS">
            <tr>
              <td align="right" class="td_text b_cell">
                <a href="<s:property value='#application.url' />timeBlock?id=<s:property value='id' />&action=Delete" class="hr_cell"><img src="<s:property value='#application.url' />js/images/delete_img.png" /></a>
              </td>
            </tr>

            <tr>
              <td align="left" class="td_text">
                <a href="#" class="hr_cell" onclick="return popwit('<s:property value='#application.url' />timeBlock?id=<s:property value='id' />','timeBlock');">
                  <s:property value="timeInfo" />
                  <s:if test="hasNextLine()">
                    <br /><s:property value="timeInfoNextLine" />
                  </s:if>
                </a>
              </td>
            </tr>
          </table>
        </s:if>
      </s:iterator>
      </td>
    </s:iterator>
  </tr>
</table>