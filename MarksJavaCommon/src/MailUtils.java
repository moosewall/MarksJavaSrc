
/*
 * Research:
 * 
 * https://javamail.java.net/nonav/docs/api/
 * 
 * http://www.javaworld.com/jw-06-1999/jw-06-javamail.html
 * 
 * 
 */
public class MailUtils 
{

	public static void test1 ()
	{
		String sFN = TraceUtils.sGetFN() ;
		
		AppLog.Log(sFN + " entered");
		
		AppLog.Log(sFN + " exited");
	}
}
