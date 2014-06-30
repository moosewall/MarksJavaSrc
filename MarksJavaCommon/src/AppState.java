/*
 * AppState class.  Manages application state information.  "Main" class should call 
 * "initAppDetails" at startup, e.g.
 
  public class MainConsole {
	public static void main(String[] args) 
	{
		AppState.initAppDetails(MainConsole.class); //call this before anything!

 */

import java.io.File;

public class AppState 
{
	
	public static class AppDetails
	{
		public String m_sMainClassDir = "" ;
		public String m_sMainClassName = "" ;
		
		public boolean m_bTraceOn = false ;
		public String m_sTraceFilePath = "" ;
		
		public String m_sLogFilePath = "" ;
		
		public String m_sIni = "" ;
	} ;
	
	public static AppDetails m_AppDetails = null  ;

	//////////////////////////////////////////////////////
	//Pass in the main class as startup to register.  e.g.
	/*
	 AppState.initAppDetails(MainConsole.class);
	 */
	//
	public static void initAppDetails (@SuppressWarnings("rawtypes") Class clsMain)
	{
		if (m_AppDetails != null)
		{
			return ;
		}
		m_AppDetails = new AppDetails()  ;
		m_AppDetails.m_sMainClassDir = sGetClassDir (clsMain) ;
		
		//ProgUtils.MsgBox("Class dir is " + m_AppDetails.m_sMainClassDir);
		
		m_AppDetails.m_sMainClassName = sGetClassName (clsMain) ;
		
		//ProgUtils.MsgBox("Class name is " + m_AppDetails.m_sMainClassName);
		
		m_AppDetails.m_sTraceFilePath = String.format("%s%s_trc.txt", 
				m_AppDetails.m_sMainClassDir,
				m_AppDetails.m_sMainClassName) ;
		
		m_AppDetails.m_sLogFilePath = String.format("%s%s_log.txt", 
				m_AppDetails.m_sMainClassDir,
				m_AppDetails.m_sMainClassName) ;
		
		m_AppDetails.m_sIni = String.format("%s%s.ini", 
				m_AppDetails.m_sMainClassDir,
				m_AppDetails.m_sMainClassName) ;

		//init system objects.
		TraceUtils.Init();
		AppLog.Init(); 
	}
	/////////////////////////////
	//
	public static String sGetClassDir (@SuppressWarnings("rawtypes") Class clsMain)
	{
		String sr = "" ;
		
		String sClassPath = clsMain.getProtectionDomain().getCodeSource().getLocation().getPath() ;
		
		File f = new File (sClassPath) ;
		if (f.isFile())
		{
			//ProgUtils.MsgBox("Parsing " + sClassPath);
			sr = FileUtils.sParseFilePath(sClassPath) ;
		}
		else
		{
			//MW, 6/30/14
			sr = f.getAbsolutePath() ;
			/*MW, 6/30/14
			sr = sClassPath ;
			*/
		}
		
		return sr ;
	}
	/////////////////////////////
	//
	public static String sGetClassName (@SuppressWarnings("rawtypes") Class clsMain)
	{
		String sr = "" ;
		
		String sName = clsMain.getName();
		sr = sName ;
		
		return sr ;
	}

}
