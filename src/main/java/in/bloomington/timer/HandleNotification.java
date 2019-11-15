package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleNotification{

    boolean debug = false, activeMail = false;
    static final long serialVersionUID = 53L;
    static String mail_host = "";
    static Logger logger = LogManager.getLogger(HandleNotification.class);
    String date="", dept_id="", pay_period_id="";
    List<Employee> emps = null;
 		
    public HandleNotification(String val, String val2, boolean val3){
				setPay_period_id(val);
				setMail_host(val2);
				setActiveMailFlag();
    }
    public HandleNotification(PayPeriod val, String val2, boolean val3){
				if(val != null){
						setPay_period_id(val.getId()); 
				}
				setMail_host(val2);
				setActiveMailFlag();
    }
		void setActiveMailFlag(){
				ServletContext ctx = SingleContextHolder.getContext();
				if(ctx != null){
						System.err.println(" ctx is Ok");				
						String val = ctx.getInitParameter("activeMail");
						if(val != null && val.equals("true")){
								activeMail = true;
						}
						System.err.println(" activeMail "+activeMail);										
				}
				else{
						System.err.println(" ctx is null, could not retreive activeMail flag ");
				}
		}
    void setActiveMail(){
				activeMail = true;
    }
    public void setMail_host(String val){
				if(val != null){		
						mail_host = val;
				}
    }
    //
    // setters
    //
    public void setDept_id(String val){
				if(val != null){		
						dept_id = val;
				}
    }
    public void setPay_period_id(String val){
				if(val != null && !val.equals("-1")){		
						pay_period_id = val;
				}
    }		
    public void setDate(String val){
				if(val != null){		
						date = val;
				}
    }
    /**
     *
     select e.first_name,e.last_name,d.id from time_documents d,employees e,jobs j where e.id=d.employee_id and e.inactive is null and j.employee_id=e.id and j.clock_time_required is null and j.inactive is null and d.pay_period_id = 549 and e.username not like 'admin' and d.id not in (select a.document_id from time_actions a,time_documents d2 where a.document_id=d2.id and d2.pay_period_id=549 and a.workflow_id=2) order by 1;

       select e.id,e.username,e.first_name,e.last_name,e.email,d.id                      from time_documents d,                                                          employees e,                                                                    jobs j                                                                          where e.id=d.employee_id                                                        and e.inactive is null                                                          and j.employee_id=e.id                                                          and j.clock_time_required is null                                               and j.inactive is null                                                          and d.pay_period_id = 549                                                       and e.username not like 'admin'                                                 and e.email is not null  and e.email <> ''                                      and d.id not in (select a.document_id from time_actions a,time_documents d2 where a.document_id=d2.id and d2.pay_period_id=549 and a.workflow_id=2) order by 2; ";
		 
    */
    //
    // find all the employees who have initiated document time but
    // forgot to submit for approval
    //
    String findToBeNotifiedEmployees(){
		
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="";
				//
				// find all non clock employees that have documents for the
				// specified pay_period_id but not submitted
				//
				String qq = " select e.id,e.username,e.first_name,e.last_name,e.email                from time_documents d,                                                          employees e,                                                                    jobs j                                                                          where e.id=d.employee_id                                                        and e.inactive is null                                                          and j.employee_id=e.id                                                          and j.clock_time_required is null                                               and j.inactive is null                                                          and d.pay_period_id = ?                                                         and e.username not like 'admin'                                                 and e.email is not null and e.email <> ''                                       and d.id not in (select a.document_id from time_actions a,time_documents d2 where a.document_id=d2.id and d2.pay_period_id=? and a.workflow_id=2) ";
				// initiated but not submitted for approval
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB";
								return msg;
						}
						if(debug){
								logger.debug(qq);
						}
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, pay_period_id);
						pstmt.setString(2, pay_period_id);
						rs = pstmt.executeQuery();
						while(rs.next()){
								Employee one = new Employee(rs.getString(1),
																						rs.getString(2),
																						rs.getString(3),
																						rs.getString(4),
																						rs.getString(5));
								if(emps == null){
										emps = new ArrayList<>();
								}
								if(!emps.contains(one))
										emps.add(one);
						}
				}catch(Exception ex){
						msg += ex;
						System.err.println(ex);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
    }
    public String process(){
				String msg = "";
				if(emps == null){
						msg = findToBeNotifiedEmployees();
						if(!msg.equals("")){
								return msg;
						}						
				}
				if(emps == null || emps.size() < 1){
						msg = "No employee to process";
						System.err.println(" Notification: no employees found ");
						return msg;
				}
				String to_str = "";
				if(activeMail){
						String success_list="";
						String failure_list="";
						String failure_error="";
						for(Employee one:emps){
								to_str = one.getFull_name()+"<"+one.getEmail()+">";
								msg = compuseAndSend(to_str);
								if(msg.equals("")){
										if(!success_list.equals("")) success_list +=", "; 
										success_list += to_str;
								}
								else{
										if(!failure_list.equals("")){
												failure_list +=", ";
												failure_error += ", ";
										}
										failure_list += to_str;
										if(failure_error.indexOf(msg) == -1){
												if(!failure_error.equals("")){
														failure_error += ", ";
												}										
												failure_error += msg;
										}
								}
						}
						if(!success_list.equals("")){
								NotificationLog nlog = new NotificationLog(success_list, body_text, "Success",null);
								nlog.doSave();										
						}
						if(!failure_list.equals("")){
								NotificationLog nlog = new NotificationLog(failure_list, body_text, "Failure", failure_error);
								nlog.doSave();		
						}						
				}
				else{
						System.err.println(" active mail is turned off, no email sent");
				}
				return msg;
    }
		String body_text ="This is an automated message from the Timetrack timekeeping system.\n\n"+
				"Today marks the beginning of a new pay period. Our records show you have not yet submitted your timesheet for the last pay period. Please complete and review your timesheet as soon as possible.\n\n"+
				"The timetrack system is available here:\n\n"+
				"https://bloomington.in.gov/timetrack\n\n"+
				"After you login, you may need to click on the previous pay period button under 'Time Details' title.\n"+
				"Then click on 'Submit for Approval' button in the middle of the page.\n\n"+
				"If you have any questions or support needs, please contact the ITS Helpdesk at (812) 349-3454 or helpdesk@bloomington.in.gov for assistance.\n\n"+
				"Usage guidelines are available here:\n"+
				"https://docs.google.com/document/d/1krOtCGtJ_SaPCOBF0cv5bMu-q6Buv1KI68PjS0TrZl0/edit#heading=h.ye6yj01u0is4\n\n"+
				"Thank you\n"+
				"\n\n";
		
    String compuseAndSend(String to_str){
				String msg = "";
				Properties props = new Properties();
				props.put("mail.smtp.host", mail_host);
				
				Session session = Session.getDefaultInstance(props, null);
				try{
						Message message = new MimeMessage(session);						
						message.setSubject("Submit timesheet reminder");
						message.setText(body_text);
						message.setFrom(new InternetAddress(CommonInc.fromEmailStr));
						InternetAddress[] addrArray = InternetAddress.parse(to_str);
						message.setRecipients(Message.RecipientType.TO, addrArray);
						// message.setRecipients(Message.RecipientType.BCC, addrArray);
						Transport.send(message);
				}
				catch (MessagingException mex){
						//
						// Failure
						msg += mex;
						//
						logger.error(mex);
						Exception ex = mex;
						do {
								if (ex instanceof SendFailedException) {
										SendFailedException sfex = (SendFailedException)ex;
										javax.mail.Address [] invalid = sfex.getInvalidAddresses();
										if (invalid != null) {
												logger.error("    ** Invalid Addresses");
												if (invalid != null) {
														for (int i = 0; i < invalid.length; i++) 
																logger.error("         " + invalid[i]);
												}
										}
										javax.mail.Address [] validUnsent = sfex.getValidUnsentAddresses();
										if (validUnsent != null) {
												logger.error("    ** ValidUnsent Addresses");
												if (validUnsent != null) {
														for (int i = 0; i < validUnsent.length; i++) 
																logger.error("         "+validUnsent[i]);
												}
										}
										javax.mail.Address [] validSent = sfex.getValidSentAddresses();
										if (validSent != null) {
												logger.error("    ** ValidSent Addresses");
												if (validSent != null) {
														for (int i = 0; i < validSent.length; i++) 
																logger.error("         "+validSent[i]);
												}
										}
								}
								if (ex instanceof MessagingException)
										ex = ((MessagingException)ex).getNextException();
								else { // any other exception
										logger.error(ex);
										ex = null;
								}
						} while (ex != null);
				} catch (Exception ex){
						logger.error(ex);
				}						
				return msg;
    }

}
