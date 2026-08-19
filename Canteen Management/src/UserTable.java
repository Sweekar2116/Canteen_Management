

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Data.Item;
import Data.User;
import service.LoginService;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import util.ImageUtil;

public class UserTable {

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
					UserTable window = new UserTable();
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
	public UserTable() throws ClassNotFoundException {
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
		panel.setBounds(10, 11, 966, 520);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		tableUSER = new JTable();
		tableUSER.setBounds(555, 5, 0, 0);
		panel.add(tableUSER);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(98, 37, 811, 247);
		panel.add(scrollPane);
		
		tableUSER = new JTable();
		scrollPane.setViewportView(tableUSER);
		tableUSER.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"USER_ID", "NAME", "E-MAIL", "PHONE", "PASSWORD"
			}
	

		));
		
		JLabel lblNewLabel = new JLabel("USER DETAILS");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBackground(Color.PINK);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(437, 5, 166, 21);
		panel.add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setBackground(Color.DARK_GRAY);
		btnNewButton_1.addActionListener(new ActionListener() {
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
		btnNewButton_1.setIcon(ImageUtil.loadImage("homeicon.jpg"));
		btnNewButton_1.setBounds(839, 313, 70, 44);
		panel.add(btnNewButton_1);
		
			 
	}		

public void insertRow() throws ClassNotFoundException {
	 DefaultTableModel model=(DefaultTableModel)tableUSER.getModel();
	 LoginService userDetails=new LoginService();
	 ArrayList<User> userList=new ArrayList<>();
	 userList=userDetails.userDetails();
	 Object[] rows=new Object[100];
	 for(int i=0;i<userList.size();i++) {
		 rows[0]=userList.get(i).getUserId();
		 rows[1]=userList.get(i).getUserName();
		 rows[2]=userList.get(i).getUserEmail();
		 rows[3]=userList.get(i).getUserPhone();
		 rows[4]=userList.get(i).getUserPassword();
		 model.addRow(rows);
	 }
}

public void setVisible(boolean b) throws ClassNotFoundException  {
	// TODO Auto-generated method stub
	UserTable window =new UserTable();
	window.frame.setVisible(true);
	
	
}
	}


		 
