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
import javax.swing.JPasswordField;


public class JSMTP_ChannelCheck_ConfigDlg extends JDialog 
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		Boolean b = false ;
		try {
			
			JSMTP_ChannelCheck_ConfigDlg dlg = new JSMTP_ChannelCheck_ConfigDlg () ; 
			b = dlg.bShowDialog () ;
			
			//String sStrEntered = sEnterStr ("", "Enter Text", null) ;
			//ProgUtils.MsgBox("String entered is " + sStrEntered);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			ProgUtils.MsgBox(sw.toString()); // stack trace as a string			
		}
	}
	///////////////////////////////////////////////////
	//
	public Boolean bShowDialog ()
	{
		Boolean br = false ;
		
		String sTitle = "" ;
		JFrame jfParent = null ;
		try {
			
			//JSMTP_ChannelCheck_ConfigDlg dialog = new JSMTP_ChannelCheck_ConfigDlg() ;
			JSMTP_ChannelCheck_ConfigDlg dialog = this ;
			//dialog.m_sText = sStartingVal ;
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
				br = true ;
			}
			else
			{
				//Dialog canceled
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return br ;	
	}
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2579811788237503214L;
	
	public boolean m_br = false ; //true if Ok pressed with valid data.

	public void DataToControls ()
	{
		tfSmtpServer.setText(this.m_sSmtpServer);
		textFromAddress.setText(this.m_sFromAddress);
		this.textToAddress.setText(this.m_sToAddress);
		this.textDurationMinutes.setText(Integer.toString(m_iDurationMins));
	}
	public void ControlsToData ()
	{
		m_sSmtpServer = tfSmtpServer.getText();
		this.m_sFromAddress = textFromAddress.getText();
		this.m_sToAddress = this.textToAddress.getText();
		
		m_iDurationMins = Integer.parseInt(textDurationMinutes.getText());
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
	private JTextField tfSmtpServer;
	public String m_sSmtpServer = "smtp.gmail.com" ;
	private JTextField textFromAddress ;
	public String m_sFromAddress = "m.sw2005@gmail.com" ;
	private JTextField textToAddress;
	public String m_sToAddress = "m.sw2005@gmail.com" ;
	private JTextField textDurationMinutes;
	public int m_iDurationMins = 60 ;
	private JPasswordField pfPassword;
	public String m_sPassword = "" ;

	/**
	 * Create the dialog.
	 */
	public JSMTP_ChannelCheck_ConfigDlg() 
	{
		setTitle("SMTP Beacon Configuration");
		setBounds(100, 100, 479, 375);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblSmtpServer = new JLabel("SMTP Server:");
		lblSmtpServer.setBounds(10, 41, 91, 14);
		contentPanel.add(lblSmtpServer);
		
		tfSmtpServer = new JTextField();
		tfSmtpServer.setBounds(143, 41, 310, 20);
		contentPanel.add(tfSmtpServer);
		tfSmtpServer.setColumns(10);
		
		JLabel lblFrom = new JLabel("From Address:");
		lblFrom.setBounds(10, 69, 91, 14);
		contentPanel.add(lblFrom);
		
		textFromAddress = new JTextField();
		textFromAddress.setColumns(10);
		textFromAddress.setBounds(143, 69, 310, 20);
		contentPanel.add(textFromAddress);
		
		JLabel lblToAddress = new JLabel("To Address:");
		lblToAddress.setBounds(10, 139, 91, 14);
		contentPanel.add(lblToAddress);
		
		textToAddress = new JTextField();
		textToAddress.setColumns(10);
		textToAddress.setBounds(143, 139, 310, 20);
		contentPanel.add(textToAddress);
		
		JLabel lblDurationminutes = new JLabel("Duration (minutes):");
		lblDurationminutes.setBounds(10, 175, 123, 14);
		contentPanel.add(lblDurationminutes);
		
		textDurationMinutes = new JTextField();
		textDurationMinutes.setColumns(10);
		textDurationMinutes.setBounds(143, 170, 65, 20);
		contentPanel.add(textDurationMinutes);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 97, 91, 14);
		contentPanel.add(lblPassword);
		
		pfPassword = new JPasswordField();
		pfPassword.setBounds(143, 100, 310, 20);
		contentPanel.add(pfPassword);
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
