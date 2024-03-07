<div class="calendar view-only <s:if test='hasJobTypes()'>mult-jobs</s:if>">
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
      <s:iterator value="#dayBlocksMap" var="dayBlocks" >
	  <s:set var="blockKey" value="#dayBlocks.key" />
	  <s:set var="dayBlock" value="#dayBlocks.value" />
	  <s:set var="dayInt" value="#dayBlock.dayInt" />
	  <s:set var="dayHours" value="#dayBlock.dayHours" />
	  <s:set var="blockList" value="#dayBlock.blocks" />      
      
      <s:if test="#blockKey == 7">
        </div><div class="week">
      </s:if>

      <s:iterator value="#blockList" status="row" >
        <s:if test="#row.first">
          <s:if test="#blockKey==5 || #blockKey==6 || #blockKey==12 || #blockKey==13">
		  <!-- this is for the weekend -->
		  <s:if test="isHoliday()">
			  <div class="day holiday"
				  data-block-id="<s:property value='id' />"
				  data-doc-id="<s:property value='document_id' />"
				  data-date="<s:property value='date' />"
				  data-order-index="<s:property value='#blockKey' />">
				  <span><s:property value="holidayName" /></span>
		  </s:if>
		  <s:else>
			  <div class="day weekend"
				  data-block-id="<s:property value='id' />"
				  data-doc-id="<s:property value='document_id' />"
				  data-date="<s:property value='date' />"
				  data-order-index="<s:property value='#blockKey' />">
		  </s:else>
          </s:if>

          <s:else>
            <!-- this is for each day -->
		  <s:if test="isHoliday()">
			  <div class="day holiday"
				  data-block-id="<s:property value='id' />"
				  data-doc-id="<s:property value='document_id' />"
				  data-date="<s:property value='date' />"
				  data-order-index="<s:property value='#blockKey' />">
				  <span><s:property value="holidayName" /></span>
		  </s:if>
		  <s:else>
		      <div class="day"
			   data-block-id="<s:property value='id' />"
			   data-doc-id="<s:property value='document_id' />"
			   data-date="<s:property value='date' />"
			   data-order-index="<s:property value='#blockKey' />">
		  </s:else>
          </s:else>
	  <div style="width:50%;display:inline;float:left"> <b><s:property value="#dayInt" /></b> </div><s:if test="#dayHours != '0.0'"><div style="width:50%;display:inline;float:right"> (<i><s:property value="#dayHours" />h</i>)</div></s:if><br />
        </s:if>
        <s:if test="hasData()">
          <div class="data"
            data-job-id="<s:property value='job_id' />"
            data-block-id="<s:property value='id' />"
            data-date="<s:property value='date' />"
	       data-job-name="<s:property value='job_name' />"
            data-time-out="<s:property value='Time_out' />">
            <s:if test="hasNextLine()">
              <span><b>Hours:</b> <i><s:property value="timeInfoNextLine" /></i></span>
            </s:if>
            <span><b><s:property value="timeInfo" /></b></span>
            <span><s:property value='job_name' /></span>

	  </div>
        </s:if>
      </s:iterator>
      </div><!-- /.day -->
    </s:iterator>
  </div><!-- /.week -->
</div><!-- /.calendar -->
