<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="node" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="node.id == ''">
			<h1>New Workflow Action</h1>
		</s:if>
		<s:else>
			<h1>Edit Workflow Action: <s:property value="%{node.name}" /></h1>
			<s:hidden id="node.id" name="node.id" value="%{node.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>

	  <p>Please do not make changes to this list of workflow actions unless you are required by your managers to do so, as this may affect the workflow of the system.</p>

		<div class="width-one-half">
			<s:if test="node.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="node.id" /> </dd>
				</div>
			</s:if>

			<div class="form-group">
				<label>Name</label>
				<s:textfield name="node.name" value="%{node.name}" size="30" maxlength="70" required="true" />
			</div>

			<div class="form-group">
				<label>Description</label>
				<s:textarea name="node.description" value="%{node.description}" rows="5" cols="50" />
			</div>

			<div class="form-group">
				<label>Managers only?</label>
				<s:checkbox name="node.managers_only" value="%{node.managers_only}" fieldValue="true" />Yes
			</div>

			<div class="form-group">
				<label>Annotation</label>
				<s:textfield name="node.annotation" value="%{node.annotation}" size="30" maxlength="50" />
			</div>

			<div class="form-group">
				<label>Inactive?</label>
				<s:checkbox name="node.inactive" value="%{node.inactive}" fieldValue="true" />Yes
			</div>

			<s:if test="node.id == ''">
				<s:submit name="action" type="button" value="Save" class="button"/></dd>
			</s:if>

			<s:else>
				<div class="button-group">
					<a href="<s:property value='#application.url' />node.action" class="button">New Workflow Action</a>
					<s:submit name="action" type="button" value="Save Changes" class="button"/>
				</div>
			</s:else>
		</div>
	</s:form>

	<s:if test="node.id == ''">
		<s:if test="hasNodes()">
			<s:set var="nodes" value="nodes" />
			<s:set var="nodesTitle" value="nodesTitle" />
			<%@ include file="nodes.jsp" %>
		</s:if>
	</s:if>
</div>
<%@ include file="footer.jsp" %>