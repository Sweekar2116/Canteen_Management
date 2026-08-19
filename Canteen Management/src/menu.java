import java.awt.EventQueue;

import javax.swing.ImageIcon;
import util.ImageUtil;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Data.Cart;
import service.LoginService;
import service.OrderService;
import service.priceService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.*;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;

public class menu extends JFrame {

	private JPanel contentPane;
	/**
	 * @wbp.nonvisual location=-45,-36
	 */
	private final JInternalFrame internalFrame = new JInternalFrame("New JInternalFrame");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menu frame = new menu();
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
	
	
	 menu() {
		 
		
		 LoginService service=new LoginService();
		 
		 priceService price=new priceService();
		 
		 OrderService cart=new OrderService();
		 
			internalFrame.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1163, 858);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(128, 128, 128));
		panel.setBounds(0, 37, 1149, 743);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(34, 35, 85));
		panel_2.setBounds(10, 11, 1117, 731);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 11, 289, 250);
		panel_2.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(26, 37, 232, 148);
		Image img=new ImageIcon(this.getClass().getResource("/dosaa.jpg")).getImage();
		lblNewLabel_1.setIcon(new ImageIcon(img));
		panel_3.add(lblNewLabel_1);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(lblNewLabel_1, popupMenu);
		
		JLabel lblNewLabel_5 = new JLabel("  Dosa");
		lblNewLabel_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_5.setBounds(120, 5, 63, 21);
		panel_3.add(lblNewLabel_5);
		
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		comboBox.setBounds(91, 218, 45, 22);
		panel_3.add(comboBox);
		

		JLabel lblDOSAPRICE = new JLabel("");
		lblDOSAPRICE.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblDOSAPRICE.setBounds(124, 186, 94, 21);
		panel_3.add(lblDOSAPRICE);
		lblDOSAPRICE.setText(""+price.price("dosa"));
		
		
		JButton btnNewButton = new JButton("Add To Cart");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dosaPrice=price.price("dosa");
				String val=(String) comboBox.getSelectedItem();
				if(price.checkQuantity("dosa", Integer.parseInt(val))) {
				cart.addItem(new Cart("dosa", dosaPrice, Integer.parseInt(val)));
				CartDetails cart;
				try {
					cart = new CartDetails();
					cart.setVisible(true);
					dispose();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}else {
					JOptionPane.showMessageDialog(null,"Quantity is not available..Enter less quantity","Sign Up Error",JOptionPane.ERROR_MESSAGE);
					}

			
				
			}
		});
		btnNewButton.setIcon(new ImageIcon(""));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton.setBounds(157, 229, 122, 21);
		panel_3.add(btnNewButton);
		
		JLabel lblNewLabel_19 = new JLabel(" PRICE");
		lblNewLabel_19.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_19.setBounds(47, 186, 63, 21);
		panel_3.add(lblNewLabel_19);
		
		JLabel lblNewLabel_13 = new JLabel("Quantity");
		lblNewLabel_13.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_13.setBounds(10, 222, 71, 15);
		panel_3.add(lblNewLabel_13);
		
		
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(418, 11, 310, 250);
		panel_2.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(34, 39, 227, 143);
		Image img1=new ImageIcon(this.getClass().getResource("/idlii.jpg")).getImage();
		lblNewLabel_2.setIcon(new ImageIcon(img1));
		panel_4.add(lblNewLabel_2);
		Image img2=new ImageIcon(this.getClass().getResource("/idlii.jpg")).getImage();
		lblNewLabel_2.setIcon(new ImageIcon(img2));
		
		JLabel lblNewLabel_5_1 = new JLabel("  Idli");
		lblNewLabel_5_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_5_1.setBounds(123, 7, 59, 21);
		panel_4.add(lblNewLabel_5_1);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		comboBox_1.setBounds(88, 221, 49, 22);
		panel_4.add(comboBox_1);
		
		JButton btnNewButton_2 = new JButton("Add To Cart");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idliPrice=price.price("idli");
				String val=(String) comboBox_1.getSelectedItem();
				if(price.checkQuantity("idli", Integer.parseInt(val))) {
				
				cart.addItem(new Cart("idli", idliPrice, Integer.parseInt(val)));
				CartDetails cart;
				try {
					cart = new CartDetails();
					cart.setVisible(true);
					dispose();
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
		btnNewButton_2.setBounds(148, 218, 134, 21);
		panel_4.add(btnNewButton_2);
		
		JLabel lblNewLabel_20 = new JLabel("PRICE");
		lblNewLabel_20.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_20.setBounds(47, 193, 49, 14);
		panel_4.add(lblNewLabel_20);
		
		JLabel lblIDLIPRICE = new JLabel("");
		lblIDLIPRICE.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblIDLIPRICE.setBounds(142, 193, 81, 14);
		panel_4.add(lblIDLIPRICE);
		
		lblIDLIPRICE.setText(""+price.price("idli"));
		
		JLabel lblNewLabel_3 = new JLabel("Quantity");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3.setBounds(10, 221, 68, 18);
		panel_4.add(lblNewLabel_3);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(826, 11, 273, 250);
		panel_2.add(panel_5);
		panel_5.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setBounds(26, 46, 211, 136);
		Image img3=new ImageIcon(this.getClass().getResource("/pulav.jpg")).getImage();
		lblNewLabel_4.setIcon(new ImageIcon(img3));
		panel_5.add(lblNewLabel_4);
		
		JLabel lblNewLabel_6 = new JLabel("   Pulao");
		lblNewLabel_6.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_6.setBounds(97, 10, 80, 25);
		panel_5.add(lblNewLabel_6);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(88, 215, 44, 22);
		panel_5.add(comboBox_2);
		comboBox_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		
		
		JButton btnNewButton_3 = new JButton("Add To Cart");
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int pulaoPrice=price.price("pulao");
				String val=(String) comboBox_2.getSelectedItem();
				
				if(price.checkQuantity("pulao", Integer.parseInt(val))) {
				cart.addItem(new Cart("pulao", pulaoPrice, Integer.parseInt(val)));
				CartDetails cart;
				try {
					cart = new CartDetails();
					cart.setVisible(true);
					dispose();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				}else {
					JOptionPane.showMessageDialog(null,"Quantity is not available..Enter less quantity","Sign Up Error",JOptionPane.ERROR_MESSAGE);
					}

			}
		});
		btnNewButton_3.setBounds(142, 218, 122, 21);
		panel_5.add(btnNewButton_3);
		
		JLabel lblNewLabel_22 = new JLabel("  PRICE");
		lblNewLabel_22.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_22.setBounds(34, 193, 71, 14);
		panel_5.add(lblNewLabel_22);
		
		JLabel lblPULAOPRICE = new JLabel("");
		lblPULAOPRICE.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblPULAOPRICE.setBounds(132, 193, 119, 14);
		panel_5.add(lblPULAOPRICE);
		lblPULAOPRICE.setText("" + price.price("pulao"));
		
		JLabel lblNewLabel_3_1 = new JLabel("Quantity");
		lblNewLabel_3_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3_1.setBounds(10, 217, 68, 18);
		panel_5.add(lblNewLabel_3_1);
		
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(10, 343, 289, 250);
		panel_2.add(panel_6);
		panel_6.setLayout(null);
		
		JLabel lblNewLabel_7 = new JLabel("");
		lblNewLabel_7.setBounds(24, 34, 233, 152);
		Image img5=new ImageIcon(this.getClass().getResource("/vada.jpg")).getImage();
		lblNewLabel_7.setIcon(new ImageIcon(img5));
		panel_6.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("Vada");
		lblNewLabel_8.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_8.setBounds(107, 10, 72, 21);
		panel_6.add(lblNewLabel_8);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setBounds(88, 215, 48, 22);
		panel_6.add(comboBox_3);
		comboBox_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox_3.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		
		JButton btnNewButton_4 = new JButton("Add To Cart");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int vadaPrice=price.price("vada");
				String val=(String) comboBox_3.getSelectedItem();
				
				if(price.checkQuantity("vada", Integer.parseInt(val))) {
				cart.addItem(new Cart("vada", vadaPrice, Integer.parseInt(val)));
				CartDetails cart;
				try {
					cart = new CartDetails();
					cart.setVisible(true);
					dispose();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}else {
					JOptionPane.showMessageDialog(null,"Quantity is not available..Enter less quantity","Sign Up Error",JOptionPane.ERROR_MESSAGE);
					}

			}
		});

		btnNewButton_4.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton_4.setBounds(139, 218, 118, 21);
		panel_6.add(btnNewButton_4);
		
		JLabel lblNewLabel_24 = new JLabel("  PRICE");
		lblNewLabel_24.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_24.setBounds(34, 188, 63, 14);
		panel_6.add(lblNewLabel_24);
		
		JLabel lblVADAPRICE = new JLabel("");
		lblVADAPRICE.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblVADAPRICE.setBounds(129, 190, 105, 14);
		panel_6.add(lblVADAPRICE);
		lblVADAPRICE.setText(" "+price.price("vada"));
		
		JLabel lblNewLabel_3_2 = new JLabel("Quantity");
		lblNewLabel_3_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3_2.setBounds(10, 217, 68, 18);
		panel_6.add(lblNewLabel_3_2);
		
		
		
		JPanel panel_7 = new JPanel();
		panel_7.setBounds(418, 343, 310, 250);
		panel_2.add(panel_7);
		panel_7.setLayout(null);
		
		JLabel lblNewLabel_9 = new JLabel("   Upma");
		lblNewLabel_9.setBounds(112, 5, 74, 26);
		lblNewLabel_9.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		panel_7.add(lblNewLabel_9);
		
		JLabel lblNewLabel_10 = new JLabel("");
		lblNewLabel_10.setBounds(36, 42, 252, 140);
		Image img6=new ImageIcon(this.getClass().getResource("/upma.jpg")).getImage();
		lblNewLabel_10.setIcon(new ImageIcon(img6));
		panel_7.add(lblNewLabel_10);
		

		JComboBox comboBox_4 = new JComboBox();
		comboBox_4.setBounds(106, 215, 51, 22);
		panel_7.add(comboBox_4);
		comboBox_4.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox_4.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		
		
		JButton btnNewButton_5 = new JButton("Add To Cart");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int upmaPrice=price.price("upma");
				String val=(String) comboBox_4.getSelectedItem();
				if(price.checkQuantity("upma", Integer.parseInt(val))) {
				cart.addItem(new Cart("upma", upmaPrice, Integer.parseInt(val)));
				CartDetails cart;
				try {
					cart = new CartDetails();
					cart.setVisible(true);
					dispose();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}else {
					JOptionPane.showMessageDialog(null,"Quantity is not available..Enter less quantity","Sign Up Error",JOptionPane.ERROR_MESSAGE);
				}
			
			}
		});
		btnNewButton_5.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton_5.setBounds(167, 218, 121, 21);
		panel_7.add(btnNewButton_5);
		
		JLabel lblNewLabel_26 = new JLabel("  PRICE");
		lblNewLabel_26.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_26.setBounds(46, 193, 66, 14);
		panel_7.add(lblNewLabel_26);
		
		JLabel lblUPMAPRICE = new JLabel("");
		lblUPMAPRICE.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblUPMAPRICE.setBounds(149, 193, 113, 14);
		panel_7.add(lblUPMAPRICE);
		lblUPMAPRICE.setText("" + price.price("upma"));
		
		JLabel lblNewLabel_3_3 = new JLabel("Quantity");
		lblNewLabel_3_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3_3.setBounds(28, 217, 68, 18);
		panel_7.add(lblNewLabel_3_3);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBounds(815, 343, 284, 250);
		panel_2.add(panel_8);
		panel_8.setLayout(null);
		
		JLabel lblNewLabel_12 = new JLabel("");
		lblNewLabel_12.setBounds(20, 37, 238, 151);
		Image img7=new ImageIcon(this.getClass().getResource("/kesaribath.jpg")).getImage();
		lblNewLabel_12.setIcon(new ImageIcon(img7));
		panel_8.add(lblNewLabel_12);
		
		JLabel lblNewLabel_11 = new JLabel("   Kesaribath");
		lblNewLabel_11.setBounds(93, 10, 125, 23);
		lblNewLabel_11.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		panel_8.add(lblNewLabel_11);
		
		JComboBox comboBox_5 = new JComboBox();
		comboBox_5.setBounds(93, 215, 48, 22);
		panel_8.add(comboBox_5);
		comboBox_5.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox_5.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		
		JButton btnNewButton_6 = new JButton("Add To Cart");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int kesaribathPrice=price.price("kesaribath");
				String val=(String) comboBox_5.getSelectedItem();
				
				if(price.checkQuantity("kesaribath", Integer.parseInt(val))) {
				

				cart.addItem(new Cart("kesaribath", kesaribathPrice, Integer.parseInt(val)));
				CartDetails cart;
				try {
					cart = new CartDetails();
					cart.setVisible(true);
					dispose();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				}else {
					JOptionPane.showMessageDialog(null,"Quantity is not available..Enter less quantity","Sign Up Error",JOptionPane.ERROR_MESSAGE);
					}

			}
		});

		btnNewButton_6.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton_6.setBounds(151, 218, 123, 21);
		panel_8.add(btnNewButton_6);
		
		JLabel lblNewLabel_21 = new JLabel("PRICE");
		lblNewLabel_21.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_21.setBounds(30, 193, 69, 14);
		panel_8.add(lblNewLabel_21);
		
		JLabel lblKESARIBATHPRICE = new JLabel("");
		lblKESARIBATHPRICE.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblKESARIBATHPRICE.setBounds(130, 193, 88, 14);
		panel_8.add(lblKESARIBATHPRICE);
		lblKESARIBATHPRICE.setText(""+price.price("kesaribath"));
		
		JLabel lblNewLabel_3_4 = new JLabel("Quantity");
		lblNewLabel_3_4.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3_4.setBounds(20, 217, 68, 18);
		panel_8.add(lblNewLabel_3_4);
		
	
		
		JButton btnNEXT = new JButton("");
		btnNEXT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Menu2 mn=new Menu2();
				mn.setVisible(true);
				dispose();
			
		            
			}
		});
		ImageIcon nextArrow = ImageUtil.loadImage("arrow grey.jpg");
		if (nextArrow != null) {
			btnNEXT.setIcon(nextArrow);
		}
		btnNEXT.setText(nextArrow == null ? "NEXT →" : "");
		btnNEXT.setBounds(1018, 638, 81, 66);
		panel_2.add(btnNEXT);
		
		
		
		
		JLabel lblNewLabel = new JLabel("  MENU ");
		lblNewLabel.setForeground(new Color(26, 27, 72));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		lblNewLabel.setBounds(10, 0, 203, 50);
		contentPane.add(lblNewLabel);
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
