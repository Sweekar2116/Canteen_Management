
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import util.ImageUtil;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import service.LoginService;

import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class LoginAdmin {

	private JFrame frame;
	private JTextField txtEMAIL;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JPasswordField txtPASSWORD;
	private JLabel lblNewLabel_2;
	private JPanel panel;
	private JPanel panel_1;
	private JLabel lblNewLabel_3;
	private JButton btnBack;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginAdmin window = new LoginAdmin();
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
	public LoginAdmin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 16));
		frame.setBackground(new Color(255, 255, 255));
		frame.setBounds(100, 100, 1000, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image img=new ImageIcon(this.getClass().getResource("/login.jpg")).getImage();
		frame.getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(new Color(192, 192, 192));
		panel.setBounds(0, 0, 986, 531);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		panel_1 = new JPanel();
		panel_1.setBounds(0, -25, 509, 556);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		lblNewLabel_3 = new JLabel("New label");
		ImageIcon adminFoodIcon = ImageUtil.loadImage("food.jpg");
		if (adminFoodIcon != null) {
			lblNewLabel_3.setIcon(adminFoodIcon);
		}
		lblNewLabel_3.setBounds(0, 11, 542, 545);
		panel_1.add(lblNewLabel_3);
		
		lblNewLabel_2 = new JLabel("ADMIN");
		lblNewLabel_2.setBounds(719, 25, 129, 44);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setBackground(Color.RED);
		lblNewLabel_2.setForeground(new Color(34, 35, 85));
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 36));
		
		lblNewLabel = new JLabel("E-Mail ID");
		lblNewLabel.setForeground(new Color(34, 35, 85));
		lblNewLabel.setBounds(592, 95, 97, 41);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		txtEMAIL = new JTextField();
		txtEMAIL.setBounds(592, 147, 317, 36);
		panel.add(txtEMAIL);
		txtEMAIL.setColumns(10);
		
		lblNewLabel_1 = new JLabel("PASSWORD");
		lblNewLabel_1.setForeground(new Color(34, 35, 85));
		lblNewLabel_1.setBounds(592, 214, 137, 21);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		txtPASSWORD = new JPasswordField();
		txtPASSWORD.setBounds(592, 260, 317, 36);
		panel.add(txtPASSWORD);
		
		JButton btnLOGIN = new JButton("LOGIN");
		btnLOGIN.setForeground(Color.WHITE);
		btnLOGIN.setBounds(699, 349, 116, 29);
		panel.add(btnLOGIN);
		btnLOGIN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String User_email=txtEMAIL.getText();
				String Password=txtPASSWORD.getText();
				LoginService login=new LoginService();
				if(login.validateLogin(User_email, Password)) {
					if(login.validateEmail(User_email)) {
				if(User_email.contains("admin@gmail.com") && Password.contains("admin12"))
				{
					Adminwelcome menu = null;
					try {
						menu = new Adminwelcome();
						menu.setVisible(true);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
			}
				else {
					
					JOptionPane.showMessageDialog(null,"Invalid login details","login Error",JOptionPane.ERROR_MESSAGE);
					
				}
					}else {
						JOptionPane.showMessageDialog(null,"Invalid email","login Error",JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(null,"Please fill the details","login Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnLOGIN.setBackground(new Color(34, 35, 85));
		btnLOGIN.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		btnBack = new JButton("BACK");
		btnBack.setForeground(Color.WHITE);
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBack.setBackground(new Color(34, 35, 85));
		btnBack.setBounds(699, 412, 116, 29);
		panel.add(btnBack);
	}

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		LoginAdmin window = new LoginAdmin();
		window.frame.setVisible(true);
	}
}
