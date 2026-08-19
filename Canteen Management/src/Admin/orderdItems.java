package Admin;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Data.Cart;
import Data.Item;
import Data.User;
import service.LoginService;
import service.OrderService;
import service.priceService;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class orderdItems {

	private JFrame frame;
	private JTable tableUSER;
	private JTable table_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					orderdItems window = new orderdItems();
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
	public orderdItems() throws ClassNotFoundException {
		initialize();
		insertRow();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setBounds(100, 100, 1000, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(21, 21, 64));
		panel.setBounds(10, 11, 966, 509);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		tableUSER = new JTable();
		tableUSER.setBounds(555, 5, 0, 0);
		panel.add(tableUSER);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(75, 43, 826, 351);
		panel.add(scrollPane);
		
		tableUSER = new JTable();
		scrollPane.setViewportView(tableUSER);
		tableUSER.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ITEM_NAME", "QUANTITY"
			}
	

		));
		
		JLabel lblNewLabel = new JLabel("ORDER DETAILS");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBackground(Color.PINK);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(391, 11, 166, 21);
		panel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("BACK");
		btnNewButton.setForeground(new Color(24, 24, 69));
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OrderTable ord;
				try {
					ord = new OrderTable();
					ord.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		btnNewButton.setIcon(null);
		btnNewButton.setBounds(845, 448, 110, 48);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("CONFIRM ORDER");
		btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				 OrderService orderService=new OrderService();
				 priceService pService=new priceService();
				 int orderId=orderService.getOrderId();
				List<Cart> itemList=new ArrayList<>();
				 itemList=orderService.getOrderdItems(orderId);
				 pService.alterQty(itemList);
				 int result=pService.addPayment(orderId,itemList);
				 if(result>0) {
					 JOptionPane.showMessageDialog(null,"Order Confirmed","Update",JOptionPane.INFORMATION_MESSAGE);
					 OrderTable ot;
					try {
						ot = new OrderTable();
						
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				 }else {
					 JOptionPane.showMessageDialog(null,"Wrong","Update",JOptionPane.INFORMATION_MESSAGE);
				 }
				
			}
				
		});
		btnNewButton_1.setForeground(new Color(24, 24, 69));
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 25));
		btnNewButton_1.setBackground(Color.GRAY);
		btnNewButton_1.setBounds(373, 448, 311, 23);
		panel.add(btnNewButton_1);
		
			 
	}		

public void insertRow() throws ClassNotFoundException {
	 DefaultTableModel model=(DefaultTableModel)tableUSER.getModel();
	 
	 OrderService orderService=new OrderService();
	 int orderId=orderService.getOrderId();
	 List<Cart> itemList=new ArrayList<>();
	 itemList=orderService.getOrderdItems(orderId);
	 Object[] rows=new Object[100];
	 for(int i=0;i<itemList.size();i++) {
		 rows[0]=itemList.get(i).getItemNAme();
		 rows[1]=itemList.get(i).getItemQtry();
		 model.addRow(rows);
	 }
	 
}

public void setVisible(boolean b) throws ClassNotFoundException  {
	// TODO Auto-generated method stub
	orderdItems window =new orderdItems();
	window.frame.setVisible(true);
	
	
}
	}


		 
