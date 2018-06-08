<s:if test="document.hasWarnings()">
  <s:set var="warnings" value="document.warnings" />
	<s:iterator var="one" value="#warnings">
		<div class="alert">
      <p><strong>Warning!</strong> <s:property /></p>
    </div>
	</s:iterator>
</s:if>