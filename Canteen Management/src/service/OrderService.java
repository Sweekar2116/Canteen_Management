package service;

import java.util.ArrayList;
import java.util.List;

import Data.Cart;
import Data.OrderDetails;
import Data.Payment;
import dao.Connection;

public class OrderService {

	public static int orderId=0;
	public static String billDate="";

 public static ArrayList<Cart> cart=new ArrayList<>();
 
 Connection con=new Connection();
 
 public void saveBillDate(String date) {
	 this.billDate=date;
 }
 
 public String getDate() {
	 return billDate;
 }
 
 public void saveOrderId(int orderId) {
	 this.orderId=orderId;
 }
 
 public int getOrderId() {
	 return orderId;
 }
 
 public void addItem(Cart data) {
	 cart.add(data);
	
 }

 public ArrayList<Cart> getCart(){
	 return cart;
 }
 
 public int calculateTotal(ArrayList<Cart> cart) {
	 
	 int grandTotal=0;
	 for(int i=0;i<cart.size();i++) {
		 int total=cart.get(i).itemQtry * cart.get(i).itemPrice;
		 grandTotal=grandTotal+ total;
	 }
	 
	 return grandTotal;
 }
 
 public int addCart(ArrayList<Cart> cartList,int user_id) {
	 try {
	 con.insertOrder(user_id);
	 int orderId=con.getOrderId();
	 for(int i=0;i<cartList.size();i++) {
		 int itemId=con.getItem_id(cartList.get(i).getItemNAme());
		 con.insertCart(orderId, itemId, cartList.get(i).getItemNAme(), cartList.get(i).getItemQtry());
	 }
	 	return 1;
	 }catch(Exception e) {
		 e.printStackTrace();
	 }
	 return 0;
 }
 
 
 public List<OrderDetails> getOrders(){
	 return con.orderDetails();
 }
 
 public List<Cart> getOrderdItems(int orderId){
	 return con.getOrderItems(orderId);
 }
 
 public List<Payment> getPayment(String date){
	 return con.getPayments(date);
 }
 

 
 
}
