<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 * this code is used to add/edit departments, positions, salary groups,
 * workflow steps 
	-->
<s:form action="holiday" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="holiday.id == ''">
		<h1>New Holiday Type</h1>
	</s:if>
	<s:else>
		<h1>Edit Holiday Type: <s:property value="%{holiday.name}" /> </h1>
		<s:hidden name="holiday.id" value="%{holiday.id}" />
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
		<dl class="fn1-output-field">
			<dt>Pick A Year </dt>
			<dd><s:select name="year" value="%{year}" list="years" onchange="doRefresh()"/> </dd>
		</dl>
		<s:if test="holiday.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="holiday.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Date </dt>
			<dd><s:textfield name="holiday.date" value="%{holiday.date}" size="10" maxlength="10" requiredLabel="true" required="true" cssClass="date" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="holiday.description" value="%{holiday.description}" size="30" maxlength="70" requiredLabel="true" required="true" />* </dd>
		</dl>
		<s:if test="holiday.id == ''">
			<s:submit name="action" holiday="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" holiday="button" value="Save Changes" class="fn1-btn"/>
			<s:submit name="action" holiday="button" value="Delete" class="fn1-btn"/>			
		</s:else>
	</div>
</s:form>
<s:if test="holidays != null">
	<s:set var="holidays" value="holidays" />
	<s:set var="holidaysTitle" value="holidaysTitle" />
	<%@  include file="holidays.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>


