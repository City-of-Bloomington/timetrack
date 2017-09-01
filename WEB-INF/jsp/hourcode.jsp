<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="hourcode" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="hourcode.id == ''">
		<h3>New Earn Code</h3>
	</s:if>
	<s:else>
		<h3>Edit Earn Code: <s:property value="hourcode.name" /></h3>
		<s:hidden name="hourcode.id" value="%{hourcode.id}" />
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
	<s:if test="hourcode.id != ''">
		<dl class="fn1-output-field">
			<dt>ID</dt>
			<dd><s:property value="%{hourcode.id}" /></dd>
		</dl>
	</s:if>
	<dl class="fn1-output-field">
		<dt>Name</dt>
		<dd><s:textfield name="hourcode.name" value="%{hourcode.name}" required="true" size="15" maxlength="20" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Description</dt>
		<dd><s:textfield name="hourcode.description" value="%{hourcode.description}" size="30" maxlength="80" /></dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>Recording Method </dt>
		<dd><s:radio name="hourcode.record_method" value="%{hourcode.record_method}" list="#{'Time':'Time','Hours':'Hours'}" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Related Accrual </dt>
		<dd><s:select name="hourcode.accrual_id" value="%{hourcode.accrual_id}" list="accruals" listKey="id" listValue="name" headerKey="-1" headerValue="Pick related accrual" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Count as regular pay?</dt>
		<dd><s:checkbox name="hourcode.count_as_regular_pay" value="%{hourcode.count_as_regular_pay}" /> Yes
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Default Regular?</dt>
		<dd><s:radio name="hourcode.reg_default" value="%{hourcode.reg_default}" list="#{'0':'Yes','1':'No'}"/> (each salary group need only one default regular)
		</dd>
	</dl>	
	<s:if test="hourcode.id != ''">
		<dl class="fn1-output-field">
			<dt>Inactive ?</dt>
			<dd><s:checkbox name="hourcode.inactive" value="%{hourcode.inactive}" /> Yes (check to dissable)
			</dd>
		</dl>
	</s:if>
	<s:if test="hourcode.id == ''">
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
	</s:if>
	<s:else>
		<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		<a href="<s:property value='#application.url' />hourcode.action?" class="fn1-btn">New Hour Code </a>							
	</s:else>
</s:form>
<s:if test="hasHourcodes()">
	<s:set var="hourcodes" value="%{hourcodes}" />
	<s:set var="hourcodesTitle" value="hourcodesTitle" />
	<%@  include file="hourcodes.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


