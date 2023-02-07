<%@ taglib uri="/struts-tags" prefix="s" %>
<s:if test="action != ''">
    <s:if test="hasData()">
	<s:set var="entries" value="entries" />
	<%@  include file="reportGroupsCsv.jsp" %>
    </s:if>
</s:if>



