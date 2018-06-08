<s:if test="document.hasWarnings()">
  <s:set var="warnings" value="document.warnings" />
	<s:iterator var="one" value="#warnings">
		<div class="alert">
      <p><strong>Warning!</strong> <s:property /></p>
      <button type="button" class="close" data-dismiss="alert" aria-label="Close">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
	</s:iterator>
</s:if>