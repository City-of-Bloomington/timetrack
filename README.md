
timetrack

timetracker is a web application to handle an organization employees time keeping data. It is designed so that some employees will use their ID card to scan their clock-in and clock-out times. Others will be able to enter their time-in and time-out using their credentials to login. The system is designed to have two level approval process. The first is the group manager approval then the final approval is the director approval named 'Payroll Process Approval'. Currently the application handles two weeks pay period. In the future we may expand the app to include other type of pay periods.

Employees data are entered in the system. Employees are divided into groups. A number of groups will constitue a department with department head (director). Each group will have at least one approver. Each department will have at least one payroll process approver. 

The app implement a multi level workflow. On each pay period a time document is initiated when the employee logs in or clock-in the system for the first time. When the payperiod the employee will submit his time for approval. The next step will be the group manager will approve then the department director will give hsi/her final approval (Payroll process approval) and the time document is complete for payroll to process and pay.

During these steps some issues may arrise, such as an employee forgets to clock-in or  clock-out. The employee can raise an issue where his/her group manager could take care of such as updating the employee time-in and time-out in behalf the employee. The group manager and the payroll processor will be able to edit the time-in/out for their employees.

The employees can leave notes that their managers can see and decide what to do.

The time document can be edited after being approved and/or payroll processed. 

The app handle employee accruals and check the availability of hours for employees to take and it will show a warning if not enough hours are available. Such as when the employee tries to take a few hours off (PTO) and their current accrual is not enough. Similarly if the employee try to take sick hours.

The application provide a log history about each change that is made by users.

The app is built by using Java jsp and struts 2. You can use ant to compile, package and deploy the application.

The application uses mysql database that need to be created and configured in META-INF/context.xml file, you can use cotext.xml.example to start just change the appropriate entries to point to your database.

We use CAS for single login system, that need to be available so that users can be authenticated. If you want to skip CAS, you need to make some changes to web.xml file and comment out the rows that are used to configure CAS.







