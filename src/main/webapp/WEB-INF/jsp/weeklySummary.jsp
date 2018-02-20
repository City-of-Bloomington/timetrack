<table width="60%" border="1" class="tbl_gray">
	<caption class="cap_left"><s:property value="#weeklyTitle" /></caption>
	<tr>
		<td class="th_text">Hour Code </td>
		<td class="th_text">Total</td>
	</tr>
	<s:iterator value="#weeklyHourCodes" var="one" >
		<s:set var="hourCodeKey" value="#one.key" />
		<s:set var="hourCodeVal" value="#one.value" />
		<tr>
			<td align="left" class="th_text"><s:property value="#hourCodeKey" /></td>
			<td align="right" class="td_text"><s:property value="#hourCodeVal" /></td>
		</tr>
	</s:iterator>
	<tr>
		<td class="th_text">Week Total</td>
		<td align="right" class="td_text"><s:property value="#weekTotal" /></td>
	</tr>
</table>



