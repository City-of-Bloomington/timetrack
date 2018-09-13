package in.bloomington.timer.util;
import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.Address;
import javax.mail.internet.*;
import javax.activation.*;
import org.apache.log4j.Logger;

/**
 * MailHandle creates a very simple text/plain message and sends it.
 * handle emails with file attachments
 */
public class MailHandle{

		final static long serialVersionUID = 580L;
		static Logger logger = Logger.getLogger(MailHandle.class);			
		static boolean debug = false;
		static String host = "localhost"; 
    String msgText = "This is a message body.\nHere's the second line.";
    String to = null;
    String from = "";

    // static String host = "localhost";		
    
    String subject = "";
    String cc = null;
		String bcc = null;
		String file_name = "", file_path = "";

    /**
     * The main constructor.
     *
     * @param to2 to email address
     * @param from2 from email address
     * @param msg2 the message
     * @param cc2 the cc email address
		 * @param bcc2 the blind carbon copy list 
     * @param debug2 the debug flag true|false
     */
    public MailHandle(String host2,
											String to2,
											String from2,
											String cc2,
											String bcc2,											 
											String subject2,
											String msg2,
											boolean debug2){
				if(host2 != null)
						host = host2;
				if(to2 != null && to2.equals("Bulk")){
						to = null;
				}
				else{
						to = to2;
				}
				cc = cc2;
				bcc = bcc2;
				from = from2;
				msgText = msg2;
				debug = debug2;
				if(subject2 != null) subject = subject2;
		}
	
		//
    public MailHandle(String host2,
											String to2, String from2,
											String subject2, String msg2,
											String cc2, String bcc2,
											String file_name2, String file_path2,
											boolean debug2){
				if(host2 != null)
						host = host2;
				if(to2 != null && to2.equals("Bulk")){
						to = null;
				}
				else{
						to = to2;
				}
				cc = cc2;
				bcc = bcc2;
				from = from2;
				msgText = msg2;
				debug = debug2;
				if(subject2 != null) subject = subject2;
				file_name = file_name2;
				file_path = file_path2;
		}
		//
		public String send(){
		
				String message = "";
				try {
						//
						// create some properties and get the default Session
						//
						Properties props = new Properties();
						props.put("mail.smtp.host", host);
						if (debug) props.put("mail.debug", "true");
			
						Session session = Session.getDefaultInstance(props, null);
						session.setDebug(debug);
						//
						// create a message
						//
						Message msg = new MimeMessage(session);
						msg.setFrom(new InternetAddress(from));
						if(to != null && !to.equals("")){
								InternetAddress[] address = {new InternetAddress(to)};
								msg.setRecipients(Message.RecipientType.TO, address);
						}
						if(cc != null && !cc.equals("")){
								InternetAddress[] address2 = {new InternetAddress(cc)};
								msg.setRecipients(Message.RecipientType.CC, address2);
						}
						if(bcc != null && !bcc.equals("")){
								InternetAddress[] address3 = javax.mail.internet.InternetAddress.parse(bcc);
								msg.setRecipients(Message.RecipientType.BCC, address3);
						}
						msg.setSubject(subject);
						msg.setSentDate(new Date());

						// If the desired charset is known, you can use
						// setText(text, charset)
						msg.setText(msgText);
						//
						Transport.send(msg);
				} catch (MessagingException mex){

						logger.debug("\n--Exception handling in MailHandle.java");
						message += " Exception in MailHandle "+mex;
						Exception ex = mex;
						do {
								if (ex instanceof SendFailedException) {
										SendFailedException sfex = (SendFailedException)ex;
										Address[] invalid = sfex.getInvalidAddresses();
										if (invalid != null) {
												System.out.println("    ** Invalid Addresses");
												if (invalid != null) {
														for (int i = 0; i < invalid.length; i++) 
																message += "         " + invalid[i];
												}
										}
										Address[] validUnsent = sfex.getValidUnsentAddresses();
										if (validUnsent != null) {
												System.out.println("    ** ValidUnsent Addresses");
												if (validUnsent != null) {
														for (int i = 0; i < validUnsent.length; i++) 
																message += "         "+validUnsent[i];
												}
										}
										Address[] validSent = sfex.getValidSentAddresses();
										if (validSent != null) {
												System.out.println("    ** ValidSent Addresses");
												if (validSent != null) {
														for (int i = 0; i < validSent.length; i++) 
																message += "         "+validSent[i];
												}
										}
								}
								System.out.println();
								if (ex instanceof MessagingException){
										ex = ((MessagingException)ex).getNextException();
								}
								else{
										ex = null;
								}
						} while (ex != null);
						logger.error(message);
				}
				return message;
    }
		public String sendWithAttach(){
		
				String message = "";
				try {
						//
						// create some properties and get the default Session
						//
						Properties props = new Properties();
						props.put("mail.smtp.host", host);
						if (debug) props.put("mail.debug", "true");
						Session session = Session.getDefaultInstance(props, null);
						session.setDebug(debug);
						//
						// create a message
						//
						Message msg = new MimeMessage(session);
						msg.setFrom(new InternetAddress(from));
						if(to != null && !to.equals("")){
								InternetAddress[] address = {new InternetAddress(to)};
								msg.setRecipients(Message.RecipientType.TO, address);
						}
						if(cc != null && !cc.equals("")){
								InternetAddress[] address2 = {new InternetAddress(cc)};
								msg.setRecipients(Message.RecipientType.CC, address2);
						}
						if(bcc != null && !bcc.equals("")){
								InternetAddress[] address3 = javax.mail.internet.InternetAddress.parse(bcc);
								msg.setRecipients(Message.RecipientType.BCC, address3);
						}
						msg.setSubject(subject);
						msg.setSentDate(new Date());
						// Create the message part 
						BodyPart messageBodyPart = new MimeBodyPart();
						//			
						// Fill the message
						messageBodyPart.setText(msgText);
						Multipart multipart = new MimeMultipart();
						multipart.addBodyPart(messageBodyPart);
						// Part two is attachment
						messageBodyPart = new MimeBodyPart();
						DataSource source = new FileDataSource(file_path+file_name);
						messageBodyPart.setDataHandler(new DataHandler(source));
						messageBodyPart.setFileName(file_name);
						multipart.addBodyPart(messageBodyPart);
						// Put parts in message
						msg.setContent(multipart);
						//
						Transport.send(msg);
				} catch (MessagingException mex){

						logger.debug("\n--Exception handling in MailHandle.java");
						message += " Exception in MailHandle "+mex;
						Exception ex = mex;
						do {
								if (ex instanceof SendFailedException) {
										SendFailedException sfex = (SendFailedException)ex;
										Address[] invalid = sfex.getInvalidAddresses();
										if (invalid != null) {
												System.out.println("    ** Invalid Addresses");
												if (invalid != null) {
														for (int i = 0; i < invalid.length; i++) 
																message += "         " + invalid[i];
												}
										}
										Address[] validUnsent = sfex.getValidUnsentAddresses();
										if (validUnsent != null) {
												System.out.println("    ** ValidUnsent Addresses");
												if (validUnsent != null) {
														for (int i = 0; i < validUnsent.length; i++) 
																message += "         "+validUnsent[i];
												}
										}
										Address[] validSent = sfex.getValidSentAddresses();
										if (validSent != null) {
												System.out.println("    ** ValidSent Addresses");
												if (validSent != null) {
														for (int i = 0; i < validSent.length; i++) 
																message += "         "+validSent[i];
												}
										}
								}
								System.out.println();
								if (ex instanceof MessagingException){
										ex = ((MessagingException)ex).getNextException();
								}
								else{
										ex = null;
								}
						} while (ex != null);
						logger.error(message);
				}
				return message;
    }
}
