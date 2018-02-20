<table width="100%" border="1">
	<caption class="cap_left">Summary</caption>
	<tr>
		<td class="th_text">Week </td>
		<td width="10%" class="th_text">Mon</td>
		<td width="10%" class="th_text">Tue</td>
		<td width="10%" class="th_text">Wed</td>
		<td width="10%" class="th_text">Thu</td>
		<td width="10%" class="th_text">Fri</td>
		<td width="10%" class="th_text">Sat</td>
		<td width="10%" class="th_text">Sun</td>
		<s:if test="#unionned">
			<td width="10%" class="th_text">Total</td>
			<td class="th_text">FLSA Total</td>			
		</s:if>
		<s:else>
			<td class="th_text">Total</td>
		</s:else>
		</tr>
		
	<s:iterator value="#daily" var="one" >
		<s:set var="dayKey" value="#one.key" />
		<s:set var="dayVal" value="#one.value" />
		<s:if test="#dayKey == 0">
	    <tr><td class="th_text">Week 1</td>
		</s:if>
		<s:if test="#dayKey == 7">
			<td align="right" class="td_text"><s:property value="#week1Total" /></td>
			<s:if test="#unionned">
				<td align="right" class="td_text"><s:property value="#week1Flsa" /></td>
			</s:if>
	    </tr>
			<tr><td class="th_text">Week 2</td>
		</s:if>
		<td align="right" class="td_text"><s:property value="dayVal" /></td>
		<s:if test="#dayKey == 13">
			<td align="right" class="td_text"><s:property value="#week2Total" /></td>
			<s:if test="#unionned">
				<td align="right" class="td_text"><s:property value="#week2Flsa" /></td>
			</s:if>
	    </tr>
		</s:if>
	</s:iterator>
	<tr>
		<td class="th_text">Pay Period Total</td>
		<td colspan="7" class="td_text">&nbsp;</td>
		<td align="right" class="td_text"><s:property value="#payPeriodTotal" /></td>
		<s:if test="#unionned">
			<td>&nbsp;</td>
		</s:if>
	</tr>
</table>



