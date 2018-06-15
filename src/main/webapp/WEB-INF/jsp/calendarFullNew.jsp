<div class="calendar">
  <div class="daily-header">
    <div>Mon</div>
    <div>Tues</div>
    <div>Wed</div>
    <div>Thu</div>
    <div>Fri</div>
    <div>Sat</div>
    <div>Sun</div>
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

              <!-- Adam Butcher (butcherad/adabutch): 06/05/18 -->
              <!-- this is a static holiday example placeholder -->
              <!-- usage: give the 'day' class an additional 'holiday' class -->
              <!-- wrap the holiday in a <span> -->
              <!-- see below -->
              <!-- <div class="day holiday">
              <span>Christmas Day</span> -->
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
          <div class="data-cell">
            <a href="#" class="" onclick="return popwit('<s:property value='#application.url' />timeBlock?id=<s:property value='id' />','timeBlock');">

              <s:if test="hasNextLine()">
                <span><b>Hours:</b> <i><s:property value="timeInfoNextLine" /></i></span>
              </s:if>
              <span><b><s:property value="timeInfo" /></b></span>

              <a href="<s:property value='#application.url' />timeBlock?id=<s:property value='id' />&action=Delete" onclick="return confirm('Are you sure you want to delete this time block?')">
                <svg id="remove-icon" xmlns='http://www.w3.org/2000/svg' viewBox='0 0 352 512'><path fill="" d='M242.72 256l100.07-100.07c12.28-12.28 12.28-32.19 0-44.48l-22.24-22.24c-12.28-12.28-32.19-12.28-44.48 0L176 189.28 75.93 89.21c-12.28-12.28-32.19-12.28-44.48 0L9.21 111.45c-12.28 12.28-12.28 32.19 0 44.48L109.28 256 9.21 356.07c-12.28 12.28-12.28 32.19 0 44.48l22.24 22.24c12.28 12.28 32.2 12.28 44.48 0L176 322.72l100.07 100.07c12.28 12.28 32.2 12.28 44.48 0l22.24-22.24c12.28-12.28 12.28-32.19 0-44.48L242.72 256z'/></svg>
              </a>
            </a>
          </div>
        </s:if>
      </s:iterator>
      </div><!-- /.day -->
    </s:iterator>
  </div><!-- /.week -->
</div><!-- /.calendar -->