clicked_button_id="";
var icons = {
    header:"ui-icon-circle-plus",
    activeHeader:"ui-icon-circle-minus"
};
// function setCurTime(id){
//  date = new Date;

//  h = date.getHours();
//   if(h<10){
//     h = "0"+h;
//   }

//   m = date.getMinutes();
//   if(m<10){
//     m = "0"+m;
//   }

//   s = date.getSeconds();
//  if(s<10)
//    s="0"+s;

//  result = ''+h+':'+m;
//  result2 = ''+h+':'+m+':'+s;

//   document.getElementById(id).innerHTML = result2;
//   document.getElementById(id+"2").value = result;

//   setTimeout('setCurTime("'+id+'");','1000');
//   return true;
// };
$(".date").datepicker({
    nextText: "Next",
    prevText:"Prev",
    buttonText: "Pick Date",
    showOn: "both",
    navigationAsDateFormat: true,
    buttonImage: "/timetrack/js/calendar.gif"
});

$("#selection_id").change(function() {
    $("#action2").val("refresh");
    $("#form_id").submit();
});

$("#a_disabled").attr('disabled','disabled');
$(document).on('click', 'a', function(e) {
    if ($(this).attr('disabled') == 'disabled') {
        e.preventDefault();
    }
});
function popwit(url, name) {
    if(typeof(popupWin) != "object" || popupWin.closed)  {
        popupWin =  window.open(url, name, 'top=200,left=200,height=400,width=400,toolbar=0,menubar=0,location=0');
    }
    else{
        popupWin.location.href = url;
    }
    if (window.focus) {popupWin.focus()}
    return false;
 }
$("#emp_name").autocomplete({
    source: APPLICATION_URL + "EmployeeService?format=json",
    minLength: 2,
    dataType:"json",
    delay: 100,
    select: function( event, ui ) {
        if(ui.item){
            $("#emp_name").val(ui.item.fullname);
            $("#last_name_id").val(ui.item.last_name);
            $("#first_name_id").val(ui.item.first_name);
            $("#username_id").val(ui.item.id);
            $("#employee_number_id").val(ui.item.employee_number);
            $("#id_code_id").val(ui.item.id_code);
            $("#email_id").val(ui.item.email);
            $("#department_id").val(ui.item.department_id);
            $("#group_id").val(ui.item.group_id);
            $("#group_id2").html(ui.item.group_id);
            // $("#h_division").val(ui.item.division);
            // $("#h_title").val(ui.item.title);
        }
    }
    })
$("#employee_name").autocomplete({
    source: APPLICATION_URL + "DbEmployeeService?format=json",
    minLength: 2,
    dataType:"json",
    delay: 100,
    select: function( event, ui ) {
        if(ui.item){
            $("#employee_name").val(ui.item.full_name);
            $("#employee_id").val(ui.item.id);
        }
    }
    })
jQuery(function ($) {
    var launcherClick = function(e)  {
            var openMenus   = $('.menuLinks.open'),
                menu        = $(e.target).siblings('.menuLinks');
            openMenus.removeClass('open');
            setTimeout(function() { openMenus.addClass('closed'); }, 300);

            menu.removeClass('closed');
            menu.   addClass('open');
            e.stopPropagation();
        },
        documentClick = function(e) {
            var openMenus   = $('.menuLinks.open');

            openMenus.removeClass('open');
            setTimeout(function() { openMenus.addClass('closed'); }, 300);
        };
    $('.menuLauncher').click(launcherClick);
    $(document       ).click(documentClick);
});
$(document).on("click","button", function (event) {
  clicked_button_id = event.target.id;
});

function doRefresh(){
    document.getElementById("action2").value="Refresh";
    document.getElementById("form_id").submit();
}
$('#show_info_button').click(function() {
    $('#show_info').hide();
    $('#hide_info').show();
    return false;
});
$('#hide_info_button').click(function() {
    $('#show_info').show();
    $('#hide_info').hide();
    return false;
});
// using group ids
$('#department_id_change').change(function() {
    var $option = $(this).find('option:selected');
    var dept_id = $option.val();
    var dept_name = $option.text();
    var $options = $("#group_name_set");
    $("#dept_name_id").val(dept_name);
    $options.prop('disabled',false);
    $options.empty();
    $options.append(new Option("Pick a group","-1"));
    $.getJSON(APPLICATION_URL + "GroupService?department_id="+dept_id, function(result) {
        $.each(result, function(key,item) {
            $options.append(new Option(item.name,item.name));
        })
    }).fail(function(xx, statusText, error){
        var err = statusText+","+error;
        // alert(err);
    })
});

/**
 * toggle input of time-in, time-out or hours depending
 * on hour code for example Reg hour code requires time-in, time-out
 * while PTO requires hours input
 */
$('#hour_code_select').change(function() {
    var $option = $(this).find('option:selected');
    var val = $option.val();
    if(val.indexOf('Hours') > -1){
        $('#div_hours').show();
        $('#hour_change').attr('tabindex',5);
        $('#time_in').val("23:59"); // 0
        $('#time_out').val("23:59");
        $('#time_in').attr('tabindex',-1);
        $('#time_out').attr('tabindex',-1);
        $('#time_overnight').attr('tabindex',-1);
        $('#div_time_in').hide();
        $('#div_time_out').hide();
        $('#div_overnight').hide();
    }
    else{
        $('#div_hour_change').val(0);
        $('#div_hours').hide();
        $('#div_time_in').show();
        $('#div_time_out').show();
        $('#div_overnight').show();
        $('#hour_change').attr('tabindex',-1);
        $('#time_in').attr('tabindex',2);
        $('#time_out').attr('tabindex',3);
        $('#time_overnight').attr('tabindex',4);
    }
});
/**
 * check the entered hours are available for certain
 * accruals such as PTO, SCK, CU
 */
$('#div_hour_change').change(function() {
    var val = $(this).val();
    var $option = $('#hour_code_select').find('option:selected');
    var code = $option.val();// 2_Hours id format
    var $obj = $('#'+code);
    if($obj.length > 0){
        var aval = $obj.val();
        if(+val > +aval){ // +x will convert to a number
            alert("Entered value "+val+" greated than available balance "+aval);
            $(this).val(0);
        }
    }
    if(code == "2_Hours"){ // PTO can not be less than 1
                           //and must be multiple of0.25
        if(+val < 1.0){
            alert("Entered value "+val+" can not be less than 1 hour ");
            $(this).val(0);
        }
        else if((+val) % 0.25 > 0){
            alert("Entered value "+val+" must be a multiple of 0.25 hour ");
            $(this).val(0);
        }
    }
});

function windowOpener(url, name, args) {
    if(typeof(popupWin) != "object" || popupWin.closed)  {
        popupWin =  window.open(url, name, args);
    }
    else{
        popupWin.location.href = url;
    }
    setTimeout(function(){popupWin.focus();},1000);
 }
function changeGroupUsers(obj, sct_id){
    var group_id = "";
    group_id = obj.options[obj.options.selectedIndex].value;
    if(group_id == '-1'){
        var sct = document.getElementById(sct_id);
        sct.options.length = 0;
        sct.options[0] = new Option ('Pick a User', '');
        sct.options[0].selected="true";
        return;
    }
    $.ajax({
        url: APPLICATION_URL + "GroupUserService?group_id="+group_id,
        dataType:'json'
    })
        .done(function( data, status ) {
            var sct = document.getElementById(sct_id);
            //
            // remove old options
            sct.options.length = 0;  // reset
            // start with empty option
            sct.options[0] = new Option ('Pick a User', '');
            sct.options[0].selected="true";
            for(var key in data){ // it is an array
                var obj2 = data[key];
                opt = document.createElement('option');
                opt.value=obj2.id;
                opt.text=obj2.fullname;
                sct.appendChild(opt);
            }
        })
        .error(function(x,status,err){
            alert(status+" "+err);
        });
}
function verifyCancel() {
    var x = confirm("Are you sure you want to cancel this request");
    if(x){
        document.getElementById("form_id").submit();
    }
    return x;
 }

/* Calendar Time Block:
 * Deletes single data entry
 * file: /jsp/calendarFullNew.jsp
 */
$(".delete-time-confirm").click(function() {
  // Selectors
  var block_id        = $(this).attr('data-block-id');
  var dataDate        = $(this).attr('data-date');
  var dateInfo        = $(this).attr('data-info');

  // Fire Remove jQuery Dialog
  removeDialog = $('.modal.remove').dialog({
    autoOpen:  false,
    title:     'Confirm Delete',
    modal:     true,
    draggable: false,
    width:     500,
    position: { my: "top center",
                at: "top center",
                of: ".tabs"
    },
    open: function (event, ui) {
      // Remove provided Titlebar, fill empty markup with values
      $(".ui-dialog-titlebar").remove();
      $('h1 small').html(dataDate);
      $('.details').html(dateInfo);
    },
    buttons: {
      Confirm: function() {
        // On confirm button click, POST, then reload the page
        $.post({
          url  : APPLICATION_URL + 'timeBlock?id=' + block_id +'&action=Delete',
          success : function(data){},
          complete: function () {
            setTimeout(function(){
              location.reload();
              window.location.reload();
            }, 5);
          },
          error: function () {}
        });
      },
      Cancel: function() {
        // On cancel button click, destroy the Dialog Modal
        removeDialog.dialog("destroy");
      }
    }
  });
  // Opens the Remove Dialog (removeDialog) Modal
  removeDialog.dialog("open");

  // Prevent click from 'normal' behavior of a href
  return false;
});

/* Calendar Time Block:
 * Edits single data entry
 * file: /jsp/calendarFullNew.jsp
 */
$(".data").click(function() {
  // Selectors
  var block_id        = $(this).attr('data-block-id');
  var timeBlockDate   = $(this).attr('data-date');
  var editURL         = APPLICATION_URL + 'timeBlock?id=' + block_id;
  var queryString     = $(".time-block-form").serialize();

  // Fire Edit jQuery Dialog
  editDialog = $('.modal.add-edit').dialog({
    autoOpen:  false,
    title:     'Edit Time Block' + timeBlockDate,
    modal:     true,
    draggable: false,
    cache:     false,
    width:     500,
    position: { my: "top center",
                at: "top center",
                of: ".tabs"
    },
    open: function (event, ui) {
      // Remove provided Titlebar
      $(".ui-dialog-titlebar").remove();

      // Get data to fill Dialog Modal
      var xhrGet = $.get({
        url  : editURL,
        data : queryString,
        success: function (data) {
          $('.modal.add-edit').html(data);
        },
        error: function () {}
      });

      // Focus Input Helper
      setTimeout(function(){
        // Selectors
        var hourCodeIdVal = $('[name="timeBlock.hour_code_id"]').val();
        var hourCodeId    = $('[name="timeBlock.hour_code_id"]');
        var hourCodeHour  = $('[name="timeBlock.hours"]');
        var timeIn        = $('[name="timeBlock.time_in"]');

        if(hourCodeIdVal === '1_Time') {
          // If opening a REG time, focus the time in
          timeIn.focus();
        } else {
          // focus the hours input for not REG hours
          hourCodeHour.focus();
        }

        // If selection changes, refocus input
        hourCodeId.change(function(){
          if($('option:selected', this).val() === '1_Time') {
            timeIn.focus();
          } else {
            hourCodeHour.focus();
          }
        })
      }, 500);
    },
    buttons: {
      Confirm: function() {
        // Selectors
        var hourCodeIdVal   = $('[name="timeBlock.hour_code_id"]').val();
        var submitURL       = APPLICATION_URL + 'timeBlock.action';
        var formData        = $(".time-block-form").serialize();
        var timeInElm       = $('[name="timeBlock.time_in"]');
        var timeOutElm      = $('[name="timeBlock.time_out"]');
        var hoursElm        = $('[name="timeBlock.hours"]');
        var alertElmP       = $('.alert').addClass('active').find('p');
        var alertElm        = $('.alert');
        var timeIn          = $('[name="timeBlock.time_in"]').val();
        var timeOut         = $('[name="timeBlock.time_out"]').val();
        var hours           = $('[name="timeBlock.hours"]').val();
        var timeInError     = hourCodeIdVal === '1_Time' && ['', 0.0].includes(timeIn);
        var timeOutError    = hourCodeIdVal === '1_Time' && ['', 0.0].includes(timeOut);
        var hoursError      = hourCodeIdVal != '1_Time' && ['', '0.0' , 0.0].includes(hours);

        // Check values before Posting
        if(timeInError){
          // Time In cannot be emtpy
          alertElmP.html("Time In cannot be empty.");
          timeInElm.focus();
          return false;
        } else if (timeOutError) {
          // Time Out cannot be emtpy
          alertElmP.html("Time Out cannot be empty.");
          timeOutElm.focus();
          return false;
        } else if (hoursError) {
          // Non-REG Hours cannot be emtpy
          alertElmP.html("Hours cannot be 0.0");
          hoursElm.focus();
          return false;
        } else {
          // Remove any alert
          alertElm.remove();
          // Post the Edited Calendar Time Block
          $.post({
            url: submitURL,
            data: formData,
            success: function() {},
            complete: function () {
              setTimeout(function(){
                location.reload();
                window.location.reload();
              }, 5);
            }
          });
        }
      },
      Cancel: function() {
        // On cancel button click, destroy the Dialog Modal
        editDialog.dialog("destroy");
        $(this).empty();
      }
    },
    Close : function() {
      $('.modal.add-edit').empty();
    }
  });

  // Opens the Edit Dialog (editDialog) Modal
  editDialog.dialog("open");

  // Prevent default click clash
  return false;
});

/* Calendar Time Block:
 * Adds single data entry
 * file: /jsp/calendarFullNew.jsp
 */
$(".day").click(function() {
  // Selectors
  var docId               = $(this).attr('data-doc-id');
  var timeBlockDate       = $(this).attr('data-date');
  var timeBlockOrderIndex = $(this).attr('data-order-index');
  var addURL              = APPLICATION_URL + 'timeBlock?document_id=' + docId + '&date=' + timeBlockDate + '&order_index=' + timeBlockOrderIndex;
  var queryString         = $(".time-block-form").serialize();

  // Fire Edit jQuery Dialog
  addDialog = $('.modal.add-edit').dialog({
    autoOpen:  false,
    title:     'Add Time Block' + timeBlockDate,
    modal:     true,
    draggable: false,
    cache:     false,
    width:     500,
    position: { my: "top center",
                at: "top center",
                of: ".tabs"
    },
    open: function (event, ui) {
      $(".ui-dialog-titlebar").remove();

      // Get data to fill Dialog Modal
      var xhrGet = $.get({
        url  : addURL,
        data : queryString,
        success: function (data) {
          $('.modal.add-edit').html(data);
        },
        error: function () {},
      });

      // Focus Input Helper
      setTimeout(function(){
        // Selectors
        var hourCodeIdVal = $('[name="timeBlock.hour_code_id"]').val();
        var hourCodeId    = $('[name="timeBlock.hour_code_id"]');
        var hourCodeHour  = $('[name="timeBlock.hours"]');
        var timeIn        = $('[name="timeBlock.time_in"]');

        if(hourCodeIdVal === '1_Time') {
          // If opening a REG time, focus the time in
          timeIn.focus();
        } else {
          // focus the hours input for not REG hours
          hourCodeHour.focus();
        }

        // If selection changes, refocus input
        hourCodeId.change(function(){
          if($('option:selected', this).val() === '1_Time') {
            timeIn.focus();
          } else {
            hourCodeHour.focus();
          }
        })
      }, 500);
    },
    buttons: {
      Confirm: function() {
        // Selectors
        var hourCodeIdVal   = $('[name="timeBlock.hour_code_id"]').val();
        var submitURL       = APPLICATION_URL + 'timeBlock.action';
        var formData        = $(".time-block-form").serialize();
        var timeInElm       = $('[name="timeBlock.time_in"]');
        var timeOutElm      = $('[name="timeBlock.time_out"]');
        var hoursElm        = $('[name="timeBlock.hours"]');
        var alertElmP       = $('.alert').addClass('active').find('p');
        var alertElm        = $('.alert');
        var timeIn          = $('[name="timeBlock.time_in"]').val();
        var timeOut         = $('[name="timeBlock.time_out"]').val();
        var hours           = $('[name="timeBlock.hours"]').val();

        var timeInError     = hourCodeIdVal === '1_Time' && ['', 0.0].includes(timeIn);
        var timeOutError    = hourCodeIdVal === '1_Time' && ['', 0.0].includes(timeOut);
        var hoursError      = hourCodeIdVal != '1_Time' && ['', '0.0' , 0.0].includes(hours);

         // Check values before Posting
        if(timeInError){
          // Time In cannot be emtpy
          alertElmP.html("Time In cannot be empty.");
          timeInElm.focus();
          return false;
        } else if (timeOutError) {
          // Time Out cannot be emtpy
          alertElmP.html("Time Out cannot be empty.");
          timeOutElm.focus();
          return false;
        } else if (hoursError) {
          // Non-REG Hours cannot be emtpy
          alertElmP.html("Hours cannot be 0.0");
          hoursElm.focus();
          return false;
        } else {
          // Remove any alert
          alertElm.remove();
          // Post the Added Calendar Time Block
          $.post({
            url: submitURL,
            data: formData,
            success: function() {},
            complete: function () {
              setTimeout(function(){
                location.reload();
                window.location.reload();
              }, 5);
            }
          });
        }
      },
      Cancel: function() {
        // On cancel button click, destroy the Dialog Modal
        addDialog.dialog("destroy");
        $(this).empty();
      }
    },
    Close : function() {
      $('.modal.add-edit').empty();
    }
  });

  // Opens the Add Dialog (addDialog) Modal
  addDialog.dialog("open");

  // Prevent default click clash
  return false;
});

/* If a Time Block day data entry has no time out,
 * this adds an 'incomplete-time' class for style */
$('.data').each(function() {
  if($(this).attr('data-time-out') == "" || $(this).attr('data-time-out') == "12:00 AM")
    $(this).addClass("incomplete-time");
});