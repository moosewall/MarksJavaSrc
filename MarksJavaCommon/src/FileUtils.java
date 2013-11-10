
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.net.URL ;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.nio.file.SimpleFileVisitor;

public class FileUtils 
{
	////////////////////////////////////////////////////
	//
	public static void main(String[] args) throws Exception 
	{
		test_CopyDirOrFile () ;
		//test_bbDumpFileIntoByteBuffer () ;
		//test_ReplaceTextInFiles() ;
		//test_RenameDirs () ;
	}
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
		public boolean m_bIsDirectory = false ;
		public FileSum (String sFilePath)
		{
			m_sFilePath = sFilePath ;
			
			File f = new File (sFilePath) ;
			
			m_bIsDirectory = f.isDirectory() ;
			
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
	public static boolean bIsDirectory (String sPath)
	{
		FileSum fs = new FileSum (sPath) ;
		return fs.m_bIsDirectory ;
	}
	////////////////////////////////
	//
	public static boolean bDumpStrIntoFile 
		(String sFilePath,
		 String sStrToWrite)
	{
		String sFN = TraceUtils.sGetFN() ;
		boolean br = false ;
		
		String s = "" ;
		
		try
		{
			FileWriter fw = null ;
			try
			{
				fw = new FileWriter (sFilePath) ;
				fw.write(sStrToWrite);
			}
			catch (Exception expWrite)
			{
				s = sFN + " exception " + expWrite.getMessage() ;
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
	public static boolean bFileExists (String sFilePath)
	{
		File f = new File (sFilePath) ;
		if (f.exists())
		{
			return true ;
		}
		return false ;
	}
	////////////////////////////////
	//
	public static String sDumpFileIntoStr (String sFilePath)
	{
		String sFN = TraceUtils.sGetFN() ;
		String sr = "" ;
		
		//Read file into a ByteBuffer, then convert it to a string.
		ByteBuffer bb = null;
		bb = bbDumpFileIntoByteBuffer (sFilePath) ;
		if (bb != null)
		{
			sr = new String (bb.array()) ;
		}
		return sr ;
		

		/*Another way to read a text file.
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
				
				//readLine is a bit quirky :
				//it returns the content of a line MINUS the newline.
				//it returns null only for the END of the stream.
				//it returns an empty String if two newlines appear in a row.
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
	    */
	}
	////////////////////////////////
	//
	//research:
	//http://www.java2s.com/Code/Java/File-Input-Output/UseNIOtoreadatextfile.htm
	//
	public static void test_bbDumpFileIntoByteBuffer ()
	{
		String sFN = TraceUtils.sGetFN() ;
	
		String sDir = "c:\\tmp\\" ;
		String sFile = sDir + "testfile.txt" ;
		ByteBuffer bb = bbDumpFileIntoByteBuffer (sFile) ;
		String s = sDumpFileIntoStr (sFile) ;
	}
	public static ByteBuffer bbDumpFileIntoByteBuffer (String sFilePath)
	{
		String sFN = TraceUtils.sGetFN() ;
		ByteBuffer bbr = null ;
		
		String sLog = "" ;
		
		try 
		{
			FileInputStream fIn = null;
			FileChannel fChan = null;
			long fSize = 0;
			ByteBuffer mBuf = null;
			    
			try
			{
				fIn = new FileInputStream(sFilePath);
			    fChan = fIn.getChannel();
			    fSize = fChan.size();
			    mBuf = ByteBuffer.allocate((int) fSize);
			    fChan.read(mBuf);
			    mBuf.rewind();
			    bbr = mBuf ;
			}
			catch (Exception expReading)
			{
				sLog = String.format("%s, exception reading %s.  %s", 
						sFN,
						sFilePath,
						expReading.getMessage()) ;
				TraceUtils.Trc(sLog);
			}
			finally 
			{
				if (fIn != null)
				{
					fIn.close();
					fIn = null ;
				}
			}
		}
		catch (IOException ex)
		{
		  ex.printStackTrace();
		}
		return bbr ;
	}
	/////////////////////////////////////////
	//
	/*
	 * research:
	 * http://codingjunkie.net/java-7-copy-move/
	 * http://www.mkyong.com/java/how-to-copy-directory-in-java/
	 */
	/*FileUtils.cpp way
	/////////////////////////////////////////////////////////////////
	//Routine to copy an exact image of the specified source
	//directory to the specified destination dir.
	//
	BOOL bCopyDir 
		(const char *lpszSrcDir,
		 const char *lpszDestDir,
		 CStringList *pLogs)
	{
		Trace ("\n::bCopyDir ") ;
		BOOL bresult = TRUE ;
	
		CString csSrcDir = lpszSrcDir ;
		CString csDestDir = lpszDestDir ;
		POSITION pos ;
		CStringList cslDirFiles ;
		CString csMask ;
		CString csFile, csFileUpper, csFileTrim ;
		CString cs ;
	
		CString csNew ;
	
		//GetFileListRecursive will copy in order for easy copying
		csMask.Format ("%s\\*.*", csSrcDir) ;
		GetFileListRecursive
			( csMask,
			  cslDirFiles ) ;
		if (cslDirFiles.GetCount() < 1)
		{
			Trace ("\n  not files in %s", csSrcDir) ;
			bresult = FALSE ;
			goto exit ;
		} ;
	
		pos = cslDirFiles.GetHeadPosition () ;
		while (pos)
		{
			csFile = cslDirFiles.GetNext (pos) ;
			csFileUpper = csFile ;
			csFileUpper.MakeUpper () ;
	
			//Make a copy of the file name without root dir
			csFileTrim = csFile.Mid (csSrcDir.GetLength()) ;
	
			csNew.Format ("%s%s",
				csDestDir,
				csFileTrim) ;
			if (bIsDirectory (csFile))
			{
				//create a new subdir in dest.
				if (!CreateDirectory (csNew, NULL))
				{
					cs.Format ("Error creating directory %s", csNew) ;
					Trace ("\n  %s", cs) ;
					if (pLogs)
					{
						pLogs->AddTail (cs) ;
					} ;
					bresult = FALSE ;
				} ;
			}
			else
			{
				//just copy the file.
				bresult = bCopyFile
					( csFile.GetBuffer(0),
					  csNew.GetBuffer(0)) ;
				if (!bresult)
				{
					//Trace ("\n  Error copying %s to %s", csFile, csNew) ;
					//goto exit ;
					cs.Format ("Error copying %s to %s",
						csFile,
						csNew) ;
					if (pLogs)
					{
						pLogs->AddTail (cs) ;
					} ;
					bresult = FALSE ;
				} ;
			} ;
		} ;
	
	exit: ;
		return bresult ;
	} ;
	*/
	public static void test_CopyDirOrFile ()
	{
		String sSrc = "C:\\tmp\\__NICE_site_Data" ;
		String sDest = "C:\\tmp\\__NICE_site_Data_COPYTEST" ;
		try 
		{
			CopyDirOrFile (sSrc, sDest) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void CopyDirOrFile
		(String sSrc,
		 String sDest) throws Exception
	{
		String sFN = TraceUtils.sGetFN() ;
		
		String sSep = File.separator ; 
		
		File fSrc = new File (sSrc) ;
		if (fSrc.isDirectory())
		{
			String sSrcDir = fSrc.getPath() ;
			
			File fDest = new File (sDest) ;
			if (!fDest.exists())
			{
				fDest.mkdir() ;
			}
			String sDestDir = fDest.getPath() ;
			
			String [] saFiles = fSrc.list() ;
			for (int iSrcFile = 0; iSrcFile<saFiles.length; iSrcFile++)
			{
				String sSrcFileName = saFiles[iSrcFile] ;
				String sSrcFilePath = sSrcDir + sSep + sSrcFileName ;
				String sDestFilePath = sDestDir + sSep + sSrcFileName ;
				CopyDirOrFile (sSrcFilePath, sDestFilePath) ;
			}
		}
		else
		{
			//is a file, copy it.
			CopyFile (sSrc, sDest) ;
		}
	}
	//////////////////////////////////////
	//
	//
	//research:
	//http://www.mkyong.com/java/how-to-copy-directory-in-java/
	//
	public static void CopyFile
		(String sSrcFile,
		 String sDestFile)  throws Exception
	{
		InputStream in = new FileInputStream(sSrcFile);
        OutputStream out = new FileOutputStream(sDestFile); 

        byte[] buffer = new byte[1024];

        int length;
        
        //copy the file content in bytes 
        while ((length = in.read(buffer)) > 0)
        {
    	   out.write(buffer, 0, length);
        }

        in.close();
        out.close();
	}
	//////////////////////////////////////////
	//
	/*FileUtils.cpp reference
	/////////////////////////////////////////////////////////////////////////////////
	//Do a case insensitive replace of specified substring in the
	//specified file list.
	//
	BOOL bReplaceTextInFiles
		( CStringList &cslFiles,
		  const char *pOldNamePrefix, 
		  const char *pNewNamePrefix)
	{
		Trace ("\nCMainWorkerThread::bReplaceTextInFiles, %s, %s",
			pOldNamePrefix, pNewNamePrefix) ;
	
		BOOL bresult = TRUE ;
		POSITION pos ;
		BOOL bLineChange ;
		CString csLog ;
	
		CString csOrigImage, csNewImage ;
	
		CString csFileName;
		pos = cslFiles.GetHeadPosition () ;
		while (pos)
		{
			csFileName = cslFiles.GetNext (pos) ;
	
			if (bIsDirectory (csFileName))
			{
				continue ;
			} ;
	
			Trace ("\n  opening for parse->%s", csFileName) ;
			if (!bDumpFileIntoBuffer
				( csFileName.GetBuffer(0),
				  csOrigImage ))
			{
				csLog.Format ("  Error opening %s",
					csFileName) ;
				Trace ("\n  %s", csLog) ;
				continue ;
			} ;
	
			Trace ("\n  %s opening, continue", csFileName) ;
	
			bLineChange = bReplaceTextInBuf
				( csOrigImage,
				  pOldNamePrefix,
				  pNewNamePrefix,
				  csNewImage ) ;
	
			if (bLineChange)
			{
				//Write out new file image.
				csLog.Format ("updating contents of %s",
					csFileName) ;
				Trace ("\n  %s", csLog) ;
	
				if (!bDumpBufToFile
					( (UCHAR*)csNewImage.GetBuffer(0),
					  csNewImage.GetLength (),
					  csFileName.GetBuffer(0)))
				{
					csLog.Format ("Error replacing strings in %s",
						csFileName) ;
					Trace ("\n  %s", csLog) ;
				} ;
			} ;
	
		} ;//while iterating file list
	
		Trace ("\n  returning %d", bresult) ;
		return bresult ;
	} ;
	 */
	public static void test_ReplaceTextInFiles ()
	{
		List<String>lstFiles = new ArrayList<String>() ;
		
		String sDir = "c:\\tmp\\" ;
		lstFiles.add(sDir + "JReplaceTextTest.txt") ;
		try 
		{
			ReplaceTextInFiles (lstFiles, "before", "after mark was here") ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void RenameFilesInDir
		(String sDir,
		 String sOldText,
		 String sNewText) throws Exception
	{
		String sFN = TraceUtils.sGetFN() ;
		
		String s = "" ;
		
		String sSep = File.separator ;
		
		String sDirMask = sDir + sSep + "*.*" ;
		List<String>lstFiles = FileGetter.lstGetFileListRecursive(sDirMask) ;
		
		for (int iFile=0; iFile<lstFiles.size(); iFile++)
		{
			String sFilePath = lstFiles.get(iFile) ;
			File fFilePath = new File (sFilePath) ;
			
			String sFileDir = sParseFilePath (sFilePath) ;
			String sFileName = sParseFileName (sFilePath) ;
			String sNewFileName = BufUtils.sReplaceTextInBuf(sFileName, sOldText, sNewText) ;
			if (!sNewFileName.equals(sFileName))
			{
				String sNewFilePath = sFileDir + sSep + sNewFileName ;
				File fNewFilePath = new File (sNewFilePath) ;
				if (!fNewFilePath.exists())
				{
					fFilePath.renameTo(fNewFilePath) ;
				}
				else
				{
					s = sFN + " error rename to file, already exists " + sNewFilePath ;
					throw new Exception (s) ;
				}
			}
		}
	}
	public static void ReplaceTextInFilesInDir
		(String sDir,
		 String sOldText,
		 String sNewText) throws Exception
	{
		String sFN = TraceUtils.sGetFN() ;
		
		String s = "" ;
		
		String sSep = File.separator ;
		
		String sDirMask = sDir + sSep + "*.*" ;
		List<String>lstFiles = FileGetter.lstGetFileListRecursive(sDirMask) ;
		
		for (int iFile=0; iFile<lstFiles.size(); iFile++)
		{
			String sFilePath = lstFiles.get(iFile) ;
			File fFilePath = new File (sFilePath) ;
			if (fFilePath.isDirectory())
			{
				continue ;
			}
			String sFileImage = sDumpFileIntoStr (sFilePath) ;
			String sNewFileImage = BufUtils.sReplaceTextInBuf(sFileImage, sOldText, sNewText) ;
			if (!sFileImage.equals(sNewFileImage))
			{
				bDumpStrIntoFile (sFilePath, sNewFileImage) ;
			}
		}
	}
	public static void ReplaceTextInFiles
		(List<String>lstFiles,
		 String sOldText,
		 String sNewText) throws Exception
	{
		String sFN = TraceUtils.sGetFN() ;
		
		boolean b = false ;
		
		for (int iFile=0; iFile<lstFiles.size(); iFile++)
		{
			String sCurFilePath = lstFiles.get(iFile);
			
			b = bIsDirectory (sCurFilePath) ;
			if (b)
			{
				continue ; //directory, skip
			}
			
			String sFileImage = sDumpFileIntoStr (sCurFilePath) ;
			if (sFileImage == "")
			{
				continue ; //empty file, skip
			}
			String sNewFileImage = BufUtils.sReplaceTextInBuf
				(sFileImage,
				 sOldText,
				 sNewText) ;
			if (sFileImage != sNewFileImage)
			{
				//file image changed, write it out.
				bDumpStrIntoFile (sCurFilePath, sNewFileImage) ;
			}
			else
			{
				//no change, nothing to do.
			}
		}
	}
	//////////////////////////////////////////////////////
	//
	/*FileUtils.cpp reference
	/////////////////////////////////////////////////////////////////////////////////
	//
	BOOL bRenameDirs
		( CStringList &cslFileNames, 
		  const char *pOldNamePrefix, 
		  const char *pNewNamePrefix)
	{
		BOOL bresult = TRUE ;
	
		BOOL bNameChanged = FALSE ;
	
		CString csDir;
	
		CString csFileName, csNewFileName, csFullFileName, csNewFullFileName ;
		POSITION pos, LastPos ;
		CString csLog ;
	
		//Now is the hard part.  Open each file
		//in the directory and replace project name text
		//with new text.
		pos = cslFileNames.GetHeadPosition () ;
		while (pos)
		{
			LastPos = pos ;
			csFullFileName = cslFileNames.GetNext (pos) ;
			//csFullFileName.MakeUpper () ;
	
	
			csFileName = csParseFileName (csFullFileName.GetBuffer(0)) ;
			csDir = csParseFilePath (csFullFileName.GetBuffer(0)) ;
	
			Trace ("\n  checking for rename: %s", csFileName) ;
	
			bNameChanged = bReplaceTextInBuf
				( csFileName,
				  pOldNamePrefix,
				  pNewNamePrefix,
				  csNewFileName ) ;
			if (bNameChanged)
			{
				Trace ("\n  Renaming...") ;
				TRY
				{
					csLog.Format ("Renaming %s to %s",
						csFileName, csNewFileName) ;
	
					csNewFullFileName.Format ("%s\\%s",
						csDir, csNewFileName) ;
	
					if (bIsDirectory (csFullFileName))
					{
						if (rename (csFullFileName, csNewFullFileName) != 0)
						{
							Trace ("\n  error renaming directory.") ;
						} ;
	
						//Update the name in the list
						cslFileNames.SetAt (LastPos, csNewFullFileName) ;				
					} ;
	
				}
				CATCH( CFileException, e )
				{
					csLog.Format ("Error renaming %s to %s (%s)",
						csFileName, csNewFileName, e->m_cause) ;
					delete e ;
					continue ;
				}
				END_CATCH
	
			} ;
				  
		} ;//while iterating file list
	
	
		return bresult ;
	} ;
    */
	//
	public static void test_RenameDirs ()
	{
		List<String> lstDirs = new ArrayList<String> () ;
		lstDirs.add("C:\\tmp\\DirToRename") ;
		try 
		{
			RenameDirs (lstDirs, "DirToRename", "DirToRenameX") ;
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public static void RenameDirs
		(List<String>lstDirs,
		 String sOldNamePre,
		 String sNewNamePre) throws Exception
	 {
		String sFN = TraceUtils.sGetFN() ;
		
		boolean b = false ;
		String s = "" ;
		
		String sSep = File.separator ; 
		
		for (int iDir=0; iDir<lstDirs.size(); iDir++)
		{
			String sDir = lstDirs.get(iDir);
			
			b = bIsDirectory (sDir) ;
			if (!b)
			{
				continue ;
			}
			
			String sDirParent = sParseFilePath (sDir) ;
			String sDirName = sParseFileName (sDir) ;
			
			String sNewDirName = BufUtils.sReplaceTextInBuf
					(sDirName,
					 sOldNamePre, 
					 sNewNamePre) ;
			if (sNewDirName != sDirName)
			{
				
				File fDir = new File (sDir) ;
				
				String sNewDirPath = sDirParent + sSep + sNewDirName ;
				File fDirNew = new File (sNewDirPath) ;
				b = fDir.renameTo(fDirNew) ;
				if (!b)
				{
					s = String.format ("%s, Error renaming directory %s to %s",
							sFN,
							sDir,
							sNewDirPath) ;
					throw new Exception (s) ;
				}
			}
		}
	 }
	
}
