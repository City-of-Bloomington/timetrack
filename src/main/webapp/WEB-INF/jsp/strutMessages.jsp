<s:if test="hasErrors()">
  <div class="alert strut-action">
    <s:property value="errors" />
  </div>
</s:if>
<s:elseif test="hasMessages()">
  <div class="alert strut-action">
    <s:property value="messages" />
  </div>
</s:elseif>
