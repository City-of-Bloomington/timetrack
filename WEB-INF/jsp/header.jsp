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
  <link rel="stylesheet" href="<s:property value='#application.url' />css/open-sans/open-sans.css" type="text/css" />
  <link rel="stylesheet" href="//bloomington.in.gov/static/fn1-releases/dev/css/default.css" type="text/css" />
  <link rel="stylesheet" href="//bloomington.in.gov/static/fn1-releases/dev/css/kirkwood.css" type="text/css" />
  <link rel="stylesheet" href="<s:property value='#application.url' />css/screen.css" type="text/css" />
  <link rel="stylesheet" href="<s:property value='#application.url' />css/ol.css" type="text/css" />
  <link rel="stylesheet" href="<s:property value='#application.url' />css/ol3-popup.css" type="text/css" />	
	
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
				<a href="<s:property value='#application.url'/>timeDetails.action">Time Details</a>
        <s:if test="#session.user.isAdmin() || (#session.user.isEmployee() && #session.user.employee.canDataEntry())">								
					<a href="<s:property value='#application.url'/>dataEntry.action">Data Entry</a>								
				</s:if>				
        <s:if test="#session.user.isAdmin() || (#session.user.isEmployee() && #session.user.employee.canApprove())">								
					<a href="<s:property value='#application.url'/>approve.action">Approvals</a>								
				</s:if>
        <s:if test="#session.user.isAdmin() || (#session.user.isEmployee() && #session.user.employee.canProcess())">								
					<a href="<s:property value='#application.url'/>payrollProcess.action">Payroll Process</a>								
				</s:if>
				<a href="<s:property value='#application.url'/>settings.action">Settings</a>				
      </nav>
    </div>
  </header>
  <main>
    <div class="fn1-main-container">
