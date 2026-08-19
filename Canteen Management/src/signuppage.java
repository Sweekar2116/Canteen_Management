import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import service.LoginService;
import util.ImageUtil;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class signuppage {

	private JFrame frame;
	private JPasswordField txtPASSWORD;
	private JPasswordField txtCONFIRMPASS;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					signuppage window = new signuppage();
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
	public signuppage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image img = ImageUtil.loadImageAsImage("signup.jpg");
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(509, 0, 477, 531);
		panel.setBackground(new Color(192, 192, 192));
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("USERNAME");
		lblNewLabel_1.setForeground(new Color(26, 27, 85));
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1.setBounds(90, 63, 161, 26);
		panel.add(lblNewLabel_1);
		
		JTextPane txtUSERNAME = new JTextPane();
		txtUSERNAME.setBounds(90, 100, 330, 26);
		panel.add(txtUSERNAME);
		
		JLabel lblNewLabel_1_1 = new JLabel("PHONE NO");
		lblNewLabel_1_1.setForeground(new Color(26, 27, 85));
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1_1.setBounds(90, 137, 161, 26);
		panel.add(lblNewLabel_1_1);
		
		JTextPane txtPHONE = new JTextPane();
		txtPHONE.setBounds(90, 174, 330, 26);
		panel.add(txtPHONE);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("EMAIL ID");
		lblNewLabel_1_1_1.setForeground(new Color(26, 27, 85));
		lblNewLabel_1_1_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1_1_1.setBounds(90, 211, 161, 26);
		panel.add(lblNewLabel_1_1_1);
		
		JTextPane txtEMAIL = new JTextPane();
		txtEMAIL.setBounds(90, 248, 330, 26);
		panel.add(txtEMAIL);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("PASSWORD");
		lblNewLabel_1_1_1_1.setForeground(new Color(26, 27, 85));
		lblNewLabel_1_1_1_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1_1_1_1.setBounds(90, 285, 161, 27);
		panel.add(lblNewLabel_1_1_1_1);
		
		txtPASSWORD = new JPasswordField();
		txtPASSWORD.setBounds(90, 323, 330, 26);
		panel.add(txtPASSWORD);
		
		JButton btnNewButton = new JButton("SIGNUP");
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String userName=txtUSERNAME.getText();
				String userEmail=txtEMAIL.getText();
				String userPassword =txtPASSWORD.getText();
				String phone =txtPHONE.getText();
				String confirmPass=txtCONFIRMPASS.getText();
				LoginService register=new LoginService();
				int status=0;
				
				try {
					if(register.validateSignUp(userName, userEmail, phone, userPassword)) {
						if(register.validateMobile(phone)) {
						if(register.validateEmail(userEmail)) {
						if(register.validateConfirmPass(userPassword, confirmPass)) {
					 status = register.userSignup(userName, userEmail, Long.valueOf(phone), userPassword) ;
					 if(status>0)
						{
							JOptionPane.showMessageDialog(null,"Signed in successfully");
							signuppage menu=new signuppage();
							menu.setVisible(true);
						}
						else {					
							JOptionPane.showMessageDialog(null,"Invalid details","Sign Up Error",JOptionPane.ERROR_MESSAGE);					
						}
					 
						
						}else{
							JOptionPane.showMessageDialog(null,"Confirm password is not matching","Sign Up Error",JOptionPane.ERROR_MESSAGE);
						}
						}else {
							JOptionPane.showMessageDialog(null,"Invalid email","Sign Up Error",JOptionPane.ERROR_MESSAGE);					
						}
						}else {
							JOptionPane.showMessageDialog(null,"Invalid mobile phone","Sign Up Error",JOptionPane.ERROR_MESSAGE);
						}
					}else {
						JOptionPane.showMessageDialog(null,"Please fill the details","Sign Up Error",JOptionPane.ERROR_MESSAGE);					
					}
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				
			}
		});
		btnNewButton.setBackground(new Color(26, 27, 85));
		btnNewButton.setBounds(166, 435, 181, 36);
		panel.add(btnNewButton);
		
		JLabel lblNewLabel_2 = new JLabel("CONFIRM PASSWORD");
		lblNewLabel_2.setForeground(new Color(26, 27, 85));
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_2.setBounds(90, 360, 257, 27);
		panel.add(lblNewLabel_2);
		
		txtCONFIRMPASS = new JPasswordField();
		txtCONFIRMPASS.setBounds(90, 398, 330, 26);
		panel.add(txtCONFIRMPASS);
		
		JButton btnNewButton_1 = new JButton("CLEAR");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUSERNAME.setText("");
				txtPHONE.setText("");
				txtEMAIL.setText("");
				txtPASSWORD.setText("");
				txtCONFIRMPASS.setText("");
			}
		});
		btnNewButton_1.setBackground(Color.GRAY);
		btnNewButton_1.setBounds(104, 482, 105, 23);
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("LOGIN");
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginpage menu=new loginpage();
				menu.setVisible(true);
				frame.dispose();
			}
		});
		btnNewButton_2.setBackground(Color.GRAY);
		btnNewButton_2.setBounds(304, 482, 105, 23);
		panel.add(btnNewButton_2);
		
		JLabel lblNewLabel_4 = new JLabel("SIGNUP");
		lblNewLabel_4.setForeground(new Color(34, 35, 85));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 36));
		lblNewLabel_4.setBackground(Color.RED);
		lblNewLabel_4.setBounds(171, 0, 176, 44);
		panel.add(lblNewLabel_4);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, -25, 509, 556);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		ImageIcon foodIcon = ImageUtil.loadImage("food.jpg");
		if (foodIcon != null) {
			lblNewLabel.setIcon(foodIcon);
		}
		lblNewLabel.setBounds(0, 23, 509, 533);
		panel_1.add(lblNewLabel);
	}

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		signuppage window = new signuppage();
		window.frame.setVisible(true);
	}
}
