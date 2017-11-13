<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="switch" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h4>Change Target Employee</h4>
	<div class="td_text">
		Note: start typing the first name or last name of the employee in the employee field then pick from a list <br />
	</div>
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
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt class="th_text">Employee </dt>
			<dd class="td_text"><s:textfield name="employee_name" value="" id="employee_name" size="20" /> ID:<s:textfield name="employee_id" value="" id="employee_id" size="5" /></dd>
		</dl>
		<s:submit name="action" type="button" value="Change" class="fn1-btn"/></dd>
	</div>
</s:form>

<%@  include file="footer.jsp" %>


