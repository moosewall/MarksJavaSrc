
import java.util.* ;
import java.io.*;

public class JavaTests 
{
	static void Trc (String sTrc)
	{
		TraceUtils.Trc(sTrc);
	}
	static void Log (String sLog)
	{
		AppLog.Log(sLog);
	}
	/////////////////////
	//
	static void StringTests ()
	{
		String sFN = TraceUtils.sGetFN () ;
		Log (sFN + "entered") ;
		
		int i = -1 ;
		String s = "" ;
		
		String s1 = "the quick brown fox jump over the lazy dog" ;
		
		String sWanted = "fox" ;
		i = s1.indexOf(sWanted) ;
		if (i >= 0)
		{
			s = s1.substring(i, i + sWanted.length()) ;
			Log (sFN + " extracted string " + s) ;
		}
		
		String s1Upper = s1.toUpperCase() ;
		Log (sFN + " converted [" + s1 + "] to upper [" + s1Upper + "]") ;
		
		String sReplaceWith = "dog" ;
		String sReplaced = s1.replace(sWanted, sReplaceWith) ;
		Log (sFN + " replaced " + sReplaced) ;

		char cPos1 = s1.charAt(0) ;
		Log (sFN + " char at first pos is " + cPos1) ;
		
		Log (sFN + "exited") ;
	}
	////////////////////////////
	//
	static class CollectionTestsClass
	{
		public String m_sData1 = "data1" ;
		public int m_iData2 = 2 ;
	}
	static void CollectionTests ()
	{
		String sFN = TraceUtils.sGetFN () ;
		Log (sFN + "entered") ;
		
		String s = "" ;
		int i = -1 ;

		//http://docs.oracle.com/javase/tutorial/collections/
		
		//http://stackoverflow.com/questions/858572/how-to-make-a-new-list-in-java
		List<String> lstStrs = new ArrayList <String>() ;
		lstStrs.add("string1") ;
		lstStrs.add("string2") ;
		s = lstStrs.get(0) ;
		
		List<CollectionTestsClass>lstCls = new ArrayList<CollectionTestsClass>() ;
		CollectionTestsClass cls = null ;
		cls = new CollectionTestsClass () ;
		cls.m_sData1 = "item1" ;
		lstCls.add(cls) ;

		cls = new CollectionTestsClass () ;
		cls.m_sData1 = "item2" ;
		lstCls.add(cls) ;
		
		cls = new CollectionTestsClass () ;
		cls.m_sData1 = "item3" ;
		lstCls.add(cls) ;
		
		cls = lstCls.get(0) ;
		
		Log (sFN + "exited") ;
	}
	static void FileTests ()
	{
		String sFN = TraceUtils.sGetFN () ;
		Log (sFN + "entered") ;

		String sWorkDir = AppState.m_AppDetails.m_sMainClassDir ;
		
		String sFileName1 = String.format("%s%s", 
				sWorkDir,
				"TestFile1.txt") ;

		String sOutImage = "the quick brown fox jumped over the lazy dog." ;
		
		FileUtils.bDumpStrIntoFile (sFileName1, sOutImage) ;
		String sInImage = FileUtils.sDumpFileIntoStr(sFileName1) ;
		
		
	}
}
