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

  <title>Time Track</title>
  <script type="text/javascript">
    var APPLICATION_URL = '<s:property value='#application.url' />';
  </script>
</head>
<body class="fn1 timetrack">
  <%@ include file="headerNavigation.jsp" %>

  <div class="tabs container">
    <s:if test="#session != null && #session.user != null">
      <a href="<s:property value='#application.url'/>timeDetails.action">Time Details</a>

      <s:if test="user.isAdmin() || (user.isEmployee() && user.canDataEntry())">
        <a href="<s:property value='#application.url'/>dataEntry.action">Data Entry</a>
      </s:if>

      <s:if test="user.isAdmin() || (user.isEmployee() && user.canApprove())">
        <a href="<s:property value='#application.url'/>approve.action">Timesheet Approval</a>
      </s:if>

      <s:if test="user.isAdmin() || (user.isEmployee() && user.canPayrollProcess())">
        <a href="<s:property value='#application.url'/>payrollProcess.action">Payroll Approval</a>
      </s:if>

    </s:if>
  </div>

  <main class="container">
