<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="empAccrual" id="form_id" method="post">
	<s:hidden name="employee_selected_id" id="action2" value="%{employee_selected_id}" />
	<s:hidden name="empAccrual.employee_id" id="action2" value="%{employee_selected_id}" />
	<s:if test="empAccrual.id == ''">
		<h3>New Employee Accrual</h3>
	</s:if>
	<s:else>
		<h3>Edit Employee Accrual: <s:property value="empAccrual.id" /></h3>
		<s:hidden name="empAccrual.id" value="%{empAccrual.id}" />
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
	<dl class="fn1-output-field">
		<dt>Employee</dt>
		<dd><s:property value="empAccrual.employee" /></dd>
	</dl>
	<s:if test="empAccrual.id != ''">
		<dl class="fn1-output-field">
			<dt>ID</dt>
			<dd><s:property value="%{empAccrual.id}" /></dd>
		</dl>
	</s:if>
	<dl class="fn1-output-field">
		<dt>Accrual</dt>
		<dd>
			<s:if test="hasAccruals()">
				<s:select name="empAccrual.accrual_id" value="%{empAccrual.accrual_id}" list="accruals" listKey="id" listValue="name" headerKey="-1" headerValue="Pick accrual" />*
			</s:if>
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Total Hours</dt>
		<dd><s:textfield name="empAccrual.hours" value="%{empAccrual.hours}" size="5" maxlength="5" /> 
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Date</dt>
		<dd><s:textfield name="empAccrual.date" value="%{empAccrual.date}" size="10" maxlength="10" cssClass="date" /> 
		</dd>
	</dl>	
	<s:if test="empAccrual.id == ''">
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
	</s:if>
	<s:else>
		<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		<a href="<s:property value='#application.url' />empAccrual.action?employee_selected_id=<s:property value='employee_selected_id' />" class="fn1-btn">New Employee Accrual </a>							
	</s:else>
</s:form>
<s:if test="hasAccruals()">
	<s:set var="empAccruals" value="%{empAccruals}" />
	<s:set var="empAccrualsTitle" value="empAccrualsTitle" />
	<%@  include file="empAccruals.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


