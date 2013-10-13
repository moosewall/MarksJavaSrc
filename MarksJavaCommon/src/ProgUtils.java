
import javax.swing.JOptionPane ;

public class ProgUtils {
	
	static public void MsgBox (String sMsg)
	{
		//http://stackoverflow.com/questions/7080205/popup-message-boxes
		JOptionPane.showMessageDialog(null, sMsg, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
	}

}
