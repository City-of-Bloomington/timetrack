<%@ taglib uri="/struts-tags" prefix="s" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
String str = (String)request.getAttribute("csvFileName");
response.setHeader("Content-Disposition","inline; filename="+str);
response.setContentType("application/csv");
%>
<s:set var="line" value="',,,,,,,,,,\n'" />
<s:iterator var="one" value="processes">
	<s:if test="netRegularHoursForFirstPay > 0">
		<s:property value="employee.employee_number" />,<s:property value="netRegularHoursForFirstPay" />,<s:property value="regCode" />,<s:property value="payPeriod.firstPayEndDate" />,<s:property value="#line" />
	</s:if>
	<s:if test="netRegularHoursForSecondPay > 0">
		<s:property value="employee.employee_number" />,<s:property value="netRegularHoursForSecondPay" />,<s:property value="regCode" />,<s:property value="payPeriod.end_date" />,<s:property value="#line" />
	</s:if>
	<s:if test="hasNonRegularFirstPay()">
	<s:iterator var="code" value="nonRegularFirstPay">
		<s:property value="employee.employee_number" />,<s:property value="#code.value" />,<s:property value="#code.key" />,<s:property value="payPeriod.firstPayEndDate" />,<s:property value="#line" />
	</s:iterator>
	</s:if>
	<s:if test="hasNonRegularSecondPay()">	
		<s:iterator var="code" value="nonRegularSecondPay"><s:property value="employee.employee_number" />,<s:property value="#code.value" />,<s:property value="#code.key" />,<s:property value="payPeriod.end_date" />,<s:property value="#line" />
		</s:iterator>
	</s:if>
	<s:if test="hasProfHours()">
		<s:property value="employee.employee_number" />,<s:property value="profHours" />,<s:property value="profHrsCode" />,<s:property value="payPeriod.end_date" />,<s:property value="#line" />
	</s:if>
</s:iterator>

