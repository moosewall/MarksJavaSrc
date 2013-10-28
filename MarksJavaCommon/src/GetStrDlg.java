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


public class GetStrDlg extends JDialog 
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			String sStrEntered = sEnterStr ("", "Enter Text", null) ;
			ProgUtils.MsgBox("String entered is " + sStrEntered);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			ProgUtils.MsgBox(sw.toString()); // stack trace as a string			
		}
	}
	public static String sEnterStr (String sStartingVal)
	{
		return sEnterStr (sStartingVal, "", null) ;
	}
	public static String sEnterStr ()
	{
		return sEnterStr ("", "", null) ;
	}
	public static String sEnterStr (String sStartingVal, String sTitle, JFrame jfParent)
	{
		String sr = "" ;
		
		try {
			
			GetStrDlg dialog = new GetStrDlg() ;
			dialog.m_sText = sStartingVal ;
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
				//Dialog OKed, result is dialog.m_sText
				sr = dialog.m_sText ;
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
	
	public String m_sText = "" ;
	public boolean m_br = false ; //true if Ok pressed with valid data.

	public void DataToControls ()
	{
		tfText.setText(m_sText);
	}
	public void ControlsToData ()
	{
		m_sText = tfText.getText() ;
	}
	public String sCheckData ()
	{
		String sErrMsgs = "" ;
		
		String s = "" ;
		
		/*
		////////////
		boolean bDirIsValid = false ;
		if (m_sText != null && m_sText != "")
		{
			File fDir = new File (m_sText) ;
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
		*/
		
		return sErrMsgs ;
	}

	private final JPanel contentPanel = new JPanel();
	private JTextField tfText;

	/**
	 * Create the dialog.
	 */
	public GetStrDlg() 
	{
		setTitle("Enter Text");
		setBounds(100, 100, 450, 164);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblText = new JLabel("Text:");
		lblText.setBounds(10, 41, 91, 14);
		contentPanel.add(lblText);
		
		tfText = new JTextField();
		tfText.setBounds(104, 38, 320, 20);
		contentPanel.add(tfText);
		tfText.setColumns(10);
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
