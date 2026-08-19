

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

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import util.ImageUtil;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CartDetails {

	private JFrame frame;
	private JTable tableUSER;
	private JTable tableCART;
	private JTable table_1;

	static ArrayList<Cart> c=new ArrayList<Cart>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CartDetails window = new CartDetails();
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
	public CartDetails() throws ClassNotFoundException {
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
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(24, 26, 65));
		panel.setBounds(10, 11, 1110, 695);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		tableCART = new JTable();
		tableCART.setBounds(555, 5, 0, 0);
		panel.add(tableCART);
		
		JLabel lblITEMNAME = new JLabel("");
		lblITEMNAME.setBackground(Color.WHITE);
		lblITEMNAME.setBounds(90, 414, 87, 14);
		panel.add(lblITEMNAME);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				 DefaultTableModel model=(DefaultTableModel)tableUSER.getModel();
				 int myindex=tableCART.getSelectedRow();
				 
				 
				 lblITEMNAME.setText(model.getValueAt(myindex, 0).toString());
						 
				
			}
		});
		scrollPane.setBounds(90, 55, 811, 321);
		panel.add(scrollPane);
		
		tableUSER = new JTable();
		scrollPane.setViewportView(tableUSER);
		tableUSER.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				 "ITEM NAME", "ITEM QUANTITY", "ITEM PRICE","TOTAL"
			}
	

		));
		
		JLabel lblNewLabel = new JLabel("CART");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBackground(Color.PINK);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblNewLabel.setBounds(414, 5, 199, 39);
		panel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Back to menu");
		btnNewButton.setForeground(Color.DARK_GRAY);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu menu=new menu();
				menu.setVisible(true);
				
			}
		});
		
		
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton.setBounds(90, 459, 188, 39);
		panel.add(btnNewButton);
		
		OrderService order2=new OrderService();
		
		LoginService loginService=new LoginService();
		
		JLabel lblNewLabel_1 = new JLabel("Total Price");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setBackground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1.setBounds(780, 403, 74, 33);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setText("" + order2.calculateTotal(c));
		
		JButton order = new JButton("PLACE ORDER");
		order.setForeground(Color.DARK_GRAY);
		order.setFont(new Font("Tahoma", Font.BOLD, 20));
		order.setBackground(Color.LIGHT_GRAY);
		order.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int order_id=loginService.getUserId(loginService.email());
				int status=order2.addCart(c, order_id);
				
				if(status>0)
				{
					JOptionPane.showMessageDialog(null,"Order placed successfully");
					c.removeAll(c);
					Welcome m=new Welcome();
					m.setVisible(true);
				}
				else {					
					JOptionPane.showMessageDialog(null,"Error","Error",JOptionPane.ERROR_MESSAGE);					
				}
				
			
			}
		});
		order.setBounds(677, 459, 224, 39);
		panel.add(order);
		
		JLabel lblNewLabel_2 = new JLabel("  TOTAL");
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setBackground(Color.WHITE);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel_2.setBounds(611, 396, 111, 39);
		panel.add(lblNewLabel_2);
		
		JLabel lblREMOVEITEM = new JLabel("");
		lblREMOVEITEM.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblREMOVEITEM.setBounds(140, 536, 157, 32);
		panel.add(lblREMOVEITEM);

			 
	}		

	
	 
	
public void insertRow() throws ClassNotFoundException {
	 DefaultTableModel model=(DefaultTableModel)tableUSER.getModel();
	 
	 OrderService order=new OrderService();
	 c=order.getCart();
	 
	 //c=check();
	 
	 Object[] rows=new Object[50];
	 for(int i=0;i<c.size();i++) {
		 if(c.get(i).itemQtry!=0) {
		 rows[0]=c.get(i).getItemNAme();
		 rows[1]=c.get(i).getItemQtry();
		 rows[2]=c.get(i).getItemPrice();
		 int total=c.get(i).getItemQtry() * c.get(i).getItemPrice();
		 rows[3]=total;
		 model.addRow(rows);
		 } 
	 }
}

public void setVisible(boolean b) throws ClassNotFoundException  {
	// TODO Auto-generated method stub
	CartDetails window =new CartDetails();
	window.frame.setVisible(true);
	
	
}
	}


		 
