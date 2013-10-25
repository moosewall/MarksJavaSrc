
import java.io.*;
import java.nio.file.*;
import java.net.URL ;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.nio.file.SimpleFileVisitor;

public class FileUtils 
{
	////////////////////////////////
	//
	public static void test1 ()
	{
		String sFN = "FileUtils::test1()" ;
		String sLog = "" ;
		String s = "" ;
		
		
		s = FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() ;
		
	
		String sFilePath = "/c:/tmp/test1.txt" ;
		File f = new File (sFilePath) ;
		s = f.getParent() ;
		
		AppLog.Log(sFN + "Parsing file path " + sFilePath);
		String sFileName = sParseFileName (sFilePath) ;
		AppLog.Log(sFN + "File name is " + sFileName);
		
		String sFileDir = sParseFilePath (sFilePath) ;
		AppLog.Log(sFN + "File dir is " + sFileDir);
		
		String sFileExt = sParseFileExt (sFilePath) ;
	}
	///////////////////////////////
	//
	public static String sGetAppDir () 
	{
		String sr = "" ;
		try
		{
			sr = new File(".").getCanonicalPath();
		}
		catch (IOException ioe)
		{
			
		}
		finally
		{
		}
		
		return sr ;
	}
	public static class FileSum
	{
		public String m_sFilePath = "" ;
		
		public String m_sFileDir = "" ;
		public String m_sFileName = "" ;
		public String m_sFileNameNoExt = "" ;
		public String m_sFileExt = "" ;
		public FileSum (String sFilePath)
		{
			m_sFilePath = sFilePath ;
			
			File f = new File (sFilePath) ;
			
			/////
			//Make sure dir has path separator at end.
			m_sFileDir = f.getParent() ;
			if (m_sFileDir != "")
			{
				String sLastCharOfPath = m_sFileDir.substring(m_sFileDir.length()-1) ;
				if (sLastCharOfPath != File.separator)
				{
					m_sFileDir += File.separator ;
				}
			}
			///
		
			///
			m_sFileName = f.getName() ;
			if (m_sFileName != "")
			{
				String[] saFileName = m_sFileName.split("\\.(?=[^\\.]+$)");
				
				if (saFileName.length == 2)
				{
					m_sFileNameNoExt = saFileName[0] ;
					m_sFileExt = saFileName [1] ;
				}
			}
			
		}
	}
	////////////////////////////////
	//
	public static String sParseFileName (String sFilePath)
	{
		return sParseFileName (sFilePath, false) ;
	}
	public static String sParseFileName (String sFilePath, boolean bOmitExt)
	{
		String sr = "" ;
		
		FileSum fs = new FileSum (sFilePath) ;
		if (bOmitExt)
		{
			sr = fs.m_sFileNameNoExt ;
		}
		else
		{
			sr = fs.m_sFileName ;
		}
		return sr ;
	}
	//////////////////////////////////////
	//
	public static String sParseFileExt (String sFilePath)
	{
		FileSum fs = new FileSum (sFilePath) ;
		return fs.m_sFileExt ;
	}
	////////////////////////////////
	//
	public static String sParseFilePath (String sFilePath)
	{
		FileSum fs = new FileSum (sFilePath) ;
		return fs.m_sFileDir ;
	
	}
	////////////////////////////////
	//
	public static boolean bDumpStrIntoFile 
		(String sFilePath,
		 String sStrToWrite)
	{
		boolean br = false ;
		
		try
		{
			FileWriter fw = null ;
			try
			{
				fw = new FileWriter (sFilePath) ;
				fw.write(sStrToWrite);
			}
			finally
			{
				if (fw != null)
				{
					fw.close(); fw = null ;
				}
				br = true ;
			}
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		
		return br ;
	}
	////////////////////////////////
	//
	public static String sDumpFileIntoStr (String sFilePath)
	{
		//http://www.javapractices.com/topic/TopicAction.do?Id=42
		
		String sEol = BufUtils.sGetEol() ;
		
	    StringBuilder contents = new StringBuilder();
	    
		try 
		{
			//use buffering, reading one line at a time
			//FileReader always assumes default encoding is OK!
			FileReader fr = new FileReader (sFilePath) ;
			BufferedReader br =  new BufferedReader(fr);
			try
			{
				String sLine = null; //not declared within while loop
				/*
				* readLine is a bit quirky :
				* it returns the content of a line MINUS the newline.
				* it returns null only for the END of the stream.
				* it returns an empty String if two newlines appear in a row.
				*/
				while ((sLine = br.readLine()) != null)
				{
				  contents.append(sLine);
				  contents.append(sEol);
				}
			  }
			  finally 
			  {
				  br.close();
			  }
		}
		catch (IOException ex)
		{
		  ex.printStackTrace();
		}
	    return contents.toString();		
	}
	//////////////////////////////////////////////////
	//Routine to return a list of the files that
	//fit the specified mask e.g. *.*, *.tmp, etc.
	//Search is recursive so all subdirectories will
	//be search also.
	//
	//http://www.javapractices.com/topic/TopicAction.do?Id=68
	//http://stackoverflow.com/questions/794381/how-to-find-files-that-match-a-wildcard-string-in-java
	//
	public static List<String> lstGetFileListRecursive 
	 (String sFileMask,
	  List<String>lstFilesIn)
	{
		 List<String> lstFiles = new ArrayList<String>();
		 

		 
		 return lstFiles ;
	}
	
}
