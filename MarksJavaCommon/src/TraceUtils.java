
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class TraceUtils 
{
	////////////////////////////////
	//
	public static void test1 ()
	{
		try
		{
			String sFN = sGetFN () ;
			
			String sTrcPath = sGetTraceFilePath () ;
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
	}
	////////////////////////////////
	//
	private static boolean m_bTrcInitialized = false ;

	static private Logger m_logger = null ;
	static private FileHandler m_fileTxt;
	static private SimpleFormatter m_formatterTxt;
	
	static private boolean m_bTrcOn = true ;
	static private boolean m_bMsgBoxTrcOn = false ;
	
	public static void Trc (String sTrc)
	{
		Init () ;
		
		if (m_bTrcOn)
		{
			m_logger.log(Level.FINEST, sTrc);
			
			if (m_bMsgBoxTrcOn)
			{
				ProgUtils.MsgBox(sTrc);
			}
		}
	}
	////////////////////////////////
	//
	public static void Init ()
	{
		if (m_bTrcInitialized)
		{
			return ;
		}
		if (!m_bTrcOn)
		{
			return ;
		}
		m_bTrcInitialized = true ;
		try
		{
			m_logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		    m_logger.setLevel(Level.FINEST); //log everything!

		    //http://docs.oracle.com/javase/1.4.2/docs/api/java/util/logging/FileHandler.html#FileHandler(java.lang.String, int, int, boolean)
			m_fileTxt = new FileHandler 
					(AppState.m_AppDetails.m_sTraceFilePath,
					 5000000,
					 1,
					 true) ;
			
			
			// Create txt Formatter
		    m_formatterTxt = new SimpleFormatter();
		    m_fileTxt.setFormatter(m_formatterTxt);
		    m_logger.addHandler(m_fileTxt);
		
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		
	}
	////////////////////////////////
	//
	public static String sGetTraceFilePath () throws Exception 
	{
		return AppState.m_AppDetails.m_sTraceFilePath ;
	}
	////////////////////////////////
	//
	//http://stackoverflow.com/questions/1069066/get-current-stack-trace-in-java
	//
	public static void TestStackTrace ()
	{
		String sFN = sGetFN () ;
		
		String sLog = "" ;
		String s = "" ;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace() ;
		
		for (int iste=0; iste<ste.length; iste++)
		{
			StackTraceElement steCur = ste[iste] ;
			
			String sSum = "" ;
			sSum += steCur.getClassName() ;
			sSum += " " ;
			sSum += steCur.getMethodName() ;
			sLog = "Stack element " + iste + " is " + sSum ;
			Trc (sFN + sLog) ;
		}
		
		//print a stack trace without an exception like this.
		Throwable t = new Throwable () ;
		t.printStackTrace(); 
		
		//print a stack trace when exception raised.
		try
		{
			throw new Exception ("testing stack trace") ;
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
	
		}
				

		s = sStackTraceToStr () ;
		Trc (s) ;
	}
	////////////////////////////////
	//
	public static String sStackTraceToStr ()
	{
		String sr = "" ;
		StackTraceElement[] ste = Thread.currentThread().getStackTrace() ;
		
		for (int iste=0; iste<ste.length; iste++)
		{
			StackTraceElement steCur = ste[iste] ;
			
			String sSum = "" ;
			sSum += steCur.getClassName() ;
			sSum += "::" ;
			sSum += steCur.getMethodName() ;
			sSum += "():" ;
			sSum += steCur.getLineNumber() ;
			
			if (sr != "")
			{
				sr += "\n" ;
			}
			sr += sSum ;
		}
	
		return sr ;
	}
	////////////////////////////////
	//
	public static String sGetFN ()
	{
		String sr = "" ;
		
		sr =  Thread.currentThread().getStackTrace()[2].getMethodName() ;
		
		return sr ;
	}
}
