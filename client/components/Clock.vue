// https://github.com/dangvanthanh/vue-clock
<template>
  <div class="clock">
    <div class="clock__hours">
      <span class="clock__hourtime">{{hourtime}}</span>
      {{hours}}
    </div>
    <div class="clock__minutes">{{minutes}}</div>
    <div class="clock__seconds">{{seconds}}</div>
  </div>
</template>

<script>
// imported from ../filters
// import { getHourTime, getZeroPad } from '../filters'
export function getHourTime (h) {
  return h >= 12 ? 'PM' : 'AM'
}

export function getZeroPad (n) {
  return (parseInt(n, 10) >= 10 ? '' : '0') + n
}

export default {
  data () {
    return {
      hours: '',
      minutes: '',
      seconds: '',
      hourtime: ''
    }
  },
  mounted () {
    setInterval(this.updateDateTime, 1000)
  },
  methods: {
    updateDateTime () {
      let now = new Date()
      this.hours = now.getHours()
      this.minutes = getZeroPad(now.getMinutes())
      this.seconds = getZeroPad(now.getSeconds())
      this.hourtime = getHourTime(this.hours)
      this.hours = this.hours % 12 || 12
    }
  }
}
</script>

<style>
*,
*:before,
*:after {
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
}
body {
  background-color: #dd4a38;
  margin: 0;
  color: #444;
  font-size: 16px;
  line-height: 1.5;
}
/*.container {
  max-width: 25rem;
  margin: 50px auto;
}
*/
.clock {
  background: #fff;
  border: .3rem solid #fff;
  border-radius: .5rem;
  display: inline-block;
  margin-bottom: 1em;
}
.clock__hours,
.clock__minutes,
.clock__seconds {
  background: linear-gradient(to bottom, #26303b 50%, #2c3540 50%);
  display: inline-block;
  color: #fff;
  font-family: 'Nunito', sans-serif;
  font-size: 10rem;
  font-weight: 300;
  padding: .5rem 1rem;
  text-align: center;
  position: relative;
}
.clock__hours {
  border-right: .15rem solid #fff;
  border-radius: .5rem 0 0 .5rem;
}
.clock__minutes {
  border-right: .15rem solid #fff;
}
.clock__seconds {
  border-radius: 0 .5rem .5rem 0;
}
.clock__hourtime {
  font-size: 3rem;
  position: absolute;
  top: 2px;
  left: 8px;
}
</style>
