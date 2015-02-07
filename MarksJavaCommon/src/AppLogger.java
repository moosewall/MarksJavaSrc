import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/////////////////////////////
//Simple abstract logger class that can be used as
//a parameter to various library objects.
//
public abstract class AppLogger {
	
	/////////////////////////
	//Over-ride this
	//
	abstract void Log (String sLog) ;

	static public class TrcAppLogger extends AppLogger {
		
		@Override
		public void Log (String sLog)
		{
			TraceUtils.Trc(sLog);
		}
	}
	
}

