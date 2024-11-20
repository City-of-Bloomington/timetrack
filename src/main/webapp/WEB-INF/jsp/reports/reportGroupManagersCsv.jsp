<%@ taglib uri="/struts-tags" prefix="s" %>
<s:if test="action != ''">
    <s:if test="hasData()">
	<s:set var="entries" value="entries" />
	<s:set var="entries2" value="entries2" />	
	<%@  include file="reportGroupsCsv.jsp" %>
    </s:if>
</s:if>



