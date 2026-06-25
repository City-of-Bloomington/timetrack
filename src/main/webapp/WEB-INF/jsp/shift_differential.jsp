<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:if test="code.id == ''">
	<h1>New Shift Differentail Code</h1>
    </s:if>
    <s:else>
	<h1>Edit Shift Differentail Code</h1>
    </s:else>
    <s:if test="hasMessages()">
	<s:set var="messages" value="%{messages}" />			
	<%@ include file="messages.jsp" %>
    </s:if>
    <s:elseif test="hasErrors()">
	<s:set var="errors" value="%{errors}" />			
	<%@ include file="errors.jsp" %>
    </s:elseif>		    
    <s:form action="shiftDifferential" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="code.id" value="%{code.id}" />
	<div class="width-one-half">
	    <div class="form-group">			
		<label for="name_id">Shift Diff Code</label>
		<s:textfield name="code.name" value="%{code.name}" size="16" id="name_id" />
	    </div>
	    <div class="form-group">			
		<label for="nw_id">NW Corresponding Code</label>
		<s:textfield name="code.nwName" value="%{code.nWname}" size="16" id="nw_id" />
	    </div>
	    <div class="button-group">
		<s:if test="code.id == ''">
		    <s:submit name="action" value="Save" class="button"/>
		</s:if>
		<s:else>
		    <s:submit name="action" value="Save Changes" class="button"/>
		    <s:submit name="action" value="Delete" class="button"/>		    
		</s:else>
	    </div>
	</div>
	For testing employee data in NW <br />	
	<div class="button-group">
	    <s:submit name="action" accrual="button" value="Test" class="button"/>
	</div>
    </s:form>
</div>
<s:if test="hasCodes()">
    <table class="width-full">
	<caption>Shift Differentail Codes </caption>
    <thead>
	<tr>
	    <th>ID</th>
	    <th>Shift Code</th>
	    <th>NW Code</th>
	</tr>
    </thead>
    <tbody>
	<s:iterator var="one" value="codes">
	    <tr>
		<td><a href="<s:property value='#application.url' />shiftDifferential.action?id=<s:property value='id' />">Edit</a></td>
		<td><s:property value="name" /></td>
		<td><s:property value="nwName" /></td>
	    </tr>
	</s:iterator>
    </tbody>
</table>

</s:if>
<%@ include file="footer.jsp" %>
