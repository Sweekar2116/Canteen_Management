import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import service.LoginService;

public class loginpage {

	private JFrame frame;
	private JTextField txtEMAIL;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JPasswordField txtPASSWORD;
	private JPanel panel;
	private LoginService login;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				loginpage window = new loginpage();
				window.frame.setVisible(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error loading application: " + e.getMessage());
			}
		});
	}

	/**
	 * Create the application.
	 */
	public loginpage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		login = new LoginService();
		
		frame = new JFrame();
		frame.setTitle("Canteen Management System - User Login");
		frame.getContentPane().setBackground(new Color(240, 240, 245));
		frame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 16));
		frame.setBackground(new Color(255, 255, 255));
		frame.setBounds(100, 100, 1000, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSignUp = new JButton("SignUp");
		btnSignUp.setBounds(712, 406, 93, 36);
		btnSignUp.addActionListener(e -> {
			signuppage sign=new signuppage();
			sign.setVisible(true);
			frame.dispose();
		});
		btnSignUp.setBackground(new Color(52, 152, 219));
		btnSignUp.setForeground(Color.WHITE);
		btnSignUp.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSignUp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		frame.getContentPane().add(btnSignUp);
		
		txtEMAIL = new JTextField();
		txtEMAIL.setBounds(559, 138, 382, 36);
		txtEMAIL.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtEMAIL.setToolTipText("Enter your email address");
		frame.getContentPane().add(txtEMAIL);
		txtEMAIL.setColumns(10);
		
		lblNewLabel = new JLabel("E-Mail ID");
		lblNewLabel.setForeground(new Color(34, 35, 85));
		lblNewLabel.setBounds(559, 97, 97, 21);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		frame.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("PASSWORD");
		lblNewLabel_1.setForeground(new Color(34, 35, 85));
		lblNewLabel_1.setBounds(559, 203, 136, 21);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		frame.getContentPane().add(lblNewLabel_1);
		
		txtPASSWORD = new JPasswordField();
		txtPASSWORD.setBounds(559, 249, 382, 36);
		txtPASSWORD.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtPASSWORD.setToolTipText("Enter your password");
		frame.getContentPane().add(txtPASSWORD);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBounds(579, 329, 116, 29);
		btnLogin.addActionListener(e -> handleLogin());
		btnLogin.setBackground(new Color(34, 35, 85));
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		frame.getContentPane().add(btnLogin);
		
		// Add Enter key support
		KeyListener enterKeyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					handleLogin();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
		};
		txtEMAIL.addKeyListener(enterKeyListener);
		txtPASSWORD.addKeyListener(enterKeyListener);
		
		JLabel lblNewLabel_2 = new JLabel("Don't have an account? Create One");
		lblNewLabel_2.setForeground(new Color(34, 35, 85));
		lblNewLabel_2.setBounds(639, 382, 246, 13);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		frame.getContentPane().add(lblNewLabel_2);
		
		JButton btnForgotPassword = new JButton("Forgot Password");
		btnForgotPassword.setForeground(Color.WHITE);
		btnForgotPassword.setBounds(778, 330, 160, 27);
		btnForgotPassword.addActionListener(e -> {
			ForgotPassword menu=new ForgotPassword();
			menu.setVisible(true);
			frame.dispose();
		});
		btnForgotPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnForgotPassword.setBackground(new Color(230, 126, 34));
		btnForgotPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		frame.getContentPane().add(btnForgotPassword);
		
		panel = new JPanel();
		panel.setBackground(new Color(52, 73, 94));
		panel.setBounds(0, -25, 509, 556);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblWelcome = new JLabel("CANTEEN");
		lblWelcome.setForeground(Color.WHITE);
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 32));
		lblWelcome.setBounds(50, 100, 400, 50);
		panel.add(lblWelcome);
		
		JLabel lblMgmtSystem = new JLabel("Management System");
		lblMgmtSystem.setForeground(new Color(52, 152, 219));
		lblMgmtSystem.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblMgmtSystem.setBounds(50, 150, 400, 40);
		panel.add(lblMgmtSystem);
		
		JLabel lblLoginTitle = new JLabel("User Login");
		lblLoginTitle.setForeground(new Color(34, 35, 85));
		lblLoginTitle.setFont(new Font("Tahoma", Font.BOLD, 36));
		lblLoginTitle.setBounds(676, 11, 250, 44);
		frame.getContentPane().add(lblLoginTitle);
		
		JLabel lblQuote = new JLabel("\"Manage your canteen efficiently\"");
		lblQuote.setForeground(Color.WHITE);
		lblQuote.setFont(new Font("Tahoma", Font.ITALIC, 14));
		lblQuote.setBounds(50, 250, 400, 30);
		panel.add(lblQuote);
	}

	private void handleLogin() {
		String email = txtEMAIL.getText().trim();
		String password = new String(txtPASSWORD.getPassword());
		
		if (email.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		try {
			if (!login.validateEmail(email)) {
				JOptionPane.showMessageDialog(null, "Please enter a valid email address", "Validation Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if (!login.validateLogin(email, password)) {
				JOptionPane.showMessageDialog(null, "Invalid login credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int status = login.userLogin(email, password);
			if (status > 0) {
				Welcome menu = new Welcome();
				menu.setVisible(true);
				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Invalid email or password", "Login Error", JOptionPane.ERROR_MESSAGE);
			}
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Login error: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setVisible(boolean b) {
		loginpage window = new loginpage();
		window.frame.setVisible(true);
	}
}
