import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.beans.PropertyVetoException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;

/*

How to use internal frames
http://docs.oracle.com/javase/tutorial/uiswing/components/internalframe.html

/*  */
public class MarksSwingMdiTest extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JDesktopPane desktopPane = new JDesktopPane();
		contentPane.add(desktopPane, BorderLayout.CENTER);
		
		JInternalFrame internalFrame = new JInternalFrame("Real Time Logs");
		internalFrame.setResizable(true);
		internalFrame.setMaximizable(true);
		internalFrame.setIconifiable(true);
		internalFrame.setIcon(true);
		internalFrame.setBounds(10, 11, 175, 209);
		desktopPane.add(internalFrame);
		
		JMenuBar menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		internalFrame.setVisible(true);
	}
}
