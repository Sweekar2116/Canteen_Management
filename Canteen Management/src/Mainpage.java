import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import util.ImageUtil;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;

import Admin.LoginAdmin;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Mainpage {

	private JFrame frame;
	private JTextField txtWelcomeToCanteen;
	private JTextField txtLoginAs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mainpage window = new Mainpage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Mainpage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(21, 22, 70));
		panel.setBounds(10, 0, 986, 542);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(182, 154, 246, 203);
		ImageIcon adminIcon = ImageUtil.loadImage("adminguy (1).jpg");
		if (adminIcon != null) {
			lblNewLabel.setIcon(adminIcon);
		}
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(593, 140, 317, 239);
		ImageIcon userIcon = ImageUtil.loadImage("userfinal (2).jpg");
		if (userIcon != null) {
			lblNewLabel_1.setIcon(userIcon);
		}
		panel.add(lblNewLabel_1);
		
		JButton btnADMIN = new JButton("ADMIN");
		btnADMIN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginAdmin menu=new LoginAdmin();
				 menu.setVisible(true);
				 
			}

					

			
		});
		btnADMIN.setBackground(Color.WHITE);
		btnADMIN.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnADMIN.setForeground(Color.BLACK);
		btnADMIN.setBounds(192, 346, 205, 53);
		panel.add(btnADMIN);
		
		JButton btnUSER = new JButton("USER");
		btnUSER.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginpage menu=new loginpage();
				 menu.setVisible(true);
				 
			}
		});
		btnUSER.setForeground(Color.BLACK);
		btnUSER.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnUSER.setBackground(Color.WHITE);
		btnUSER.setBounds(603, 346, 205, 53);
		panel.add(btnUSER);
		
		txtWelcomeToCanteen = new JTextField();
		txtWelcomeToCanteen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		txtWelcomeToCanteen.setText("                                 WELCOME TO CANTEEN MANAGEMENT SYSTEM");
		txtWelcomeToCanteen.setBounds(70, 27, 840, 20);
		panel.add(txtWelcomeToCanteen);
		txtWelcomeToCanteen.setColumns(10);
		
		txtLoginAs = new JTextField();
		txtLoginAs.setFont(new Font("Times New Roman", Font.BOLD, 20));
		txtLoginAs.setText("LOGIN AS");
		txtLoginAs.setBounds(439, 113, 110, 42);
		panel.add(txtLoginAs);
		txtLoginAs.setColumns(10);
	}

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		Mainpage window = new Mainpage();
		window.frame.setVisible(true);
	}
}
