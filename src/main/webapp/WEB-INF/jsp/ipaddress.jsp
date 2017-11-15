<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="ipaddress" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="ipaddress.id == ''">
		<h3>New IP Address </h3>
	</s:if>
	<s:else>
		<h3>Edit IP Address: <s:property value="%{ipaddress.ip_address}" /> </h3>
		<s:hidden name="ipaddress.id" value="%{ipaddress.id}" />
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
		<s:if test="ipaddress.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="ipaddress.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>IP </dt>
			<dd><s:textfield name="ipaddress.ip_address" value="%{ipaddress.ip_address}" size="15" maxlength="20" requiredLabel="true" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Description </dt>
			<dd><s:textarea name="ipaddress.description" value="%{ipaddress.description}" rows="5" maxlength="50" /> </dd>
		</dl>
		<s:if test="ipaddress.id == ''">
			<s:submit name="action" accrual="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" accrual="button" value="Save Changes" class="fn1-btn"/>
		</s:else>
	</div>
</s:form>
<s:if test="ipaddresses != null">
	<s:set var="ipaddresses" value="ipaddresses" />
	<s:set var="ipaddressesTitle" value="ipaddressesTitle" />
	<%@  include file="ipaddresses.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


