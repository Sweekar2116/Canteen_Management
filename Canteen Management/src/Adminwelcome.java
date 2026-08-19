
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import service.LoginService;

public class Adminwelcome {

	private JFrame frame;
	private JTextField txtWelcomeAdmin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Adminwelcome window = new Adminwelcome();
				window.frame.setVisible(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error loading admin dashboard: " + e.getMessage());
			}
		});
	}

	/**
	 * Create the application.
	 * @throws ClassNotFoundException 
	 */
	public Adminwelcome() throws ClassNotFoundException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws ClassNotFoundException 
	 */
	private void initialize() throws ClassNotFoundException {
		LoginService userCount=new LoginService();
		LoginService itemCount=new LoginService();
		LoginService orderCount=new LoginService();
		frame = new JFrame();
		frame.setTitle("Canteen Management System - Admin Dashboard");
		frame.setBounds(100, 100, 1000, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 240, 245));
		panel.setBounds(0, 0, 986, 531);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, -23, 320, 554);
		panel_1.setBackground(new Color(34, 35, 85));
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		txtWelcomeAdmin = new JTextField();
		txtWelcomeAdmin.setFont(new Font("Tahoma", Font.BOLD, 24));
		txtWelcomeAdmin.setText("WELCOME ADMIN!");
		txtWelcomeAdmin.setBounds(10, 38, 254, 45);
		txtWelcomeAdmin.setEditable(false);
		txtWelcomeAdmin.setBackground(new Color(34, 35, 85));
		txtWelcomeAdmin.setForeground(Color.WHITE);
		panel_1.add(txtWelcomeAdmin);
		txtWelcomeAdmin.setColumns(10);
		
		JButton btnUSERDET = new JButton("USER DETAILS");
		btnUSERDET.setBounds(10, 129, 242, 45);
		panel_1.add(btnUSERDET);
		btnUSERDET.addActionListener(e -> {
			try {
				UserTable table = new UserTable();
				table.setVisible(true);
			} catch (ClassNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "Error loading user table: " + e1.getMessage());
			}
		});
		btnUSERDET.setBackground(Color.GRAY);
		btnUSERDET.setForeground(Color.WHITE);
		btnUSERDET.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnUSERDET.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		JButton btnITEMDET = new JButton("ITEM DETAILS");
		btnITEMDET.setBounds(10, 208, 242, 45);
		panel_1.add(btnITEMDET);
		btnITEMDET.addActionListener(e -> {
			try {
				ItemTable menu = new ItemTable();
				menu.setVisible(true);
			} catch (ClassNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "Error loading item table: " + e1.getMessage());
			}
		});
		btnITEMDET.setBackground(Color.GRAY);
		btnITEMDET.setForeground(Color.WHITE);
		btnITEMDET.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnITEMDET.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		
		JButton btnORDERDET = new JButton("ORDER DETAILS");
		btnORDERDET.setBounds(10, 289, 242, 45);
		panel_1.add(btnORDERDET);
		btnORDERDET.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnORDERDET.setForeground(Color.WHITE);
		btnORDERDET.addActionListener(e -> {
			try {
				OrderTable table = new OrderTable();
				table.setVisible(true);
			} catch (ClassNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "Error loading order table: " + e1.getMessage());
			}
		});
		btnORDERDET.setBackground(Color.GRAY);
		btnORDERDET.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		JButton btnCheckPayment = new JButton("CHECK PAYMENT");
		btnCheckPayment.addActionListener(e -> {
			CheckPayment ck=new CheckPayment();
			ck.setVisible(true);
		});
		btnCheckPayment.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnCheckPayment.setBackground(Color.GRAY);
		btnCheckPayment.setForeground(Color.WHITE);
		btnCheckPayment.setBounds(10, 368, 254, 45);
		btnCheckPayment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		panel_1.add(btnCheckPayment);
		
		JLabel lblNewLabel = new JLabel("Dashboard");
		lblNewLabel.setBounds(343, 11, 170, 33);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setForeground(new Color(34, 35, 85));
		panel.add(lblNewLabel);
		
		JButton btnLOGOUT = new JButton("LOGOUT");
		btnLOGOUT.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				Mainpage main=new Mainpage();
				main.setVisible(true);
				frame.dispose();
			}
		});
		btnLOGOUT.setBounds(873, 487, 103, 33);
		btnLOGOUT.setForeground(Color.WHITE);
		btnLOGOUT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		panel.add(btnLOGOUT);
		btnLOGOUT.setBackground(new Color(34, 35, 85));
		btnLOGOUT.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(215, 244, 213));
		panel_2.setBounds(356, 102, 245, 164);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("USER");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel_1.setBounds(10, 11, 90, 29);
		lblNewLabel_1.setForeground(new Color(34, 35, 85));
		panel_2.add(lblNewLabel_1);
		
		JLabel lblUSER = new JLabel("");
		lblUSER.setForeground(new Color(21, 21, 64));
		lblUSER.setFont(new Font("Times New Roman", Font.BOLD, 90));
		lblUSER.setBounds(70, 37, 108, 102);
		panel_2.add(lblUSER);
		lblUSER.setText(""+userCount.userCount());
		
		JPanel panel_2_1 = new JPanel();
		panel_2_1.setBackground(new Color(191, 191, 223));
		panel_2_1.setBounds(683, 102, 245, 164);
		panel.add(panel_2_1);
		panel_2_1.setLayout(null);
		
		JLabel lblNewLabel_1_1 = new JLabel("ITEM");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel_1_1.setBounds(10, 11, 90, 29);
		lblNewLabel_1_1.setForeground(new Color(34, 35, 85));
		panel_2_1.add(lblNewLabel_1_1);
		
		JLabel lblITEM = new JLabel("  2");
		lblITEM.setForeground(new Color(21, 21, 64));
		lblITEM.setFont(new Font("Times New Roman", Font.BOLD, 90));
		lblITEM.setBounds(102, 34, 108, 102);
		panel_2_1.add(lblITEM);
		lblITEM.setText(""+itemCount.itemCount());
		
		JPanel panel_2_1_1 = new JPanel();
		panel_2_1_1.setBackground(new Color(255, 200, 200));
		panel_2_1_1.setBounds(531, 312, 245, 164);
		panel.add(panel_2_1_1);
		panel_2_1_1.setLayout(null);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("ORDER");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel_1_1_1.setBounds(10, 11, 90, 29);
		lblNewLabel_1_1_1.setForeground(new Color(34, 35, 85));
		panel_2_1_1.add(lblNewLabel_1_1_1);
		
		JLabel lblORDER = new JLabel("  2");
		lblORDER.setForeground(new Color(21, 21, 64));
		lblORDER.setFont(new Font("Times New Roman", Font.BOLD, 90));
		lblORDER.setBounds(80, 51, 108, 102);
		panel_2_1_1.add(lblORDER);
		lblORDER.setText(""+orderCount.orderCount());
	}

	
	public void setVisible(boolean b) throws ClassNotFoundException {
		
		Adminwelcome window = new Adminwelcome();
		window.frame.setVisible(true);
		
	}
}
