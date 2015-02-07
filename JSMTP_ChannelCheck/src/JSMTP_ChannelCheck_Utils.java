

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
		
		AppLogger.TrcAppLogger tal = new AppLogger.TrcAppLogger () ;  
		TestSmtp (tal) ;
		
		TraceUtils.Trc(sFN + " exited");
	}
	/////////////////////////////////////////////
	//
	//example from here:
	//http://stackoverflow.com/questions/73580/how-do-i-send-an-smtp-message-from-java
	//
	public static class TestSmtpConfig
	{
		public String m_sServer = "smtp.gmail.com" ;
		public String m_sServerAccountName = "mark.wallace.work@gmail.com" ;
		public String m_sServerAccountPassword = "" ;
		
		public String m_sFromName = "Mark Wallace (work)" ;
		public String m_sFromAddress = "mark.wallace.work@gmail.com" ;
		
		public String m_sToName = "Mark Wallace (new mw.biz)" ;
		public String m_sToAddress = "m.sw2005@gmail.com" ;
		
		public String m_sSubj = "" ;
		public String m_sBody = "" ;
		
		///////////////////////////////
		//
		public void CheckMe () throws Exception
		{
			String sFN = TraceUtils.sGetFN();
			String sErrs = "" ;
			
			if (m_sServer == "")
			{
				sErrs += "No server\n" ;
			}
			if (this.m_sServerAccountPassword == "")
			{
				sErrs += "No pass\n" ;
			}
			
			if (sErrs != "")
			{
				throw new Exception (sFN + " errors.  " + sErrs) ;
			}
		}
	}
	public static void TestSmtp
		(AppLogger logger)
	{
		TestSmtpConfig config = new TestSmtpConfig () ; 

        logger.Log ("Prompting for password...") ;
        config.m_sServerAccountPassword = GetStrDlg.sEnterStr("", "Enter password", null) ;
		
		
		config.m_sSubj = "Java SMTP test " + System.currentTimeMillis() ;
		config.m_sBody = config.m_sSubj ;
		
		TestSmtp (config, logger) ;
	}
	public static void TestSmtp 
		(TestSmtpConfig config,
		 AppLogger logger)
	{
		String sFN = TraceUtils.sGetFN () ;
		TraceUtils.Trc (sFN + " entered") ;
		logger.Log (sFN + " entered") ;
		
		String sLog = "" ;
		
		try
		{
			config.CheckMe();
			
			Properties props = System.getProperties();
			
			/*
	        props.put("mail.smtps.host","smtp.gmail.com");
	        */
			props.put("mail.smtps.host",config.m_sServer);
			
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
	        
	        //msg.setFrom(new InternetAddress("mark.wallace.work@gmail.com"));;
	        logger.Log ("Setting from...") ;
	        msg.setFrom(new InternetAddress("mark.wallace.ctr@navy.mil", "Mark Wallace"));;
	        
	        /*
	        msg.setRecipients
	        (Message.RecipientType.TO,
	        InternetAddress.parse("m.sw2005@gmail.com", false));

	        msg.setRecipients(Message.RecipientType.TO,
	        InternetAddress.parse("rdavidson@aceweb.com", false));
	        */
	        msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse("m.sw2005@gmail.com", false));
	        //msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse("mark.wallace.ctr@navy.mil", false));
	        
	        /*
	        msg.setSubject("Java SMTP test "+System.currentTimeMillis());
	        */
	        msg.setSubject (config.m_sSubj) ;
	        
	        /*
	        msg.setText("Hi, Just testing!  Mark :-)");
	        */
	        msg.setText(config.m_sBody); ;
	        
	        msg.setHeader("X-Mailer", "test");
	        msg.setSentDate(new Date());
	        SMTPTransport t =
	            (SMTPTransport)session.getTransport("smtps");
	        
	        /*passed in now
	        logger.Log ("Prompting for password...") ;
	        String sPassword = "" ;
	        sPassword = GetStrDlg.sEnterStr("", "Enter password", null) ;
	        if (sPassword == "")
	        {
	        	throw new Exception ("No password entered") ;
	        }
	        */
	        
	        logger.Log ("Connecting...") ;
	        /*
	        t.connect("smtp.gmail.com", "mark.wallace.work@gmail.com", sPassword);
	        */
	        t.connect
	        	(config.m_sServer, 
	        	 config.m_sServerAccountName, 
	        	 config.m_sServerAccountPassword);
	        
	        logger.Log ("sending test message") ;
	        t.sendMessage(msg, msg.getAllRecipients());
	        
	        String sLastServerResponse = t.getLastServerResponse() ;
	        System.out.println("Response: " + sLastServerResponse);
	        logger.Log ("response " + sLastServerResponse) ;
	        
	        t.close();
		}
		catch (Exception exp)
		{
			sLog = sFN + " exception " + exp.getMessage() ;
			TraceUtils.Trc (sLog) ;
			logger.Log(sLog);
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
