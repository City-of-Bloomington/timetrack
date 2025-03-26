<%@ include file="header.jsp" %>
<div class="internal-page">
    <s:form action="benefitGroupRef" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="benefitGroupRef.id == ''">
	    <h1>New Benefit Group Reference</h1>
	</s:if>

	<s:else>
	    <h1>Edit Benefit Group Reference</h1>
	    <s:hidden name="salaryGroup.id" value="%{salaryGroup.id}" />
	</s:else>
	
	<div class="width-one-half">
	    <s:if test="benefitGroupRef.id != ''">
		<div class="form-group">
		    <label>ID</label>
		    <s:property value="benefitGroupRef.id" />
		</div>
	    </s:if>
	    <div class="form-group">
		<label>Name</label>
		<s:textfield name="benefitGroupRef.name" value="%{benefitGroupRef.name}" size="30" maxlength="70" requiredLabel="true" required="true" id="type_name_id" />
	    </div>
	    <div class="form-group">
		<label>Salary Group</label>
		<s:select name="benefitGroupRef.salaryGroup_id" value="%{benefitGroupRef.salaryGroup_id}" required="true" list="salaryGroups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Salary Group" />
	    </div>	    
	    <s:if test="benefitGroupRef.id == ''">
		<s:submit name="action" type="button" value="Save" class="button"/></dd>
	    </s:if>
	    <s:else>
		<s:submit name="action" type="button" value="Save Changes" class="button"/>
	    </s:else>
	</div>
    </s:form>
    
    <s:if test="benefitGroupRefs != null">
	<s:set var="benefitGroupRefs" value="benefitGroupRefs" />
	<s:set var="benefitGroupRefsTitle" value="'Benefit Group References'" />
	<%@ include file="benefit_group_refs.jsp" %>
    </s:if>
</div>
<%@ include file="footer.jsp" %>
