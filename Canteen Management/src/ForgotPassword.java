import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import service.LoginService;
import util.ImageUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ForgotPassword extends JFrame {

	private JPanel contentPane;
	private JTextField txtEMAIL;
	private JPasswordField txtPASSWORD;
	private JPasswordField txtCONFIRMPASS;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ForgotPassword frame = new ForgotPassword();
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
	public ForgotPassword() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 568);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(192, 192, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		Image img=new ImageIcon(this.getClass().getResource("/login.jpg")).getImage();
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Forgot Password?");
		lblNewLabel_1.setForeground(new Color(34, 35, 85));
		lblNewLabel_1.setBounds(615, 24, 230, 30);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 24));
		contentPane.add(lblNewLabel_1);
		
		JLabel textfield1 = new JLabel("E-Mail ID");
		textfield1.setForeground(new Color(34, 35, 85));
		textfield1.setBounds(537, 117, 126, 19);
		textfield1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		contentPane.add(textfield1);
		
		txtEMAIL = new JTextField();
		txtEMAIL.setBounds(715, 117, 216, 20);
		contentPane.add(txtEMAIL);
		txtEMAIL.setColumns(10);
		
		JLabel txtfield2 = new JLabel("New Password");
		txtfield2.setForeground(new Color(34, 35, 85));
		txtfield2.setBounds(537, 171, 158, 19);
		txtfield2.setFont(new Font("Times New Roman", Font.BOLD, 20));
		contentPane.add(txtfield2);
		
		txtPASSWORD = new JPasswordField();
		txtPASSWORD.setBounds(715, 171, 216, 20);
		contentPane.add(txtPASSWORD);
		
		JLabel txtCONFIRM = new JLabel("Confirm Password");
		txtCONFIRM.setForeground(new Color(34, 35, 85));
		txtCONFIRM.setBounds(537, 231, 158, 19);
		txtCONFIRM.setFont(new Font("Times New Roman", Font.BOLD, 20));
		contentPane.add(txtCONFIRM);
		
		txtCONFIRMPASS = new JPasswordField();
		txtCONFIRMPASS.setBounds(715, 231, 216, 20);
		contentPane.add(txtCONFIRMPASS);
		
		JButton btnUPDATE = new JButton("Update");
		btnUPDATE.setForeground(Color.WHITE);
		btnUPDATE.setBounds(704, 301, 96, 30);
		btnUPDATE.setBackground(new Color(34, 35, 85));
		btnUPDATE.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnUPDATE.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String userEMAIL=txtEMAIL.getText();
				String userPASSWORD=txtPASSWORD.getText();
				String confirmPass=txtCONFIRMPASS.getText();
				LoginService forgotpassword=new LoginService();
				int status=0;
				int checkEmail=0;
				try {	
					if(forgotpassword.validateForgotPass(userEMAIL, userPASSWORD)) {
						if(forgotpassword.validateConfirmPass(userPASSWORD, confirmPass)) {
					checkEmail=forgotpassword.checkEmail(userEMAIL);
					
					status=forgotpassword.forgotpassword(userEMAIL, userPASSWORD);
					
					if(checkEmail>0) {
					if(status>0)
					{
						JOptionPane.showMessageDialog(null,"Password Updated Successfully","Update",JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						JOptionPane.showMessageDialog(null,"error ","password error",JOptionPane.ERROR_MESSAGE);
					}
					}else {
						JOptionPane.showMessageDialog(null,"Invalid email-id","user_email error",JOptionPane.ERROR_MESSAGE);
					}
					}else{
						JOptionPane.showMessageDialog(null,"Confirm password is not matching","Sign Up Error",JOptionPane.ERROR_MESSAGE);
					}
					
					}else {
						JOptionPane.showMessageDialog(null,"Please fill all details","user_email error",JOptionPane.ERROR_MESSAGE);
					}
						
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		contentPane.add(btnUPDATE);
		
		JButton btnLOGIN = new JButton("Back");
		btnLOGIN.setForeground(Color.DARK_GRAY);
		btnLOGIN.setBounds(773, 371, 96, 30);
		btnLOGIN.setBackground(Color.GRAY);
		btnLOGIN.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLOGIN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginpage menu=new loginpage();
				menu.setVisible(true);
				dispose();
			}
		});
		contentPane.add(btnLOGIN);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, -25, 509, 556);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		ImageIcon foodIcon = ImageUtil.loadImage("food.jpg");
		if (foodIcon != null) {
			ImageIcon scaledIcon = ImageUtil.scaleImage(foodIcon, 509, 534);
			lblNewLabel.setIcon(scaledIcon);
		}
		lblNewLabel.setBounds(0, 22, 509, 534);
		panel.add(lblNewLabel);
		
		JButton btnCLEAR = new JButton("Clear");
		btnCLEAR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPASSWORD.setText("");
				txtEMAIL.setText("");
				txtCONFIRMPASS.setText("");
			}
		});
		btnCLEAR.setBackground(new Color(34, 35, 85));
		btnCLEAR.setForeground(Color.WHITE);
		btnCLEAR.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCLEAR.setBounds(842, 301, 89, 29);
		contentPane.add(btnCLEAR);
	}
}
