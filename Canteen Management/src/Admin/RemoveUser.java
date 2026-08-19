package Admin;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JTextField;

import service.LoginService;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RemoveUser {

	private JFrame frame;
	private JTextField txtID;
	private JTextField txtEMAIL;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RemoveUser window = new RemoveUser();
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
	public RemoveUser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 620, 418);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 254, 436, -253);
		frame.getContentPane().add(panel);
		
		JLabel lblNewLabel = new JLabel("   REMOVE USER");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(156, 27, 214, 33);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("  USER E-MAIL");
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setBounds(72, 133, 143, 30);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_2.setBackground(Color.GRAY);
		frame.getContentPane().add(lblNewLabel_2);
		
		
		
		txtEMAIL = new JTextField();
		txtEMAIL.setBounds(214, 141, 154, 20);
		frame.getContentPane().add(txtEMAIL);
		txtEMAIL.setColumns(10);
		
		JButton btnREMOVE = new JButton("REMOVE");
		btnREMOVE.setForeground(Color.DARK_GRAY);
		btnREMOVE.setBounds(216, 172, 154, 23);
		btnREMOVE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userEMAIL=txtEMAIL.getText();
				LoginService removeuser=new LoginService();
				int status=0;
				int checkEmail=0;
				
				try {	
					checkEmail=removeuser.checkEmail(userEMAIL);
					
					status=removeuser.removeuser(userEMAIL);
					
					if(checkEmail>0) {
					if(status>0)
					{
						JOptionPane.showMessageDialog(null,"User deleted Successfully","Delete",JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						JOptionPane.showMessageDialog(null," error "," error",JOptionPane.ERROR_MESSAGE);
					}
					}else {
						JOptionPane.showMessageDialog(null,"Invalid email-id","user_email error",JOptionPane.ERROR_MESSAGE);
					}
						
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnREMOVE.setBackground(Color.GRAY);
		btnREMOVE.setFont(new Font("Tahoma", Font.BOLD, 18));
		frame.getContentPane().add(btnREMOVE);
		
		JButton btnBACK = new JButton("BACK");
		btnBACK.setForeground(Color.DARK_GRAY);
		btnBACK.setBounds(216, 243, 154, 23);
		btnBACK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserTable menu = null;
				try {
					menu = new UserTable();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					menu.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnBACK.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(btnBACK);
		
		JButton btnCLEAR = new JButton("CLEAR");
		btnCLEAR.setForeground(Color.DARK_GRAY);
		btnCLEAR.setBounds(216, 209, 154, 23);
		btnCLEAR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtID.setText("");
				txtEMAIL.setText("");
				
			}
		});
		btnCLEAR.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(btnCLEAR);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(34, 35, 85));
		panel_1.setBounds(0, 0, 606, 381);
		frame.getContentPane().add(panel_1);
	}

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		RemoveUser window = new RemoveUser();
		window.frame.setVisible(true);
		
	}

	
	
	 
}	// TODO Auto-generated method stub
		
	

