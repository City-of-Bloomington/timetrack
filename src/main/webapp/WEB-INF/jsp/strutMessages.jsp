<s:if test="hasActionErrors()">
  <div class="alert strut-action">
    <s:actionerror/>
  </div>
</s:if>

<s:elseif test="hasActionMessages()">
  <div class="alert strut-action">
    <s:actionmessage/>
  </div>
</s:elseif>