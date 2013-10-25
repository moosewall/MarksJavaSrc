
import java.io.File;

import javax.swing.JFileChooser ;

public class JWinUtils 
{
	
	//////////////////////////////
	//
	public static void main(String[] args) throws Exception 
	{
		test_sBrowseDlg () ;
	}
	//////////////////////////////
	//
	public static void test_sBrowseDlg()
	{
		String s = "" ;
		
		String sDirSel = sBrowseDlg ("c:\\tmp") ;
		if (sDirSel != "")
		{
			ProgUtils.MsgBox("Directory selected is " + sDirSel) ;
		}
		else
		{
			ProgUtils.MsgBox("No directory selected");
		}
	}
	//http://stackoverflow.com/questions/4779360/browse-for-folder-dialog/4779743#4779743
	//http://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
	//
	public static String sBrowseDlg (String sStartingDir)
	{
		String sFN = TraceUtils.sGetFN() ;
		String sr = "" ;
		
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		

		File fStartDir = new File (sStartingDir) ;
		chooser.setCurrentDirectory (fStartDir) ; //http://stackoverflow.com/questions/7780892/set-the-jfilechooser-to-open-current-directory
		 
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            chooser.getSelectedFile().getName());
	       
	       sr = chooser.getSelectedFile().getAbsolutePath() ;
	    }
	    
	    return sr ;
	}

}
