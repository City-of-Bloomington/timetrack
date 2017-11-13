<%@ taglib uri="/struts-tags" prefix="s" %>
<s:if test="action != ''">
	<s:if test="report.hasEntries()">
		<s:set var="mapEntries" value="report.mapEntries" />
		<s:set var="hoursSums" value="report.hoursSums" />
		<s:set var="amountsSums" value="report.amountsSums" />
		<s:set var="totalHours" value="report.totalHours" />
		<s:set var="totalAmount" value="report.totalAmount" />
		<s:set var="reportTitle" value="reportTitle" />
		<s:set var="dateRange" value="dateRange" />
		<%@  include file="mpoReportCsv.jsp" %>
	</s:if>
</s:if>



