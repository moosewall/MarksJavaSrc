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
import javax.swing.JTextArea;


public class SendSMTPTest_ConfigDlg extends JDialog 
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//call this before anything so can trace, etc.
		AppState.initAppDetails(SendSMTPTest_ConfigDlg.class); 
		
		String sFN = TraceUtils.sGetFN () ;
		TraceUtils.Trc(sFN + " entered");
		
		Boolean b = false ;
		try {
			
			SendSMTPTest_ConfigDlg dlg = new SendSMTPTest_ConfigDlg () ; 
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
		
		TraceUtils.Trc(sFN + " exited");
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
			SendSMTPTest_ConfigDlg dialog = this ;
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
		FromAddress_textField.setText(this.m_sFromAddress);
		this.ToSmtpAddress_textfield.setText(this.m_sToAddress);
	}
	public void ControlsToData ()
	{
		m_sSmtpServer = tfSmtpServer.getText();
		this.m_sFromAddress = FromAddress_textField.getText();
		this.m_sToAddress = this.ToSmtpAddress_textfield.getText();
	
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
	private JTextField FromAddress_textField ;
	public String m_sFromAddress = "m.sw2005@gmail.com" ;
	private JTextField ToSmtpAddress_textfield;
	public String m_sToAddress = "m.sw2005@gmail.com" ;
	public int m_iDurationMins = 60 ;
	private JPasswordField pfPassword;
	public String m_sPassword = "" ;
	
	private JLabel lblSmtpServer ;
	private JTextField Subj_textfield;
	private JTextField ToName_textField;
	private JTextField FromName_textField;

	/**
	 * Create the dialog.
	 */
	public SendSMTPTest_ConfigDlg() 
	{
		setTitle("Configure SMTP Send");
		setBounds(100, 100, 479, 492);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblSmtpServer = new JLabel("SMTP Server:");
		lblSmtpServer.setBounds(10, 11, 91, 14);
		contentPanel.add(lblSmtpServer);
		
		tfSmtpServer = new JTextField();
		tfSmtpServer.setBounds(143, 11, 310, 20);
		contentPanel.add(tfSmtpServer);
		tfSmtpServer.setColumns(10);
		
		JLabel FromAddress_Label = new JLabel("From Address:");
		FromAddress_Label.setBounds(10, 108, 91, 14);
		contentPanel.add(FromAddress_Label);
		
		FromAddress_textField = new JTextField();
		FromAddress_textField.setColumns(10);
		FromAddress_textField.setBounds(143, 108, 310, 20);
		contentPanel.add(FromAddress_textField);
		
		JLabel ToSmtpAddressLabel = new JLabel("To SMTP Address:");
		ToSmtpAddressLabel.setBounds(10, 139, 91, 14);
		contentPanel.add(ToSmtpAddressLabel);
		
		ToSmtpAddress_textfield = new JTextField();
		ToSmtpAddress_textfield.setColumns(10);
		ToSmtpAddress_textfield.setBounds(143, 139, 310, 20);
		contentPanel.add(ToSmtpAddress_textfield);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 36, 91, 14);
		contentPanel.add(lblPassword);
		
		pfPassword = new JPasswordField();
		pfPassword.setBounds(143, 39, 310, 20);
		contentPanel.add(pfPassword);
		
		JLabel Subject_Label = new JLabel("Subject:");
		Subject_Label.setBounds(10, 213, 91, 14);
		contentPanel.add(Subject_Label);
		
		Subj_textfield = new JTextField();
		Subj_textfield.setColumns(10);
		Subj_textfield.setBounds(143, 210, 310, 20);
		contentPanel.add(Subj_textfield);
		
		JLabel ToSmtpNameLabel = new JLabel("To SMTP Name:");
		ToSmtpNameLabel.setBounds(10, 170, 91, 14);
		contentPanel.add(ToSmtpNameLabel);
		
		ToName_textField = new JTextField();
		ToName_textField.setColumns(10);
		ToName_textField.setBounds(143, 170, 310, 20);
		contentPanel.add(ToName_textField);
		
		JLabel BodyLabel = new JLabel("Body:");
		BodyLabel.setBounds(10, 241, 91, 14);
		contentPanel.add(BodyLabel);
		
		JTextArea Body_textArea = new JTextArea();
		Body_textArea.setBounds(143, 241, 310, 169);
		contentPanel.add(Body_textArea);
		
		JLabel FromName_label = new JLabel("From Name:");
		FromName_label.setBounds(10, 70, 91, 14);
		contentPanel.add(FromName_label);
		
		FromName_textField = new JTextField();
		FromName_textField.setColumns(10);
		FromName_textField.setBounds(143, 70, 310, 20);
		contentPanel.add(FromName_textField);
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
