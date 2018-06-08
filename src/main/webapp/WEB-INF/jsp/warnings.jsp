<s:if test="document.hasWarnings()">
  <s:set var="warnings" value="document.warnings" />
  <strong>Warnings</strong>
  <ul>
  	<s:iterator var="one" value="#warnings">
  		<li class="alert"><s:property /></li>
  	</s:iterator>
  </ul>

  <div class="alert alert-warning alert-dismissible fade show">
    <strong>Holy guacamole!</strong> You should check in on some of those fields below.
  </div>
</s:if>