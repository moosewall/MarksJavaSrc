import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

/*

How to use internal frames
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
	JInternalFrame m_jifRealTimeLogs = null ;
	

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
	public MarksSwingMdiTest() throws Exception {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		m_contentPane = new JPanel();
		m_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		m_contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(m_contentPane);
		
		m_desk = new JDesktopPane();
		m_contentPane.add(m_desk, BorderLayout.CENTER);
		m_desk.setBackground(Color.gray);
		
		/*
		m_jifRealTimeLogs = new JInternalFrame("Real Time Logs");
		*/
		
		/*
		 * e.g.
		                // create internal frame
               JInternalFrame frame = new JInternalFrame( 
                  "Internal Frame", true, true, true, true );

               MyJPanel panel = new MyJPanel(); // create new panel
               frame.add( panel, BorderLayout.CENTER ); // add panel
               frame.pack(); // set internal frame to size of contents

               theDesktop.add( frame ); // attach internal frame
               frame.setVisible( true ); // show internal frame

		 */
		m_jifRealTimeLogs = new JInternalFrame( 
                 "Real Time Logs", true, true, true, true );
		MyJPanel panel = new MyJPanel () ;
		m_jifRealTimeLogs.add(panel, BorderLayout.CENTER) ;
		m_jifRealTimeLogs.pack () ;
		m_desk.add(m_jifRealTimeLogs) ;
		m_jifRealTimeLogs.setVisible(true);

		/*
		m_jifRealTimeLogs.setResizable(true);
		m_jifRealTimeLogs.setMaximizable(true);
		m_jifRealTimeLogs.setIconifiable(true);
		m_jifRealTimeLogs.setClosable(false);
		m_jifRealTimeLogs.setIcon(true);
		
		m_jifRealTimeLogs.setBounds(10, 11, 175, 209);
		m_jifRealTimeLogs.setMaximumSize(this.getMaximumSize()) ;
		m_jifRealTimeLogs.setMaximum(true);
		*/
		/*
		//m_jifRealTimeLogs.pack(); //seems to minimize too.
		m_desk.add(m_jifRealTimeLogs, BorderLayout.CENTER);
		*/
		
		//m_jifRealTimeLogs.setIcon(true); disappears
		
		JMenuBar menuBar = new JMenuBar();
		m_contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
		mnWindow.add(mntmNewMenuItem);
		
		
	
		//m_jifRealTimeLogs.setVisible(true);
	}
	// class to display an ImageIcon on a panel
	public class CustomStuff
	{
		
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

