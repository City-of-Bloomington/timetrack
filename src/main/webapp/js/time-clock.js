const timeClockForm = document.getElementById("form_id");

let clearFocusTimeClockInput = () => {
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
clearFocusTimeClockInput();

function showNextButton() {
  let jobSelectXHR  = new XMLHttpRequest();
  let jobSelectFD   = new FormData();
  let url           = APPLICATION_URL + "timeClock.action";
  let processButton = document.querySelectorAll('#form_id_action')[0];
  let radioElms     = document.querySelectorAll('input[type=radio]');
  let radioCount    = radioElms.length;

  function sendData() {
    jobSelectXHR.addEventListener("error", (e) => {
      alert('Oops! Something went wrong, please try again.');
    });

    jobSelectXHR.open("POST", url);
    jobSelectXHR.send(jobSelectFD);
  }

  for (var i = 0; i < radioCount; i++) {
    radioElms[i].onclick = () => {
      processButton.classList.add("active");
    };
  }

  processButton.onclick = (e) => {
    let selectedJob = document.querySelectorAll('input[type=radio]:checked')[0];
    if(!selectedJob){
      e.preventDefault();
      alert('Please select a job and try again.')
    } else { sendData(); }
  }
}

timeClockForm.addEventListener("submit", (e) => {
  e.preventDefault();

  let request             = new XMLHttpRequest();
  let url                 = APPLICATION_URL + "timeClock.action";
  let timeClockFormData   = new FormData();
  let action              = document.getElementById("form_id_action").value;
  let timeClockTime       = document.getElementById("time_clock_id2").value;
  let timeClockIdCodeVal  = document.getElementById("emp_id_code").value;

  request.open("POST", url, true);
  request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  request.onreadystatechange = () => {
    if (
      request.readyState === 4 &&
      request.status     === 200
    ) {
      let mainElement        = document.querySelector('main');
      mainElement.innerHTML  = request.response;

      let timeClockWindowLocation = () => window.top.location = url;
      let radioElmsLength = document.querySelectorAll('input[type=radio]').length;

      if(radioElmsLength >= 2) {
        setTimeout(function(){timeClockWindowLocation()}, 10000);
        showNextButton();
      } else {
        setTimeout(function(){timeClockWindowLocation()}, 10000);
      }

      clearFocusTimeClockInput();
    }
  };

  timeClockFormData.append('action', action);
  timeClockFormData.append('timeClock.time', timeClockTime);
  timeClockFormData.append('timeClock.id_code', timeClockIdCodeVal);

  // iterates over the formData
  // for (let pair of timeClockFormData.entries()) {
//    console.log(pair[0]+ ', ' + pair[1]);
  // }

  let queryString = new URLSearchParams(timeClockFormData).toString();
  request.send(queryString);
});

function timeUpdate() {
  let btownTime    = moment().tz("America/Indiana/Indianapolis");
  let now          = moment();
  let exp          = moment(btownTime);
  let bigTime      = document.getElementsByClassName("time")[0];
  let bigTimeAmPm  = document.getElementsByClassName("a")[0];
  let beforeNoon   = btownTime.clone().hour(12).minute(0).second(0);
  let isBeforeNoon = moment(btownTime).isBefore(beforeNoon);
  let headingMetaElm = document.getElementById('meta');

  if(headingMetaElm != null || headingMetaElm != undefined){
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