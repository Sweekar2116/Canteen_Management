package Admin;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Data.Item;
import Data.Payment;
import service.LoginService;
import service.OrderService;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentTable {

	private JFrame frame;
	private JTable table;

	static List<Payment> paymentList=new ArrayList<>();
	
	private static String d="";
	private static int total=0;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PaymentTable window = new PaymentTable();
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
	public PaymentTable() throws ClassNotFoundException {
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
		panel.setForeground(new Color(0, 0, 0));
		panel.setBackground(new Color(24, 24, 69));
		panel.setBounds(0, 11, 986, 509);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(118, 74, 751, 272);
		panel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Bill Date", "Payment Id", "Order Id", "User Email", "Total"
			}
		));
		
		JLabel lblNewLabel = new JLabel("      PAYMENT DETAILS");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBackground(Color.RED);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblNewLabel.setBounds(313, 11, 311, 52);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("  TOTAL");
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setBackground(new Color(192, 192, 192));
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1.setBounds(609, 357, 97, 34);
		panel.add(lblNewLabel_1);
		
		OrderService os=new OrderService();
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setBounds(750, 357, 69, 26);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setText(""+total);
		
		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Adminwelcome ad;
				try {
					ad = new Adminwelcome();
					ad.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		btnNewButton.setForeground(Color.DARK_GRAY);
		btnNewButton.setBounds(887, 464, 89, 23);
		panel.add(btnNewButton);
	}

	public void insertRow() throws ClassNotFoundException {
		 DefaultTableModel model=(DefaultTableModel)table.getModel();
		 OrderService os=new OrderService();
		 
		 d=os.getDate();
		 paymentList=os.getPayment(d);
		 List<Integer> amounts=paymentList.stream().map(id->id.getTotal()).collect(Collectors.toList());
		 total=amounts.stream().mapToInt(Integer::intValue).sum();
			
		 
		 Object[] rows=new Object[100];
		 for(int i=0;i<paymentList.size();i++) {
			 rows[0]=paymentList.get(i).getBillDate();
			 rows[1]=paymentList.get(i).getPaymentId();
			 rows[2]=paymentList.get(i).getOrderId();
			 rows[3]=paymentList.get(i).getUserEmail();
			 rows[4]=paymentList.get(i).getTotal();
			 model.addRow(rows); 
		 }
	}

	public void setVisible(boolean b) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		PaymentTable window = new PaymentTable();
		window.frame.setVisible(true);
	}
}
