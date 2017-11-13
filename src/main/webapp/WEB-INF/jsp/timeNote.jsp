<%@  include file="headerMin.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="timeNote" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="timeNote.document_id" value="%{timeNote.document_id}" />
	<s:if test="action != ''" >
		<script>
    window.onunload = refreshParent;
    function refreshParent() {
      window.opener.location.reload();
			window.close();
    }
		</script>
	</s:if>
	<h3>New Notes</h3>
	<s:if test="hasActionErrors()">
		<div class="errors">
      <s:actionerror/>
		</div>
  </s:if>
  <s:elseif test="hasActionMessages()">
		<div class="welcome">
      <s:actionmessage/>
		</div>
  </s:elseif>
	<b>Notes </b><br />
	<s:textarea name="timeNote.notes" value="%{timeNote.notes}" rows="5" cols="40" required="true" />* <br />
	<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
</s:form>
<s href="#" onclick="javascript:window.close()">Close</a>
<%@  include file="footer.jsp" %>


