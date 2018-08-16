<s:if test="hasErrors()">
  <div class="alert strut-action">
    <p><s:property value="errors" /></p>
  </div>
</s:if>
<s:elseif test="hasMessages()">
  <div class="alert strut-action">
    <p><s:property value="messages" /></p>
  </div>
</s:elseif>
