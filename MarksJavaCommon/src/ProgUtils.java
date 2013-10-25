
import javax.swing.JFrame;
import javax.swing.JOptionPane ;

public class ProgUtils {

	static public void MsgBox (String sMsg)
	{
		MsgBox (sMsg, null) ;
	}
	static public void MsgBox (String sMsg, JFrame jfParent)
	{
		//http://stackoverflow.com/questions/7080205/popup-message-boxes
		JOptionPane.showMessageDialog(jfParent, sMsg, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
	}

}
