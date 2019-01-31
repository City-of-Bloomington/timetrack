let url              = APPLICATION_URL + "timeClock.action";
const pickJobForm    = document.querySelector(".clock-pick-job-form");

function timeUpdate() {
  let btownTime      = moment().tz("America/Indiana/Indianapolis");
  let now            = moment();
  let exp            = moment(btownTime);
  let bigTime        = document.getElementsByClassName("time")[0];
  let bigTimeAmPm    = document.getElementsByClassName("a")[0];
  let beforeNoon     = btownTime.clone().hour(12).minute(0).second(0);
  let isBeforeNoon   = moment(btownTime).isBefore(beforeNoon);
  let headingMetaElm = document.getElementById('meta');

  if(headingMetaElm  != null || headingMetaElm != undefined){
    headingMetaElm.innerHTML = exp.format('MMMM Do YYYY, h:mm:ss a');
  }

  if(bigTime != null || bigTime != undefined) {
    bigTime.innerHTML = exp.format('h:mm');
    bigTimeAmPm.innerHTML = exp.format('a').toUpperCase();
    isBeforeNoon ? bigTimeAmPm.classList.add("am") : bigTimeAmPm.classList.add("pm");
  }
}
setInterval(function() { timeUpdate(); }, 10);
timeUpdate();

let clearInput = () => {
  let inputElement = document.getElementById("emp_id_code");
  if(inputElement != null || inputElement != undefined) {
    inputElement.value = "";
    inputElement.focus();
    inputElement.addEventListener("blur", function(event){
      if(inputElement.value.length == 0)
        inputElement.focus();
    });
  }
}
clearInput();

if(pickJobForm){
  let submitButton   = document.getElementById('form_id_action');
  let radios         = document.querySelectorAll('input[type=radio]');
  let radiosLength   = radios.length;
  let jobSelected    = false;

  pickJobForm.addEventListener("submit", (e) => {
    if(!jobSelected) {
      e.preventDefault();
      alert('Please select a job and try again.');
    }
  });

  for (var i = 0; i < radiosLength; i++) {
    radios[i].onclick = () => {
      submitButton.classList.add("active");
      jobSelected = true;
    };
  }

  setTimeout(() => {
    if(!jobSelected) {
      window.top.location = url;
    } else {
      submitButton.click();
    }
  }, 10000);
}