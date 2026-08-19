package Admin;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Data.Item;
import service.LoginService;
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

public class ItemTable {

	private static final String DbUtils = null;
	private JFrame frame;
	private JTable tableITEM;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JTextField txtITEMNAME;
	private JTextField txtNEWPRICE;
	private JButton btnUPDATE;
	private JButton btnCLEAR;
	private JPanel panel;
	private JButton btnNewButton;
	private JTextField txtQUANTITY;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ItemTable window = new ItemTable();
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
	public ItemTable() throws ClassNotFoundException {
		initialize();
		insertRow();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1031, 614);
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
				"ITEM ID", "ITEM NAME", "ITEM PRICE","ITEM QUANTITY"
                }
				));
		
		lblNewLabel = new JLabel("    ITEM");
		lblNewLabel.setForeground(Color.LIGHT_GRAY);
		lblNewLabel.setBounds(107, 49, 156, 33);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		frame.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("ITEM NAME");
		lblNewLabel_1.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_1.setBounds(51, 130, 118, 33);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		frame.getContentPane().add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("NEW PRICE");
		lblNewLabel_2.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_2.setBounds(51, 208, 118, 33);
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 18));
		frame.getContentPane().add(lblNewLabel_2);
		
		txtITEMNAME = new JTextField();
		txtITEMNAME.setBounds(194, 138, 156, 20);
		frame.getContentPane().add(txtITEMNAME);
		txtITEMNAME.setColumns(10);
		
		txtNEWPRICE = new JTextField();
		txtNEWPRICE.setBounds(194, 216, 156, 20);
		frame.getContentPane().add(txtNEWPRICE);
		txtNEWPRICE.setColumns(10);
		
		panel = new JPanel();
		panel.setBackground(new Color(34, 35, 85));
		panel.setBounds(10, 11, 997, 555);
		frame.getContentPane().add(panel);
		
		JButton btnviewdatabase = new JButton("REFRESH");
		btnviewdatabase.setBounds(184, 438, 131, 27);
		btnviewdatabase.setForeground(Color.DARK_GRAY);
		btnviewdatabase.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnviewdatabase.setBackground(Color.LIGHT_GRAY);
		btnviewdatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					ItemTable itable=new ItemTable();
					itable.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel.setLayout(null);
		panel.add(btnviewdatabase);
		
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
			homeIcon = ImageUtil.scaleImage(homeIcon, 89, 56);
			btnNewButton.setIcon(homeIcon);
		}
		btnNewButton.setBounds(898, 488, 89, 56);
		panel.add(btnNewButton);
		
		txtQUANTITY = new JTextField();
		txtQUANTITY.setColumns(10);
		txtQUANTITY.setBounds(187, 275, 156, 27);
		panel.add(txtQUANTITY);
		
		JLabel lblNewLabel_2_1 = new JLabel("QUANTITY");
		lblNewLabel_2_1.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_2_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_2_1.setBounds(34, 274, 118, 33);
		panel.add(lblNewLabel_2_1);
		
		btnCLEAR = new JButton("CLEAR");
		btnCLEAR.setBounds(184, 387, 136, 23);
		panel.add(btnCLEAR);
		btnCLEAR.setForeground(Color.DARK_GRAY);
		btnCLEAR.setBackground(Color.LIGHT_GRAY);
		btnCLEAR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtITEMNAME.setText("");
				txtNEWPRICE.setText("");
				
			}
		});
		btnCLEAR.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		btnUPDATE = new JButton("UPDATE");
		btnUPDATE.setBounds(184, 332, 138, 23);
		panel.add(btnUPDATE);
		btnUPDATE.setForeground(Color.DARK_GRAY);
		btnUPDATE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemName=txtITEMNAME.getText();
				String itemPRICE=txtNEWPRICE.getText();
				String itemQUANTITY=txtQUANTITY.getText();
				LoginService priceupdate=new LoginService();
				LoginService checkName=new LoginService();
				int checkNAME=0;
				int status=0;
			try {	
					
					checkNAME=checkName.checkName(itemName);
					
					status=priceupdate.priceupdate(itemName, itemPRICE,itemQUANTITY);
					
					if(checkNAME>0) 
					{
					if(status>0)
					{
						JOptionPane.showMessageDialog(null,"Updated Successfully","Update",JOptionPane.INFORMATION_MESSAGE);
					
					}
					else
					{
						JOptionPane.showMessageDialog(null,"error ","error",JOptionPane.ERROR_MESSAGE);
					}
					}else {
						JOptionPane.showMessageDialog(null,"Invalid ITEM NAME","item_name error",JOptionPane.ERROR_MESSAGE);
					}
						
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnUPDATE.setBackground(Color.LIGHT_GRAY);
		btnUPDATE.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		
	}
	
	public void insertRow() throws ClassNotFoundException {
		 DefaultTableModel model=(DefaultTableModel)tableITEM.getModel();
		 LoginService itemDetails=new LoginService();
		 ArrayList<Item> itemList=new ArrayList<>();
		 itemList=itemDetails.itemDetails();
		 Object[] rows=new Object[itemList.size()];
		 for(int i=0;i<itemList.size();i++) {
			 rows[0]=itemList.get(i).getItemId();
			 rows[1]=itemList.get(i).getItemName();
			 rows[2]=itemList.get(i).getItemPrice();
			 rows[3]=itemList.get(i).getItemQty();
			 model.addRow(rows);
			 
			 
			 
		 }
	}

	public void setVisible(boolean b) throws ClassNotFoundException  {
		// TODO Auto-generated method stub
		ItemTable window =new ItemTable();
		window.frame.setVisible(true);
		
		
	}
}
