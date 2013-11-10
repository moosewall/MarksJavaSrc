/*
 * BufUtils.java
 *
 * Created on September 1, 2003, 1:53 PM
 */

import java.nio.* ;

/**
 *
 * @author  Mark Wallace
 */
public class BufUtils {
    
    /** Creates a new instance of BufUtils */
    public BufUtils() 
    {
    }
    ////////////////////////////////////////////////
    //
    public static boolean bStrIsEmpty (String s)
    {
    	if (s == null)
    	{
    		return true ;
    	}
    	if (s.isEmpty())
    	{
    		return true ;
    	}
    	return false ;
    }
    ////////////////////////////////////////////////
    //
    public static String sGetEol ()
    {
    	//One way
    	//String sEol = System.getProperty("line.separator") ;
    	
    	//Platform independent way per http://stackoverflow.com/questions/207947/java-how-do-i-get-a-platform-independent-new-line-character
    	String sEol = String.format("%n") ;
    	
    	return sEol ;
    }
    ////////////////////////////////////////////////
    //
    public static String AppendStr (String sStrToAppendTo, String sStrToAppend)
    {
    	if (sStrToAppend != "")
    	{
	    	if (sStrToAppendTo != "")
	    	{
	    		//http://stackoverflow.com/questions/207947/java-how-do-i-get-a-platform-independent-new-line-character
	    		sStrToAppendTo += "%n" ;
	    	}
	    	sStrToAppendTo += sStrToAppend ;
    	}
    	return sStrToAppendTo ;
    }

    /**
     *Replace all occurences of sOldText with sNewText in
     *sOrigTextImage ;
     *
     *Example:
     *
     String sBefore = "the quick brown fox is a cool fox" ;
     String sOld = "fox" ;
     String sNew = "cat" ;
     String sAfter ;
     sAfter = MyJavaCommon.MyBufUtils.BufUtils.sReplaceTextInBuf
      (sBefore, 
       sOld,
       sNew) ;

    */
    public static String sReplaceTextInBuf
        ( String sOrigTextImage,
          String sOldText,
          String sNewText )
    {
        String sFN = "ByteBuffer bReplaceTextInBuf" ;
        String sResult = new String ("") ;
        
        int iWork = sOrigTextImage.getBytes().length ;
        ByteBuffer bbWork = ByteBuffer.allocate (iWork) ;
        ByteBuffer bbResult ;
    
        //This is more or less to convert the string object
        //to a raw ByteBuffer.
        //
        System.arraycopy
            (sOrigTextImage.toString().getBytes(), 
             0, 
             bbWork.array(), 
             0, 
             iWork);
        
        //Do the ByteBuffer search and replace.
        //
        bbResult = bbReplaceTextInBuf
            ( bbWork,
              sOldText,
              sNewText ) ;
        
        //Convert resulting ByteBuffer to
        // a string for the caller.
        //
        sResult = new String (bbResult.array()) ;
        
        //Return that bad boy.
        return sResult ;
    } ;

    /**
     *replace every occurence of sOldText with sNewText
     *in the ByteBuffer provided.
     *
     *Use code from following but tweeked a bit to be
     *binary file image friendly:
     *
     **Searching and replacing strings in a text file
     *By Xiangyang Liu 
     *
     *http://www.codeproject.org/useritems/textrep.asp
     *
     */
    public static ByteBuffer bbReplaceTextInBuf
        ( ByteBuffer bbOrigImage,
          String sOldText,
          String sNewText )
    {
        String sFN = "ByteBuffer bReplaceTextInBuf" ;

        //ultimate result buf.
        //ibbresult ;
        int ibbresult_len = 0 ;
        ByteBuffer bbresult = ByteBuffer.allocate (ibbresult_len) ;
        
        int iNumChanges = 0 ;

        if (sOldText.compareTo (sNewText) == 0)
        {
            Log (sFN + " Error, sOldText can't be equal to sNewText") ;
            return bbresult ;
        } ;
        
        int iOutputChunkSz = bbOrigImage.capacity() * 2 ;
        int iOutputSz = iOutputChunkSz ;
        ByteBuffer bbOutput = ByteBuffer.allocate (iOutputSz) ;
        ByteBuffer bbTmp ;
        int iOutput = 0 ;

	if (iOutputChunkSz == 0)        
        {
            return bbresult ;
        } ;

        //StringBuffer strOutput = new StringBuffer(bbOrigImage.capacity() * 2);
        String strInput = new String(bbOrigImage.array());

        int iRawInputBufIndex;
        int iRawBufNumToCopy ;
        
        // find all instances of sOldText and replace it with sNewText
        int nPos = 0;
        while(true)
        {
                int nIndex = strInput.indexOf(sOldText,nPos);
                
                // if sOldText can no longer be found, then copy the rest of the input
                if(nIndex<0)
                {
                        //strOutput.append(strInput.substring(nPos));
                    
                        iRawInputBufIndex = nPos ;
                        iRawBufNumToCopy 
                            = (strInput.substring(nPos).length()) ;
                        
                        bbOutput = bbGrowBuffer 
                            ( bbOutput,
                              (iOutput + iRawBufNumToCopy)) ;
                        System.arraycopy
                            ( bbOrigImage.array(),
                              iRawInputBufIndex,
                              bbOutput.array (),
                              iOutput,
                              iRawBufNumToCopy ) ;
                        iOutput += iRawBufNumToCopy ;
                        break;
                }
                // otherwise, replace it with args[2] and continue
                else
                {
                        iNumChanges++ ;
                        
                        //Copy all the data upto the index of where the old
                        //text just found.
                        
                        //String buffer has 2 bytes for each char.
                        iRawInputBufIndex = nPos;
                        iRawBufNumToCopy 
                            = (strInput.substring(nPos,nIndex).length()) ;
                        bbOutput = bbGrowBuffer 
                            ( bbOutput,
                              (iOutput + iRawBufNumToCopy)) ;
                        System.arraycopy
                            ( bbOrigImage.array(),
                              iRawInputBufIndex,
                              bbOutput.array (),
                              iOutput,
                              iRawBufNumToCopy ) ;
                        iOutput += iRawBufNumToCopy ;
                        
                        //Lay in the new text over where the old text would be.
                        iRawBufNumToCopy = (sNewText.length()) ;
                        bbOutput = bbGrowBuffer 
                            ( bbOutput,
                              (iOutput + iRawBufNumToCopy)) ;
                        
                        System.arraycopy
                            ( sNewText.getBytes(),
                              0,
                              bbOutput.array (),
                              iOutput,
                              iRawBufNumToCopy) ;
                        iOutput += iRawBufNumToCopy ;
                        
                        //Adjust master index.    
                        nPos = nIndex+sOldText.length();
                }
        }

        //copy working buffer into the callers.
        ibbresult_len = iOutput ;
        bbresult = ByteBuffer.allocate(ibbresult_len) ;
        System.arraycopy
            (bbOutput.array (), 
             0, 
             bbresult.array(), 
             0, 
             ibbresult_len);
        if (iNumChanges == 0)
        {
            //compare new buf with starting one, should be the
            //same.
            if (bbresult.compareTo(bbOrigImage) != 0)
            {
                //This is a p
                Log (sFN + "  warning, no changes applied but result diff?") ;
            } ;
        } ;
        return bbresult ;
    } ;
    /**
     *Allocate more size for the provided ByteBuffer if needed.
     *Return the new ByteBuffer or the one past in if it doesn't
     *change.
     */
    public static ByteBuffer bbGrowBuffer 
        (ByteBuffer bbBuf,
         int iNeededSize)
    {
        int ibbresult_len = 0 ;
        ByteBuffer bbresult = ByteBuffer.allocate (ibbresult_len) ;
        
        //int iAddChunkSz = 10000 ;
        int iAddChunkSz = (iNeededSize + 1) ;
        
        ByteBuffer bbTmp ;
        
       //if ((iOutput + iRawBufNumToCopy) >= iOutputSz)
        if ((iNeededSize) > bbBuf.capacity())
        {
            //need more mem on end.

            //Allocate a new temp buf with 
            ibbresult_len = bbBuf.capacity() + iAddChunkSz ;
            bbresult = ByteBuffer.allocate (ibbresult_len) ;

            //ram the current buffer into the
            //new one.
            System.arraycopy
                (bbBuf.array (),
                 0,
                 bbresult.array (),
                 0,
                 //ibbresult_len) ;
                 bbBuf.capacity ()) ;
            return bbresult ;
        }
        else
        {
            //plenty of room, do nothing.
            return bbBuf;
        }
    } ;
         
    /**
     *replace every occurence of sOldText with sNewText
     *in the ByteBuffer provided.
     *
     *Use code from:
     *
     **Searching and replacing strings in a text file
     *By Xiangyang Liu 
     *
     *http://www.codeproject.org/useritems/textrep.asp
     *
     */
    public static ByteBuffer bbReplaceTextInBuf_WorksJustStrings
        ( ByteBuffer bbOrigImage,
          String sOldText,
          String sNewText )
    {
        String sFN = "ByteBuffer bReplaceTextInBuf" ;

        //ultimate result buf.
        //ibbresult ;
        int ibbresult_len = 0 ;
        ByteBuffer bbresult = ByteBuffer.allocate (ibbresult_len) ;
        
        int iNumChanges = 0 ;

        if (sOldText.compareTo (sNewText) == 0)
        {
            Log (sFN + " Error, sOldText can't be equal to sNewText") ;
            return bbresult ;
        } ;
        
        StringBuffer strOutput = new StringBuffer(bbOrigImage.capacity() * 2);
        String strInput = new String(bbOrigImage.array());
   
        // find all instances of sOldText and replace it with sNewText
        int nPos = 0;
        while(true)
        {
                int nIndex = strInput.indexOf(sOldText,nPos);
                
                // if sOldText can no longer be found, then copy the rest of the input
                if(nIndex<0)
                {
                        strOutput.append(strInput.substring(nPos));
                        break;
                }
                // otherwise, replace it with args[2] and continue
                else
                {
                        iNumChanges++ ;
                        strOutput.append(strInput.substring(nPos,nIndex));
                        strOutput.append(sNewText);
                        
                        nPos = nIndex+sOldText.length();
                }
        }

        ///////////////////////////////
        //return modified buffer to caller by
        //allocating a new one and copying the result into it.
        //
        /*
        if (iNumChanges > 0)
        {
            ibbresult_len = strOutput.toString().getBytes().length ;
            bbresult = ByteBuffer.allocate(ibbresult_len) ;
            System.arraycopy
                (strOutput.toString().getBytes(), 
                 0, 
                 bbresult.array(), 
                 0, 
                 ibbresult_len);
            return bbresult ;
        }
        else
        {
            //no changes, just return starting buffer.
            return bbOrigImage ;
        }
         */
        ///////////////////////////////
        
        ibbresult_len = strOutput.toString().getBytes().length ;
        bbresult = ByteBuffer.allocate(ibbresult_len) ;
        System.arraycopy
            (strOutput.toString().getBytes(), 
             0, 
             bbresult.array(), 
             0, 
             ibbresult_len);
        
        if (iNumChanges == 0)
        {
            //compare new buf with starting one, should be the
            //same.
            if (bbresult.compareTo(bbOrigImage) != 0)
            {
                //This is a p
                Log (sFN + "  warning, no changes applied but result diff?") ;
            } ;
        } ;
        return bbresult ;
        
    } ;
    
    /**
     *
     */
    public static void Log (String sLog)
    {
        StdioLogger.Log(sLog) ;
    }
}
