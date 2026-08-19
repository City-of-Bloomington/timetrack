<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page container clearfix settings">
    <h1>New Employees With No Employee Number</h1>
    
    <div class="width-full float-left">
       	<s:if test="hasMessages()">
	    <s:set var="messages" value="messages" />			
	    <%@ include file="../messages.jsp" %>
	</s:if>
	<s:elseif test="hasErrors()">
	    <s:set var="errors" value="errors" />			
	    <%@ include file="../errors.jsp" %>
	</s:elseif>
	<p>To find the list of the new hires with no employee number
	    click on 'Submit'
	</p>
    	<s:form action="empNoNumReport" id="form_id" method="post" >
	    <div class="button-group">
		<s:submit name="action" type="button" value="Submit"/>
	    </div>
	</s:form>
	<s:if test="action != ''">
	    <s:if test="hasEntries()">
		<table>
		    <caption>New Employees</caption>
		    <tr><th>ID</th>
			<th>Username</th>
			<th>Full Name</th>
			<th>Group Name</th>
		    </tr>
		    <s:iterator var="row" value="entries">
			<tr>
			    <s:iterator var="row2" value="top" status="rowStatus">
				<s:if test="#rowStatus.index == 0">
				    <td><a href="<s:property value='#application.url' />employee.action?emp_id=<s:property />"><s:property /></a></td>
				</s:if>
				<s:else>
				    <td><s:property /></td>	
				</s:else>
			    </s:iterator>
			</tr>
		    </s:iterator>
		</table>		
	    </s:if>
	</s:if>
    </div>
    <%@ include file="../footer.jsp" %>
</div>

