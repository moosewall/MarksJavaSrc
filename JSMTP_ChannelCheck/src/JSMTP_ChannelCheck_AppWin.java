//JSMTP_ChannelCheck_AppWin.java

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
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.JScrollPane;


import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.TextField;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.Toolkit;

public class JSMTP_ChannelCheck_AppWin extends JFrame
{
	
	public JSMTP_ChannelCheck_AppSettings m_AppSettings = new JSMTP_ChannelCheck_AppSettings () ;

	/*
	//http://www.seasite.niu.edu/cs580java/JList_Basics.htm
	//http://docs.oracle.com/javase/7/docs/api/javax/swing/DefaultListModel.html
	//
	private DefaultListModel m_listModel;
	private int m_ilistModelMax = 500 ;

	*/
	//http://www.seasite.niu.edu/cs580java/JList_Basics.htm
	//http://docs.oracle.com/javase/7/docs/api/javax/swing/DefaultListModel.html
	//
	private DefaultListModel <String> m_listModel;
	private int m_ilistModelMax = 500 ;
	private JList <String> m_list = null ;
	private JScrollPane m_scrollPane = null ;
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
					/*
					//default
					UIManager.setLookAndFeel
					 (UIManager.getCrossPlatformLookAndFeelClassName());
					 */
					
					//Current OS look and feel.  When running on
					//windows, looks windows like.  cool!
					//
					UIManager.setLookAndFeel
					 (UIManager.getSystemLookAndFeelClassName());
					
					JSMTP_ChannelCheck_AppWin window = new JSMTP_ChannelCheck_AppWin();
			
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
	public JSMTP_ChannelCheck_AppWin() {
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		/////////////////////////
		//
		AppState.initAppDetails(JSMTP_ChannelCheck_AppWin.class); //call this before anything!
		
		String sFN = TraceUtils.sGetFN () ;
		AppLog.Log(sFN + " initialized");

		m_AppSettings = new JSMTP_ChannelCheck_AppSettings () ;
		m_AppSettings.ReadSettingsFromIni();
		m_AppSettings.WriteSettingsToIni();  //write back so a stub .ini exists
		//////////////////////////
		
		m_Frame = new JFrame();
		m_Frame.setIconImage(Toolkit.getDefaultToolkit().getImage(JSMTP_ChannelCheck_AppWin.class.getResource("/com/sun/java/swing/plaf/motif/icons/DesktopIcon.gif")));
		m_Frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				m_cs.ShutdownWinApp();
			}
		});
		m_Frame.setTitle("JSMTP_ChannelCheck");
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
				m_cs.Close(); 
			}
		});
		mnFile.add(mntmExit);

		JMenu mnCommands = new JMenu("Commands");
		menuBar.add(mnCommands);
		JMenuItem mntmCloneGenericProject = new JMenuItem("Clone Generic Project");
		mntmCloneGenericProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				m_cs.Do_GenericProjectClone();
			}
		});
		mnCommands.add(mntmCloneGenericProject);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				//m_EventHandlers.HandleAbout();
				m_cs.Do_About(); 
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
				m_cs.Do_GenericProjectClone();
			}
		});
		toolBar_Top.add(btnGenericSourceProjectClone);
		
		JButton btnAbout = new JButton("A");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				m_cs.Do_About();  
			}
		});
		toolBar_Top.add(btnAbout);
		
		JToolBar toolBar_Bottom = new JToolBar();
		m_Frame.getContentPane().add(toolBar_Bottom, BorderLayout.SOUTH);
		
		//Per this
		//http://www.seasite.niu.edu/cs580java/JList_Basics.htm
		//Create an external "model" object that contains the data
		//for the list.
		m_listModel = new DefaultListModel () ;
		
		
		m_list = new JList();
		//contentPane.add(list, BorderLayout.CENTER);
		m_list.setModel(m_listModel);

		//construct a scrolling pane with the list.
		m_scrollPane = new JScrollPane(m_list);
		m_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		m_scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		m_Frame.getContentPane().add(m_scrollPane, BorderLayout.CENTER);
		
		
		JLabel lblStatus = new JLabel("");
		toolBar_Bottom.add(lblStatus);
	
	
		m_lblStatus = lblStatus ; //save a reference to update on fly.
		
		//background thread.
		m_cs = new CustomStuff (this.m_Frame) ;
		m_cs.StartWinApp(); 
	}
	/*
	void Close ()
	{
		m_Frame.setVisible (false) ;
		m_Frame.dispose () ;
	}
	*/
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

	////////////////////////////////////////
	//
	private class CustomStuff
	{
		private JFrame m_jfPar = null ;
		public CustomStuff (JFrame jfPar)
		{
			m_jfPar = jfPar ;
		}
		
		public void StartWinApp ()
		{
			m_WorkerThread.start () ;
			
			SetStatus (m_sStatusDefault) ;

			LogStartupSum () ;
		}
		public void ShutdownWinApp ()
		{
			m_AppSettings.WriteSettingsToIni();
			m_WorkerThread.StopThread () ;
		}

		private void LogStartupSum ()
		{
			Log ("Program Started") ;
			Log ("Demos JList and JMenu") ;
		}
		public void Close ()
		{
			//signal controlling form to close gracefully.
			m_jfPar.dispatchEvent(new WindowEvent(m_jfPar, WindowEvent.WINDOW_CLOSING));
		}
		public void Do_GenericProjectClone ()
		{
			String sFN = TraceUtils.sGetFN() ;
			String s = "" ;
			
			String sSep = File.separator ;
			
			SetStatus (sFN) ;
			
			try
			{

/*				
				//s = sFN + " todo" ;
				//ProgUtils.MsgBox(s);

				String sSrcDir = "" ;
				String sDestDir = "" ;
				String sNewPrjName = "" ;
				
				sSrcDir = GetDirDlg.sBrowseDir(m_AppSettings.m_sSrcDir, "Select source project directory", m_Frame) ;
				if (sSrcDir == "")
				{
					throw new Exception ("No source directory selected") ;
				}
				
				sDestDir = GetDirDlg.sBrowseDir(m_AppSettings.m_sDestDir, "Select destination project directory", m_Frame) ;
				if (sDestDir == "")
				{
					throw new Exception ("No destination directory selected") ;
				}
				
				sNewPrjName = JSMTP_ChannelCheck_ConfigDlg.sEnterStr(m_AppSettings.m_sDestName, "Enter destination project name", m_Frame) ;
				if (sNewPrjName == "")
				{
					throw new Exception ("No destination project name specified") ;
				}
				
				//
				s = String.format("Creating [%s] in [%s] from [%s]...", 
						sNewPrjName,
						sDestDir,
						sSrcDir) ;
				Log (s) ;

				String sDestPrjDir = sDestDir + sSep + sNewPrjName ;
				
				File fDestPrjDir = new File (sDestPrjDir) ;
				if (fDestPrjDir.exists())
				{
					s = "Destination directory already exists " + sDestPrjDir ;
					throw new Exception (s) ;
				}
				FileUtils.CopyDirOrFile(sSrcDir, sDestPrjDir);
				
				String sOldPrjName = FileUtils.sParseFileName(sSrcDir) ;
				
				Log ("Renaming Files in " + sDestPrjDir) ;
				FileUtils.RenameFilesInDir(sDestPrjDir, sOldPrjName, sNewPrjName);
				
				Log ("Replacing Text in File in " + sDestPrjDir) ;
				FileUtils.ReplaceTextInFilesInDir(sDestPrjDir, sOldPrjName, sNewPrjName);
				
				m_AppSettings.m_sSrcDir = sSrcDir ;
				m_AppSettings.m_sDestDir = sDestDir ;
				m_AppSettings.m_sDestName = sNewPrjName ;
*/
				
				Log (sFN + ", Operation completed OK") ;
			}
			catch (Exception exp)
			{
				s = String.format("%s, exception %s", sFN, exp.getMessage()) ;
				Log (s) ;
			}
			
			SetStatus (m_sStatusDefault) ;			
		}
		public void Do_About ()
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
		public void Do_AddLog ()
		{
			String sFN = TraceUtils.sGetFN() ;
			Log (sFN + " testing new log add") ;
		}
		public void Do_AddLogs ()
		{
			String sFN = TraceUtils.sGetFN() ;
			
			String sLog = "" ;
			
			for (int iLog = 0; iLog < 600; iLog++)
			{
				sLog = String.format("%s, Test Log %d", sFN, iLog + 1) ;
				Log (sLog) ;
			}
		}
		public void Do_MoveToEndOfLogs ()
		{
			String sFN = TraceUtils.sGetFN() ;
			SetListToEnd () ;
		}
		void SetListToEnd ()
		{
			//http://stackoverflow.com/questions/5147768/scroll-jscrollpane-to-bottom
			JScrollBar vert = m_scrollPane.getVerticalScrollBar();
			
			//int iPos = m_listModel.getSize() ;
			int iPosWanted = vert.getMaximum() ; 
			int iPosCurrent = vert.getValue() ;
			if (iPosWanted != iPosCurrent)
			{
				vert.setValue(iPosWanted);
			}
			/*
			m_list.setSelectedIndex(m_listModel.getSize() - 1);
			*/
		}
		/////////////////////////
		//Force JList refresh.
		//
		void ForceListRefresh ()
		{
			//Trick to ensure the JList doesn't go blank.
			m_list.setVisible(false);
			m_listModel.addElement(""); //add a dummy element to end.
			m_listModel.removeElementAt(m_listModel.getSize()-1); //remove dummy element
			m_list.setVisible(true);
		}
		void FlushLogs ()
		{
			int iMaxFlush = 100 ;
			int iNumFlushed = 0 ;
			while (true)
			{
				if (iNumFlushed >= iMaxFlush)
				{
					break ;
				}
				String sLog = m_Logs.sGetLog() ;
				if (sLog == "")
				{
					break ;
				}

				m_listModel.addElement(sLog);
				
				int iSize = m_listModel.getSize() ;
				if (iSize + 1 >= m_ilistModelMax)
				{
					//pop the first element off.
					m_listModel.remove (0) ;
				}
				iNumFlushed++ ;
			}
			
			if (iNumFlushed > 0)
			{
				ForceListRefresh () ;
			}
		}
		void Log (String sLog)
		{
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date = new Date();
			String sDate = dateFormat.format(date);
			
			String sFullLog = sDate + " " + sLog ;
			
			m_Logs.Log (sFullLog) ;
		}
		void SetStatus (String sStatus)
		{
			m_lblStatus.setText(sStatus);
		}
		//////////////////////////////////
		//
		public class Logs
		{
			private List<String> m_lstLogs = new ArrayList<String>();
			public synchronized void Log (String sLog)
			{
				m_lstLogs.add(sLog) ;
			}
			public synchronized String sGetLog ()
			{
				String sr = "" ;
				if (m_lstLogs.size() > 0)
				{
					sr = m_lstLogs.get(0) ;
					m_lstLogs.remove(0) ;
				}
				return sr ;
			}
		}
		Logs m_Logs = new Logs () ; 
				
		//////////////////////////////////
		//http://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
		//
		public class BackGroundThread extends Thread 
		{
			private boolean m_bDoShutdown = false ;
			public synchronized void SignalShutdown ()
			{
				m_bDoShutdown = true ;
			}
			public synchronized boolean bDoShutdown ()
			{
				return m_bDoShutdown ;
			}
			public void StopThread ()
			{
				SignalShutdown () ;
				while (isAlive()) ;
			}
			
		    public void run() 
		    {
		    	String sFN = TraceUtils.sGetFN() ;
		        //System.out.println("Hello from a thread!");
		    	
		    	String sLog = "" ;
		    	
		    	try
		    	{
			    	int iLoopCount = 0 ;
			    	while (true)
			    	{
			    		if (bDoShutdown())
			    		{
			    			//exit loop to shutdown.
			    			break ;
			    		}
			    		
			    		Thread.sleep(1);
			    		//looping in the thread.
			    		iLoopCount++ ;
			    		
			    		if ((iLoopCount % 1000)==0)
			    		{
			    			sLog = String.format("%s,  %d times",
			    					sFN,
			    					iLoopCount) ;
			    			//Log (sLog) ;
			    		}
			    		
			    		FlushLogs () ;
		    			SetListToEnd () ;
			    	}
		    	}
		    	catch (Exception exp)
		    	{
		    		//exception raised.
		    		sLog = String.format ("%s, exception %s",
		    				sFN,
		    				exp.getMessage()) ;
		    		TraceUtils.Trc(sLog);
		    	}
		    }

		    public void startMe() 
		    {
		    	this.start () ;
		    }
		    public void stopMe ()
		    {
		    }

		}
		private BackGroundThread m_WorkerThread = new BackGroundThread () ;
	} //class CustomStuff def
	private CustomStuff m_cs = null ;
	
}
