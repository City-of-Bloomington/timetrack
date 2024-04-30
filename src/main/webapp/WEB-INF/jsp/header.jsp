<?xml version="1.0" encoding="UTF-8" ?>
<!--
 * @copyright Copyright (C) 2014-2018 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<s:head />
	<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
	<link rel="SHORTCUT ICON" href="<s:property value='#application.url' />images/favicon.ico" />
	<link rel="stylesheet" href="<s:property value='#application.url' />css/jquery-ui.min-1.13.2.css" type="text/css" media="all" />
	<link rel="stylesheet" href="<s:property value='#application.url' />css/jquery-ui.theme.min-1.13.2.css" type="text/css" media="all" />
	
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
      <s:if test="user.isTermManager()">
	  <a href="<s:property value='#application.url'/>activeEmployees.action">Current Employees</a>
      </s:if>
      <s:if test="user.canPayrollProcess()">
          <a href="<s:property value='#application.url'/>payrollProcess.action">Payroll Approval</a>
      </s:if>
      <s:if test="user.isHrAdmin()">
          <a href="<s:property value='#application.url'/>reportFmla.action">FMLA Report</a>
          <a href="<s:property value='#application.url'/>reportEl.action">EL Report</a>	      
	  <li><a href="<s:property value='#application.url'/>reportAsa.action">ASA Report</a></li>								
          <a href="<s:property value='#application.url'/>lookup.action">Lookup Time Entries</a>
      </s:if>
      <s:if test="user.isITSAdmin()">
	  <a href="<s:property value='#application.url'/>searchEmployees.action">Search Employees</a>
      </s:if>
      <s:if test="user.canRunTargetEmployee()">
          <a href="<s:property value='#application.url'/>switch.action">Change Target Employee</a>
      </s:if>
      <s:if test="user.canSearchEmployee()">
          <a href="<s:property value='#application.url'/>searchEmployees.action">Search Employees</a>
      </s:if>
      <s:if test="user.canAdSearch()">
          <a href="<s:property value='#application.url'/>employee.action">Search AD</a>
      </s:if>      
      <s:if test="user.isEngineeringAdmin()">
	  <a href="<s:property value='#application.url'/>reportFiber.action">Fiber Report</a>
      </s:if>      
      <s:if test="user.isPublicWorksAdmin()">
        <a href="<s:property value='#application.url'/>reportPublicWorks.action">Asset Management Report</a>
      </s:if>			
      <s:if test="user.isPoliceAdmin()">
	  <a href="<s:property value='#application.url'/>reportReason.action">Police Reason Report</a>
      </s:if>
      <s:if test="user.canRunMpoReport()">
              <a href="<s:property value='#application.url'/>reportPlan.action">Planning MPO Report</a>
      </s:if>
      <s:if test="user.canRunHandReport()">
              <a href="<s:property value='#application.url'/>reportHand.action">HAND MPO Report</a>
      </s:if>			
      <s:if test="user.canRunFireReport()">
          <a href="<s:property value='#application.url'/>shiftTime.action?department_id=16">Employee Shift Times</a>
      </s:if>
      
      <s:if test="user.isParkAdmin()">
	  <a href="<s:property value='#application.url'/>jobsReport.action?department_id=5">Current Employees Jobs</a>
	  <a href="<s:property value='#application.url'/>reportTimes.action?department_id=5">Employee Time Details</a>
      </s:if>
      <s:if test="user.canRunTimewarp()">
	  <a href="<s:property value='#application.url'/>tmwrpWrap.action">Timewarp </a>	
      </s:if>      
    </s:if>
  </div>

  <main class="container">
