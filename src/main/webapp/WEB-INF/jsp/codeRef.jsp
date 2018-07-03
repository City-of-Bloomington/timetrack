<%@ include file="header.jsp" %>
<div class="internal-page">
	<s:form action="codeRef" id="form_id" method="post" >
		<s:hidden name="action2" id="action2" value="" />
		<s:if test="codeRef.id == ''">
			<h1>New Code Cross Ref </h1>
		</s:if>
		<s:else>
			<h1>Edit Code Cross Ref: <s:property value="%{codeRef.nw_code}" /> </h1>
			<s:hidden name="codeRef.id" value="%{codeRef.id}" />
		</s:else>

	  <%@ include file="strutMessages.jsp" %>
	  <div class="width-one-half">			
			<s:if test="codeRef.id != ''">
				<div class="form-group">
					<label>ID</label>
					<s:property value="codeRef.id" />
				</div>
			</s:if>

			<div class="form-group">
				<label>Code</label>
				<s:select name="codeRef.code_id" value="%{codeRef.code_id}" list="hourCodes" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Hour Code" />
			</div>

			<div class="form-group">
				<label>NW Code</label>
				<s:textfield name="codeRef.nw_code" value="%{codeRef.nw_code}" size="20" maxlength="20" requiredLabel="true" required="true" />
			</div>
		
			<div class="form-group">
				<label>GL String</label>
				<s:textfield name="codeRef.gl_value" value="%{codeRef.gl_value}" size="20" maxlength="20" />
			</div>

			<div class="form-group">
				<label>PTO Ratio</label>
				<s:textfield name="codeRef.pto_ratio" value="%{codeRef.pto_ratio}" size="20" maxlength="20" /><br />
				(for HAND Dept)
			</div>

			<s:if test="codeRef.id == ''">
				<s:submit name="action" codeRef="button" value="Save" class="button"/>
			</s:if>
			<s:else>
				<s:submit name="action" codeRef="button" value="Save Changes" class="button"/>
			</s:else>
		</div>
	</s:form>

	<s:if test="codeRefs != null">
		<s:set var="codeRefs" value="codeRefs" />
		<s:set var="codeRefsTitle" value="codeRefsTitle" />
		<%@ include file="codeRefs.jsp" %>
	</s:if>
<%@  include file="footer.jsp" %>
