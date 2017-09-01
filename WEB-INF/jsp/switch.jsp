<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="switch" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h3>Change Target Employee</h3>
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
			<dt>Employee </dt>
			<dd><s:select name="employee_id" value="%{employee_id}" list="employees" listKey="id" listValue="user.full_name" headerKey="-1" headerValue="Pick Employee" /> </dd>
		</dl>
		<s:submit name="action" type="button" value="Change" class="fn1-btn"/></dd>
	</div>
</s:form>

<%@  include file="footer.jsp" %>


