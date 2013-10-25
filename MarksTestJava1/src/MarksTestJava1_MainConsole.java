
public class MarksTestJava1_MainConsole {
	public static void main(String[] args) throws Exception 
	{
		//HelloWorld () ;
		
		/*
		//TraceUtils.test1 () ;
		FileUtils.test1();  
		//TraceUtils.TestStackTrace () ;
		 
		 */
		
		AppState.initAppDetails(MarksTestJava1_MainConsole.class); //call this before anything!
		
		String sFN = TraceUtils.sGetFN () ;
		AppLog.Log(sFN + " initialized");
		
		// TODO Auto-generated method stub

		//StdioLogger.Log("test");
	
		
		MarksTestJava1_AppSettings settings = new MarksTestJava1_AppSettings () ;
		settings.ReadSettingsFromIni();
		settings.WriteSettingsToIni();  //write back so a stub .ini exists
		
		settings.CheckMe();
		
		JavaTests.StringTests();
		JavaTests.CollectionTests();
		JavaTests.FileTests();
		
		CryptUtils.test1();
		
		AppLog.Log(sFN + " exited");
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
