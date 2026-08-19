package Admin;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Data.Item;
import Data.OrderDetails;
import service.LoginService;
import service.OrderService;
import util.ImageUtil;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

public class OrderTable {

	private static final String DbUtils = null;
	private JFrame frame;
	private JTable tableITEM;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JButton btnSEARCH;
	private JButton btnCLEAR;
	private JPanel panel;
	private JTextField txtORDERID;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrderTable window = new OrderTable();
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
	public OrderTable() throws ClassNotFoundException {
		initialize();
		insertRow();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(425, 61, 529, 287);
		frame.getContentPane().add(scrollPane);
		
		tableITEM = new JTable();
		scrollPane.setViewportView(tableITEM);
		tableITEM.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
				"ORDER ID", "USER","ORDER DATE"
                }
				));
		
		lblNewLabel = new JLabel("ORDER DETAILS");
		lblNewLabel.setForeground(Color.LIGHT_GRAY);
		lblNewLabel.setBounds(51, 49, 299, 33);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		frame.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Enter the order id :");
		lblNewLabel_1.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_1.setBounds(51, 130, 299, 33);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 24));
		frame.getContentPane().add(lblNewLabel_1);
		
		panel = new JPanel();
		panel.setBackground(new Color(34, 35, 85));
		panel.setBounds(10, 11, 997, 555);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		txtORDERID = new JTextField();
		txtORDERID.setBounds(42, 186, 278, 38);
		panel.add(txtORDERID);
		txtORDERID.setColumns(10);
		
		btnSEARCH = new JButton("SEARCH");
		btnSEARCH.setBounds(118, 258, 138, 42);
		panel.add(btnSEARCH);
		btnSEARCH.setForeground(Color.DARK_GRAY);
		btnSEARCH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int orderId=Integer.parseInt(txtORDERID.getText()) ;
				OrderService orderservice2=new OrderService();
				orderservice2.saveOrderId(orderId);
				orderdItems menu;
				try {
					menu = new orderdItems();
					menu.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnSEARCH.setBackground(Color.LIGHT_GRAY);
		btnSEARCH.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		btnCLEAR = new JButton("CLEAR");
		btnCLEAR.setBounds(118, 332, 136, 33);
		panel.add(btnCLEAR);
		btnCLEAR.setForeground(Color.DARK_GRAY);
		btnCLEAR.setBackground(Color.LIGHT_GRAY);
		btnCLEAR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtORDERID.setText("");
				
				
			}
		});
		btnCLEAR.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Adminwelcome wel;
				try {
					wel = new Adminwelcome();
					wel.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		ImageIcon homeIcon = ImageUtil.loadImage("homeicon.jpg");
		if (homeIcon != null) {
			homeIcon = ImageUtil.scaleImage(homeIcon, 89, 53);
			btnNewButton.setIcon(homeIcon);
		}
		btnNewButton.setBounds(874, 447, 89, 53);
		panel.add(btnNewButton);
	}
	
	public void insertRow() throws ClassNotFoundException {
		 DefaultTableModel model=(DefaultTableModel)tableITEM.getModel();
		 OrderService orderService=new OrderService();
		 List<OrderDetails> itemList=new ArrayList<>();
		 itemList=orderService.getOrders();
		 Object[] rows=new Object[100];
		 for(int i=0;i<itemList.size();i++) {
			 rows[0]=itemList.get(i).getOrderId();
			 rows[1]=itemList.get(i).getUserEmail();
			 rows[2]=itemList.get(i).getOrderdDate();
			 model.addRow(rows);
			 
		 }
	}

	public void setVisible(boolean b) throws ClassNotFoundException  {
		// TODO Auto-generated method stub
		OrderTable window =new OrderTable();
		window.frame.setVisible(true);
		
		
	}
}
