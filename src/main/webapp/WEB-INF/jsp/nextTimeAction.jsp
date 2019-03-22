<s:if test="document.hasLastWorkflow()">
  <s:if test="document.lastWorkflow.hasNextNode()">
    <p><strong>Next action:</strong> <s:property value="document.LastWorkflow.nextNode" /> (<s:property value="document.nextActionerNames" />)</p>
  </s:if>
</s:if>
