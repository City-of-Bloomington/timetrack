clicked_button_id="";
var icons = {
    header:"ui-icon-circle-plus",
    activeHeader:"ui-icon-circle-minus"
};
function setCurTime(id){
		date = new Date;
		h = date.getHours();
    if(h<10){
        h = "0"+h;
    }
    m = date.getMinutes();
    if(m<10){
        m = "0"+m;
    }
		result = ''+h+':'+m;
    document.getElementById(id).value = result;
    setTimeout('setCurTime("'+id+'");','1000');
    return true;		
};
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
            $("#emp_name").val(ui.item.id);
						$("#phone").html(ui.item.phone);
						$("#dept").html(ui.item.dept);
						$("#h_phone").val(ui.item.phone);
						$("#h_email").val(ui.item.email);
						$("#h_dept").val(ui.item.dept);
						$("#h_division").val(ui.item.division);
						$("#h_title").val(ui.item.title);						
        }
    }
		})
//		.data('ui-autocomplete')._renderItem = function (ul, item) {
//        return $('<li>')
//						.attr("data-value",item.value)
//            .appendTo(ul);
//    };

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
$('#department_id_change').change(function() {
		var $option = $(this).find('option:selected');
		var dept_id = $option.val();
		var $options = $("#group_id_set");
		$options.prop('disabled',false);
		$options.empty();
		$options.append(new Option("Pick a group","-1"));
		$.getJSON(APPLICATION_URL + "GroupService?department_id="+dept_id, function(result) {
				$.each(result, function(key,item) {
						$options.append(new Option(item.name,item.id));
				})
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
				$('#div_time_in').val(0);
				$('#div_time_out').val(0);			
				$('#div_time_in').hide();
				$('#div_time_out').hide();				
		}
		else{
				$('#div_hour_change').val(0);
				$('#div_hours').hide();
				$('#div_time_in').show();
				$('#div_time_out').show();	
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
		// alert(" selected "+code);
		var $obj = $('#'+code);
		if($obj.length > 0){ 
				var aval = $obj.val();
				if(+val > +aval){ // +x will convert to a number
						alert("Entered value "+val+" greated than available balance "+aval);
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
