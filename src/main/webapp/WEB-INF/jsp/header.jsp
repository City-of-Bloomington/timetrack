<?xml version="1.0" encoding="UTF-8" ?>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
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

  <link rel="stylesheet" href="//bloomington.in.gov/static/fn1-releases/dev/css/default.css" type="text/css" />
  <link rel="stylesheet" href="//bloomington.in.gov/static/fn1-releases/dev/css/kirkwood.css" type="text/css" />

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
<body class="fn1-body">
  <header class="fn1-siteHeader">
    <div class="fn1-siteHeader-container">
      <div class="fn1-site-title">
        <h1 id="application_name"><a href="<s:property value='#application.url'/>">Time Track</a></h1>
        <div class="fn1-site-location" id="location_name"><a href="<s:property value='#application.url'/>">City of Bloomington, IN</a></div>
      </div>
      <s:if test="#session != null && #session.user != null">
        <div class="fn1-site-utilityBar">
          <nav id="user_menu">
            <div class="menuLauncher"><s:property value='#session.user.full_name' /></div>
            <div class="menuLinks closed" style="background-color:wheat">
              <br />
              <a href="<s:property value='#application.url'/>Logout">Logout</a>
            </div>
          </nav>
          <s:if test="#session.user.isAdmin()">
            <nav id="admin_menu">
              <div class="menuLauncher">Admin</div>
              <div class="menuLinks closed" style="background-color:wheat">
                <br />
                <a href="<s:property value='#application.url'/>settings.action">Settings</a>
              </div>
            </nav>
          </s:if>
        </div>
    </s:if>
  </div>
  <div class="fn1-nav1">
      <nav class="fn1-nav1-container">
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
  </header>
  <main>
    <div class="fn1-main-container tbl_gray">
