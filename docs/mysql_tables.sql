
;; 
;; departments table
;;
CREATE TABLE `departments` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `ref_id` varchar(30) DEFAULT NULL,
  `ldap_name` varchar(80) DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB;
;;
;; groups table
;;
 CREATE TABLE `groups` (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(80) NOT NULL,
  description varchar(512) DEFAULT NULL,
  department_id int(10) unsigned NOT NULL,
	excess_hours_earn_type enum('Earn Time','Monetary','Donation'),
  inactive char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  KEY `name` (`name`),
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`)
) ENGINE=InnoDB;
;;
;; employees table
;;
 CREATE TABLE `employees` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(70) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) NOT NULL,
  `id_code` varchar(10) DEFAULT NULL,
  `employee_number` varchar(20) DEFAULT NULL,
  `email` varchar(70) DEFAULT NULL,
  `role` enum('Employee','Admin') DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `employee_number` (`employee_number`),
  UNIQUE KEY `id_code` (`id_code`)
) ENGINE=InnoDB;
;;
;; holidays table
;;
 CREATE TABLE `holidays` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `description` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;; group_employees table
;;
CREATE TABLE `group_employees` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(10) unsigned NOT NULL,
  `employee_id` int(10) unsigned NOT NULL,
  `effective_date` date NOT NULL,
  `expire_date` date DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_id` (`group_id`,`employee_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `group_employees_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `group_employees_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB;
;;
;; department_employees table
;;
 CREATE TABLE `department_employees` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int(10) unsigned NOT NULL,
  `department_id` int(10) unsigned NOT NULL,
  `department2_id` int(10) unsigned DEFAULT NULL,
  `effective_date` date NOT NULL,
  `expire_date` date DEFAULT NULL,
   PRIMARY KEY (`id`),
   KEY `department_id` (`department_id`),
   KEY `employee_id` (`employee_id`),
   KEY `department2_id` (`department2_id`),
   CONSTRAINT `department_employees_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`),
   CONSTRAINT `department_employees_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`),
   CONSTRAINT `department_employees_ibfk_3` FOREIGN KEY (`department2_id`) REFERENCES `departments` (`id`)
) ENGINE=InnoDB;
;;
;; workflow_nodes table
;;
 CREATE TABLE `workflow_nodes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `managers_only` char(1) DEFAULT NULL,
  `annotation` varchar(50) DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;; workflows table
;;
 CREATE TABLE `workflows` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `node_id` int(10) unsigned NOT NULL,
  `next_node_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `node_id` (`node_id`),
  KEY `next_node_id` (`next_node_id`),
  CONSTRAINT `workflows_ibfk_1` FOREIGN KEY (`node_id`) REFERENCES `workflow_nodes` (`id`),
  CONSTRAINT `workflows_ibfk_2` FOREIGN KEY (`next_node_id`) REFERENCES `workflow_nodes` (`id`)
) ENGINE=InnoDB;
;;
;; group managers
;;
CREATE TABLE `group_managers` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(10) unsigned NOT NULL,
  `employee_id` int(10) unsigned NOT NULL,
  `wf_node_id` int(10) unsigned NOT NULL,
  `start_date` date NOT NULL,
  `expire_date` date DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  KEY `employee_id` (`employee_id`),
  KEY `wf_node_id` (`wf_node_id`),
  CONSTRAINT `group_managers_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `group_managers_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`),
  CONSTRAINT `group_managers_ibfk_3` FOREIGN KEY (`wf_node_id`) REFERENCES `workflow_nodes` (`id`)
) ENGINE=InnoDB;
;;
;; accruals table
;;
 CREATE TABLE `accruals` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `pref_max_level` tinyint(4) DEFAULT '0',
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB;
;;
;; accrual_warnings table
;;
 CREATE TABLE `accrual_warnings` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `hour_code_id` int(10) unsigned NOT NULL,
	 accrual_id int(10) unsigned Not null,
  `min_hrs` double(4,2) DEFAULT NULL,
  `step_hrs` double(4,2) DEFAULT NULL,
  `related_accrual_max_leval` double(5,2) DEFAULT NULL,
  `step_warning_text` varchar(80) DEFAULT NULL,
  `min_warning_text` varchar(80) DEFAULT NULL,
  `excess_warning_text` varchar(80) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `hour_code_id` (`hour_code_id`),
	foreign Key(accrual_id) references accruals(id),
  CONSTRAINT `accrual_warnings_ibfk_1` FOREIGN KEY (`hour_code_id`) REFERENCES `hour_codes` (`id`)
) ENGINE=InnoDB;
;;
;; employee_accruals 
;;
CREATE TABLE `employee_accruals` (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  accrual_id int(10) unsigned NOT NULL,
  employee_id int(10) unsigned NOT NULL,
  hours decimal(7,2) DEFAULT NULL,
  date date DEFAULT NULL,
  PRIMARY KEY (id),
  KEY accrual_id (accrual_id),
  KEY employee_id (employee_id),
  CONSTRAINT employee_accruals_ibfk_1 FOREIGN KEY (accrual_id) REFERENCES accruals (id),
  CONSTRAINT employee_accruals_ibfk_2 FOREIGN KEY (employee_id) REFERENCES employees (id)
) ENGINE=InnoDB;
;;
;; benefit_groups
;;
 CREATE TABLE `benefit_groups` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `fullTime` char(1) DEFAULT NULL,
  `exempt` char(1) DEFAULT NULL,
  `unioned` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;; city_cross_ref table
;;
CREATE TABLE `city_cross_ref` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(15) NOT NULL,
  `nw_code` varchar(20) NOT NULL,
  `gl_string` varchar(15) DEFAULT NULL,
  `pto_ratio` int(11) DEFAULT NULL,
  `description` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB;
;;
;; code_cross_ref table
;;
 CREATE TABLE `code_cross_ref` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code_id` int(10) unsigned DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `nw_code` varchar(20) NOT NULL,
  `gl_string` varchar(15) DEFAULT NULL,
  `pto_ratio` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `code_id` (`code_id`),
  CONSTRAINT `code_cross_ref_ibfk_1` FOREIGN KEY (`code_id`) REFERENCES `hour_codes` (`id`)
) ENGINE=InnoDB;
;;
;; employee_logs
;;
CREATE TABLE `employees_logs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `emps_id_set` varchar(1000) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `status` enum('Success','Failure') DEFAULT NULL,
  `errors` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;; hour_codes table
;;
CREATE TABLE `hour_codes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `description` varchar(80) DEFAULT NULL,
  `record_method` enum('Time','Hours') DEFAULT 'Time',
  `accrual_id` int(10) unsigned DEFAULT NULL,
  `count_as_regular_pay` char(1) DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  `reg_default` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `accrual_id` (`accrual_id`),
  CONSTRAINT `hour_codes_ibfk_1` FOREIGN KEY (`accrual_id`) REFERENCES `accruals` (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `salary_groups` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `description` varchar(80) DEFAULT NULL,
  `default_regular_id` int(10) unsigned DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `default_regular_id` (`default_regular_id`),
  CONSTRAINT `salary_groups_ibfk_1` FOREIGN KEY (`default_regular_id`) REFERENCES `hour_codes` (`id`)
) ENGINE=InnoDB;
;;
;; positions table
;; 
CREATE TABLE `positions` (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  alias varchar(64) not null,
  description varchar(512) DEFAULT NULL,
  inactive char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB;
;;
;; hour_code_conditions table
;;
 CREATE TABLE `hour_code_conditions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `hour_code_id` int(10) unsigned NOT NULL,
  `department_id` int(10) unsigned DEFAULT NULL,
  `salary_group_id` int(10) unsigned DEFAULT NULL,
	 group_id int unsigned default null,
  `date` date DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_codes` (`hour_code_id`,`department_id`,`salary_group_id`),
  KEY `salary_group_id` (`salary_group_id`),
  KEY `department_id` (`department_id`),
	foreign key (group_id) references groups(id),
  CONSTRAINT `hour_code_conditions_ibfk_1` FOREIGN KEY (`salary_group_id`) REFERENCES `salary_groups` (`id`),
  CONSTRAINT `hour_code_conditions_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`),
  CONSTRAINT `hour_code_conditions_ibfk_3` FOREIGN KEY (`hour_code_id`) REFERENCES `hour_codes` (`id`)
) ENGINE=InnoDB;
;;
;; ip_allowed table
;;
 CREATE TABLE locations (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(15) NOT NULL,
  `name` varchar(128) not NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ip_address` (`ip_address`)
) ENGINE=InnoDB;

	create table group_locations(                                                      id int unsigned NOT NULL AUTO_INCREMENT primary key,                            group_id int unsigned not null,                                                 location_id int unsigned not null,                                              foreign key(group_id) references groups(id),                                    foreign key(location_id) references locations(id),                              unique(group_id,location_id)                                                  )Engine=InnoDB;

		 
;;
;; jobs table
;;
CREATE TABLE jobs (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  position_id int(10) unsigned NOT NULL,
  salary_group_id int(10) unsigned NOT NULL,
  employee_id int(10) unsigned NOT NULL,
	group_id int unsigned not null,
  effective_date date DEFAULT NULL,
  expire_date date DEFAULT NULL,
  primary_flag char(1) DEFAULT NULL,
  weekly_regular_hours tinyint(4) DEFAULT '40',
  comp_time_weekly_hours tinyint(4) DEFAULT '40',
  comp_time_factor` double(2,1) DEFAULT '1.0',
  holiday_comp_factor double(2,1) DEFAULT '1.0',
  clock_time_required char(1) DEFAULT NULL,
	hourly_rate double(12,2) default 0.00,
  inactive char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `position_id` (`position_id`),
  KEY `salary_group_id` (`salary_group_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `jobs_ibfk_1` FOREIGN KEY (`position_id`) REFERENCES `positions` (`id`),
  CONSTRAINT `jobs_ibfk_2` FOREIGN KEY (`salary_group_id`) REFERENCES `salary_groups` (`id`),
  CONSTRAINT `jobs_ibfk_3` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB;
;;
;; notification_logs table
;;
 CREATE TABLE notification_logs (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  receipants text,
  message  varchar(512) DEFAULT NULL,
  date datetime DEFAULT NULL,
  status enum('Success','Failure') DEFAULT NULL,
  error_msg text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;; pay_periods table
;;
CREATE TABLE `pay_periods` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;; roll_backs table
;;
CREATE TABLE `roll_backs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date_time` datetime DEFAULT NULL,
  `group_employees` int(11) DEFAULT NULL,
  `department_employees` int(11) DEFAULT NULL,
  `group_managers` int(11) DEFAULT NULL,
  `jobs` int(11) DEFAULT NULL,
  `groups` int(11) DEFAULT NULL,
  `positions` int(11) DEFAULT NULL,
  `employees` int(11) DEFAULT NULL,
  `is_success` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
;; time_documents
;; modified to handle multiple jobs for an employee
;;
CREATE TABLE `time_documents` (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  employee_id int(10) unsigned NOT NULL,
  pay_period_id int(10) unsigned NOT NULL,
	job_id int(10) unsigned not null,
  initiated datetime NOT NULL,
  initiated_by int(10) unsigned NOT NULL,
  PRIMARY KEY (id),
	foreign key(job_id) references jobs(id),
 	FOREIGN KEY (employee_id) REFERENCES employees (id),
  FOREIGN KEY (pay_period_id) REFERENCES pay_periods (id),
  FOREIGN KEY (initiated_by) REFERENCES employees (id),	
  UNIQUE KEY emp_pay_period_u (employee_id,pay_period_id, job_id)
) ENGINE=InnoDB;

;;
;; time_blocks
;;
CREATE TABLE `time_blocks` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `document_id` int(10) unsigned NOT NULL,
  `hour_code_id` int(10) unsigned NOT NULL,
  `date` date NOT NULL,
  `begin_hour` int(11) DEFAULT NULL,
  `begin_minute` int(11) DEFAULT NULL,
  `end_hour` int(11) DEFAULT NULL,
  `end_minute` int(11) DEFAULT NULL,
  `hours` decimal(5,2) DEFAULT 0,
  `clock_in` char(1) DEFAULT NULL,
  `clock_out` char(1) DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `document_id` (`document_id`),
  KEY `hour_code_id` (`hour_code_id`),
  KEY `date` (`date`),
  CONSTRAINT `time_blocks_ibfk_1` FOREIGN KEY (`document_id`) REFERENCES `time_documents` (`id`),
  CONSTRAINT `time_blocks_ibfk_3` FOREIGN KEY (`hour_code_id`) REFERENCES `hour_codes` (`id`)
) ENGINE=InnoDB;
;;
;; time_actions table
;;
 CREATE TABLE `time_actions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `workflow_id` int(10) unsigned NOT NULL,
  `document_id` int(10) unsigned NOT NULL,
  `action_by` int(10) unsigned NOT NULL,
  `action_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `workflow_id` (`workflow_id`),
  KEY `document_id` (`document_id`),
  KEY `action_by` (`action_by`),
  CONSTRAINT `time_actions_ibfk_1` FOREIGN KEY (`workflow_id`) REFERENCES `workflows` (`id`),
  CONSTRAINT `time_actions_ibfk_2` FOREIGN KEY (`document_id`) REFERENCES `time_documents` (`id`),
  CONSTRAINT `time_actions_ibfk_3` FOREIGN KEY (`action_by`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB;
;;
;; time_block_logs
;; 
CREATE TABLE `time_block_logs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `document_id` int(10) unsigned NOT NULL,
  `hour_code_id` int(10) unsigned NOT NULL,
  `date` date DEFAULT NULL,
  `begin_hour` int(11) DEFAULT NULL,
  `begin_minute` int(11) DEFAULT NULL,
  `end_hour` int(11) DEFAULT NULL,
  `end_minute` int(11) DEFAULT NULL,
  `hours` decimal(5,2) DEFAULT NULL,
  `ovt_pref` enum('CTE','Money') DEFAULT NULL,
  `clock_in` char(1) DEFAULT NULL,
  `clock_out` char(1) DEFAULT NULL,
  `time_block_id` int(10) unsigned NOT NULL,
  `action_type` enum('Add','Update','Delete','ClockIn','ClockOut') DEFAULT NULL,
  `action_by_id` int(10) unsigned NOT NULL,
  `action_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `time_block_id` (`time_block_id`),
  KEY `hour_code_id` (`hour_code_id`),
  KEY `action_by_id` (`action_by_id`),
  CONSTRAINT `time_block_logs_ibfk_1` FOREIGN KEY (`time_block_id`) REFERENCES `time_blocks` (`id`),
  CONSTRAINT `time_block_logs_ibfk_3` FOREIGN KEY (`hour_code_id`) REFERENCES `hour_codes` (`id`),
  CONSTRAINT `time_block_logs_ibfk_4` FOREIGN KEY (`action_by_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB;
;;
;; time_issues 
;;
 CREATE TABLE `time_issues` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `time_block_id` int(10) unsigned NOT NULL,
  `reported_by` int(10) unsigned NOT NULL,
  `date` datetime DEFAULT NULL,
  `issue_notes` varchar(512) DEFAULT NULL,
  `status` enum('Open','Closed') DEFAULT NULL,
  `closed_date` datetime DEFAULT NULL,
  `closed_by` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `time_block_id` (`time_block_id`),
  KEY `reported_by` (`reported_by`),
  KEY `closed_by` (`closed_by`),
  CONSTRAINT `time_issues_ibfk_1` FOREIGN KEY (`time_block_id`) REFERENCES `time_blocks` (`id`),
  CONSTRAINT `time_issues_ibfk_2` FOREIGN KEY (`reported_by`) REFERENCES `employees` (`id`),
  CONSTRAINT `time_issues_ibfk_3` FOREIGN KEY (`closed_by`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB;
;;
;; time_notes table
;;
 CREATE TABLE `time_notes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `document_id` int(10) unsigned NOT NULL,
  `reported_by` int(10) unsigned NOT NULL,
  `date` datetime DEFAULT NULL,
  `notes` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `document_id` (`document_id`),
  KEY `reported_by` (`reported_by`),
  CONSTRAINT `time_notes_ibfk_1` FOREIGN KEY (`document_id`) REFERENCES `time_documents` (`id`),
  CONSTRAINT `time_notes_ibfk_2` FOREIGN KEY (`reported_by`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB;
;;
 CREATE TABLE email_logs (                                                        id int(10) unsigned NOT NULL AUTO_INCREMENT,                                    user_id int(10) unsigned,                                                       date_time datetime,                                                             email_from varchar(56),                                                         email_to varchar(56),                                                           cc      varchar(56),                                                            bcc     varchar(512),                                                           subject varchar(56),                                                            text_message varchar(1024),                                                     send_errors  varchar(1024),                                                     type enum('Approvers','Processors'),                                            PRIMARY KEY (`id`),                                                             foreign key(user_id) references employees(id)                                   ) ENGINE=InnoDB;

;;
;; we need the following data in tables
;;
insert into workflow_nodes values( 1,'Initiated','Document first initiated for data entry',NULL,'Document initiated',NULL),
(2,'Submit for Approval','Submit timesheet for approval',            NULL          ,'Routed for Approval',NULL),     
(  3 ,'Approve','manager first step approve','y',             'Approved', NULL     ),
(  4 , 'Payroll Approve','final director approve',                  'y',  'Payroll Approved',  NULL     ),
(  5,'Time Maintain','Time data maintainace for other employees','y','Time Maintained', NULL),
(  6, 'Review','Can review data entry', 'y', 'Reviewed',           NULL); 
;;
;; workflows data
;;
insert into workflows values(1,1,2),(2,2,3),(3,3,4),(4,4,null);
;;
;; salary_groups data
;;
insert into salary_groups values(1,'Exempt','Exempt employees',1,null),
(2,'Non Exempt','Non exempt employees',1,null),
(3,'Temp','Temp employees and interns',14,null),
(4,'Union','Union employees',1,null),
(5,'Part Time','Part time exempt or non-exempt, does not include temps',1,null);
;;
;; we need at least the following codes in hour_codes table
;;
insert into hour_codes values(1,'Reg','Regular Hours worked','Time',null,'y',null,0),(14,'TEMP','Temporary hours worked','Time',null,'y',null,0);
;;
;; you need a script to create two weeks pay periods, something 
;; like this short list, this list is good for couple months, 
;; later need to add the rest
;;
insert into pay_periods values(0,'2018-08-13','2018-08-26'),
(0,'2018-08-27','2018-09-09'), 
(0,'2018-09-10','2018-09-23'),
(0,'2018-09-24','2018-10-07'),
( 0 , '2018-10-08', '2018-10-21'),
( 0 , '2018-10-22', '2018-11-04'),
( 0 , '2018-11-05', '2018-11-18'),
( 0 , '2018-11-19', '2018-12-02'),
( 0 , '2018-12-03', '2018-12-16'),
( 0 , '2018-12-17', '2018-12-30'),
( 0 , '2018-12-31', '2019-01-13'),
( 0 , '2019-01-14', '2019-01-27'),
( 0 , '2019-01-28', '2019-02-10');
;;
;;
;; the user need to create quartz tables, get them from
;; quartz website
;;
;; alter table jobs add hourly_rate double(6,2) default 0.00 after clock_time_required;
;;
;; changes on 10/19/2018 
;; to handle multiple jobs for an employee we moved
;; job from time block to document
;; alter table time_documents add job_id int unsigned not null after pay_period_id;
;; Now we need to delete the documents that do not have time_blocks
;; delete from time_actions where document_id not in (select document_id from time_blocks);
;; delete from time_documents where id not in ( select document_id from time_blocks);
;; update time_documents set job_id = (select t.job_id from time_blocks t where t.document_id = time_documents.id limit 1);

;; alter table time_documents add foreign key(job_id) references jobs(id);
;;
;; we need to change the unique constraint, so we need to drop the old one
;; alter table time_documents drop foreign key time_documents_ibfk_1;
;; alter table time_documents drop foreign key time_documents_ibfk_2;			
;; alter table time_documents drop index emp_pay_period_u;
;; now we need to add these foreign keys again
;; alter table time_documents add foreign key(employee_id) references employees(id);
;; alter table time_documents add foreign key(pay_period_id) references pay_periods(id);
;;  alter table time_documents add unique(pay_period_id, job_id, employee_id);
;; 

;; Now we remove the time_blocks foreign key job_id
;; alter table time_blocks drop foreign key time_blocks_ibfk_2
;;
;; Now we can drop the column job_id
;; alter table time_blocks drop column job_id;
;;
;; we do similar thing to time_block_logs
;;
;; alter table time_block_logs drop foreign key time_block_logs_ibfk_2;
;; alter table time_block_logs drop column job_id;
;;
;; alter table jobs add group_id int unsigned not null after employee_id;
;; delete from jobs where id=44; // admin job
;; update jobs set group_id = (select ge.group_id from group_employees ge where ge.employee_id=jobs.employee_id limit 1);


;; alter table jobs add foreign key(group_id) references groups(id);
;;
;; alter table positions add alias varchar(64) not null after name;
;; update positions set description=name where description is null;
;; update positions set alias=name where alias ='';
;;
;; alter table employees modify username varchar(70);
;;
;; 11/27/2018 updates 
;;
;; alter table accrual_warnings add accrual_id int unsigned after hour_code_id;
;; alter table accrual_warnings add foreign key(accrual_id) references accruals(id);
;; alter table ip_allowed change description name varchar(128);
;; rename table ip_allowed to locations;
;;
;; update accrual_warnings set accrual_id=(select accrual_id from hour_codes where id=accrual_warnings.hour_code_id);
;;
;; update accrual_warnings set step_warning_text='Paid time off used should be in increments of 0.25 hr' where id=1;
;; update accrual_warnings set min_warning_text='Min paid time off taken should not be less than one hr' where id=1;
;; update accrual_warnings set excess_warning_text='Excess paid time off hours used' where id=1;
;;
;; update accrual_warnings set excess_warning_text='Excess comp time hours used, you can use as small as 0.01 hr increment' where id=2;
;; update accrual_warnings set excess_warning_text='Excess holiday comp time hours used, you can use as small as 0.01 hr increment' where id=2;
;;
;; insert into accrual_warnings values(4,8,2,0,0,0,null,null,'Excess of sick time hours used, you can use as small as 0.01 hr increment');
;;
;; add HCE1.0 to hour_codes and to restrictions for exempt,non-exemp, union
;;
;; new table group_locations
;;
;; update code_cross_ref set code_id=(select id from hour_codes c where c.name like code_cross_ref.code);
;; 
;; select id,name from hour_codes where name not in (select code from code_cross_ref);
;; alter table accrual_warnings drop foreign key accrual_warnings_ibfk_1;
;; alter table accrual_warnings drop column hour_code_id;
;;
;; add the following to salary_groups
;; insert into salary_groups values(6,'Police Sworn','Police Sworn',1,null),(7,'Police Sworn Det','Police Sworn Detectives',1,null),(8,'Police Sworn Mgt','Police Sworn Management',1,null),(9,'Fire Sworn','Fire Sworn',1,null),(10,'Fire Sworn 5x8','Fire Sworn 5 to 8',1,null),(11,'Part Time Non-Exempt','Part Time non exempt',1,null);
;;
;; 12/07/2018
;; Adding scheduling and shifts 
;;
CREATE TABLE shifts (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
	name varchar(72) not null,
	start_hour    int unsigned,
	start_minute  int unsigned,
	duration      int unsigned,
	start_minute_window int unsigned,
	end_minute_window int unsigned,
	minute_rounding int unsigned,
	inactive char(1),
	primary key(id)
)engine=InnoDB;

CREATE TABLE schedules (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
	name varchar(72) not null,
	start_date date,
	end_date date,
	group_id int unsigned not null,
	inactive char(1),
	primary key(id),
	foreign key(group_id) references groups(id)
)engine=InnoDB;

CREATE TABLE schedule_shifts (
  id int unsigned NOT NULL AUTO_INCREMENT,
	shedule_id int unsigned,
	shift_id  int unsigned,
	employee_id int unsigned,
	assigned_by_id int unsigned,
	primary key(id)	
	foreign key(shedule_id) references schedules(id),
	foreign key(shift_id) references shifts(id),
	foreign key(employee_id) references employees(id),
  foreign key(assigned_by_id) references employees(id)			
	)engine=InnoDB;

;;
;; for employees with fixed shifts
;;
	CREATE TABLE group_shifts (			
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
	group_id int unsigned not null,		
	shift_id int unsigned not null,
	start_date date,
	expire_date date,
	inactive char(1),
	primary key(id),
	foreign key(group_id) references groups(id),	
	foreign key(shift_id) references shifts(id)
)Engine=InnoDB;

;; ===================================================
;; 12/12/2018
;; adding group_id to hour_code_conditions table
;; 
alter table hour_code_conditions drop foreign key hour_code_conditions_ibfk_1;
alter table hour_code_conditions drop foreign key hour_code_conditions_ibfk_2;
alter table hour_code_conditions drop foreign key hour_code_conditions_ibfk_3;

alter table hour_code_conditions drop index unique_codes;

alter table hour_code_conditions add group_id int unsigned after salary_group_id ;
alter table hour_code_conditions add foreign key(salary_group_id) references salary_groups(id);
alter table hour_code_conditions add foreign key(group_id) references groups(id);
alter table hour_code_conditions add foreign key(department_id) references departments(id);
alter table hour_code_conditions add foreign key(hour_code_id) references hour_codes(id);
alter table hour_code_conditions add constraint unique_codes unique (hour_code_id,group_id,department_id,salary_group_id);

alter table hour_codes add type enum('Regular','Used','Earned','Overtime','Unpaid','Other');

alter table hour_codes change column inactive inactive char(1) after type;

;;
;; 12/18/2018
alter table groups add default_earn_code_id int unsigned after department_id;
alter table groups add foreign key(default_earn_code_id) references hour_codes(id);
;; CE1.5 = 34
update groups set default_earn_code_id=34 ;
alter table shifts drop column prefered_earn_time;
;;
;;
;; 1/2/2019
alter table shifts add end_minute_window int unsigned after start_minute_window;
update shifts set end_minute_window=start_minute_window;
;;
;; 1/4/19
;; users table is not used anymore
;;
drop table users;
;;
;; update employees table to change role field to roles and type to varchar(256)
;;
;;
alter table employees add roles varchar(256) after role;
update employees set roles=role;
alter table employees drop column role;
;;
;; 1/16/2019
;;
	CREATE TABLE accrual_contributes (			                                        id int(10) unsigned NOT NULL AUTO_INCREMENT,                                    name varchar(80) not null,                                                      accrual_id int unsigned not null,		                                            hour_code_id int unsigned not null,                                             factor decimal(3,1),                                                            primary key(id),                                                                foreign key(accrual_id) references accruals(id),	                              foreign key(hour_code_id) references hour_codes(id)                            )Engine=InnoDB;

;;
;; 1/17/2019
;;
alter table hour_codes modify type enum('Regular','Used','Earned','Overtime','Unpaid','Other','On Call','Call Out');
;;
alter table salary_groups add excess_culculation enum('Weekly','Daily','Pay Period','Other') default 'Weekly' after default_regular_id;
;;
;; 1/31/2019
;;
alter table hour_codes modify type enum('Regular','Used','Earned','Overtime','Unpaid','Other','On Call','Call Out','Monetary');
update hour_codes set type='Monetary' where type='On Call';
alter table hour_codes modify type enum('Regular','Used','Earned','Overtime','Unpaid','Other','Call Out','Monetary');

;; modify salary groups
;;;====================
;; union: Daily
;; police (all): Daily
;; fire Sworn:Pay Period
;; fire 8x5: Other (they use oncall instead)
;;
;;

;; 2/4/2019
;;
;; alter table jobs add added_date date after hourly_rate;
;; update jobs set added_date = effective_date;
;; alter table groups add excess_hours_calculation_method enum('Earn Time','Monetary','Donation') default 'Earn Time' after department_id;
;; update groups set excess_hours_calculation_method='Monetary' where default_earn_code_id=43;
;; alter table groups drop foreign key groups_ibfk_2;
;; alter table groups drop column default_earn_code_id;
;; alter table group_managers add primary_flag char(1) after expire_date;
;;

;;
;; 2/8/2019
;;
;; alter table employees add added_date date after roles;
;; update employees e set e.added_date=(select d.effective_date from department_employees d where d.employee_id=e.id limit 1);
;;
;;
;; 2/11/2019
alter table group_employees drop foreign key group_employees_ibfk_1;
alter table group_employees drop foreign key group_employees_ibfk_2;
alter table group_employees drop index group_id;
;;
alter table group_employees add foreign key(group_id) references groups(id);
alter table group_employees add foreign key(employee_id) references employees(id);
alter table group_employees add unique(group_id,employee_id, effective_date);
;;
;; 2/13/2019
;;

CREATE TABLE shift_times (
  id int unsigned NOT NULL AUTO_INCREMENT primary key,
	pay_period_id int unsigned not null,
	group_id int unsigned not null,
	default_hour_code_id int unsigned not null,
	start_time varchar(5),
	end_time varchar(5),
	dates varchar(512),
	added_by_id int unsigned not null,
	added_time datetime,
	processed char(1),
	foreign key(group_id) references groups(id),
	foreign key(default_hour_code_id) references hour_codes(id),			
	foreign key(pay_period_id) references pay_periods(id),
	foreign key(added_by_id) references employees(id)
	) ENGINE=InnoDB;
;;
;;
;;	2/18/2019
;; update fire sworn weekly hours to 48, comp time weely to 53
;;
update jobs set weekly_regular_hours=48, comp_time_weekly_hours=106 where salary_group_id=9;
;;
;;
;;
alter table hour_codes modify reg_default char(1);
update hour_codes set reg_default='y' where reg_default='0';
update hour_codes set reg_default=null where reg_default='1';


;;
;;
;; 3/5/2019
;;
;; for certain hour_codes we can use monetary value instead of time and hours
;; we are using amount for dollar value
;;
;;
alter table time_blocks add amount decimal(6,2) default 0 after hours;
alter table time_block_logs add amount decimal(6,2) default 0 after hours;
alter table hour_codes modify record_method enum('Time','Hours','Monetary');
alter table hour_codes drop column count_as_regular_pay;
alter table hour_codes add default_monetary_amount decimal(6,2) default 0 after type;
;; update the view
;;

       create or replace view time_blocks_view as                                            select t.id time_block_id,                                                     t.document_id document_id,                                                      t.hour_code_id hour_code_id,                                                    t.date,                                                                         t.begin_hour begin_hour,                                                        t.begin_minute begin_minute,                                                    t.end_hour end_hour,                                                            t.end_minute end_minute,                                                        t.hours hours,                                                                  t.amount amount,                                                                t.clock_in clock_in,                                                            t.clock_out clock_out,                                                          t.inactive inactive,                                                            datediff(t.date,p.start_date) order_id,                                         c.name code_name,                                                               c.description code_description,                                                 c.record_method record_method,                                                  c.accrual_id accrual_id,                                                        c.type code_type,                                                               c.default_monetary_amount,                                                      cf.nw_code nw_code_name,                                                        ps.name job_name,                                                               j.id job_id,                                                                    d.pay_period_id pay_period_id,                                                  d.employee_id employee_id                                                       from time_blocks t                                                              join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join jobs j on d.job_id=j.id                                                    join positions ps on j.position_id=ps.id                                        join hour_codes c on t.hour_code_id=c.id                                        left join code_cross_ref cf on c.id=cf.code_id ;		
			 
;;
;;  need to update the following hour codes to Monetary
;; ONCALL 40, ONCALL 35 35, ONCALL FIRE 100, CA no default value, FRP 10
;;
;; 3/8/2019
;;
CREATE TABLE tmwrp_runs (
  id int unsigned NOT NULL AUTO_INCREMENT primary key,
	document_id int unsigned not null,
	reg_code_id int unsigned not null,
	run_time datetime,	
	week1_grs_reg_hrs decimal (6,2),
	week2_grs_reg_hrs decimal (6,2),	
	week1_net_reg_hrs decimal (6,2),
	week2_net_reg_hrs decimal (6,2),
	cycle1_net_reg_hrs decimal (6,2) default 0,
	cycle2_net_reg_hrs decimal (6,2) default 0,
	foreign key(document_id) references time_documents(id),
	foreign key(reg_code_id) references hour_codes(id),
	unique (document_id)
	) ENGINE=InnoDB;
;;
;;
;; these will be sums of similar hour_codes, for the whole pay period
;; OT, CE1.0, will be one
;; since we are going to do a lot of delete and insert into this table
;; we decided to make primary key(id, run_id) so that we can recycle id
;; numbers without adding a new index
;;
CREATE TABLE tmwrp_blocks (
  id int unsigned not null,
	run_id int unsigned not null,
	hour_code_id int unsigned not null,
	term_type enum('Week 1','Week 2','Cycle'),
	cycle_order tinyint unsigned default 1,
	hours decimal (6,2) default 0,
	amount decimal (6,2) default 0,
	primary key(id,run_id),
	foreign key(hour_code_id) references hour_codes(id),
	foreign key(run_id) references tmwrp_runs(id)			
	) ENGINE=InnoDB;
;;
;;
;; find all documents and employees that have times but not tmwrp_runs
;;
	select distinct(e.id),e.first_name,e.last_name from time_documents d left join time_blocks t on t.document_id=d.id left join employees e on e.id=d.employee_id where d.pay_period_id=554 and d.id not in (select document_id from tmwrp_runs);



;;
;; 4/2/2019
;;
;; Police dept needed an item in timetrack to track their earn codes related
;; to certain reasons (projects) during their duties
;;
create table reason_categories(
  id int unsigned not null auto_increment,
	name varchar(56) not null,
	inactive char(1),
	unique(name),
	primary key(id)
) ENGINE=InnoDB;

insert into reason_categories (name) values('ADMIN'),('COMMUNITY EVENTS'),('COURT'),('DETECTIVES'),('DISPATCH'),('DOWNTOWN PATROLS'),('EVIDENCE'),('IU BASKETBALL'),('IU FOOTBALL'),('OFFICER SHORTAGE'),('PARKING'),('PATROL ASSIGNED DUTY'),('POSITION'),('RECORDS'),('SPECIAL DETAILS'),('SPECIALTY DUTY'),('TRAINING');

CREATE TABLE earn_code_reasons (
  id int unsigned not null auto_increment,
	name varchar(56) not null,
	description varchar(164),
	reason_category_id int unsigned,
	inactive char(1),
	unique(name),
	foreign key(reason_category_id) references reason_categories(id),
	primary key(id)
) ENGINE=InnoDB;

insert into earn_code_reasons (id,name,description,reason_category_id) values(1,'Admistrative Duty','ADMIN: Admistrative Duty',1),
(2,'Citizens Academy','ADMIN: Citizens Academy',1),
(3,'Hiring Process/Backgrounds','ADMIN: Hiring Process/Backgrounds, etc',1),
(4,'Community Engagement','COMMUNITY EVENTS: Community Engagement',2),
(5,'Explorer/Teen Academy','COMMUNITY EVENTS: Explorer/Teen Academy',2),
(6,'Court','COURT: Court',3),
(7,'Assist Other Agency','DETECTIVES: Assist Other Agency',4),
(8,'Assist Uniform Division','DETECTIVES: Assist Uniform Division',4),
(9,'ATF','DETECTIVES: ATF',4),
(10,'Callout','DETECTIVES: Callout',4),
(11,'DEA','DETECTIVES: DEA',4);

insert into earn_code_reasons (id,name,description,reason_category_id) values
(12,'Investigation','DETECTIVES: Investigation',4),
(13,'On Call','DETECTIVES: On Call',4),
(14,'DETECTIVES:Other','DETECTIVES: Other',4),
(15,'SIU Drups','DETECTIVES: SIU Drugs',4),
(16,'SIU Investigation','DETECTIVES: SIU Investigation',4),
(17,'Busy','DISPATCH: Busy',5),
(18,'Holiday','DISPATCH: Holiday',5),
(19,'IDACS Coordinator work','DISPATCH: IDACS Coordinator work',5),
(20,'Manpower/PTO, Hol','DISPATCH: Manpower/PTO, Hol',5),
(21,'Manpower/Sickness','DISPATCH: Manpower/Sickness',5),
(22,'Meetings/Evaluations/Admin','DISPATCH: Meetings/Evaluations/Admin',5),
(23,'Dispatch: Other','DISPATCH: Other',5),
(24,'Person Shortage','DISPATCH: Person Shortage',5),
(25,'Training','DISPATCH: Training',5);

insert into earn_code_reasons (id,name,description,reason_category_id) values
(26,'Downtown Patrol','DOWNTOWN PATROLS: Downtown Patrol',6),
(27,'Downtown Patrol PEO','DOWNTOWN PATROLS: Downtown Patrol PEO',6),
(28,'Wylie Downtown','DOWNTOWN PATROLS: Wylie Downtown',6),
(29,'Evidence: Call Out ','EVIDENCE: Call Out',7),
(30,'Process/Busy in Lab','EVIDENCE: Process/Busy in Lab',7),
(31,'Scene Process','EVIDENCE: Scene Process',7),
(32,'Evidence: Training','EVIDENCE: Training',7),
(33,'IU Basketball Sworn Officer','IU BASKETBALL: IU Basketball Sworn Officer',8),
(34,'IU Football Sworn Officer','IU FOOTBALL: IU Football Sworn Officer',9),
(35,'Hold Over Due to Event/Weather','OFFICER SHORTAGE: Hold Over Due to Event/Weather',10),
(36,'Officer Shortage:Other','OFFICER SHORTAGE: Other',10),
(37,'Sickness','OFFICER SHORTAGE: Sickness',10),
(38,'IU Basketball PEO','PARKING: : IU Basketball PEO',11),
(39,'IU Football PEO','PARKING: : IU Football PEO',11),
(40,'PEO for Police (Other)','PARKING: PEO for Police (Other)',11);


insert into earn_code_reasons (id,name,description,reason_category_id) values
(41,'Assist SIU or Another Agency','PATROL ASSIGNED DUTY: Assist SIU or Another Agency',12),
(42,'Case Report/Late Call','PATROL ASSIGNED DUTY: Case Report/Late Call',12),
(43,'Patrol Assigned Duty: Other','PATROL ASSIGNED DUTY: Other',12),
(44,'POTC DOR Probationary Officer','PATROL ASSIGNED DUTY: POTC DOR Probationary Officer',12),
(45,'CAD/RMS Administrator','POSITION: CAD/RMS Administrator',13),
(46,'CALEA/Comm Engagement','POSITION: CALEA/Comm Engagement',13),
(47,'Custodian','POSITION: Custodian',13),
(48,'Executive Assistant','POSITION: Executive Assistant',13),
(49,'NRS duties','POSITION: NRS duties',13),
(50,'Offfice Manage','POSITION: Offfice Manager',13),
(51,'Social Worker','POSITION: Social Worker',13),
(52,'Records: Busy','RECORDS: Busy',14),
(53,'Records: Holiday','RECORDS: Holiday',14),
(54,'Meetings/Evaluations','RECORDS: Meetings/Evaluations',14),
(55,'Records: Other','RECORDS: Other',14),
(56,'Records: Person Shortage','RECORDS: Person Shortage',14),
(57,'Records: Training','RECORDS: Training',14),
(58,'Records: Weekend Duty','RECORDS: Weekend Duty',14);


insert into earn_code_reasons (id,name,description,reason_category_id) values
(59,'Burglary Patrol','SPECIAL DETAILS: Burglary Patrol',15),
(60,'DUI','SPECIAL DETAILS: DUI',15),
(61,'Little 500','SPECIAL DETAILS: Little 500',15),
(62,'OPO','SPECIAL DETAILS: OPO',15),
(63,'Special Details: Other','SPECIAL DETAILS: Other',15),
(64,'Quiet Nights','SPECIAL DETAILS: Quiet Nights',15),
(65,'Special Assignment','SPECIAL DETAILS: Special Assignment',15),
(66,'Special Detail','SPECIAL DETAILS: Special Detail',15),
(67,'Accident Reconstructionist','SPECIALTY DUTY: Accident Reconstructionist',16),
(68,'Bike Patrol','SPECIALTY DUTY: Bike Patrol',16),
(69,'Civil Disturbance Unit','SPECIALTY DUTY: Civil Disturbance Unit',16),
(70,'CIRT','SPECIALTY DUTY: CIRT',16),
(71,'Dive','SPECIALTY DUTY: Dive',16),
(72,'Downtown Resource Officer','SPECIALTY DUTY: Downtown Resource Officer',16),
(73,'Specialty Duty: PTO','SPECIALTY DUTY: FTO',16),
(74,'Honor Guard','SPECIALTY DUTY: Honor Guard',16),
(75,'Hostage Negotiator','SPECIALTY DUTY: Hostage Negotiator',16),
(76,'K9','SPECIALTY DUTY: K9',16),
(77,'Training Instructor','SPECIALTY DUTY: Training Instructor',16),
(78,'Officer Training/Travel Time','TRAINING: Officer Training/Travel Time',17);

create table code_reason_conditions(
  id int unsigned not null auto_increment,
	reason_id int unsigned not null,
	hour_code_id int unsigned not null,
	salary_group_id int unsigned not null,
	department_id int unsigned,
	group_id int unsigned,
	inactive char(1),
	primary key(id),
	foreign key(reason_id) references earn_code_reasons(id),
	foreign key(hour_code_id) references hour_codes(id),
	foreign key(salary_group_id) references salary_groups(id),	
	foreign key(department_id) references departments(id),		
	foreign key(group_id) references groups(id)		
) ENGINE=InnoDB;
;;
;;
;;
;; 4/15/2019 
alter table groups change excess_hours_calculation_method	excess_hours_earn_type enum('Earn Time','Monetary','Donation');
;;
;; ------------------------------------------------
;; 4/18/2019
;; 
alter table time_blocks add earn_code_reason_id int unsigned after hour_code_id;
alter table time_blocks add foreign key(earn_code_reason_id) references earn_code_reasons(id);
alter table time_block_logs add earn_code_reason_id int unsigned after hour_code_id;
alter table time_block_logs add foreign key(earn_code_reason_id) references earn_code_reasons(id);
alter table  code_cross_ref add unique(code_id);
;;
;; add PoliceAdmin role to Elaine and Jamie in Police
;;

;;
;; update the view
;;
       create or replace view time_blocks_view as                                            select t.id time_block_id,                                                     t.document_id document_id,                                                      t.hour_code_id hour_code_id,                                                    t.earn_code_reason_id earn_code_reason_id,                                      t.date,                                                                         t.begin_hour begin_hour,                                                        t.begin_minute begin_minute,                                                    t.end_hour end_hour,                                                            t.end_minute end_minute,                                                        t.hours hours,                                                                  t.amount amount,                                                                t.clock_in clock_in,                                                            t.clock_out clock_out,                                                          t.inactive inactive,                                                            datediff(t.date,p.start_date) order_id,                                         c.name code_name,                                                               c.description code_description,                                                 c.record_method record_method,                                                  c.accrual_id accrual_id,                                                        c.type code_type,                                                               c.default_monetary_amount,                                                      cf.nw_code nw_code_name,                                                        ps.name job_name,                                                               j.id job_id,                                                                    d.pay_period_id pay_period_id,                                                  d.employee_id employee_id,                                                      r.description reason                                                            from time_blocks t                                                              join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join jobs j on d.job_id=j.id                                                    join positions ps on j.position_id=ps.id                                        join hour_codes c on t.hour_code_id=c.id                                        left join code_cross_ref cf on c.id=cf.code_id 			            left join earn_code_reasons r on r.id=t.earn_code_reason_id 			 
;;
;; ====================================================
;; 
;; Leave Management (in progress started on 10/01/2018)
;;
;; leave_documents table
;;
CREATE TABLE leave_documents (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  employee_id int(10) unsigned NOT NULL,
  pay_period_id int(10) unsigned NOT NULL,
	job_id int(10) unsigned not null,
  initiated datetime NOT NULL,
  initiated_by int(10) unsigned NOT NULL,
	request_approval_time datetime,
	request_approval_by int(10) unsigned NOT NULL,
  PRIMARY KEY (id),
	foreign key(job_id) references jobs(id),
 	FOREIGN KEY (employee_id) REFERENCES employees (id),
  FOREIGN KEY (pay_period_id) REFERENCES pay_periods (id),
  FOREIGN KEY (initiated_by) REFERENCES employees (id),
	FOREIGN KEY (request_approval_by) REFERENCES employees (id),			
  UNIQUE KEY emp_pay_period_job_u (employee_id,pay_period_id, job_id)
) ENGINE=InnoDB;
;;
;;
alter table leave_documents add request_approval_date date;

;;
;; leave_blocks
;;
CREATE TABLE leave_blocks (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  document_id int(10) unsigned NOT NULL,
  hour_code_id int(10) unsigned NOT NULL,
  date date NOT NULL,
  hours decimal(5,2),
	request_date datetime,
	action_status  enum('Approved','Denied'),
	action_by int unsigned,
	action_date datetime,
  inactive char(1),
  PRIMARY KEY (`id`),
	foreign key(document_id) references leave_documents(id),
	foreign key(hour_code_id) references hour_codes(id),
	foreign key(action_by) references employees(id)
) ENGINE=InnoDB;
;;
alter table leave_blocks drop column request_approval;

CREATE TABLE leave_block_logs (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  document_id int(10) unsigned NOT NULL,
	block_id int(10) unsigned not null,
  hour_code_id int(10) unsigned NOT NULL,
  date date NOT NULL,
  hours decimal(5,2),
	request_date  datetime,
	action_status  enum('Approved','Denied'),
	action_by int unsigned,
	action_date datetime,
	change_type enum('Add','Update','Delete'),
	change_time datetime,
	change_by int unsigned,
  PRIMARY KEY (`id`),
	foreign key(document_id) references leave_documents(id),
	foreign key(hour_code_id) references hour_codes(id),
	foreign key(change_by) references employees(id),
  foreign key(action_by) references employees(id)			
) ENGINE=InnoDB;

;;
;; find users who logged during 3/23, 3/24

select distinct(document_id) from time_block_logs where action_time like '2019-03-24%' or action_time like '2019-03-24%';
;;
;; name and email
select distinct(concat_ws(' ',e.first_name,e.last_name)),e.email from time_block_logs l, time_documents d, employees e where (l.action_time like '2019-03-24%' or l.action_time like '2019-03-24%') and d.employee_id=e.id and l.document_id=d.id and not (e.email is null or e.email = '');
;;
;; email only
select distinct(e.email) from time_block_logs l, time_documents d, employees e where (l.action_time like '2019-03-24%' or l.action_time like '2019-03-24%') and d.employee_id=e.id and l.document_id=d.id and not (e.email is null or e.email = '');
;;

;;
;; using command line prepared statements
;;
prepare stmt1 from "select id from time_block_logs l where date(l.action_time) = str_to_date(?,'%m/%d/%Y') ";

set @a='04/20/2019';
execute stmt1 using @a;


;;
;; 6/20/2019
;; adding an earn factor to earn_codes (hour_codes) table related to eanred
;; type earn_codes 
;;
alter table hour_codes add earn_factor decimal(5,2) default 0 after default_monetary_amount;

;;
;; modify the view for earn_factor field
;;
     create or replace view time_blocks_view as                                         select t.id time_block_id,                                                     t.document_id document_id,                                                      t.hour_code_id hour_code_id,                                                    t.earn_code_reason_id earn_code_reason_id,                                      t.date,                                                                         t.begin_hour begin_hour,                                                        t.begin_minute begin_minute,                                                    t.end_hour end_hour,                                                            t.end_minute end_minute,                                                        t.hours hours,                                                                  t.amount amount,                                                                t.clock_in clock_in,                                                            t.clock_out clock_out,                                                          t.inactive inactive,                                                            datediff(t.date,p.start_date) order_id,                                         c.name code_name,                                                               c.description code_description,                                                 c.record_method record_method,                                                  c.accrual_id accrual_id,                                                        c.type code_type,                                                               c.default_monetary_amount,                                                      c.earn_factor earn_factor,                                                      cf.nw_code nw_code_name,                                                        ps.name job_name,                                                               j.id job_id,                                                                    d.pay_period_id pay_period_id,                                                  d.employee_id employee_id,                                                      r.description reason                                                            from time_blocks t                                                              join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join jobs j on d.job_id=j.id                                                    join positions ps on j.position_id=ps.id                                        join hour_codes c on t.hour_code_id=c.id                                        left join code_cross_ref cf on c.id=cf.code_id 			                           left join earn_code_reasons r on r.id=t.earn_code_reason_id;
 
;;
;; set earn_factor amounts for the following earn_codes
;; CE1.0 1, CE1.5 1.5, CE2.0 2,
;; CEt1.0 1, CEt1.5 1.5, CEt2.0 2,
;; HCE1.0 1, HCE1.5 1.5, HCE2.0 2
;;
;; modify earn_codes above and link to accruals id
;; CE1.0,CE1.5,CE2.0 ==> CUA; HCE1.0, HCE1.5,HCE2.0 ==> HCUA
;;
;;
;; 06/24/2019
;; allowing the use of pending accruals to be configured by group
;;
 alter table groups add allow_pending_accrual char(1) after excess_hours_earn_type;
 alter table departments add allow_pending_accrual char(1) after ldap_name;
 ;;
 ;; 07/02/2019
 ;; adding 'Cancel' action to time_actions table for now only 'Payroll Approve' option
 ;;
  alter table time_actions add cancelled_by int unsigned;
  alter table time_actions add cancelled_time datetime;
  alter table time_actions add foreign key(cancelled_by) references employees(id);

;; 07/31/2019
;;
alter table time_blocks add minutes int default 0 after hours;
alter table time_block_logs add minutes int default 0 after hours;

update time_blocks set minutes = hours*60;
update time_block_logs set minutes = hours*60;

 update time_blocks set minutes = (end_hour*60+end_minute) - (begin_hour*60+begin_minute) where hour_code_id in (select id from hour_codes where record_method='Time') and ((clock_in is null and clock_out is null) or (clock_in is not null and clock_out is not null)) ;
 
 update time_block_logs set minutes = (end_hour*60+end_minute) - (begin_hour*60+begin_minute) where hour_code_id in (select id from hour_codes where record_method='Time') and ((clock_in is null and clock_out is null) or (clock_in is not null and clock_out is not null)) ;
 
 ;;
;; modify the view for earn_factor field
;;
     create or replace view time_blocks_view as                                         select t.id time_block_id,                                                     t.document_id document_id,                                                      t.hour_code_id hour_code_id,                                                    t.earn_code_reason_id earn_code_reason_id,                                      t.date,                                                                         t.begin_hour begin_hour,                                                        t.begin_minute begin_minute,                                                    t.end_hour end_hour,                                                            t.end_minute end_minute,                                                        t.hours hours,                                                                  t.minutes minutes,                                                              t.amount amount,                                                                t.clock_in clock_in,                                                            t.clock_out clock_out,                                                          t.inactive inactive,                                                            datediff(t.date,p.start_date) order_id,                                         c.name code_name,                                                               c.description code_description,                                                 c.record_method record_method,                                                  c.accrual_id accrual_id,                                                        c.type code_type,                                                               c.default_monetary_amount,                                                      c.earn_factor earn_factor,                                                      cf.nw_code nw_code_name,                                                        ps.name job_name,                                                               j.id job_id,                                                                    d.pay_period_id pay_period_id,                                                  d.employee_id employee_id,                                                      r.description reason                                                            from time_blocks t                                                              join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join jobs j on d.job_id=j.id                                                    join positions ps on j.position_id=ps.id                                        join hour_codes c on t.hour_code_id=c.id                                        left join code_cross_ref cf on c.id=cf.code_id 			                           left join earn_code_reasons r on r.id=t.earn_code_reason_id;

 
;;
;; 8/14/2019
;;
   alter table employees add ad_sid varchar(8) after employee_number;
;;
;; after the field is populated we add the constraint
;;
   alter table employees add unique(ad_sid);
;;
;;
;; we do not need this info any more
;;
   delete from employees_logs;
;;
;; 08/30/2019
;;
  alter table shift_times add hours varchar(5) after end_time;
  alter table shift_times add amount varchar(5) after hours;
;;
;; update the hours to reflect the real value
;;
	update shift_times set hours = (substring_index(end_time,':',1)+substring_index(end_time,':',-1)/60)  - (substring_index(start_time,':',1)+substring_index(start_time,':',-1)/60) where start_time is not null;

	alter table hour_codes add holiday_related char(1) after earn_factor;
;;
;; update the view
;;
     create or replace view time_blocks_view as                                         select t.id time_block_id,                                                     t.document_id document_id,                                                      t.hour_code_id hour_code_id,                                                    t.earn_code_reason_id earn_code_reason_id,                                      t.date,                                                                         t.begin_hour begin_hour,                                                        t.begin_minute begin_minute,                                                    t.end_hour end_hour,                                                            t.end_minute end_minute,                                                        t.hours hours,                                                                  t.minutes minutes,                                                              t.amount amount,                                                                t.clock_in clock_in,                                                            t.clock_out clock_out,                                                          t.inactive inactive,                                                            datediff(t.date,p.start_date) order_id,                                         c.name code_name,                                                               c.description code_description,                                                 c.record_method record_method,                                                  c.accrual_id accrual_id,                                                        c.type code_type,                                                               c.default_monetary_amount,                                                      c.earn_factor earn_factor,                                                      c.holiday_related holiday_related,                                              cf.nw_code nw_code_name,                                                        ps.name job_name,                                                               j.id job_id,                                                                    d.pay_period_id pay_period_id,                                                  d.employee_id employee_id,                                                      r.description reason                                                            from time_blocks t                                                              join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join jobs j on d.job_id=j.id                                                    join positions ps on j.position_id=ps.id                                        join hour_codes c on t.hour_code_id=c.id                                        left join code_cross_ref cf on c.id=cf.code_id 			                           left join earn_code_reasons r on r.id=t.earn_code_reason_id;

;;
;; mark the following earn codes as holiday related H1.0, HCE1.0, HCE1.5, HCE2.0;; and HF
;;


;;
;; 10/01
;; to be able to recycle badge numbers we added a new table to hold
;; the available badge numbers
;;
CREATE TABLE available_badges (
  badge_num int unsigned NOT NULL unique
) ENGINE=InnoDB;



;;
;; 10/23/2019
;; changed:
;;
;; updated Login to give warning on reserved email users
;;
;; add the following flag to timetrack.xml in tomcat configuration
;; set the flag to true in production and false in test
;; the default is true
;; non production set to false
 run_employee_update = false;
;;
;; 11/06/2019
;;
create table service_keys(
 id int unsigned auto_increment primary key,
 key_name varchar(56),
 key_value varchar(256),
 inactive char(1)
)engine=InnoDB;


 

 

