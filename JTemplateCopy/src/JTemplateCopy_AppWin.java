//JTemplateCopy_AppWin.java

/*
 * Logs
 * 10/25/2013, MW, testing GITHUB exchange
 *
*/
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.AbstractListModel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.JScrollPane;

import java.awt.List;

import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.TextField;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JTemplateCopy_AppWin 
{
	
	public JTemplateCopy_AppSettings m_AppSettings = new JTemplateCopy_AppSettings () ;
	
	//http://www.seasite.niu.edu/cs580java/JList_Basics.htm
	//http://docs.oracle.com/javase/7/docs/api/javax/swing/DefaultListModel.html
	//
	private DefaultListModel m_listModel;
	private int m_ilistModelMax = 500 ;

	private JLabel m_lblStatus = null ;
	private String m_sStatusDefault = "Ready" ;

	private JFrame m_Frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		LaunchMe () ;
	}

	public static void LaunchMe ()
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{
					JTemplateCopy_AppWin window = new JTemplateCopy_AppWin();
			
				
					window.m_Frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public JTemplateCopy_AppWin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		/////////////////////////
		//
		AppState.initAppDetails(JTemplateCopy_AppWin.class); //call this before anything!
		
		String sFN = TraceUtils.sGetFN () ;
		AppLog.Log(sFN + " initialized");

		m_AppSettings = new JTemplateCopy_AppSettings () ;
		m_AppSettings.ReadSettingsFromIni();
		m_AppSettings.WriteSettingsToIni();  //write back so a stub .ini exists
		//////////////////////////
		
		m_Frame = new JFrame();
		m_Frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				String sFN = TraceUtils.sGetFN() ;
				AppLog.Log(sFN + " exiting");
				m_AppSettings.WriteSettingsToIni();  //write back so a stub .ini exists
			}
		});
		m_Frame.setTitle("JTemplateCopy");
		m_Frame.setBounds(100, 100, 560, 582);
		m_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		m_Frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{

				Close () ;
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnCommands = new JMenu("Commands");
		menuBar.add(mnCommands);
		JMenuItem mntmCloneGenericProject = new JMenuItem("Clone Generic Project");
		mntmCloneGenericProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				m_EventHandlers.HandleGenericProjectClone();				
			}
		});
		mnCommands.add(mntmCloneGenericProject);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				m_EventHandlers.HandleAbout();
			}
		});
		mnHelp.add(mntmAbout);
		
		
		m_Frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JList list = new JList();
		m_Frame.getContentPane().add(list);
		
		//Per this
		//http://www.seasite.niu.edu/cs580java/JList_Basics.htm
		//Create an external "model" object that contains the data
		//for the list.
		m_listModel = new DefaultListModel () ;
		list.setModel(m_listModel);
		
		JToolBar toolBar_Top = new JToolBar();
		m_Frame.getContentPane().add(toolBar_Top, BorderLayout.NORTH);
		
		JButton btnGenericSourceProjectClone = new JButton("G");
		btnGenericSourceProjectClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				m_EventHandlers.HandleGenericProjectClone();
			}
		});
		toolBar_Top.add(btnGenericSourceProjectClone);
		
		JButton btnAbout = new JButton("A");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				m_EventHandlers.HandleAbout(); 
			}
		});
		toolBar_Top.add(btnAbout);
		
		JToolBar toolBar_Bottom = new JToolBar();
		m_Frame.getContentPane().add(toolBar_Bottom, BorderLayout.SOUTH);
		
		JLabel lblStatus = new JLabel("");
		toolBar_Bottom.add(lblStatus);
	
		
		m_lblStatus = lblStatus ; //save a reference to update on fly.
		
		SetStatus (m_sStatusDefault) ;
	}
	
	void Close ()
	{
		m_Frame.setVisible (false) ;
		m_Frame.dispose () ;
	}
	void Log (String sLog)
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		String sDate = dateFormat.format(date);
		
		String sFullLog = sDate + " " + sLog ;
		m_listModel.addElement(sFullLog);
		
		int iSize = m_listModel.getSize() ;
		if (iSize > m_ilistModelMax)
		{
			//pop the first element off.
			m_listModel.remove (0) ;
		}
	}
	void SetStatus (String sStatus)
	{
		m_lblStatus.setText(sStatus);
	}
	//////////////////////////////////////
	//Central class to contain all event handlers
	//
	private class EventHandlers
	{
		public void HandleGenericProjectClone ()
		{
			String sFN = TraceUtils.sGetFN() ;
			String s = "" ;
			
			SetStatus (sFN) ;
			
			try
			{
				//s = sFN + " todo" ;
				//ProgUtils.MsgBox(s);
				
				String sSrcDir = "" ;
				sSrcDir = GetDirDlg.sBrowseDir(m_AppSettings.m_sSrcDir, "Select source project directory", m_Frame) ;
				if (sSrcDir == "")
				{
					throw new Exception ("No source directory selected") ;
				}
				
				String sDestDir = "" ;
				sDestDir = GetDirDlg.sBrowseDir(m_AppSettings.m_sDestDir, "Select destination project directory", m_Frame) ;
				if (sDestDir == "")
				{
					throw new Exception ("No destination directory selected") ;
				}
				
				m_AppSettings.m_sSrcDir = sSrcDir ;
				m_AppSettings.m_sDestDir = sDestDir ;
			}
			catch (Exception exp)
			{
				
			}
			
			exit: ;
			SetStatus (m_sStatusDefault) ;			
		}
		public void HandleAbout ()
		{
			String sFN = TraceUtils.sGetFN() ;
			String s = "" ;
			
			Log (sFN + " launching about box") ;
			SetStatus (sFN + " launching about box") ;
			
			s = "" ;
			s += "Program to clone project source directories." ;
			ProgUtils.MsgBox(s, m_Frame);
			
			SetStatus (m_sStatusDefault) ;
		}
	} ;
	private EventHandlers m_EventHandlers = new EventHandlers () ; 
	//////////////////////////////////////
}
