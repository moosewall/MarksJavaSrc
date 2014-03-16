
import microsoft.exchange.webservices.*;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;

/*
 MW, 3/15/2014, attempting to use the EWS API.
 
 
 */
public class MarksEwsTest1_ConsoleApp {
	public static void main(String[] args) throws Exception 
	{
		//HelloWorld () ;
		
		/*
		//TraceUtils.test1 () ;
		FileUtils.test1();  
		//TraceUtils.TestStackTrace () ;
		 
		 */
		
		AppState.initAppDetails(MarksEwsTest1_ConsoleApp.class); //call this before anything!
		
		String sFN = TraceUtils.sGetFN () ;
		AppLog.Log(sFN + " initialized");
		
		// TODO Auto-generated method stub

		//StdioLogger.Log("test");
	
		/*
		MarksTestJava1_AppSettings settings = new MarksTestJava1_AppSettings () ;
		settings.ReadSettingsFromIni();
		settings.WriteSettingsToIni();  //write back so a stub .ini exists
		
		settings.CheckMe();
		
		JavaTests.StringTests();
		JavaTests.CollectionTests();
		
		CryptUtils.test1();
		*/
		/*
		 * JavaTests.FileTests();
		 */
		
		TestEws () ;
		
		AppLog.Log(sFN + " exited");
	}
	/////////////////////////////////////////
	//3/15/2014
	//
	public static void TestEws ()
	{
		String sFN = TraceUtils.sGetFN () ;

		String sLog = "" ;
		try
		{
			ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
			
			//String sExchangeUrl = "https://webmail.east.nmci.navy.mil/owa/" ;
			String sAutoUrl = "mark.wallace@DMDS.WS" ;
			service.autodiscoverUrl(sAutoUrl) ;
		}
		catch (Exception exp)
		{
			sLog = sFN + " exception " + exp.getMessage() ;
			AppLog.Log(sLog);
		}
	}
	public static void HelloWorld ()
	{
		System.out.println("Hello World!");
	}
	/*
	////////////////////////////
	//
	public static void Init ()
	{
		String sFN = TraceUtils.sGetFN() ;
		String sLog = "" ;
		
		FileUtils.m_sMainClassDir = sGetClassDir () ;
		FileUtils.m_sMainClassName = sGetClassName () ;
		
		sLog = sFN + "Class " + FileUtils.m_sMainClassName + " in " + FileUtils.m_sMainClassDir + " loading..." ;
		TraceUtils.Trc(sLog);
	}
	/////////////////////////////
	//
	public static String sGetClassDir ()
	{
		String sr = "" ;
		
		sr = MainConsole.class.getProtectionDomain().getCodeSource().getLocation().getPath() ;
		
		return sr ;
	}
	/////////////////////////////
	//
	public static String sGetClassName ()
	{
		String sr = "" ;
		
		String sName = MainConsole.class.getName();
		sr = sName ;
		
		return sr ;
	}
	*/
}
