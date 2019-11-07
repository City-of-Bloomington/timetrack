<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page">
	<div class="width-one-half">
		<h1>Service Keys</h1>
		<s:form action="serviceKey" id="form_id" method="post" >
			<s:hidden name="action2" id="action2" value="" />
			<s:if test="key.id == ''">
				<h3>New Service Key</h3>
			</s:if>
			<s:else>
				<h3>Edit Service Key </h3>
			</s:else>
			<s:if test="hasErrors()">
				<s:set var="errors" value="errors" />
				<div class="errors">
					<%@  include file="errors.jsp" %>
				</div>
			</s:if>
			<s:elseif test="hasMessages()">
				<s:set var="messages" value="messages" />
				<div class="welcome">			
					<%@  include file="messages.jsp" %>
				</div>
			</s:elseif>
  		<div class="form-group">			
				<label>Key Name </label>
				<s:textfield name="key.keyName" value="%{key.keyName}" size="30" required="true" /> 
			</div>
  		<div class="form-group">			
				<label>Key Value </label>
				<s:textfield name="key.keyValue" value="%{key.keyValue}" size="80" required="true" /> 
			</div>
			<div class="button-group">
				<s:if test="key.id == ''">		
					<s:submit name="action" type="button" value="Save"/></dd>
				</s:if>
				<s:else>
					<s:submit name="action" type="button" value="Save Changes" /></dd>
				</s:else>
			</div>
		</s:form>
	</div>
</div>
<s:if test="hasKeys()">
	<s:set var="keys" value="keys" />
	<s:set var="keysTitle" value="keysTitle" />
	<%@  include file="serviceKeys.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


