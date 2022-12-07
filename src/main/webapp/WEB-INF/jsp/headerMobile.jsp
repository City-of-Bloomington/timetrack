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

  <link rel="stylesheet" href="<s:property value='#application.url' />js/jquery-ui.min-1.13.2.css" type="text/css" media="all" />
  <link rel="stylesheet" href="<s:property value='#application.url' />js/jquery-ui.theme.min-1.13.2.css" type="text/css" media="all" />

  <link rel="stylesheet" href="<s:property value='#application.url' />css/main.css" type="text/css">

  <title>Time Track</title>
  <script type="text/javascript">
    var APPLICATION_URL = '<s:property value='#application.url' />';
  </script>
  <script type="text/javascript">
  var locations = [
       <s:iterator var="one" value="#locations" status="iterStatus">
       [<s:property value="latitude" />,<s:property value="longitude" />,<s:property value="radius" />] <s:if test="#iterStatus.last != true ">,</s:if>
       </s:iterator>
   ];
  </script>	
</head>
<body class="fn1 timetrack">
  <main>
