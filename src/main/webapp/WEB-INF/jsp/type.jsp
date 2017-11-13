<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="type" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="type_name" value="%{type_name}" />	
	<s:if test="type.id == ''">
		<h1>New <s:property value="title" /></h1>
	</s:if>
	<s:else>
		<h1>Edit <s:property value="title" /></h1>
		<s:hidden name="type.id" value="%{type.id}" />
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
		<s:if test="id != ''">
			If you make any change, please hit the 'Save Changes' button
		</s:if>
		<s:else>
			You must hit 'Save' button to save data.
		</s:else>
	</p>
	<div class="tt-row-container">
		<s:if test="type.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="type.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="type.name" value="%{type.name}" size="30" maxlength="70" requiredLabel="true" required="true" id="type_name_id" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Description </dt>
			<dd><s:textarea name="type.description" value="%{type.description}" rows="5" maxlength="50" /> </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Inactive?</dt>
			<dd><s:checkbox name="type.inactive" value="%{type.inactive}" fieldValue="true" />Yes </dd>
		</dl>
		<s:if test="type.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
			<a href="<s:property value='#application.url'/>type.action?type_name=<s:property value='type_name' />" class="fn1-btn">New <s:property value="type_name" /></a>
		</s:else>
	</div>
</s:form>
<s:if test="type.id != ''">
	<s:if test="groups != null">
		<s:set var="groups" value="groups" />
		<s:set var="groupsTitle" value="'Groups in this Department'" />
		<%@  include file="groups.jsp" %>
	</s:if>
</s:if>
<s:else>
	<s:if test="types != null">
		<s:set var="types" value="types" />
		<s:set var="typesTitle" value="typesTitle" />
		<s:set var="type_name" value="type_name" />
		<%@  include file="types.jsp" %>
	</s:if>
</s:else>
<%@  include file="footer.jsp" %>


