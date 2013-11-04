import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

/*
 
-Key Example showing how to show a "Windows Like" MDI implementation:
.\\fig22_11_12
http://www.deitel.com/articles/java_tutorials/20060505/

-How to use internal frames
http://docs.oracle.com/javase/tutorial/uiswing/components/m_jifRealTimeLogs.html

http://books.google.com/books?id=my6jzH-O8psC&pg=PT413&lpg=PT413&dq=JInternalFrame+merge+with+JDesktopPane&source=bl&ots=SBfHnTOff6&sig=dbe2YQYYVfbDirpSAhho7Nf3y6I&hl=en&sa=X&ei=0KtyUuCyC7Ku4AOj34HgCw&ved=0CEQQ6AEwAzgU#v=onepage&q=JInternalFrame%20merge%20with%20JDesktopPane&f=false

https://forums.oracle.com/message/6229532

Having trouble with minimize.  The frame disappears.
http://stackoverflow.com/questions/9414728/minimizing-jinternal-frame-without-clicking-the-button/9422246#9422246
this person has same problem:
https://forums.oracle.com/message/6229532

/*  */
public class MarksSwingMdiTest extends JFrame {

	private JPanel m_contentPane = null;
	private JDesktopPane m_desk = null ;
	private JInternalFrame m_jifRealTimeLogs = null ;
	
	//http://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
	private JPopupMenu m_popup;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
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
					
					MarksSwingMdiTest frame = new MarksSwingMdiTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public MarksSwingMdiTest() throws Exception 
	{
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				m_cs.ShutdownWinApp();
			}
		});
		
		m_cs.m_jfPar = this ;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		m_contentPane = new JPanel();
		m_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		m_contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(m_contentPane);
		
		m_desk = new JDesktopPane();
		m_contentPane.add(m_desk, BorderLayout.CENTER);
		m_desk.setBackground(Color.gray);
		
		m_jifRealTimeLogs = new JInternalFrame( 
                 "Real Time Logs", true, true, true, true );
		MyJPanel panel = new MyJPanel () ;
		m_jifRealTimeLogs.add(panel, BorderLayout.CENTER) ;
		//m_jifRealTimeLogs.pack () ; //this minimizes i.e. iconifies
		m_desk.add(m_jifRealTimeLogs) ;
		m_jifRealTimeLogs.setVisible(true);
		m_jifRealTimeLogs.setMaximum(true);
		m_jifRealTimeLogs.setClosable(false);  //real time logs always up
		
		//how to make a new inner class
		//http://stackoverflow.com/questions/70324/java-inner-class-and-static-nested-class
		m_cs.m_rtlm = m_cs.new RealTimeLogFrameMgr(m_jifRealTimeLogs) ;
		
		JMenuBar menuBar = new JMenuBar();
		m_contentPane.add(menuBar, BorderLayout.NORTH);
		
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
		
		//////////////////
		//"Windows" options
		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);
		
		JMenuItem mntmCascadeWindows = new JMenuItem("Cascade");
		mntmCascadeWindows.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String sFN = TraceUtils.sGetFN() ;
				boolean bTrickleLogTestOn = m_cs.m_WorkerThread.bToggleTrickleLogTest() ;
			}
		});
		mnWindow.add(mntmCascadeWindows);
		//////////////////
		
		////////////////////
		//Create the popup menu.
	    m_popup = new JPopupMenu();
	    JMenuItem menuItem = null ;
	    menuItem = new JMenuItem("Copy To Clipboard");
	    menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				m_cs.Do_CopyToClipboard();
			}
		});
	    m_popup.add(menuItem);

	    //Add listener to components that can bring up popup menus.
	    MouseListener popupListener = new PopupListener();
	    m_cs.m_rtlm.m_list.addMouseListener(popupListener);
		
		m_cs.StartWinApp();
	}
	class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) 
	        {
	            m_popup.show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }
	}	
	
	// class to display an ImageIcon on a panel
	private CustomStuff m_cs = new CustomStuff () ;
	public class CustomStuff
	{
		public JFrame m_jfPar = null ;
		
		public void StartWinApp ()
		{
			//m_BackgroundTasks = new MyBackgroundTasks () ;
			m_WorkerThread.start () ;
			LogStartupSum () ;
		}
		public void ShutdownWinApp ()
		{
			m_WorkerThread.StopThread () ;
			//m_BackgroundTasks.bDoShutdown() ;
		}

		private void LogStartupSum ()
		{
			Log ("Program Started") ;
			Log ("Demos MDI java app") ;
		}
		public void Close ()
		{
			//signal from to close gracefully.
			m_jfPar.dispatchEvent(new WindowEvent(m_jfPar, WindowEvent.WINDOW_CLOSING));
		}
		public void Do_CopyToClipboard ()
		{
			String sFN = TraceUtils.sGetFN() ;
			
			String sEol = BufUtils.sGetEol() ;
			String sSelAll = "" ;
			
			int [] iaSelIs = m_rtlm.m_list.getSelectedIndices() ;
			for (int iSelI = 0; iSelI < iaSelIs.length; iSelI++)
			{
				int iSel = iaSelIs[iSelI] ;
				String sSel = m_rtlm.m_listModel.get(iSel) ;
				if (sSelAll.equals("") == false)
				{
					sSelAll += sEol ;
				}
				sSelAll += sSel ;
			}
			StringSelection stringSelection = new StringSelection (sSelAll);
			Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clpbrd.setContents (stringSelection, null);			
		}
		public void Do_TrickleLogTest ()
		{
			String sFN = TraceUtils.sGetFN() ;
			boolean bTrickleLogTestOn = m_WorkerThread.bToggleTrickleLogTest() ;
			//boolean bTrickleLogTestOn = m_BackgroundTasks.bToggleTrickleLogTest() ;
			Log (sFN + " bTrickleLogTestOn == " + bTrickleLogTestOn) ;
		}
		public void Do_ClearLogs ()
		{
			m_rtlm.m_listModel.clear(); 
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

			int iListSize = m_rtlm.m_listModel.size() ;
			if (iListSize > 0)
			{
				m_rtlm.m_list.setSelectedIndex(iListSize - 1);
			}
			m_rtlm.SetListToEnd () ;
		}
		public RealTimeLogFrameMgr m_rtlm = null ;
		public class RealTimeLogFrameMgr
		{
			private DefaultListModel<String> m_listModel ;
			private int m_ilistModelMax = 5000 ;
			private JList<String> m_list = null ;
			private JScrollPane m_scrollPane = null ;
			private JInternalFrame m_jfRealTimeLogs = null ;
			private JPanel m_contentPane = null;
			
			public RealTimeLogFrameMgr (JInternalFrame jfRealTimeLogs)
			{
				m_jfRealTimeLogs = jfRealTimeLogs ; 
				InitRealTimeLogsFrame () ;
			}
			private void InitRealTimeLogsFrame ()
			{
				m_contentPane = new JPanel();
				m_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
				m_contentPane.setLayout(new BorderLayout(0, 0));
				m_jfRealTimeLogs.setContentPane(m_contentPane);
				
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
				m_contentPane.add(m_scrollPane, BorderLayout.CENTER);
				
				
				
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
					m_listModel.addElement(sLog);
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
				
				if (iNumFlushed > 0)
				{
					ForceListRefresh () ;
				}
			}
		
		} //class RealTimeLogFrameMgr
		
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
			    		
			    		m_rtlm.FlushLogs () ;
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
		public BackGroundThread m_WorkerThread = new BackGroundThread () ;
		
	} //class CustomStuff
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
} //public class MarksSwingMdiTest extends JFrame {


//class to display an ImageIcon on a panel
class MyJPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Random generator = new Random();
	private ImageIcon picture; // image to be displayed
	private String[] images = { "yellowflowers.png", "purpleflowers.png",
	   "redflowers.png", "redflowers2.png", "lavenderflowers.png" };
	
	// load image
	public MyJPanel()
	{
	   int randomNumber = generator.nextInt( 5 );
	   picture = new ImageIcon( images[ randomNumber ] ); // set icon
	} // end MyJPanel constructor
	
	// display imageIcon on panel
	public void paintComponent( Graphics g )
	{
	   super.paintComponent( g );
	   picture.paintIcon( this, g, 0, 0 ); // display icon
	} // end method paintComponent
	
	// return image dimensions
	public Dimension getPreferredSize()
	{
	   return new Dimension( picture.getIconWidth(), 
	      picture.getIconHeight() );  
	} // end method getPreferredSize
} // end class MyJPanel

