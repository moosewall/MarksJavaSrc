import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;


public class GetDirDlg extends JDialog 
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			String sDirSel = sBrowseDir ("c:\\tmp", "put title here", null) ;
			if (sDirSel != "")
			{
				ProgUtils.MsgBox("Dir selected is " + sDirSel);
			}
			else
			{
				throw new Exception ("No directory selected") ;
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			ProgUtils.MsgBox(sw.toString()); // stack trace as a string			
		}
	}
	
	private static String m_sLastDir = "" ;
	public static String sBrowseDir (String sStartingDir)
	{
		return sBrowseDir (sStartingDir, "", null) ;
	}
	public static String sBrowseDir ()
	{
		return sBrowseDir ("", "", null) ;
	}
	public static String sBrowseDir (String sStartingDir, String sTitle, JFrame jfParent)
	{
		String sr = "" ;
		
		try {
			
			GetDirDlg dialog = new GetDirDlg() ;
			dialog.m_sDir = sStartingDir ;
			
			if (dialog.m_sDir.equals(""))
			{
				dialog.m_sDir = m_sLastDir ;
			}
			
			dialog.DataToControls();
			
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			
			if (sTitle != null && sTitle != "")
			{
				dialog.setTitle(sTitle);
			}
			
			if (jfParent != null)
			{
				dialog.setLocationRelativeTo(jfParent);
			}
			
			//show dialog modal.
			dialog.setModal(true);
			dialog.setVisible(true);
			if (dialog.m_br == true)
			{
				//Dialog OKed, result is dialog.m_sDir
				sr = dialog.m_sDir ;
				
				m_sLastDir = sr ;
			}
			else
			{
				//Dialog canceled
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sr ;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2579811788237503214L;
	
	public String m_sDir = "" ;
	public boolean m_br = false ; //true if Ok pressed with valid data.

	public void DataToControls ()
	{
		tfDirPath.setText(m_sDir);
	}
	public void ControlsToData ()
	{
		m_sDir = tfDirPath.getText() ;
	}
	public String sCheckData ()
	{
		String sErrMsgs = "" ;
		
		String s = "" ;
		
		////////////
		boolean bDirIsValid = false ;
		if (m_sDir != null && m_sDir != "")
		{
			File fDir = new File (m_sDir) ;
			if (fDir.isDirectory())
			{
				bDirIsValid = true ;
			}
		}
		if (!bDirIsValid)
		{
			s = "No valid directory selected" ;
			sErrMsgs = BufUtils.AppendStr(sErrMsgs,  s) ;
		}
		
		return sErrMsgs ;
	}

	private final JPanel contentPanel = new JPanel();
	private JTextField tfDirPath;

	/**
	 * Create the dialog.
	 */
	public GetDirDlg() 
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(GetDirDlg.class.getResource("/com/sun/java/swing/plaf/windows/icons/Directory.gif")));
		setTitle("Select Directory");
		setBounds(100, 100, 450, 164);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblDirectoryPath = new JLabel("Directory Path:");
		lblDirectoryPath.setBounds(10, 41, 91, 14);
		contentPanel.add(lblDirectoryPath);
		
		tfDirPath = new JTextField();
		tfDirPath.setBounds(104, 38, 246, 20);
		contentPanel.add(tfDirPath);
		tfDirPath.setColumns(10);
		
		JButton btnBrowseDir = new JButton("Browse");
		btnBrowseDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String sDirSel = JWinUtils.sBrowseDlg(m_sDir) ;
				if (sDirSel != "")
				{
					m_sDir = sDirSel ;
					DataToControls () ;
				}
				
			}
		});
		btnBrowseDir.setBounds(353, 37, 79, 23);
		contentPanel.add(btnBrowseDir);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						m_br = false ;
						
						ControlsToData () ;
						
						String sDataCheckMsgs = sCheckData () ;
						if (sDataCheckMsgs != "")
						{
							ProgUtils.MsgBox(sDataCheckMsgs);
							return ;
						}
						
						m_br = true ;
						setVisible (false) ;
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						m_br = false ;
						setVisible (false) ;
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	
	}
}
