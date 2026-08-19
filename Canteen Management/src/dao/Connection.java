package dao;

import Data.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class Connection {

	java.sql.Connection con;
	PreparedStatement st;
	
	// Database configuration
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/cms";
	private static final String USER = "root";
	private static final String PASSWORD = "password";
	
	
	public void connection() throws SQLException, ClassNotFoundException {
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "MySQL Driver not found! Please ensure MySQL connector is installed.", "Driver Error", JOptionPane.ERROR_MESSAGE);
			throw e;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Database connection failed! Ensure MySQL server is running.\n\nError: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}

	public void closeConnection() throws SQLException {
		if (con != null && !con.isClosed()) {
			con.close();
		}
	}

	public int insertUser(String userName, String userEmail, long phone, String password)
			throws ClassNotFoundException {

		try {
			connection();
			st = con.prepareStatement("insert into user(user_name,user_email,user_phone,user_password)values(?,?,?,?)");
			st.setString(1, userName);
			st.setString(2, userEmail);
			st.setLong(3, phone);
			st.setString(4, password);

			int result = st.executeUpdate();
			closeConnection();
			return result;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error inserting user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
		}

		return 0;
	}

	public int login(String userEmail, String password) throws ClassNotFoundException, SQLException {
		try {
			connection();
			st = con.prepareStatement("select * from user where user_email=? and user_password=?");
			st.setString(1, userEmail);
			st.setString(2, password);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				closeConnection();
				return 1;
			}
			closeConnection();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Login error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return 0;

	}

	public int check(String userEmail) throws ClassNotFoundException {
		try {
			connection();
			st = con.prepareStatement("Select user_email from user where user_email=?");
			st.setString(1, userEmail);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;

	}

	
	
	public int checkName(String itemName) throws ClassNotFoundException {
		try {
			connection();
			st = con.prepareStatement("Select item_name from item where item_name=?");
			st.setString(1, itemName);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;

	}
	public int forgotpassword(String userEmail, String password) throws ClassNotFoundException {
		try {
			connection();
			st = con.prepareStatement("update user set user_password=? where user_email=?");
			st.setString(1, password);
			st.setString(2, userEmail);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;

	}

	public int ItemDetails(String itemName, Float itemPrice) throws ClassNotFoundException {
		try {
			int tableITEM = 0;
			connection();
			st = con.prepareStatement("select * from item");
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				tableITEM = rs.getInt(1);
			}
			return tableITEM;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}
	public int userDetails(String itemName, Float itemPrice) throws ClassNotFoundException {
		try {
			int tableUSER = 0;
			connection();
			st = con.prepareStatement("select * from user");
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				tableUSER = rs.getInt(1);
			}
			return tableUSER;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

	public int priceupdate(String itemName, String itemPRICE,String itemQUANTITY) throws ClassNotFoundException {
		try {
			connection();
			st = con.prepareStatement("update item set item_price=?,item_qty=? where item_name=?");
			st.setString(1, itemPRICE);
			st.setString(2, itemQUANTITY);
			st.setString(3, itemName);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public int dosaPrice(String itemName) {
		try {

			int price = 0;
			connection();
			st = con.prepareStatement("select item_price from item where item_name=?");
			st.setString(1, itemName);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				price = rs.getInt(1);
			}
			return price;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public ArrayList<Item> itemList() {
		ArrayList<Item> items = new ArrayList<>();
		try {
			connection();
			st = con.prepareStatement("select * from item");
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				items.add(new Item(rs.getInt(1), rs.getString(2), rs.getInt(3),rs.getInt(4)));
			}
			;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public ArrayList<User> userList() {
		ArrayList<User> users = new ArrayList<>();
		try {
			connection();
			CallableStatement cStmt = con.prepareCall("{call getUsers}");

			ResultSet rs = cStmt.executeQuery();

			while (rs.next()) {
				users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getString(5)));
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	public int removeuser(String userEmail) throws ClassNotFoundException {
		try {
			connection();
			st = con.prepareStatement("delete from user where user_email=?");
			st.setString(1, userEmail);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public int getItem_id(String itemName) {

		try {

			int itemId = 0;
			connection();
			st = con.prepareStatement("select item_id from item where item_name=?");
			st.setString(1, itemName);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt(1);
			}
			return itemId;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	
	public int getUserId(String userEmail) {
		int userId = 0;
		try {
			connection();
			st = con.prepareStatement("select user_id FROM user where user_email=?");
			st.setString(1, userEmail);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				userId = rs.getInt(1);
			}
			return userId;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
	
	public int getOrderId() {
		int orderId = 0;
		try {
			connection();
			st = con.prepareStatement("select order_id FROM order_details ORDER BY order_id DESC LIMIT 1;");
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			return orderId;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
	
	public String currentDateTime() {
		 //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy"); 
		   LocalDateTime now = LocalDateTime.now();   
		   return "" + dtf.format(now);
	}
	
	
	public int insertOrder(int userId) {
		try {
			connection();
			String date=currentDateTime();
			st = con.prepareStatement("insert into order_details(user_id,order_date)values(?,?)");
			st.setInt(1,userId);
			st.setString(2,date);
			return st.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void insertCart(int orderId,int itemId,String itemName,int itemQty) {
		try {
			connection();
			st = con.prepareStatement("insert into cart(order_id,item_name,item_qty,item_id)values(?,?,?,?)");
			st.setInt(1,orderId);
			st.setString(2, itemName);
			st.setInt(3,itemQty);
			st.setInt(4,itemId);
			st.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<OrderDetails> orderDetails() {
		try {
			
			List<OrderDetails> orderList=new ArrayList<>();
			
		connection();
		
		CallableStatement cStmt = con.prepareCall("{call getOrder}");

		ResultSet rs=cStmt.executeQuery();
		
		while(rs.next()) {
			
			orderList.add(new OrderDetails(rs.getInt(1), rs.getString(2), rs.getString(3)));
		}
			return orderList;
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public int userCount() {
		try {
			 int countUser=0;
			connection();
			st = con.prepareStatement("select count( * )as countUser from user");
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				countUser = rs.getInt(1);
			}
			return countUser;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int itemCount() {
		try {
			 int countItem=0;
			connection();
			st = con.prepareStatement("select count( * )as countItem from item");
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				countItem = rs.getInt(1);
			}
			return countItem;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int orderCount() {
		try {
			 int countOrder=0;
			connection();
			st = con.prepareStatement("select count( * ) from order_details where order_status=?");
			st.setInt(1,0);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				countOrder = rs.getInt(1);
			}
			return countOrder;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public List<Cart> getOrderItems(int orderId){
		List<Cart> items=new ArrayList<>();
		try {
			connection();
			st = con.prepareStatement("select c.item_name,c.item_qty,o.order_date from order_details o inner join cart c on o.order_id = c. order_id where o.order_id=?");
			st.setInt(1, orderId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				items.add(new Cart(rs.getString(1), rs.getInt(2)));
			}
			return items;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int payment(int orderId,int total,int userId) {
		try {
			connection();
			st = con.prepareStatement("insert into payment(bill_date,order_id,user_id,total)values(?,?,?,?)");
			st.setString(1, currentDateTime());
			st.setInt(2,orderId);
			st.setInt(3,2);
			st.setInt(4,total);
			return st.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getItemQty(String itemName) {
		int qty = 0;
		try {
			connection();
			st = con.prepareStatement("select item_qty from item where item_name=?");
			st.setString(1, itemName);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				qty = rs.getInt(1);
			}
			return qty;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public void updateQty(String itemName,int qty) {
		try {
			connection();
			st = con.prepareStatement("UPDATE item set item_qty=? where item_name=?");
			st.setInt(1,qty);
			st.setString(2,itemName);
			st.executeUpdate();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getUserId(int orderId) {
		int userId = 0;
		try {
			connection();
			st = con.prepareStatement("select user_id from order_details where order_id=?");
			st.setInt(1,orderId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				userId = rs.getInt(1);
			}
			return userId;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public List<Integer> getItemIds(int orderId){
		List<Integer>ids=new ArrayList<>();
		try {
			connection();
			st = con.prepareStatement("select item_id from cart where order_id=?");
			st.setInt(1,orderId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
			return ids;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int getPrice(int itemId){
		int price=0;
		try {
			connection();
			st = con.prepareStatement("select item_price from item where item_id=?");
			st.setInt(1,itemId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				price=rs.getInt(1);
			}
			return price;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	
	public List<Payment> getPayments(String date)
	{
		try {
			List<Payment> payments=new ArrayList<>();
			connection();
			st = con.prepareStatement("select p.bill_date,p.payment_id,p.order_id,u.user_email,p.total from payment p inner join user u on u.user_id=p.user_id where p.bill_date=?");
			st.setString(1,date);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				payments.add(new Payment(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getString(4),rs.getInt(5)));
			}
			return payments;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int getItemPrice(String itemName){
		int price=0;
		try {
			connection();
			st = con.prepareStatement("select item_price from item where item_name=?");
			st.setString(1,itemName);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				price=rs.getInt(1);
			}
			return price;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
