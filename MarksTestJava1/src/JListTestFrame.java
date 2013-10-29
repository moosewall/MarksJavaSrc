//JAVA Swing JList and JMenu example

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JScrollBar;
import javax.swing.ListModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class JListTestFrame extends JFrame {

	//http://www.seasite.niu.edu/cs580java/JList_Basics.htm
	//http://docs.oracle.com/javase/7/docs/api/javax/swing/DefaultListModel.html
	//
	private DefaultListModel<String> m_listModel ;
	private int m_ilistModelMax = 5000 ;
	private JList<String> m_list = null ;
	private JScrollPane m_scrollPane = null ;
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JListTestFrame frame = new JListTestFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public JListTestFrame() 
	{
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				m_cs.ShutdownWinApp();
			}
		});
		
		setTitle("JList Test Form");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		////////////////////
		//menu
		//
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		///
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

		///
		JMenu mnCommands = new JMenu("Commands");
		menuBar.add(mnCommands);
		
		JMenuItem mntmAddLog = new JMenuItem("Add Log");
		mntmAddLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				m_cs.Do_AddLog(); 
			}
		});
		mnCommands.add(mntmAddLog);
		
		menuBar.add(mnCommands);
		
		JMenuItem mntmAddLogs = new JMenuItem("Add Many Logs");
		mntmAddLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				m_cs.Do_AddLogs();
			}
		});
		mnCommands.add(mntmAddLogs);
		
		JMenuItem mntmTrickleLogTest = new JMenuItem("Trickle Log Test");
		mntmTrickleLogTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				m_cs.Do_TrickleLogTest();
			}
		});
		mnCommands.add(mntmTrickleLogTest);

		JMenuItem mntmClearLogs = new JMenuItem("Clear Logs");
		mntmClearLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				m_cs.Do_ClearLogs();
			}
		});
		mnCommands.add(mntmClearLogs);
		

		JMenuItem mntmMoveToEndOfLogs = new JMenuItem("Move Log View To End");
		mntmMoveToEndOfLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				m_cs.Do_MoveToEndOfLogs();
			}
		});
		mnCommands.add(mntmMoveToEndOfLogs);
		
		////////////////////
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

	
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
		contentPane.add(m_scrollPane, BorderLayout.CENTER);
	
		//background thread.
		m_cs.StartWinApp(); 
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
			LogStartupSum () ;
		}
		public void ShutdownWinApp ()
		{
			m_WorkerThread.StopThread () ;
		}

		private void LogStartupSum ()
		{
			Log ("Program Started") ;
			Log ("Demos JList and JMenu") ;
		}
		public void Close ()
		{
			/*
			m_WorkerThread.StopThread();
			setVisible (false) ;
			dispose () ;
			*/
			//signal from to close gracefully.
			m_jfPar.dispatchEvent(new WindowEvent(m_jfPar, WindowEvent.WINDOW_CLOSING));
		}
		public void Do_TrickleLogTest ()
		{
			String sFN = TraceUtils.sGetFN() ;
			boolean bTrickleLogTestOn = m_WorkerThread.bToggleTrickleLogTest() ;
			Log (sFN + " bTrickleLogTestOn == " + bTrickleLogTestOn) ;
		}
		public void Do_ClearLogs ()
		{
			m_listModel.clear(); 
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
			int iListSize = m_list.getModel().getSize() ;
			if (iListSize == 0)
			{
				return ;
			}
			
			//http://stackoverflow.com/questions/5147768/scroll-jscrollpane-to-bottom
			JScrollBar vert = m_scrollPane.getVerticalScrollBar();
			
			//int iPos = m_listModel.getSize() ;
			int iPosWanted = vert.getMaximum() ; 
			int iPosCurrent = vert.getValue() ;
			if (iPosWanted != iPosCurrent)
			{
				vert.setValue(iPosWanted);
			}
			/*attempt at flushing display
			if (m_listModel.size() > 0)
			{
				m_listModel.add(0, "");
				m_listModel.remove(0) ;
			}
			*/
			/*
			int iSize = m_list.getModel().getSize() ;
			if (iSize > 0)
			{
				m_list.ensureIndexIsVisible(iSize-1);
			}
			*/
		}
		void FlushLogs ()
		{
			boolean bAutoScrollDisplay = true ;
			int iSel = m_list.getSelectedIndex() ;
			int iListSize = m_list.getModel().getSize() ; 
			
			if (   iSel != -1
				&& (iSel + 1) < iListSize)
			{
				bAutoScrollDisplay = false ;
			}

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
				
				int iSize = 0 ;
				m_listModel.ensureCapacity(iSize + 1);
				m_listModel..addElement(sLog);
				iSize = m_listModel.getSize() ;
				
				if (iSize + 1 >= m_ilistModelMax)
				{
					//pop the first element off.
					//m_listModel.remove (0) ;
					m_listModel.removeElementAt(0);
				}
				iNumFlushed++ ;
			}
			iListSize = m_list.getModel().getSize() ;

			/*
			while (m_listModel.getSize() > m_ilistModelMax)
			{
				//pop the first element off.
				m_listModel.remove (0) ;
			}
			*/
		
			
			if (bAutoScrollDisplay)
			{
				if (   iSel != -1
				    && iListSize > 0)
				{
					m_list.setSelectedIndex(iListSize - 1);
				}
				SetListToEnd () ;
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
		//////////////////////////////////
		//
		public class Logs
		{
			private List<String> m_lstLogs = new ArrayList<String>();
			public void Log (String sLog)
			{
				synchronized (m_lstLogs)
				{
					m_lstLogs.add(sLog) ;
				}
			}
			public String sGetLog ()
			{
				String sr = "" ;
				
				synchronized (m_lstLogs)
				{
					if (m_lstLogs.size() > 0)
					{
						sr = m_lstLogs.get(0) ;
						m_lstLogs.remove(0) ;
					}
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
			/////
			private boolean m_bDoShutdown = false ;
			public synchronized void SignalShutdown ()
			{
				m_bDoShutdown = true ;
			}
			public synchronized boolean bDoShutdown ()
			{
				return m_bDoShutdown ;
			}
			
			/////
			private boolean m_bTrickleLogTestOn = false ;
			private Date m_dtTrickleLogTest_LastTime = new Date () ;
			private int m_iTrickleLogTest_IntervalSeconds = 2 ; 
			public synchronized boolean bToggleTrickleLogTest ()
			{
				if (m_bTrickleLogTestOn)
				{
					m_bTrickleLogTestOn = false ;
				}
				else
				{
					m_bTrickleLogTestOn = true ;
				}
				return m_bTrickleLogTestOn ;
			}
			
			public void StopThread ()
			{
				SignalShutdown () ;
				while (isAlive()) ;
			}
			/////////////////////////////////////////////////
			//
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
			    		Date dtCur = new Date();
			    		
			    		if (bDoShutdown())
			    		{
			    			//exit loop to shutdown.
			    			break ;
			    		}
			    		
			    		Thread.sleep(10);
			    		//looping in the thread.
			    		iLoopCount++ ;
			    		
			    		if (this.m_bTrickleLogTestOn)
			    		{
			    			//http://stackoverflow.com/questions/635935/how-can-i-calculate-a-time-span-in-java-and-format-the-output
			    			long lNumSecsSinceLastTrickle = (dtCur.getTime() - this.m_dtTrickleLogTest_LastTime.getTime()) / 1000 ;
			    			if (lNumSecsSinceLastTrickle >= m_iTrickleLogTest_IntervalSeconds)
			    			{
			    				Log (sFN + " new trickle log test log") ;
			    				m_dtTrickleLogTest_LastTime = new Date () ;
			    			}
			    		}
			    		
			    		if ((iLoopCount % 1000)==0)
			    		{
			    			sLog = String.format("%s,  %d times",
			    					sFN,
			    					iLoopCount) ;
			    			//Log (sLog) ;
			    		}
			    		
			    		FlushLogs () ;
			    		/*do in FlushLogs
		    			SetListToEnd () ;
		    			*/
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
		    /////////////////////////////////////////
		    //
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
	private CustomStuff m_cs = new CustomStuff (this) ;
	
}



