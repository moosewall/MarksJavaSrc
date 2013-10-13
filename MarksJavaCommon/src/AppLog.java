
//Thanks to Lars Vogel
//http://www.vogella.com/articles/Logging/article.html

import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class AppLog 
{
	static private boolean m_bInitialized = false ;

	static private Logger m_logger = null ;
	static private FileHandler m_fileTxt;
	static private SimpleFormatter m_formatterTxt;

	static public boolean m_bLogToConsole = true ;
	////////////////////////
	//
	static public void Log (String sLog)
	{
		Init () ;
		
		//StdioLogger.Log(sLog);
		m_logger.log(Level.INFO, sLog);
	}
	/////////////////////////
	//
	static public void Init ()
	{
		if (m_bInitialized)
		{
			return ;
		}
		m_bInitialized = true ;
		try
		{
			m_logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		    m_logger.setLevel(Level.INFO);
	
		    //http://docs.oracle.com/javase/1.4.2/docs/api/java/util/logging/FileHandler.html
			m_fileTxt = new FileHandler 
					(AppState.m_AppDetails.m_sLogFilePath,
					 5000000,
					 1,
					 true) ;
			
			// Create txt Formatter
		    m_formatterTxt = new SimpleFormatter() ;
		    
		    m_fileTxt.setFormatter(m_formatterTxt);
		    m_logger.addHandler(m_fileTxt);
		    
		    if (m_bLogToConsole)
		    {
		    	//http://stackoverflow.com/questions/9199598/using-java-util-logging-to-log-on-the-console
			    ConsoleHandler handler = new ConsoleHandler();
			    handler.setFormatter(new SimpleFormatter());
			    
			    //handler.setLevel(Level.ALL); //everything goes to console.
			    
			    m_logger.addHandler(handler);
		    }
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
	}
}
