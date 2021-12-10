USE [LogosDB]
GO

/****** Object:  StoredProcedure [HR].[HRReport_EmployeePayRateReport]    Script Date: 12/9/2021 2:48:48 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


/***********************************************************************************************
 Procedure Name: HR.HRReport_EmployeePayRateReport
 Developer Name:
 Purpose	     : Employee Pay Rate Report

 09/29/2010 YDU Replace the old ReportProjectedMassRateIncrease for HRNG Report
 05/27/2011 YDU SCR 406984 Fixed the Pay Rate to include step information in it.
 06/20/2014 BJ TFS 154993 Add longevity amount to projected salary
 11/12/2014 BJ TFS 167287 fix projected increase issue, make current rate as the base rate, 
				      the project rate will not include longevity.
 07/09/2015 JParquette : Added columns for Longevity,Certification and SpecialAssignment.
 02/15/2016 JParquette : Added new field
 07/20/2016 YDu: Added four flags
 09.15.2017 Ganz - SCR 524338 - use rates from function fn_GetEmployee_All_PayRates_ByDate 
 01.03.2018 Ganz - CRM 5878904 - changed 'CurrentRate' column to use the rate matching the job not the default primary rate
						   and added EmployeeID parameter
 05/25/2018 BJ: JIRA NWERP-51691, use daily hour and number of day in the year to calculate the annual hours
************************************************************************************************/
CREATE PROCEDURE [HR].[HRReport_EmployeePayRateReport]
  @EffectiveDate	    DATETIME,
  @ProjectedIncrease    VARCHAR(10) = '0',
  @EmployeeID		    INT  = NULL,
  @strOrgStructureID    VARCHAR(MAX) = NULL,
  @strxGroupHeaderID    VARCHAR(MAX) = NULL,
  @strPayTypeID	    VARCHAR(MAX) = NULL,
  @RoundDecimals	    INT = 4,
  @ProposedRate	    BIT,
  @IncludeLongevity	    BIT,
  @IncludeSP		    BIT,
  @IncludeCertification BIT,
  @UserID			    INT
AS
SET NOCOUNT  ON
BEGIN TRY
  DECLARE @DaysInYear int
  IF @EffectiveDate IS NULL SET @EffectiveDate = GETDATE()
  SET @DaysInYear = (SELECT Counter 
                       FROM CompanyCounter a 
                      WHERE a.CounterYear = YEAR(@EffectiveDate) AND a.CounterType = 25)

  CREATE TABLE #OrgStructure (OrgStructureID INT PRIMARY KEY)
  CREATE TABLE #BenefitGroup (xGroupHeaderID INT PRIMARY KEY)

    DECLARE  @Increase DECIMAL(5,4),
		   @DepartmentDelimiter char(1),
		   @ShiftToCertFlag bit 


	/***********************************************************
		  Settings
	***********************************************************/
    SET @strPayTypeID = ISNULL(@strPayTypeID,'1,2,3')
    
    SELECT @Increase = CAST(@ProjectedIncrease AS DECIMAL(5,4))   
    SELECT @DepartmentDelimiter = DepartmentLevelDelimiter FROM LogosSystemSettings
    SELECT @ShiftToCertFlag = ApplyCertificationToShiftFlag FROM HRSystemSettings  


    /***********************************************************
		  Departments
    ***********************************************************/
	SELECT O.OrgStructureID
	INTO #Departments		
	FROM OrgApproval OA
		JOIN Processes P ON OA.ProcessID = P.ProcessID
		JOIN SubLedger S ON P.SubLedgerID = S.SubLedgerID
		JOIN ApprovalUsers AU ON OA.OrgApprovalID = AU.OrgApprovalID
		JOIN dbo.fn_ScrunchedOrgStructure(@DepartmentDelimiter,',') O ON OA.OrgStructureID = O.OrgStructureID	
	WHERE OA.ProcessID = 10
		 AND @EffectiveDate between OA.EffectiveStartDate and OA.EffectiveEndDate
		 AND OA.ApprovalLevel is null				
		 AND OA.ApprovalLevelName is null
		 AND AU.UserID = @UserID

    --Filter out those departments not in the Payroll process list
    IF @strOrgStructureID IS NOT NULL
	   INSERT INTO #OrgStructure
	   SELECT A.RecordID
			FROM ReturnTableOfIntegers(@strOrgStructureID) A 
				JOIN #Departments B ON A.RecordID = B.OrgStructureID	   
    ELSE
	   INSERT INTO #OrgStructure
	   SELECT OrgStructureID From #Departments
	      

    /***********************************************************
		  Benefit Groups
    ***********************************************************/        
    IF @strxGroupHeaderID IS NOT NULL
	   INSERT INTO #BenefitGroup
	   SELECT RecordID
	   FROM   ReturnTableOfIntegers(@strxGroupHeaderID)
    ELSE
	   INSERT INTO #BenefitGroup
	   SELECT xGroupHeaderID
	   FROM   dbo.xGroupHeader
    

    /***********************************************************
		  Return results to SSRS
    ***********************************************************/  
    SELECT DISTINCT 
           DEP.OrgStructureID,
           DEP.OrgStructureCodeconcatenated AS DepartmentCode,
           DEP.OrgStructureDescconcatenated AS DepartmentDescription,
           EMP.EmployeeID,
           EMP.EmployeeNumber,
           EMP.EmployeeName,
           EJ.IsPrimaryJob AS PrimaryFlag,
           G.GradeType,
           CASE
              WHEN G.GradeType = 2 THEN ROUND(S.ProjectedAnnualSalary, @RoundDecimals) -- 1: hourly, 2: Annual, 3: Salary, 
              ELSE PAYR.BaseHourlyRate
		         END AS CurrentRate,
           G.GradeCode,
           GS.StepCode,
		         CASE WHEN GS.StepCode IS NOT NULL THEN G.GradeCode + ' - ' + GS.StepCode
			             ELSE G.GradeCode
		          END AS GradeStepDesc,
           ROUND(S.ProjectedAnnualSalary + ISNULL(PAYR.LongHourlyRate,0), @RoundDecimals) AS AnnualSalary,
           CASE
              WHEN G.GradeType = 2 THEN ROUND(S.ProjectedAnnualSalary * (1 + @Increase), @RoundDecimals)
              ELSE ROUND(ISNULL(PAYR.BaseHourlyRate,0) * (1 + @Increase), @RoundDecimals)
           END AS ProjectedRate,           
           ROUND((S.ProjectedAnnualSalary + ISNULL(PAYR.LongHourlyRate,0)) * (1 + @Increase), @RoundDecimals) AS ProjectedAnnualSalary,        
           CASE
              WHEN G.GradeType = 2 THEN ROUND(ISNULL(PAYR.LongHourlyRate,0) * EJ.DailyHours * @DaysInYear, @RoundDecimals)
              ELSE ROUND(ISNULL(PAYR.LongHourlyRate,0), @RoundDecimals)
           END AS LongevityHourly,
           CASE
              WHEN G.GradeType = 2 THEN ROUND(ISNULL(PAYR.CertHourlyRate,0) * EJ.DailyHours * @DaysInYear, @RoundDecimals) 
              ELSE ROUND(ISNULL(PAYR.CertHourlyRate,0), @RoundDecimals) 
           END AS CertificationHourly,
           CASE
              WHEN G.GradeType = 2 THEN ROUND(ISNULL(PAYR.SAHourlyRate,0) * EJ.DailyHours * @DaysInYear, @RoundDecimals)  
              ELSE ROUND(ISNULL(PAYR.SAHourlyRate,0), @RoundDecimals)
           END AS SpecialAssignmentHourly,         
           ROUND(S.ProjectedAnnualSalary,@RoundDecimals) AS BaseAnnual,
           ROUND(ISNULL(PAYR.LongHourlyRate,0) * EJ.DailyHours  * @DaysInYear, @RoundDecimals) AS LongevityAnnual,
           ROUND(ISNULL(PAYR.CertHourlyRate,0) * EJ.DailyHours * @DaysInYear, @RoundDecimals) AS CertificationAnnual,
           ROUND(ISNULL(PAYR.SAHourlyRate,0) * EJ.DailyHours * @DaysInYear, @RoundDecimals) AS SpecialAssignmentAnnual,           
		         CASE 
              WHEN G.GradeType = 2 THEN 1
              ELSE EJ.AnnualHours
           END AS AnnualHours,
           CASE WHEN G.GradeType = 2 THEN 1
                ELSE CASE WHEN V.AltValue like 'W%' THEN 52
                          WHEN V.AltValue like 'B%' THEN 26
                          WHEN V.AltValue like 'S%' THEN 24
                          WHEN V.AltValue like 'M%' THEN 12
                          WHEN V.AltValue like 'Y%' THEN 1
                          ELSE NULL
                      END 
            END as NumberofPayment,
            CASE WHEN G.GradeType = 2 THEN 1
                 ELSE EJ.CycleHours
             END as CycleHours
    FROM   HR.EmployeeJob EJ
           JOIN dbo.fn_ScrunchedOrgStructure(NULL,NULL) DEP ON EJ.DepartmentId = DEP.OrgstructureID
           JOIN #OrgStructure a ON EJ.DepartmentId = a.OrgStructureID
           JOIN #BenefitGroup b ON EJ.BenefitGroupId = b.xGroupHeaderID
           JOIN HR.Grade G ON EJ.GradeId = G.GradeId
           JOIN dbo.ReturnTableOfIntegers(@strPayTypeID) c ON G.GradeType = c.RecordID
           JOIN HR.fn_EmployeeStatus(@EffectiveDate,NULL) EMP ON EJ.EmployeeId = EMP.EmployeeID
												     AND EMP.ActiveFlag = 1
           LEFT JOIN HR.GradeStep GS ON EJ.GradeStepId = GS.GradeStepId
           LEFT JOIN HR.fn_GetEmployee_Base_ProjectedSalary_ByDate(@EffectiveDate,NULL) S ON EJ.EmployeeJobId = S.EmployeeJobID
           LEFT JOIN HR.JobEventReason JER ON EJ.JobEventReasonId = JER.JobEventReasonID
		         LEFT JOIN HR.fn_GetEmployee_All_PayRates_ByDate(@EffectiveDate,NULL) PAYR ON PAYR.EmployeeID = EJ.EmployeeId
										 --AND PAYR.IsPrimaryJob = 1
											AND PAYR.EmployeeJobID = EJ.EmployeeJobId
           LEFT JOIN (SELECT AltValue, EntryID   
                        FROM dbo.ValidationEntryAlternate 
                       WHERE vsAltUsageID = (SELECT TOP 1 EntryID from dbo.ValidationSetEntry WHERE EntryValue = 'NWS Pay Groups')) V
           on EJ.vsPayGroup = V.EntryID 
    WHERE  @EffectiveDate BETWEEN EJ.EffectiveDate AND EJ.EffectiveEndDate
           AND (JER.OutOfPosition = 0 OR EJ.JobEventReasonId IS NULL)
           AND (@EmployeeID IS NULL OR (EJ.EmployeeId = @EmployeeID))
END TRY

  
BEGIN CATCH
    DECLARE  @ErrorMessage  NVARCHAR(4000),
             @ErrorSeverity INT,
             @ErrorState    INT,
             @ErrorNumber   INT;
    
    SET @ErrorMessage = ERROR_MESSAGE();   
    SET @ErrorSeverity = ERROR_SEVERITY();   
    SET @ErrorState = ERROR_STATE();    
    SET @ErrorNumber = ERROR_NUMBER();
    
    RAISERROR (@ErrorMessage,@ErrorSeverity,@ErrorState);    
    RETURN @ErrorNumber;
END CATCH

GO

