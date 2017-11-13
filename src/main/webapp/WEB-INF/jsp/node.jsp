<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="node" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="node.id == ''">
		<h3>New Workflow Action</h3>
	</s:if>
	<s:else>
		<h3>Edit Workflow Action: <s:property value="%{node.name}" /></h3>
		<s:hidden id="node.id" name="node.id" value="%{node.id}" />
	</s:else>
  <s:if test="hasActionErrors()">
		<div class="errors">
      <s:actionerror/>
		</div>
  </s:if>
  <s:elseif test="hasActionMessages()">
		<div class="welcome">
      <s:actionmessage/>
		</div>
  </s:elseif>
  <p>* Required field <br />
		Please do not make changes to this list of workflow actions unless you are required by your managers to do so, as this may affect the workflow of the system. <br />
		<s:if test="id != ''">
			If you make any change, please hit the 'Save Changes' button
		</s:if>
		<s:else>
			You must hit 'Save' button to save data.
		</s:else>
	</p>
	<div class="tt-row-container">
		<s:if test="node.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="node.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="node.name" value="%{node.name}" size="30" maxlength="70" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Description </dt>
			<dd><s:textarea name="node.description" value="%{node.description}" rows="5" cols="50" /> </dd>
		</dl>
		
		<dl class="fn1-output-field">
			<dt>Managers only?</dt>
			<dd><s:checkbox name="node.managers_only" value="%{node.managers_only}" fieldValue="true" />Yes </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Annotation </dt>
			<dd><s:textfield name="node.annotation" value="%{node.annotation}" size="30" maxlength="50" /> </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Inactive?</dt>
			<dd><s:checkbox name="node.inactive" value="%{node.inactive}" fieldValue="true" />Yes </dd>
		</dl>
		<s:if test="node.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
			<a href="<s:property value='#application.url' />node.action" class="fn1-btn">New Workflow Action</a>					
		</s:else>
	</div>
</s:form>
<s:if test="node.id == ''">
	<s:if test="hasNodes()">
		<s:set var="nodes" value="nodes" />
		<s:set var="nodesTitle" value="nodesTitle" />
		<%@  include file="nodes.jsp" %>
	</s:if>
</s:if>
<%@  include file="footer.jsp" %>


