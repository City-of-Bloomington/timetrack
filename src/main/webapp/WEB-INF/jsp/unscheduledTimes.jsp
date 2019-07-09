<h1><s:property value="#unscheduledTitle" /></h1>
<table class="groups width-full">
	<thead>
		<tr>				
			<s:if test="#hasHeaderTitles">		
				<s:iterator var="one" value="#headerTitles">				
					<th><s:property /></th>
				</s:iterator>					
			</s:if>
			<s:else>
				<th>Date</th>
				<th>Earn Code</th>
				<th>Hours</th>
			</s:else>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="trplt" value="#unscheduleds">
			<tr>
				<s:iterator var="one" value="#trplt">				
					<td><s:property /></td>
				</s:iterator>					
			</tr>
		</s:iterator>
	</tbody>
</table>
