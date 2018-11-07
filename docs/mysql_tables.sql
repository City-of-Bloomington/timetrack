
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
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `department_id` int(10) unsigned NOT NULL,
  `inactive` char(1) DEFAULT NULL,
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
  `username` varchar(10) DEFAULT NULL,
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
  `effective_date` date DEFAULT NULL,
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
  `effective_date` date DEFAULT NULL,
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
  `start_date` date DEFAULT NULL,
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
  `min_hrs` double(4,2) DEFAULT NULL,
  `step_hrs` double(4,2) DEFAULT NULL,
  `related_accrual_max_leval` double(5,2) DEFAULT NULL,
  `step_warning_text` varchar(80) DEFAULT NULL,
  `min_warning_text` varchar(80) DEFAULT NULL,
  `excess_warning_text` varchar(80) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `hour_code_id` (`hour_code_id`),
  CONSTRAINT `accrual_warnings_ibfk_1` FOREIGN KEY (`hour_code_id`) REFERENCES `hour_codes` (`id`)
) ENGINE=InnoDB;
;;
;; employee_accruals 
;;
CREATE TABLE `employee_accruals` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `accrual_id` int(10) unsigned NOT NULL,
  `employee_id` int(10) unsigned NOT NULL,
  `hours` decimal(7,2) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `accrual_id` (`accrual_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `employee_accruals_ibfk_1` FOREIGN KEY (`accrual_id`) REFERENCES `accruals` (`id`),
  CONSTRAINT `employee_accruals_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
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
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `alias varchar(64) not null,                                                     description` varchar(512) DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
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
  `date` date DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_codes` (`hour_code_id`,`department_id`,`salary_group_id`),
  KEY `salary_group_id` (`salary_group_id`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `hour_code_conditions_ibfk_1` FOREIGN KEY (`salary_group_id`) REFERENCES `salary_groups` (`id`),
  CONSTRAINT `hour_code_conditions_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`),
  CONSTRAINT `hour_code_conditions_ibfk_3` FOREIGN KEY (`hour_code_id`) REFERENCES `hour_codes` (`id`)
) ENGINE=InnoDB;
;;
;; ip_allowed table
;;
 CREATE TABLE `ip_allowed` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(15) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ip_address` (`ip_address`)
) ENGINE=InnoDB;
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
  `hours` decimal(5,2) DEFAULT NULL,
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
(4,'Union','Union employees',1,null);
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
;; update jobs set group_id = (select ge.group_id from group_employees ge where ge.employee_id=jobs.employee_id limit 1);
;; delete from jobs where id=44; // admin job
;; alter table jobs add foreign key(group_id) references groups(id);
;;
;; alter table positions add alias varchar(64) not null after name;
;; update positions set description=name where description is null;
;; update positions set alias=name where alias ='';
;;

;;
;; ====================================================
;;
;; Leave management tables
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
  PRIMARY KEY (id),
	foreign key(job_id) references jobs(id),
 	FOREIGN KEY (employee_id) REFERENCES employees (id),
  FOREIGN KEY (pay_period_id) REFERENCES pay_periods (id),
  FOREIGN KEY (initiated_by) REFERENCES employees (id),	
  UNIQUE KEY emp_pay_period_job_u (employee_id,pay_period_id, job_id)
) ENGINE=InnoDB;
;;
;;
;; leave_blocks
;;
CREATE TABLE leave_blocks (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  document_id int(10) unsigned NOT NULL,
  hour_code_id int(10) unsigned NOT NULL,
  date date NOT NULL,
  hours decimal(5,2),
  request_date date,
	request_approval char(1),
	action_status  enum('Approved','Denied'),
	action_by int unsigned,
	action_date date,
  inactive char(1),
  PRIMARY KEY (`id`),
	foreign key(document_id) references leave_documents(id),
	foreign key(hour_code_id) references hour_codes(id),
	foreign key(action_by) references employees(id)
) ENGINE=InnoDB;

CREATE TABLE leave_block_logs (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  document_id int(10) unsigned NOT NULL,
	block_id int(10) unsigned not null,
  hour_code_id int(10) unsigned NOT NULL,
  date date NOT NULL,
  hours decimal(5,2),
  request_date date,
	request_approval char(1),
	action_status  enum('Approved','Denied'),
	action_by int unsigned,
	action_date date,
	change_type enum('Add','Update','Delete'),
	change_time datetime,
	change_by int unsigned,
  PRIMARY KEY (`id`),
	foreign key(document_id) references leave_documents(id),
	foreign key(hour_code_id) references hour_codes(id),
	foreign key(change_by) references employees(id),
  foreign key(action_by) references employees(id)			
) ENGINE=InnoDB;

