
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Window;

import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JTextField;

import service.LoginService;
import service.OrderService;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CheckPayment {

	private JFrame frame;

	private JTextField txtDate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CheckPayment window = new CheckPayment();
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
	public CheckPayment() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 254, 436, -253);
		frame.getContentPane().add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(34, 35, 85));
		panel_1.setBounds(0, 0, 686, 531);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("   CHECK SUMMARY");
		lblNewLabel.setBounds(171, 24, 366, 39);
		panel_1.add(lblNewLabel);
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		
		
		
		txtDate = new JTextField();
		txtDate.setBounds(316, 125, 252, 39);
		panel_1.add(txtDate);
		txtDate.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("  ENTER DATE");
		lblNewLabel_2.setBounds(57, 125, 204, 30);
		panel_1.add(lblNewLabel_2);
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblNewLabel_2.setBackground(Color.GRAY);
		
		JButton btnREMOVE = new JButton("CHECK");
		btnREMOVE.setBounds(358, 223, 154, 23);
		panel_1.add(btnREMOVE);
		btnREMOVE.setForeground(Color.DARK_GRAY);
		btnREMOVE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String date=txtDate.getText();
				LoginService check=new LoginService();
				OrderService o=new OrderService();
				if(check.validateDate(date)==1) {
					o.saveBillDate(date);
					PaymentTable pt;
					try {
						pt = new PaymentTable();
						pt.setVisible(true);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showMessageDialog(null,"Please enter date in dd/mm/yyyy format","Date Error",JOptionPane.ERROR_MESSAGE);
				}
				
				
				
			}
		});
		btnREMOVE.setBackground(Color.GRAY);
		btnREMOVE.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JButton btnCLEAR = new JButton("CLEAR");
		btnCLEAR.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCLEAR.setBounds(358, 292, 154, 23);
		panel_1.add(btnCLEAR);
		btnCLEAR.setForeground(Color.DARK_GRAY);
		btnCLEAR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				txtDate.setText("");
				
			}
		});
		btnCLEAR.setBackground(Color.LIGHT_GRAY);
		
		JButton btnBACK = new JButton("BACK");
		btnBACK.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnBACK.setBounds(358, 358, 154, 23);
		panel_1.add(btnBACK);
		btnBACK.setForeground(Color.DARK_GRAY);
		btnBACK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Adminwelcome ad;
				try {
					ad = new Adminwelcome();
					ad.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnBACK.setBackground(Color.LIGHT_GRAY);
	}

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		CheckPayment window = new CheckPayment();
		window.frame.setVisible(true);
		
	}

	
	
	 
}	// TODO Auto-generated method stub
		
	

