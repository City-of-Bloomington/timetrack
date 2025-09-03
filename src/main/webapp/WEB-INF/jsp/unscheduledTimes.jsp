<table class="groups width-full">
    <caption style="text-align:left;font-weight:bold">	
	<s:property value="#unscheduledTitle" />
    </caption>
    <tr>
	<s:if test="#hasHeaderTitles">		
	    <s:iterator var="one" value="#headerTitles">
		<td><s:property /></td>
	    </s:iterator>					
	</s:if>
	<s:else>
	    <td>Date</td>
	    <td>&nbsp;</td>
	    <td>Earn Code</td>
	    <td>Hours</td>
	</s:else>
    </tr>
    <s:iterator var="trplt" value="#unscheduleds">
	<tr>
	    <s:iterator var="one" value="#trplt" status="row">
		<s:if test="#row.index == 1">
		    <td>&nbsp;</td>		    
		</s:if>
		<td><s:property /></td>
	    </s:iterator>					
	</tr>
    </s:iterator>
</table>
