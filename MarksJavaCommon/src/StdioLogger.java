/*
 * StdioLogger.java
 *
 * Created on September 1, 2003, 1:57 PM
 */

/**
 *
 * @author  Mark Wallace
 */
public class StdioLogger {
    
    /** Creates a new instance of StdioLogger */
    public StdioLogger() {
    }

    static public void Log (String sLog)
    {
        String sFormattedLog ;
        sFormattedLog = sLog ;
        sFormattedLog = sFormattedLog + "\n" ;
        //System.out.println("Hello World!");
        System.out.println (sFormattedLog) ;
    }
}
