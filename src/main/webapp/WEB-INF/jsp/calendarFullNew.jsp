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
	  <s:iterator value="#dailyBlocks" var="block" >
		  <s:set var="blockKey" value="#block.key" />
		  <s:set var="blockList" value="#block.value" />
		  <s:if test="#blockKey == 7">
  </div>
  <div class="week">
		  </s:if>
		  <s:iterator value="#blockList" status="row" >
			  <s:if test="#row.first">
				  <s:if test="#blockKey==5 || #blockKey==6 || #blockKey==12 || #blockKey==13">
					  <!-- this is for the weekend -->
					  <s:if test="isToday()">
						  <div class="day today"
							  tabindex="1"
							  data-block-id="<s:property value='id' />"
							  data-doc-id="<s:property value='document_id' />"
							  data-date="<s:property value='date' />"
							  data-order-index="<s:property value='#blockKey' />">
					  </s:if>
					  <s:else>
						  <div class="day weekend"
                   tabindex="1"
                   data-block-id="<s:property value='id' />"
                   data-doc-id="<s:property value='document_id' />"
                   data-date="<s:property value='date' />"
                   data-order-index="<s:property value='#blockKey' />">
						</s:else>
          </s:if>

          <s:else>
            <!-- this is for each day -->
		  <s:if test="isToday()">
			  <div class="day today"
                   tabindex="1"
                   data-block-id="<s:property value='id' />"
                   data-doc-id="<s:property value='document_id' />"
                   data-date="<s:property value='date' />"
                   data-order-index="<s:property value='#blockKey' />">
		  </s:if>

		  <s:elseif test="isHoliday()">
			  <div class="day holiday"
                   tabindex="1"
                   data-block-id="<s:property value='id' />"
                   data-doc-id="<s:property value='document_id' />"
                   data-date="<s:property value='date' />"
                   data-order-index="<s:property value='#blockKey' />">
				  <span><s:property value="holidayName" /></span>
		  </s:elseif>

		  <s:else>
			  <div class="day"
                   tabindex="1"
                   data-block-id="<s:property value='id' />"
                   data-doc-id="<s:property value='document_id' />"
                   data-date="<s:property value='date' />"
                   data-order-index="<s:property value='#blockKey' />">
		  </s:else>
          </s:else>
          <s:property value="dayInt" />
        </s:if>

        <s:if test="hasData()">
          <div class="data<s:if test='!hasNextLine()'> non-regular</s:if><s:if test='isMonetaryType()'> monetary</s:if>"
               tabindex="1"
               data-block-id="<s:property value='id' />"
               data-date="<s:property value='date' />"
						   data-job-name="<s:property value='job_name' />"
               data-time-out="<s:property value='Time_out' />">

            <s:if test="hasNextLine()">
              <span><b>Hours:</b> <i><s:property value="timeInfoNextLine" /></i></span>
            </s:if>


            <span><b><s:property value="timeInfo" /></b></span>

            <span><s:property value='job_name' /></span>


            <a class="delete-time-confirm"
               tabindex="1"
               data-info="<s:property value='timeInfo' />"
               data-date="<s:property value='date' />"
               data-block-id="<s:property value='id' />">
              <svg id="remove-icon" xmlns='http://www.w3.org/2000/svg' viewBox='0 0 352 512'><path fill="" d='M242.72 256l100.07-100.07c12.28-12.28 12.28-32.19 0-44.48l-22.24-22.24c-12.28-12.28-32.19-12.28-44.48 0L176 189.28 75.93 89.21c-12.28-12.28-32.19-12.28-44.48 0L9.21 111.45c-12.28 12.28-12.28 32.19 0 44.48L109.28 256 9.21 356.07c-12.28 12.28-12.28 32.19 0 44.48l22.24 22.24c12.28 12.28 32.2 12.28 44.48 0L176 322.72l100.07 100.07c12.28 12.28 32.2 12.28 44.48 0l22.24-22.24c12.28-12.28 12.28-32.19 0-44.48L242.72 256z'/></svg>
            </a>
          </div>
        </s:if>
      </s:iterator>
      </div><!-- /.day -->
    </s:iterator>
  </div><!-- /.week -->
</div><!-- /.calendar -->

<!-- jQuery Dialog Modal: Time Block Removal -->
<div class="modal remove" class="timetrack" style="display: none;">
  <main class="time-block">
    <h1>Delete Time Block<small></small></h1>
    <p>Are you sure you'd like to remove this time block?</p>
    <p class="details"></p>
  </main>
</div>

<!-- jQuery Dialog Modal: Time Block Add/Edit -->
<div class="modal add-edit" style="display: none;"></div>
