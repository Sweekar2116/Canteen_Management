import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Data.Cart;
import service.LoginService;
import service.OrderService;
import service.priceService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import util.ImageUtil;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class Menu2 {

	private JFrame frame;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu2 window = new Menu2();
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
	public Menu2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		LoginService service=new LoginService();
		priceService price=new priceService();
		 OrderService cart=new OrderService();
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setBounds(100, 100, 1200, 668);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(26, 27, 72));
		panel.setBounds(10, 11, 1155, 587);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(46, 23, 278, 309);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(30, 33, 227, 215);
		ImageIcon parotaIcon = ImageUtil.loadImage("parota.jpg");
		if (parotaIcon != null) {
			lblNewLabel.setIcon(parotaIcon);
		}
		panel_1.add(lblNewLabel);
		
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		comboBox.setBounds(100, 277, 42, 22);
		panel_1.add(comboBox);
		
		
		JButton btnNewButton = new JButton("Add to Cart");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int parotaPrice=price.price("parota");
				String val=(String) comboBox.getSelectedItem();
				
				if(price.checkQuantity("parota", Integer.parseInt(val))) {
				cart.addItem(new Cart("parota", parotaPrice,Integer.parseInt(val)));
				
				CartDetails cart;
				try {
					cart = new CartDetails();
					cart.setVisible(true);
					
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				}else {
					JOptionPane.showMessageDialog(null,"Quantity is not available..Enter less quantity","Sign Up Error",JOptionPane.ERROR_MESSAGE);
					}

				}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton.setBounds(158, 279, 113, 23);
		panel_1.add(btnNewButton);
		
		JLabel lblNewLabel_3 = new JLabel("Quantity");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3.setBounds(10, 279, 74, 19);
		panel_1.add(lblNewLabel_3);
		
		
		JLabel lblNewLabel_4 = new JLabel("  PRICE");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_4.setBounds(10, 245, 63, 23);
		panel_1.add(lblNewLabel_4);
		
		JLabel lblPAROTAPRICE = new JLabel("");
		lblPAROTAPRICE.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblPAROTAPRICE.setBounds(100, 245, 120, 23);
		panel_1.add(lblPAROTAPRICE);
		lblPAROTAPRICE.setText(""+price.price("parota"));
		
		JLabel lblNewLabel_5 = new JLabel("  PAROTA");
		lblNewLabel_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_5.setBounds(86, 0, 99, 25);
		panel_1.add(lblNewLabel_5);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(427, 23, 295, 309);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		comboBox_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox_1.setBounds(95, 277, 57, 22);
		panel_2.add(comboBox_1);
		
		JButton btnNewButton_1 = new JButton("Add to Cart");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int frenchtoastPrice=price.price("french toast");
				String val=(String) comboBox_1.getSelectedItem();
				
				if(price.checkQuantity("french toast", Integer.parseInt(val))) {
				cart.addItem(new Cart("french toast", frenchtoastPrice,Integer.parseInt(val)));
				CartDetails cart;
				try {
					cart = new CartDetails();
					cart.setVisible(true);
					
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				JOptionPane.showMessageDialog(null,"Quantity is not available..Enter less quantity","Sign Up Error",JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton_1.setBounds(172, 275, 113, 23);
		panel_2.add(btnNewButton_1);
		
		JLabel lblNewLabel_1 = new JLabel("");
		ImageIcon frenchIcon = ImageUtil.loadImage("frenchtoast.jpg");
		if (frenchIcon != null) {
			lblNewLabel_1.setIcon(frenchIcon);
		}
		lblNewLabel_1.setBounds(34, 35, 251, 213);
		panel_2.add(lblNewLabel_1);
		
		JLabel lblNewLabel_3_1 = new JLabel("Quantity");
		lblNewLabel_3_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3_1.setBounds(10, 279, 74, 19);
		panel_2.add(lblNewLabel_3_1);
		
		
		
		JLabel lblFRENCHPRICE = new JLabel("");
		lblFRENCHPRICE.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblFRENCHPRICE.setBounds(114, 245, 113, 19);
		panel_2.add(lblFRENCHPRICE);
		lblFRENCHPRICE.setText(""+price.price("french toast"));
		
		
		JLabel lblNewLabel_4_1 = new JLabel("  PRICE");
		lblNewLabel_4_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_4_1.setBounds(21, 245, 63, 23);
		panel_2.add(lblNewLabel_4_1);
		
		JLabel lblNewLabel_5_1 = new JLabel("  FRENCH TOAST");
		lblNewLabel_5_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_5_1.setBounds(72, 11, 175, 25);
		panel_2.add(lblNewLabel_5_1);
		
		JButton btnHOME = new JButton("");
		btnHOME.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Welcome wel=new Welcome();
				wel.setVisible(true);
				
			
			}
		});
		ImageIcon homeIcon = ImageUtil.loadImage("homeicon.jpg");
		if (homeIcon != null) {
			homeIcon = ImageUtil.scaleImage(homeIcon, 61, 53);
			btnHOME.setIcon(homeIcon);
		}
		btnHOME.setBounds(1046, 477, 61, 53);
		panel.add(btnHOME);
		
		JPanel panel_2_1 = new JPanel();
		panel_2_1.setBounds(823, 23, 295, 309);
		panel.add(panel_2_1);
		panel_2_1.setLayout(null);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		comboBox_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox_2.setBounds(106, 277, 53, 22);
		panel_2_1.add(comboBox_2);
		
		JButton btnNewButton_2 = new JButton("Add to Cart");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int chapathiPrice=price.price("chapathi");
				String val=(String) comboBox_2.getSelectedItem();
				
				if(price.checkQuantity("chapathi", Integer.parseInt(val))) {
				
				cart.addItem(new Cart("chapathi", chapathiPrice,Integer.parseInt(val)));
				CartDetails cart;
				try {
					cart = new CartDetails();
					cart.setVisible(true);
					
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				}else {
					JOptionPane.showMessageDialog(null,"Quantity is not available..Enter less quantity","Sign Up Error",JOptionPane.ERROR_MESSAGE);
					}

			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton_2.setBounds(172, 275, 113, 23);
		panel_2_1.add(btnNewButton_2);
		
		JLabel lblNewLabel_2 = new JLabel("");
		ImageIcon chapathiIcon = ImageUtil.loadImage("chapathi.jpg");
		if (chapathiIcon != null) {
			chapathiIcon = ImageUtil.scaleImage(chapathiIcon, 249, 215);
			lblNewLabel_2.setIcon(chapathiIcon);
		}
		lblNewLabel_2.setBounds(36, 31, 249, 215);
		panel_2_1.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3_1_1 = new JLabel("Quantity");
		lblNewLabel_3_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3_1_1.setBounds(20, 279, 74, 19);
		panel_2_1.add(lblNewLabel_3_1_1);
		
		
		
		JLabel lblCHAPATHIPRICE = new JLabel("");
		lblCHAPATHIPRICE.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblCHAPATHIPRICE.setBounds(106, 250, 123, 14);
		panel_2_1.add(lblCHAPATHIPRICE);
		lblCHAPATHIPRICE.setText(""+price.price("chapathi"));
		
		JLabel lblNewLabel_4_2 = new JLabel("  PRICE");
		lblNewLabel_4_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_4_2.setBounds(20, 245, 63, 23);
		panel_2_1.add(lblNewLabel_4_2);
		
		JLabel lblNewLabel_5_2 = new JLabel("  CHAPATHI");
		lblNewLabel_5_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_5_2.setBounds(95, 11, 113, 25);
		panel_2_1.add(lblNewLabel_5_2);
		
		JButton btnBACK = new JButton("");
		btnBACK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu menu=new menu();
				menu.setVisible(true);
				
			}

		});
		ImageIcon backArrow = ImageUtil.loadImage("arrow grey (1).jpg");
		if (backArrow != null) {
			btnBACK.setIcon(backArrow);
		}
		btnBACK.setText(backArrow == null ? "← BACK" : "");
		btnBACK.setBounds(46, 478, 79, 52);
		panel.add(btnBACK);
	}

	
	

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		Menu2 window = new Menu2();
		window.frame.setVisible(true);
		
	}
}
