<div class="calendar">
  <div class="daily-header">
    <div>Sun</div>
    <div>Mon</div>
    <div>Tues</div>
    <div>Wed</div>
    <div>Thu</div>
    <div>Fri</div>
    <div>Sat</div>
  </div>

  <div class="week">
    <s:iterator value="document.dailyBlocks" var="block" >
      <s:set var="blockKey" value="#block.key" />
      <s:set var="blockList" value="#block.value" />

      <s:if test="#blockKey == 7">
        </div><div class="week">
      </s:if>

      <s:iterator value="#blockList" status="row" >
        <s:if test="#row.first">
          <s:if test="#blockKey==5 || #blockKey==6 || #blockKey==12 || #blockKey==13">
            <!-- this is for the weekend -->
            <div class="day weekend">
          </s:if>

          <s:else>
            <!-- this is for each day -->
            <div class="day">
          </s:else>

          <a href="#"
             class="day-of-month"
             onclick="return popwit('<s:property value='#application.url' />timeBlock?document_id=<s:property value='document_id' />&date=<s:property value='date' />&order_index=<s:property value='#blockKey' />','timeBlock');">
             <s:property value="dayInt" />
          </a>
        </s:if>

        <s:if test="hasData()">
          <a href="<s:property value='#application.url' />timeBlock?id=<s:property value='id' />&action=Delete">
            <img src="<s:property value='#application.url' />js/images/delete_img.png" />
          </a>

          <a href="#" class="" onclick="return popwit('<s:property value='#application.url' />timeBlock?id=<s:property value='id' />','timeBlock');">
            <s:property value="timeInfo" />
            <s:if test="hasNextLine()">
              <s:property value="timeInfoNextLine" />
            </s:if>
          </a>
        </s:if>
      </s:iterator>
      </div><!-- /.day -->
    </s:iterator>
  </div><!-- /.week -->
</div><!-- /.calendar -->