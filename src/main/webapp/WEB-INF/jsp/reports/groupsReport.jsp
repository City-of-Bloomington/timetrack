<%@  include file="../header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="internal-page container clearfix settings">
    <h1>Current Group Managers Report</h1>
    <div class="width-full float-left">
	<ul>
	    <li>Choose The Department</li>
	    <li>You may choose the output as csv file</li>
	</ul>
	<s:form action="groupsReport" id="form_id" method="post" >
	    <s:if test="hasMessages()">
		<s:set var="messages" value="messages" />			
		<%@ include file="../messages.jsp" %>
	    </s:if>
	    <s:elseif test="hasErrors()">
		<s:set var="errors" value="errors" />			
			<%@ include file="../errors.jsp" %>
	    </s:elseif>
	    <div class="form-group">
		<label>Department</label>
		<s:select name="dept_id"
				value="%{dept_id}"
				list="departments"
				listKey="id" listValue="name" headerKey="-1"
				headerValue="Pick Department"
				id="department_id_change" />
		</div>
		<div class="form-group">			
		    <label>Output Type:</label>				
		    <s:radio name="outputType" value="%{outputType}" list="#{'html':'Web page format','csv':'CSV format'}" />
		</div>
		<div class="button-group">
			<s:submit name="action" type="button" value="Submit"/>
		</div>
	</s:form>
	<s:if test="action != ''">
	    <s:if test="hasData()">
		<table>
		    <caption>Dept-Group Jobs</caption>
		    <s:iterator var="row" value="entries" status="rowStatus">
			<s:if test="#rowStatus.index==0">
			<tr>
			    <s:iterator var="row2" value="top">
				<th><b><s:property /> </b></th>
			    </s:iterator>
			</tr>
			</s:if>
			<s:else>
			    <tr>
				<s:iterator var="row2" value="top">
				    <td><s:property /> </td>
				</s:iterator>
			    </tr>
			</s:else>
		    </s:iterator>
		</table>		
		<table>
		    <caption>Dept - Groups and Managers</caption>
		    <s:iterator var="row" value="entries2" status="rowStatus2">
			<s:if test="#rowStatus2.index==0">
			    <tr>
				<s:iterator var="row2" value="top">
				    <th><b><s:property /> </b></th>
				</s:iterator>
			    </tr>
			</s:if>
			<s:else>
			    <tr>
				<s:iterator var="row2" value="top">
				    <td><s:property /> </td>
				</s:iterator>
			    </tr>
			</s:else>
		    </s:iterator>
		</table>
	    </s:if>
	</s:if>
    </div>
    <%@ include file="../footer.jsp" %>
</div>

