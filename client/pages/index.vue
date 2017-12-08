<template>
  <v-layout justify-center align-center>
    <v-flex xs12 sm8 md8>
      <h4>Time Track</h4>
      <Clock></Clock>      
      <h4>Swipe your ID Card</h4>
    </v-flex>

    <v-flex xs12 sm4 md4>
  
      <form id="form_id" name="form_id" action="/timetrack/timeClock.action" method="post">
	<input type="hidden" name="action2" value="" id="action2"/>
	
	<div class="tt-row-container">	
	  
	  <dl class="fn1-output-field">
	    <dd><input type="text" name="timeClock.id_code" size="10" maxlength="10" value="" id="emp_id_code" autofocus="autofocus" required="true"/></dd>
	  </dl>
	  <button type="submit" id="form_id_action" name="action" value="Submit" class="fn1-btn">
	    Submit
	  </button>
	
	  
	</div>
      </form>      

    </v-flex>
  </v-layout>
</template>

<script>
import Clock from './../components/Clock'

export default {
  components: {
    Clock
  }

}
</script>

<style>
#form_id_action {
  display: none;
}
#emp_id_code {
  border: none;
  border-color: transparent;
}
</style>

