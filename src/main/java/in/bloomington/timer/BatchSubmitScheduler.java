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

public class BatchSubmitScheduler {

		static boolean debug = false;
		static Logger logger = LogManager.getLogger(BatchSubmitScheduler.class);
		int month = 11, day = 4, year=2019; // first day of a pay period
		Date startDate, endDate = null;
		public BatchSubmitScheduler(String date){
				try{
						if(!date.isEmpty()){
								String strArr[] = date.split("/");
								month = Integer.parseInt(strArr[0]);
								day = Integer.parseInt(strArr[1]);
								year = Integer.parseInt(strArr[2]);
								Calendar cal = new GregorianCalendar();
								cal.set(year, (month-1), day);
								cal.set(Calendar.HOUR_OF_DAY, 7);//to run at 7am of the specified day
								cal.set(Calendar.MINUTE, 30);
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
						String jobName = "batch_"+month+"_"+day+"_"+year;
						String groupName = "batch";
						JobDetail job = JobBuilder.newJob(BatchSubmitJob.class)
								.withIdentity(jobName, groupName)
								.build();
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
															.withIntervalInHours(24*14) // 24*14 two weeks
															.repeatForever()
															.withMisfireHandlingInstructionIgnoreMisfires())
								// .endAt(endDate)						  
								.build();
		
						// Tell quartz to schedule the job using our trigger
						sched.scheduleJob(job, trigger);
						sched.start();

				}catch(Exception ex){
						logger.error(ex);
						msg += ex;
				}
				return msg;
    }
	
}
