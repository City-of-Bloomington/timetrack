# Timetrack

Timetrack is a web application to handle an organization's employees' time keeping data. It is designed so that some employees will use their ID card to scan their clock-in and clock-out times. Others will be able to enter their time-in and time-out using the web interface directly. The system is designed to have two level approval process. The first is the group manager approval then the final approval is the director approval named 'Payroll Process Approval'. Currently the application handles two weeks pay period. 

Employees' data is entered in the system. Employees are divided into groups. A number of groups will constitue a department with a department head (director). Each group will have at least one approver. Each department will have at least one payroll process approver.

The application implements a multi level workflow. On each pay period a time document is initiated when the employee logs in or clocks-in to the system for the first time. When the pay period ends, the employee will submit his time for approval. The next step will be the group manager will approve. Finally, the department director will give his/her final approval (Payroll process approval) and the time document is complete for payroll to process and pay.

During these steps some issues may arise, such as an employee forgets to clock-in or clock-out. The employee can raise an issue where his/her group manager could take care of such as updating the employee time-in and time-out in behalf the employee. The group manager and the payroll processor will be able to edit the time-in/out for their employees.

The employees can leave notes that their managers can see and decide what to do.

The time document can be edited after being approved and/or payroll processed.

The app handles employee accruals and check the availability of hours for employees to take and it will show a warning if not enough hours are available. One example is when an employee tries to take a few hours off (PTO) and their current accrual is not enough. A similar condition could come up if the employee try to take sick hours without any available.

The application provides a detailed action history log view about each change that is made by users.

Punch clock functionality is designed to work well with a Raspberry Pi, touchscreen, and RFID reader. 
[See here for documentation for configuration of a Raspberry Pi + Touchscreen](https://city-of-bloomington.github.io/timetrack/)

## Requirements

Timetrack requires Tomcat version 8.0.47 or above.  If you are using Ubuntu, this version is only provided with Ubuntu 17.10 or above.


## Development

Timetrack uses Maven to build a WAR file version.

```bash
mvn clean package
```

## Deployment

We use Ansible to deploy a previously built WAR file.  Ansible looks for the WAR file: `/target/timetrack.war`.  If you are using Ansible to deploy, your inventory will need to set all the variables in `/ansible/group_vars/all.yml`.

If you are deploying manually, you will need to create your own context.xml and quartz.properties files before building with Maven.  Look in `/src/main/webapp/META-INF` for examples to start from.

```bash
mvn clean package
cd ansible
ansible-playbook deploy.yml -i /path/to/inventory
```
