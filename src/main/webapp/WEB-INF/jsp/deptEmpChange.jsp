<%@  include file="header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page">
	<s:form action="deptEmpChange" id="form_id" method="post">
    <s:hidden name="action2" id="action2" value="" />
    <s:hidden name="departmentEmployee.id" value="%{departmentEmployee.id}" />
    <s:hidden name="departmentEmployee.employee_id" value="%{departmentEmployee.employee_id}" />
    <s:hidden name="departmentEmployee.department_id" value="%{departmentEmployee.department_id}" />
    <s:property value="%{departmentEmployee.employee.user}" />
    <h1>Change Employee Department </h1>
    <s:if test="hasMessages()">
      <s:set var="messages" value="%{messages}" />
      <%@ include file="messages.jsp" %>
    </s:if>
    <s:elseif test="hasErrors()">
      <s:set var="errors" value="%{errors}" />
      <%@ include file="errors.jsp" %>
    </s:elseif>		
    Note: To change the employee department pick the pay period where the
    pay period start date will be the employee department start date. <br />
    Then you will need to change employee group and job using the same effective pay period. <br />

    <div class="width-one-half">
      <div class="form-group">
        <label>Employee</label>
        <a href="<s:property value='#application.url' />employee.action?id=<s:property value='departmentEmployee.employee_id' />" /><s:property value="%{departmentEmployee.employee}" /></a>
      </div>
      <div class="form-group">
        <label>Old Department</label>
        <a href="<s:property value='#application.url' />department.action?id=<s:property value='departmentEmployee.department_id' />" /><s:property value="%{departmentEmployee.department}" /></a>
      </div>
      <div class="form-group">
        <label>New Department</label>
        <s:select name="departmentEmployee.new_department_id" value="%{departmentEmployee.new_department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" />
      </div>
      <div class="form-group">
        <label>Secondary Department</label>
        <<s:select name="departmentEmployee.department2_id" value="%{departmentEmployee.department2_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Department" />
      </div>
      <div class="form-group">
        <label>New Dept Effective Start Pay Period</label>
        <s:select name="departmentEmployee.pay_period_id" value="%{departmentEmployee.pay_period_id}" list="payPeriods" listKey="id" listValue="dateRange" headerKey="-1" headerValue="Effective start pay period" />
      </div>
      <div class="button-group">
        <s:submit name="action" type="button" value="Change Department" class="fn1-btn"/>
      </div>
    </div>
  </s:form>
</div>

<%@  include file="footer.jsp" %>


