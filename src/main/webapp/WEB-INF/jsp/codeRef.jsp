<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="codeRef" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="codeRef.id == ''">
		<h1>New Code Cross Ref </h1>
	</s:if>
	<s:else>
		<h1>Edit Code Cross Ref: <s:property value="%{codeRef.nw_code}" /> </h1>
		<s:hidden name="codeRef.id" value="%{codeRef.id}" />
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
	<s:if test="codeRef.id != ''">
		<dl>
			<dt>ID </dt>
			<dd><s:property value="codeRef.id" /> </dd>
		</dl>
	</s:if>		
	<dl>
		<dt>Code </dt>
		<dd><s:select name="codeRef.code_id" value="%{codeRef.code_id}" list="hourCodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Hour Code" /> </dd>
	</dl>
	<dl>
		<dt>NW Code </dt>
		<dd><s:textfield name="codeRef.nw_code" value="%{codeRef.nw_code}" size="20" maxlength="20" requiredLabel="true" required="true" />* </dd>
	</dl>		
	<dl>
		<dt>GL String </dt>
		<dd><s:textfield name="codeRef.gl_value" value="%{codeRef.gl_value}" size="20" maxlength="20" /> </dd>
	</dl>
	<dl>
		<dt>PTO Ratio </dt>
		<dd><s:textfield name="codeRef.pto_ratio" value="%{codeRef.pto_ratio}" size="20" maxlength="20" />(for HAND Dept) </dd>
	</dl>
	<s:if test="codeRef.id == ''">
		<s:submit name="action" codeRef="button" value="Save" class="fn1-btn"/></dd>
	</s:if>
	<s:else>
		<s:submit name="action" codeRef="button" value="Save Changes" class="fn1-btn"/>
	</s:else>
</s:form>
<s:if test="codeRefs != null">
	<s:set var="codeRefs" value="codeRefs" />
	<s:set var="codeRefsTitle" value="codeRefsTitle" />
	<%@  include file="codeRefs.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


