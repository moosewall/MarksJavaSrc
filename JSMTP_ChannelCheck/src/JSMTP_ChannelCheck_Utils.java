

import java.io.*;
import java.net.InetAddress;
import java.util.Properties;
import java.util.Date;

import javax.mail.*;

import javax.mail.internet.*;

import com.sun.mail.smtp.*;

/*
 * Notes:
 * 
 * How to send email with java
 * http://stackoverflow.com/questions/73580/how-do-i-send-an-smtp-message-from-java
 * 
 * JavaMail
 * https://java.net/projects/javamail/pages/Home
 * https://java.net/projects/javamail/pages/Home#Download_JavaMail_1.5.1_Release
 */

public class JSMTP_ChannelCheck_Utils {

	public static void main(String[] args) 
	{
		AppState.initAppDetails(JSMTP_ChannelCheck_Utils.class); //call this before anything!
		
		String sFN = TraceUtils.sGetFN() ;
		TraceUtils.Trc(sFN + " entered");
		
		TestSmtp () ;
		
		TraceUtils.Trc(sFN + " exited");
	}
	/////////////////////////////////////////////
	//
	//example from here:
	//http://stackoverflow.com/questions/73580/how-do-i-send-an-smtp-message-from-java
	//
	public static void TestSmtp ()
	{
		String sFN = TraceUtils.sGetFN () ;
		TraceUtils.Trc (sFN + " entered") ;
		try
		{
			Properties props = System.getProperties();
	        props.put("mail.smtps.host","smtp.gmail.com");
	        props.put("mail.smtps.auth","true");
	        Session session = Session.getInstance(props, null);
	        
	        Message msg = new MimeMessage(session);
	        
	        /*side note
	         *
	         *can I import a prepared .eml image into a MimeMessage using this?
	         *
	         * http://docs.oracle.com/javaee/6/api/javax/mail/internet/MimeMessage.html
	         * 	MimeMessage(Session session, java.io.InputStream is) 
          Constructs a MimeMessage by reading and parsing the data from the specified MIME InputStream.
	         */
	        
	        msg.setFrom(new InternetAddress("m.sw2005@gmail.com"));;
	        
	        /*
	        msg.setRecipients
	        (Message.RecipientType.TO,
	        InternetAddress.parse("m.sw2005@gmail.com", false));

	        msg.setRecipients(Message.RecipientType.TO,
	        InternetAddress.parse("rdavidson@aceweb.com", false));
	        */
	        msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse("m.sw2005@gmail.com", false));
	        //msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse("mark.wallace.ctr@navy.mil", false));
	        
	        msg.setSubject("Java SMTP test "+System.currentTimeMillis());
	        msg.setText("Hi, Just testing!  Mark :-)");
	        msg.setHeader("X-Mailer", "test");
	        msg.setSentDate(new Date());
	        SMTPTransport t =
	            (SMTPTransport)session.getTransport("smtps");
	        
	        String sPassword = "" ;
	        sPassword = GetStrDlg.sEnterStr("", "Enter password", null) ;
	        if (sPassword == "")
	        {
	        	throw new Exception ("No password entered") ;
	        }
	        t.connect("smtp.gmail.com", "m.sw2005@gmail.com", sPassword);
	        t.sendMessage(msg, msg.getAllRecipients());
	        
	        String sLastServerResponse = t.getLastServerResponse() ;
	        System.out.println("Response: " + sLastServerResponse);
	        
	        t.close();
		}
		catch (Exception exp)
		{
			TraceUtils.Trc (sFN + " exception " + exp.getMessage()) ;
		}
		
		TraceUtils.Trc (sFN + " exiting") ;
	}
	/////////////////////////////////////////////
	//
	public static void DoGenericProjectCopy 
		(JSMTP_ChannelCheck_AppWin aw)
	{
		
	}
	
}
