<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="searchGroups" id="form_id" method="post">
		<s:hidden name="action2" id="action2" value="" />
		<h1>Group Search</h1>

	  <%@ include file="strutMessages.jsp" %>

	  <div class="width-one-half">
	      <div class="form-group">
		  <label>Group ID</label>
		  <s:textfield name="grplst.id" value="%{grplst.id}" size="10" id="group_id" />
	      </div>
	      
	      <div class="form-group">
		  <label>Name</label>
		  <s:textfield name="grplst.name" value="%{grplst.name}" size="30" id="group_name" /><br />
		  (key words)
	      </div>
	      
	      <div class="form-group">
		  <label>Department</label>
		  <s:select name="grplst.department_id" value="%{grplst.department_id}" list="departments" listKey="id" listValue="name" headerKey="-1" headerValue="All"  />
	      </div>
	      
	      <div class="form-group">
		  <label>Active?</label>
		  <s:radio name="grplst.active_status" value="%{grplst.active_status}" list="#{'-1':'All','Active':'Active only','Inactive':'Inactive only'}"/>
		  
	      </div>
	      <div class="form-group">
		  <label>Allow Pending Accrual?</label>
		  <s:radio name="grplst.pending_accrual_status" value="%{grplst.pending_accrual_status}" list="#{'-1':'All','Allowed':'Allowed','Not_Allowed':'Not Allowed'}"/>
		  
	      </div>
	    <div class="form-group">
		<label>Required Punch Clock?</label>
		<s:radio name="grplst.clock_status" value="%{grplst.clock_status}" list="#{'-1':'All','y':'Yes','n':'No'}" />
	    </div>
	    <div class="form-group">
		<label>
		    <s:checkbox name="grplst.includeInAutoBatch" value="%{grplst.includeInAutoBatch}" fieldValue="true" />Included in Auto Submit Batch Only</label>
	    </div>	      
	      <div class="button-group">
		  <s:submit name="action" type="button" value="Submit" class="button"/>
		  <a href="<s:property value='#application.url' />group.action" class="button">New Group</a>
	      </div>
	  </div>
	</s:form>
	
	<s:if test="groups != null && groups.size() > 0">
		<s:set var="groups" value="%{groups}" />
		<s:set var="groupsTitle" value="groupsTitle" />
		<%@ include file="groups.jsp" %>
	</s:if>
</div>
<%@ include file="footer.jsp" %>
