<?xml version="1.0" encoding="UTF-8" ?>
<!--
 * @copyright Copyright (C) 2014-2018 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <s:head />
  <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
  <link rel="SHORTCUT ICON" href="https://apps.bloomington.in.gov/favicon.ico" />

  <link rel="stylesheet" href="<s:property value='#application.url' />js/jquery-ui.min-1.12.1.css" type="text/css" media="all" />
  <link rel="stylesheet" href="<s:property value='#application.url' />js/jquery-ui.theme-1.12.1.css" type="text/css" media="all" />

  <link rel="stylesheet" href="<s:property value='#application.url' />css/main.css" type="text/css">

	<style>
	.tbl_wheat{
		background-color:#FFFFF0;
		color:black;
	}
	.tbl_weekend{
		background-color:lightgray;
		color:black;
	}
	.tbl_gray{
		background-color:#E9E9E9;
		color:black;
	}
	.td_text {
		font-size: x-small;
		padding: 1px 5px 1px 5px;
		}
	.th_text {
		font-size: small;
		font-weight: bold;
		padding: 1px 5px 1px 5px;
		}
	.th_text2 {
		display: block;
		height: 100%;
		width: 100%;
		}
	.cap_left {
		text-align:left;
		font-weight:bold;
		}
	.hr_cell{
		padding:1px 5px 1px 5px;
		display:block;
		text-decoration:none;
	}
	.b_cell{
		border:1px dotted;
		background-color: brown;
		color:white;
	}
	.button_link{
		background-color: brown;
		color:white;
	}
	.tbl_wide{
		width:95%;
		border:1px solid blue;
	}

	</style>
  <title>Time Track</title>
  <script type="text/javascript">
    var APPLICATION_URL = '<s:property value='#application.url' />';
  </script>
</head>
<body class="fn1 timetrack">
  <header>
    <div class="container">
      <div class="brand">
        <a href="<s:property value='#application.url'/>">
          <img src="data:image/svg+xml;utf8,<svg id='cob-logo' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 225 225'><path id='shape' d='M91.071,0,112.5,21.429,133.929,0V42.857l21.428-21.428V64.286L133.929,85.714,112.5,64.286,91.071,85.714,69.643,64.286V21.429L91.071,42.857ZM50.3,35.412,36.729,21.838V36.991H21.576L35.15,50.564,21.429,64.286H64.286V21.429ZM0,133.929,21.429,112.5,0,91.071H42.857L21.429,69.643H64.286L85.714,91.071,64.286,112.5l21.428,21.429L64.286,155.357H21.429l21.428-21.428Zm64.286,26.785H21.429L35.412,174.7,21.838,188.272H36.99v15.152L50.564,189.85l13.722,13.721ZM133.929,225,112.5,203.571,91.071,225V182.143L69.643,203.571V160.714l21.428-21.428L112.5,160.714l21.429-21.428,21.428,21.428v42.857l-21.428-21.428Zm69.642-64.286H160.714v42.857L174.7,189.588l13.574,13.574V188.009h15.152L189.85,174.436ZM225,91.071,203.571,112.5,225,133.929H182.143l21.428,21.428H160.714l-21.428-21.428L160.714,112.5,139.286,91.071l21.428-21.428h42.857L182.143,91.071ZM189.588,50.3l13.574-13.574H188.009V21.576L174.436,35.15,160.714,21.429V64.286h42.857Z' transform='translate(0 0)' fill='%231e59ae'/></svg>">
          <h1>Time Track</h1>
          <h2>City of Bloomington, IN</h2>
        </a>
      </div>

      <div class="dropdown-wrapper">
        <s:if test="#session != null && #session.user != null">
          <nav class="dropdown">
            <button id="genericMenuLauncher"
                    class="launcher"
                    aria-haspopup="true"
                    aria-expanded="false">
                    <s:property value='#session.user.full_name' />
            </button>

            <div class="links" aria-labeledby="genericMenuLauncher" hidden>
              <s:if test="#session.user.isAdmin()">
                <a href="<s:property value='#application.url'/>settings.action">Settings</a>
              </s:if>
              <a href="<s:property value='#application.url'/>Logout">Logout</a>
            </div>
          </nav>
        </s:if>
      </div>
    </div>
  </header>

  <main class="container">
    <div class="nav1">
      <nav class="nav1 container">
        <s:if test="#session != null && #session.user != null">
          <a href="<s:property value='#application.url'/>timeDetails.action">Time Details</a>

          <s:if test="#session.user.isAdmin() || (#session.user.isEmployee() && #session.user.canDataEntry())">
            <a href="<s:property value='#application.url'/>dataEntry.action">Data Entry</a>
          </s:if>

          <s:if test="#session.user.isAdmin() || (#session.user.isEmployee() && #session.user.canApprove())">
            <a href="<s:property value='#application.url'/>approve.action">Timesheet Approval</a>
          </s:if>

          <s:if test="#session.user.isAdmin() || (#session.user.isEmployee() && #session.user.canProcess())">
            <a href="<s:property value='#application.url'/>payrollProcess.action">Payroll Approval</a>
          </s:if>

          <a href="<s:property value='#application.url'/>settings.action">Settings</a>
        </s:if>
      </nav>
    </div>
