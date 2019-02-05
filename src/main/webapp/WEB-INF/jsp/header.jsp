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

      <s:if test="user.canMaintain()">
        <a href="<s:property value='#application.url'/>dataEntry.action">Time Maintenance</a>
      </s:if>

      <s:if test="user.canReview()">
        <a href="<s:property value='#application.url'/>review.action">Timesheet Review</a>
      </s:if>

      <s:if test="user.canApprove()">
        <a href="<s:property value='#application.url'/>approve.action">Timesheet Approval</a>
      </s:if>

      <s:if test="user.canPayrollProcess()">
        <a href="<s:property value='#application.url'/>payrollProcess.action">Payroll Approval</a>
      </s:if>
      <s:if test="user.isHrAdmin()">
        <a href="<s:property value='#application.url'/>reportFmla.action">HR FMLA Report</a>
        <a href="<s:property value='#application.url'/>switch.action">Change Target Employee</a>				
      </s:if>
      <s:if test="user.canRunMpoReport()">
        <a href="<s:property value='#application.url'/>reportPlan.action">Planning MPO Report</a>
      </s:if>			
      <s:if test="user.canRunTimewarp()">
        <a href="<s:property value='#application.url'/>payperiodProcess.action">Timewarp</a>
      </s:if>
			<s:if test="user.canRunParkReport()">
				<a href="<s:property value='#application.url'/>switch.action">Change Target Employee</a>
				<a href="<s:property value='#application.url'/>jobTitles.action"> Jobs Need Intervention</a>				
				<a href="<s:property value='#application.url'/>parksJobReport.action">Current Employee Jobs</a>
			</s:if>
      <s:elseif test="user.canRunTargetEmployee()">
        <a href="<s:property value='#application.url'/>switch.action">Change Target Employee</a>
      </s:elseif>
    </s:if>
  </div>

  <main class="container">
