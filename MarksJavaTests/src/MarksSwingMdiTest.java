import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		m_jifRealTimeLogs.pack () ;
		m_desk.add(m_jifRealTimeLogs) ;
		m_jifRealTimeLogs.setVisible(true);
		
		m_cs.m_rtlm = new CustomStuff.RealTimeLogFrameMgr(m_jifRealTimeLogs) ;

		
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
		
		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
		mnWindow.add(mntmNewMenuItem);
		
		
	}
	// class to display an ImageIcon on a panel
	private CustomStuff m_cs = new CustomStuff () ;
	public static class CustomStuff
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
		
		public RealTimeLogFrameMgr m_rtlm = null ;
		public static class RealTimeLogFrameMgr
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
		
		static void Log (String sLog)
		{
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date = new Date();
			String sDate = dateFormat.format(date);
			
			String sFullLog = sDate + " " + sLog ;
			
			m_Logs.Log (sFullLog) ;
		}
		//////////////////////////////////
		//
		public static class Logs
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
		static Logs m_Logs = new Logs () ;
		
		//////////////////////////////////
		//http://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
		//
		public static class BackGroundThread extends Thread 
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
		    public static void run() 
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
		private BackGroundThread m_WorkerThread = new BackGroundThread () ;
		
	} //class CustomStuff
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

