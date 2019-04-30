clicked_button_id="";
var icons = {
    header:"ui-icon-circle-plus",
    activeHeader:"ui-icon-circle-minus"
};

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
$("#employee_name2").autocomplete({
    source: APPLICATION_URL + "DbEmployeeService?format=json",
    minLength: 2,
    dataType:"json",
    delay: 100,
    select: function( event, ui ) {
        if(ui.item){
            $("#employee_name2").val(ui.item.full_name);
            $("#employee_id").val(ui.item.id);
        }
    }
    })
$("#employee_name").autocomplete({
    source: APPLICATION_URL + "DbEmployeeService?format=json&department_id="+$("#department_id").val(),
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
//
// when department change we want to populate
// the group list
//
$('#department_id_change').change(function() {
    var $option = $(this).find('option:selected');
    var dept_id = $option.val();
    var dept_name = $option.text();
    $.ajax({
        url: APPLICATION_URL + "GroupService?department_id="+dept_id,
        dataType:'json'
    })
    .done(function( data, status ) {
				var options = $("#group_id_set");
				options.prop('disabled',false);
				options.empty();
				options.append(new Option("Pick a group","-1"));
        for(key in data){ // it is an array
            // var obj = data[key];
            options.append(new Option(data[key].name, data[key].id));
        }
    })
    .error(function(x,status,err){
        alert(status+" "+err);
    });
})
//
// the following function will be used by timeBlock page
// when hour code change to one that have earnReasons, we need to
// show earn_code_reason_id options
// disabled for now

function handleShowCodeReason(val){
    $.ajax({
        url: APPLICATION_URL + "CodeReasonService?salary_group_id="+$("#salary_group_id").val()+"&group_id="+$("#group_id").val()+"&hour_code_id="+val,
        dataType:'json'
    })
    .done(function( data, status ) {
				var options = $("#select_reason_id");
				options.prop('disabled',false);
				options.empty();
				options.append(new Option("Pick a Reason","-1"));
        for(key in data){ // it is an array
            // var obj = data[key];
            options.append(new Option(data[key].name, data[key].id));
        }
				$("#reason_div_id").show();				
    })
    .error(function(x,status,err){
        alert(status+" "+err);
    });
}

//
//
$('#job_salary_group_change').change(function() {
    var $option = $(this).find('option:selected');
    var sel_name = $option.text();
		if(sel_name == 'Temp'){
				$("#weekly_hrs_id").val("20");
				$("#comp_factor_id").val("1.5");
				$("#holiday_factor_id").val("1.5");
				$("#clock_required_id").attr('checked', true);				
		}
})
$('#start_date_id').change(function() {
    var val = $(this).val();
		var all_dates = $("#all_dates_id").val();
		if(all_dates.trim() != ''){
				if(all_dates.indexOf(val) == -1){
						all_dates += ", "+val;
						$("#all_dates_id").val(all_dates);
				}
		}
		else{
				$("#all_dates_id").val(val);
		}
		$(this).val('');
})

/**
 * toggle input of time-in, time-out, hours or amount depending
 * on hour code for example Reg hour code requires time-in, time-out
 * while PTO requires hours input
 */
$('#hour_code_select').change(function() {
    var $option = $(this).find('option:selected');
    var val = $option.val();
    if(val.indexOf('Hours') > -1){
        $('#div_hours').show();
				$('#div_amount').hide();
        $('#hour_change').attr('tabindex',5);
        $('#time_in').attr('tabindex',-1);
        $('#time_out').attr('tabindex',-1);
        $('#time_overnight').attr('tabindex',-1);
				$('#amount_change').val('');				
        $('#div_time_in').hide();
        $('#div_time_out').hide();
        $('#div_overnight').hide();
				$("#reason_div_id").hide();	
				$("#select_reason_id").val('');
    }
    else if(val.indexOf('Monetary') > -1){
        $('#div_amount').show();
        $('#amount_change').attr('tabindex',5);
        $('#div_hours').hide();				
        $('#time_in').attr('tabindex',-1);
        $('#time_out').attr('tabindex',-1);
        $('#time_overnight').attr('tabindex',-1);
        $('#div_time_in').hide();
        $('#div_time_out').hide();
        $('#div_overnight').hide();
				$("#reason_div_id").hide();	
				$("#select_reason_id").val('');
				var $obj = $('#'+val);
				if($obj){
						var amount = $obj.val();
						$('#amount_change').val(amount);						
				}
    }		
    else{
        $('#div_hour_change').val(0);
        $('#div_hours').hide();
        $('#div_amount').hide();
				$('#amount_change').val('');						
        $('#div_time_in').show();
        $('#div_time_out').show();
        $('#div_overnight').show();
        $('#hour_change').attr('tabindex',-1);
        $('#time_in').attr('tabindex',2);
        $('#time_out').attr('tabindex',3);
        $('#time_overnight').attr('tabindex',4);
				//
				// we call earn_code_reason function here
				//
				var codes = ['113','114','115','116'];
				var code_id = val.substring(0,val.indexOf('_')); // id only
				if(codes.includes(code_id)){
						handleShowCodeReason(code_id);
				}
				else{
						$("#reason_div_id").hide();	
						$("#select_reason_id").val('');
				}
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
				var aval2 = 0; // old value if exist
				var $obj2 = $('#'+code+'_old');
				if($obj2){
						if($obj2.length > 0){ // if old value exist for the same code
								aval2 = $obj2.val();
								aval = (+aval+aval2);
						}
				}
        if(+val > +aval){ // +x will convert to a number
            alert("Entered value "+val+" greated than available balance "+aval);
            $(this).val(0);
        }
    }
    if(code == "2_Hours"){ //2 is PTO id, PTO can not be less than 1
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

function outsideClickCloseModal(dialogType) {
  $('.ui-widget-overlay').bind('click', function() {
    $('.modal').empty();
    dialogType.dialog("destroy");
  });
}

/* Calendar Time Block:
 * Deletes single data entry
 * file: /jsp/calendarFullNew.jsp
 */
$(".delete-time-confirm").on("keyup click", function(e) {
  e.stopPropagation();
  if (e.type == "click" || e.keyCode === 13) {
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
      position: { my: "top",
                  at: "top center",
                  of: ".tabs"
      },
      open: function (event, ui) {
        // Remove provided Titlebar, fill empty markup with values
        $(".ui-dialog-titlebar").remove();
        $('h1 small').html(dataDate);
        $('.details').html(dateInfo);

        outsideClickCloseModal(removeDialog);
      },
      buttons: {
        Confirm: function() {
          // On confirm button click, POST, then reload the page
          var jqxhr = $.post({
            url  : APPLICATION_URL + 'timeBlock?id=' + block_id +'&action=Delete'
          })
          .done(function(data) {
            setTimeout(function(){
              window.location = window.location;
            }, 5);
          })
          .fail(function(jqXHR, status, textStatus, error, responseText) {
            var resText = JSON.parse(jqXHR.responseText);
            $.each( resText, function( i, val ) {
              $(".alert p").addClass('show').html(val);
            });
          })
          .always(function() {});
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
  }
});

/* Sets the tabindex attribute value for confirm/cancel modal
 * dialog actions. Note: this value needs to be higher than
 * any value set on form elements. */
var dialogConfirmBtn = $(".ui-dialog-buttonpane .ui-dialog-buttonset button:nth-of-type(1)");
var dialogCancelBtn = $(".ui-dialog-buttonpane .ui-dialog-buttonset button:nth-of-type(2)");
dialogConfirmBtn.attr("tabindex", "19");
dialogCancelBtn.attr("tabindex", "20");

// Confirm/Submit - Time Block Dialog Enter click to submit
function submitDialogOnEnter(){
  $('.ui-dialog').on('keyup', function(event) {
    if (event.keyCode === $.ui.keyCode.ENTER) {
      $('.ui-dialog-buttonpane button:first', $(this)).click();
    }
  });
}

/* Calendar Time Block:
 * Edits single data entry
 * file: /jsp/calendarFullNew.jsp
 */
$(".data").on("keyup click", function(e) {
  e.stopPropagation();
  if (e.type == "click" || e.keyCode === 13) {
    // Selectors
    var block_id        = $(this).attr('data-block-id');
    var timeBlockDate   = $(this).attr('data-date');
    var alertElm        = $('.time-block .alert');
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
      position: { my: "top",
                  at: "top center",
                  of: ".tabs"
      },
      open: function (event, ui) {
        // Remove provided Titlebar
        $(".ui-dialog-titlebar").remove();

        outsideClickCloseModal(editDialog);
        submitDialogOnEnter();

        // Get data to fill Dialog Modal
        var jqxhr = $.get({
          url  : editURL,
          data : queryString,
        })
        .done(function(data) {
          $('.modal.add-edit').html(data);
        })
        .fail(function(jqXHR, status, textStatus, error, responseText) {
          var resText = JSON.parse(jqXHR.responseText);
          $.each( resText, function( i, val ) {
            alertElm.addClass('show').children( "p" ).html(val);
          });
        })
        .always(function() {});

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
          var alertElmP       = $('.time-block .alert').addClass('active').find('p');
          var alertElm        = $('.time-block .alert');
          var timeIn          = $('[name="timeBlock.time_in"]').val();
          var timeOut         = $('[name="timeBlock.time_out"]').val();
          var hours           = $('[name="timeBlock.hours"]').val();

          var timeInError     = hourCodeIdVal === '1_Time' && ['', 0.0].indexOf(timeIn) != -1;
          var timeOutError    = hourCodeIdVal === '1_Time' && ['', 0.0].indexOf(timeOut) != -1;
          var hoursError      = hourCodeIdVal != '1_Time' && ['', '0.0' , 0.0].indexOf(hours) != -1;

          // Post the Edited Calendar Time Block
          var jqxhr = $.post({
            url: submitURL,
            data: formData,
          })
          .done(function(data) {
            alertElm.remove();
            alertElm.addClass('hide');
            setTimeout(function(){
              window.location = window.location;
            }, 5);
          })
          .fail(function(jqXHR, status, textStatus, error, responseText) {
            var resText = JSON.parse(jqXHR.responseText);
            $.each( resText, function( i, val ) {
              alertElm.addClass('show').children( "p" ).html(val);
            });
          })
          .always(function() { });
        },
        Cancel: function() {
          // On cancel button click, destroy the Dialog Modal
          editDialog.dialog("destroy");
          $(this).empty();
        }
      },
      close: function() {
        $('.modal.add-edit').empty();
      }
    });

    // Opens the Edit Dialog (editDialog) Modal
    editDialog.dialog("open");

    // Prevent default click clash
    return false;
  }
});

/* Calendar Time Block:
 * Adds single data entry
 * file: /jsp/calendarFullNew.jsp
 */
$(".day").on("keyup click", function(e) {
  e.stopPropagation();
  if (e.type == "click" || e.keyCode === 13) {
    // Selectors
    var docId               = $(this).attr('data-doc-id');
    var timeBlockDate       = $(this).attr('data-date');
    var timeBlockOrderIndex = $(this).attr('data-order-index');
    var alertElm            = $('.time-block .alert');
    var addURL              = APPLICATION_URL + 'timeBlock?document_id=' + docId + '&date=' + timeBlockDate + '&order_index=' + timeBlockOrderIndex;
    var queryString         = $(".time-block-form").serialize();

    // Fire Add jQuery Dialog
    addDialog = $('.modal.add-edit').dialog({
      autoOpen:  false,
      title:     'Add Time Block' + timeBlockDate,
      modal:     true,
      draggable: false,
      cache:     false,
      width:     500,
      position: { my: "top",
                  at: "top center",
                  of: ".tabs"
      },
      open: function (event, ui) {
        $(".ui-dialog-titlebar").remove();

        outsideClickCloseModal(addDialog);
        submitDialogOnEnter();

        // Get data to fill Dialog Modal
        var jqxhr = $.get({
          url  : addURL,
          data : queryString
        })
        .done(function(data) {
          alertElm.remove();
          alertElm.addClass('hide');
          $('.modal.add-edit').html(data);
        })
        .fail(function(jqXHR, status, textStatus, error, responseText) {
          var resText = JSON.parse(jqXHR.responseText);
          $.each( resText, function( i, val ) {
            alertElm.addClass('show').children( "p" ).html(val);
          });
        })
        .always(function() {});

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
        Confirm: function(e) {
          // Selectors
          var hourCodeIdVal   = $('[name="timeBlock.hour_code_id"]').val();
          var submitURL       = APPLICATION_URL + 'timeBlock.action';
          var formData        = $(".time-block-form").serialize();
          var timeInElm       = $('[name="timeBlock.time_in"]');
          var timeOutElm      = $('[name="timeBlock.time_out"]');
          var hoursElm        = $('[name="timeBlock.hours"]');
          var alertElmP       = $('.time-block .alert').addClass('active').find('p');
          var alertElm        = $('.time-block .alert');
          var timeIn          = $('[name="timeBlock.time_in"]').val();
          var timeOut         = $('[name="timeBlock.time_out"]').val();
          var hours           = $('[name="timeBlock.hours"]').val();

          var timeInError     = hourCodeIdVal === '1_Time' && ['', 0.0].indexOf(timeIn) != -1;
          var timeOutError    = hourCodeIdVal === '1_Time' && ['', 0.0].indexOf(timeOut) != -1;
          var hoursError      = hourCodeIdVal != '1_Time' && ['', '0.0' , 0.0].indexOf(hours) != -1;


          e.preventDefault();
          alertElm.addClass('hide');
          // Post the Added Calendar Time Block
          var jqxhr = $.post({
            url: submitURL,
            data: formData
          })
          .done(function(jqxhr, status, data) {
            alertElm.remove();
            alertElm.addClass('hide');
            setTimeout(function(){
              window.location = window.location;
            }, 5);
          })
          .fail(function(jqXHR, status, textStatus, error, responseText) {
            var resText = JSON.parse(jqXHR.responseText);
            $.each( resText, function( i, val ) {
              alertElm.addClass('show').children( "p" ).html(val);
            });
          })
          .always(function() {});
        },
        Cancel: function() {
          // On cancel button click, destroy the Dialog Modal
          addDialog.dialog("destroy");
          $(this).empty();
        }
      },
      close: function(event, ui) {
        $('.modal.add-edit').empty();
      }
    });

    // Opens the Add Dialog (addDialog) Modal
    addDialog.dialog("open");

    // Prevent default click clash
    return false;
  }
});

/* If a Time Block day data entry has no time out,
 * this adds an 'incomplete-time' class for style */
$('.data').each(function() {
  var timeOut = $(this).attr('data-time-out');
  if(timeOut == "")
    $(this).addClass("incomplete-time");
});

/* Pay Period Notes:
 * Add notes to a pay period
 * file: /jsp/timeDetails.jsp
 */
$(".pay-notes").click(function() {
  // Selectors
  var docId               = $(this).attr('data-doc-id');
  var addURL              = APPLICATION_URL + 'timeNote?document_id=' + docId;
  var queryString         = $(".pay-notes-form").serialize();

  // Fire Pay Notes jQuery Dialog
  payNotesDialog = $('.modal.pay-notes').dialog({
    autoOpen:  false,
    title:     'Add Pay Period Notes',
    modal:     true,
    draggable: false,
    cache:     false,
    width:     500,
    position: { my: "top",
                at: "top center",
                of: ".tabs",
                collision: "none"
    },
    open: function (event, ui) {
      outsideClickCloseModal(payNotesDialog);

      $(".ui-dialog-titlebar").remove();
      $(event.target).parent().css('position', 'fixed');

      // Focus Input Helper
      setTimeout(function(){
        var textAreaElm     = $("#form_id_timeNote_notes");
        textAreaElm.focus();
      }, 500);

      // Get data to fill Dialog Modal
      var jqxhr = $.get({
        url  : addURL,
        data : queryString
      })
      .done(function(data, staus) {
        $('.modal.pay-notes').html(data);
      })
      .fail(function(jqxhr, status, error) {
        var err = JSON.parse(xhr.responseText);
        alert(err.error);
      })
      .always(function() {
        // alert( "finished" );
      });
    },
    buttons: {
      Submit: function() {
        var submitURL       = APPLICATION_URL + 'timeNote.action';
        var formData        = $(".pay-notes-form").serialize();

        var textAreaVal     = $("#form_id_timeNote_notes").val();
        var alertElmP       = $('.alert').addClass('active').find('p');
        var alertElm        = $('.alert');

        alertElm.remove();
        // Post the Added Calendar Time Block
        var jqxhr = $.post({
          url: submitURL,
          data: formData,
        })
        .done(function(data) {
          setTimeout(function(){
            window.location = window.location;
          }, 5);
        })
        .fail(function(jqxhr, status, error) {
          var err = JSON.parse(xhr.responseText);
          alert(err.error);
        })
        .always(function() {
          // alert( "finished" );
        });
      },
      Cancel: function() {
        // On cancel button click, destroy the Dialog Modal
        payNotesDialog.dialog("destroy");
        $(this).empty();
      }
    },
    Close : function() {
      $('.modal.pay-notes').empty();
    }
  });

  // Opens the Add Dialog (payNotesDialog) Modal
  payNotesDialog.dialog("open");

  // Prevent default click clash
  return false;
});
// Adam & Walid
// change class name to timesheet-submit-2 to cancel this effect right now
//
submitTimesheetObserver();
function submitTimesheetObserver(){
  // Selectors
  var submitFormBtn   = $('.timesheet-submit-2 input[type=submit]')
  var weekOneTotalVal = submitFormBtn.attr('data-week-one-total');
  var weekTwoTotalVal = submitFormBtn.attr('data-week-two-total');

  // Disabled Attribute Toggle
  if (weekOneTotalVal >= 40 && weekTwoTotalVal >= 40) {
    submitFormBtn.attr('disabled', false);
  } else {
    submitFormBtn.attr('disabled', true);
  }
}
/**
 * a toggle on approve and payroll process page to select all
 * checkboxes when select all is checked
 */
$('#approve_select_all').click(function() {
  $('.status-tag :checkbox').prop('checked', this.checked);
});

/**
 * Add active class to navigation tabs
 */
var locPath        = window.location.pathname;
var navItems       = $(".tabs a");
navItems.each(function(index, element) {
  if (this.pathname == locPath) {
    $(this).addClass("active");
  }
})

/**
 * Timesheet Approve / Process - Individual Approval Button
 */
$('.quick-approve').click(function() {
  var docId           = $(this).attr("data-doc-id");
  var pageURL         = window.location.pathname;

  var approveHref     = '/timetrack/approve.action';
  var processHref     = '/timetrack/payrollProcess.action';

  var approveURL      = APPLICATION_URL + 'approve.action?action=ApproveOne&document_id=' + docId;
  var processURL      = APPLICATION_URL + 'payrollProcess.action?action=PayrollOne&document_id=' + docId;

  if(pageURL == approveHref) {
    var submitURL  = approveURL;
  } else if(pageURL == processHref) {
    var submitURL = processURL;
  } else {
    alert("We don't know the URL for this click action.")
  }

  var jqxhr = $.post({ url: submitURL })
  .done(function(data) {
    setTimeout(function(){
      window.location = window.location.href;
    }, 50);
  })
  .fail(function(jqxhr, status, error) {
    var err = JSON.parse(xhr.responseText);
    alert(err.error);
  })
  .always(function() {});
});
