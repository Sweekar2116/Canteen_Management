import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import javax.swing.UIManager;
import util.ImageUtil;

public class Welcome extends JFrame {

	private JPanel contentPane;
	
	public String userEmail;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				Welcome frame = new Welcome();
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
	public Welcome() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 568);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.background"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		Image img = ImageUtil.loadImageAsImage("welcome.jpg");
		
		JButton btnMENU = new JButton("MENU");
		btnMENU.setForeground(Color.DARK_GRAY);
		btnMENU.setBounds(40, 141, 356, 62);
		btnMENU.setBackground(Color.LIGHT_GRAY);
		btnMENU.setFont(new Font("Times New Roman", Font.BOLD, 40));
		btnMENU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu menu=new menu();
				menu.setVisible(true);
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnMENU);
		
		JLabel lblNewLabel_1 = new JLabel("   WELCOME USER!");
		lblNewLabel_1.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 34));
		lblNewLabel_1.setBounds(10, 0, 356, 88);
		contentPane.add(lblNewLabel_1);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mainpage main=new Mainpage();
				main.setVisible(true);
				dispose();
			}
		});
		btnLogout.setForeground(Color.DARK_GRAY);
		btnLogout.setFont(new Font("Times New Roman", Font.BOLD, 40));
		btnLogout.setBackground(Color.LIGHT_GRAY);
		btnLogout.setBounds(40, 344, 356, 62);
		contentPane.add(btnLogout);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(-11, 0, 997, 544);
		ImageIcon welcomeIcon = ImageUtil.loadImage("bluewelcome.jpg");
		if (welcomeIcon != null) {
			lblNewLabel.setIcon(welcomeIcon);
		}
		contentPane.add(lblNewLabel);
	}



}
