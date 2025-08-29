<s:if test="document.hasLastWorkflow()">
  <s:if test="document.lastWorkflow.hasNextNode()">
    <strong>Next action:</strong> <s:property value="document.LastWorkflow.nextNode" /> (<s:property value="document.nextActionerNames" />)
  </s:if>
</s:if>
