package Admin;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Adminwelcome window = new Adminwelcome();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		frame.setBounds(100, 100, 1000, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(192, 192, 192));
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
		txtWelcomeAdmin.setText("Welcome Admin");
		txtWelcomeAdmin.setBounds(10, 38, 254, 45);
		panel_1.add(txtWelcomeAdmin);
		txtWelcomeAdmin.setColumns(10);
		
		JButton btnUSERDET = new JButton("USER DETAILS");
		btnUSERDET.setBounds(10, 129, 242, 45);
		panel_1.add(btnUSERDET);
		btnUSERDET.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserTable table;
				try {
					table = new UserTable();
					table.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnUSERDET.setBackground(Color.GRAY);
		btnUSERDET.setFont(new Font("Times New Roman", Font.BOLD, 24));
		
		JButton btnITEMDET = new JButton("ITEM DETAILS");
		btnITEMDET.setBounds(10, 208, 242, 45);
		panel_1.add(btnITEMDET);
		btnITEMDET.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				ItemTable menu = null;
				try {
					menu = new ItemTable();
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
		btnITEMDET.setBackground(Color.GRAY);
		btnITEMDET.setFont(new Font("Times New Roman", Font.BOLD, 24));
		
		
		JButton btnORDERDET = new JButton("ORDER DETAILS");
		btnORDERDET.setBounds(10, 289, 242, 45);
		panel_1.add(btnORDERDET);
		btnORDERDET.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnORDERDET.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OrderTable table;
				try {
					table = new OrderTable();
					table.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnORDERDET.setBackground(Color.GRAY);
		
		JButton btnCheckPayment = new JButton("CHECK PAYMENT");
		btnCheckPayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckPayment ck=new CheckPayment();
				ck.setVisible(true);
			}
		});
		btnCheckPayment.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnCheckPayment.setBackground(Color.GRAY);
		btnCheckPayment.setBounds(10, 368, 254, 45);
		panel_1.add(btnCheckPayment);
		
		JLabel lblNewLabel = new JLabel("Dashboard");
		lblNewLabel.setBounds(343, 11, 170, 33);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(lblNewLabel);
		
		JButton btnLOGOUT = new JButton("LOGOUT");
		btnLOGOUT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class<?> mainpageClass = Class.forName("Mainpage");
					Object mainpage = mainpageClass.getDeclaredConstructor().newInstance();
					mainpageClass.getMethod("setVisible", boolean.class).invoke(mainpage, true);
					frame.dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnLOGOUT.setBounds(873, 487, 103, 33);
		btnLOGOUT.setForeground(Color.WHITE);
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
		panel_2_1_1.add(lblNewLabel_1_1_1);
		
		JLabel lblORDER = new JLabel("  2");
		lblORDER.setForeground(new Color(21, 21, 64));
		lblORDER.setFont(new Font("Times New Roman", Font.BOLD, 90));
		lblORDER.setBounds(80, 51, 108, 102);
		panel_2_1_1.add(lblORDER);
		lblORDER.setText(""+orderCount.orderCount());
	}

	
	public void setVisible(boolean b) throws ClassNotFoundException {
		
		// TODO Auto-generated method stub
		Adminwelcome window = new Adminwelcome();
		window.frame.setVisible(true);
		
	}
}
