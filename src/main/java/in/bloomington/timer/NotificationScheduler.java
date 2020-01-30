package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import org.quartz.TriggerBuilder;
import org.quartz.DateBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationScheduler {

		static boolean debug = false, activeMail = false;
		static String mail_host = "";
		static Logger logger = LogManager.getLogger(NotificationScheduler.class);
		int month = 1, day = 7, year=2017;
		Date startDate, endDate = null;
		public NotificationScheduler(String date,
																 String mail_host,
																 boolean activeMailFlag){
				if(activeMailFlag)
						activeMail = true;
				this.mail_host = mail_host;
				try{
						if(!date.isEmpty()){
								String strArr[] = date.split("/");
								month = Integer.parseInt(strArr[0]);
								day = Integer.parseInt(strArr[1]);
								year = Integer.parseInt(strArr[2]);
								Calendar cal = new GregorianCalendar();
								cal.set(year, (month-1), day);
								cal.set(Calendar.HOUR_OF_DAY, 7);//to run at 7am of the specified day
								cal.set(Calendar.MINUTE, 0);
								startDate = cal.getTime();
						}
				}
				catch(Exception ex){
						logger.error(ex);
				}
		}
	
		public String run() throws Exception {
				String msg = "";
				//
				//   Logger log = LoggerFactory.getLogger(SponScheduler.class);

        logger.info("------- Initializing ----------------------");

        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        logger.info("------- Initialization Complete -----------");

        // computer a time that is on the next round minute
				//  Date runTime = evenMinuteDate(new Date());

        logger.info("------- Scheduling Job  -------------------");

        // define the job and tie it to our Job class
				try{
						String jobName = "notification_"+month+"_"+day+"_"+year;
						String groupName = "notification";
						JobDetail job = JobBuilder.newJob(NotificationJob.class)
								.withIdentity(jobName, groupName)
								.build();
						job.getJobDataMap().put("activeMail", ""+activeMail);
						job.getJobDataMap().put("mail_host", ""+mail_host);						
						// 
						// Trigger will run at 7am on the speciified date
						// cron date and time entries (year can be ignored)
						// second minute hour day-of-month month week-day year
						// you can use ? no specific value, 0/5 for incrment (every 5 seconds)
						// * for any value (in minutes mean every minute
						Trigger trigger = TriggerBuilder.newTrigger()
								.withIdentity(jobName, groupName)
								.startAt(startDate)
								.withSchedule(simpleSchedule()
															// .withIntervalInMinutes(3)
															.withIntervalInHours(24*14) // 24*14 two weeks
															.repeatForever()
															// .withRepeatCount(2) 
															// .withMisfireHandlingInstructionFireNow())
															.withMisfireHandlingInstructionIgnoreMisfires())
								// .endAt(endDate)						  
								.build();
		
						// Tell quartz to schedule the job using our trigger
						sched.scheduleJob(job, trigger);
						//  logger.info(job.getKey() + " will run at: " + runTime);  

						// Start up the scheduler (nothing can actually run until the 
						// scheduler has been started)
						sched.start();

						/*
							logger.info("------- Started Scheduler -----------------");

							// wait long enough so that the scheduler as an opportunity to 
							// run the job!
							logger.info("------- Waiting 65 seconds... -------------");
							try {
							// wait 65 seconds to show job
							Thread.sleep(65L * 1000L); 
							// executing...
							} catch (Exception e) {
							}

							// shut down the scheduler
							logger.info("------- Shutting Down ---------------------");
							sched.shutdown(true);
							logger.info("------- Shutdown Complete -----------------");
						*/
				}catch(Exception ex){
						logger.error(ex);
						msg += ex;
				}
				return msg;
    }
	
}
