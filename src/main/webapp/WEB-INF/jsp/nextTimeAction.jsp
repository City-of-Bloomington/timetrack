<s:if test="document.hasLastWorkflow()">
  <s:if test="document.lastWorkflow.hasNextNode()">
    <p><strong>Next waiting action:</strong> <s:property value="document.LastWorkflow.nextNode" /></p>
  </s:if>
</s:if>