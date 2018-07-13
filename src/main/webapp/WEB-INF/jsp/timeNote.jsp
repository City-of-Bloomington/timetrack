<%@ include file="headerMin.jsp" %>
<div class="time-block">
  <s:form action="timeNote" class="pay-notes-form" id="form_id" method="post" >
  	<input type="hidden" name="action" value="Save">
    <s:hidden name="action2" id="action2" value="" />
  	<s:hidden name="timeNote.document_id" value="%{timeNote.document_id}" />

    <h1>Add Pay Period Note</h1>

    <%@ include file="strutMessages.jsp" %>
    <div class="alert"><p></p></div>

    <div class="form-group">
      <label>Note</label>
      <s:textarea
         name="timeNote.notes"
         value="%{timeNote.notes}"
         required="true" />
    </div>
  </s:form>
</div>