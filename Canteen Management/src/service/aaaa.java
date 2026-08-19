package service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import Data.Cart;
import dao.Connection;

public class aaaa {

	 public static boolean isValid(String email)
	    {
	        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
	                            "[a-zA-Z0-9_+&*-]+)*@" +
	                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
	                            "A-Z]{2,7}$";
	                              
	        Pattern pat = Pattern.compile(emailRegex);
	        if (email == null)
	            return false;
	        return pat.matcher(email).matches();
	    }
	
	 static boolean validatePhoneNumber(String phoneNumber) {
		  // validate phone numbers of format "1234567890"
		  if (phoneNumber.matches("\\d{10}"))
		   return true;
		 
		  else if (phoneNumber.matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}"))
		   return true;
		  else if (phoneNumber.matches("\\(\\d{5}\\)-\\d{3}-\\d{3}"))
		   return true;

		  else if (phoneNumber.matches("\\(\\d{4}\\)-\\d{3}-\\d{3}"))
		   return true;
		  // return false if nothing matches the input
		  else
		   return false;

		 }

	
	public static void main(String[] args) throws ClassNotFoundException {
//		 int n = 5;
//		 ArrayList<Cart> cart=new ArrayList<>();
//		 cart.add(new Cart("aa", n, n));
//		 cart.add(new Cart("bb", n, n));
//		
//		
//		 
//		 for(int i=0;i<cart.size();i++) {
//			 if("aa"==cart.get(i).getItemNAme()) {
//				 cart.remove(i);
//			 }
//		 }
//		 
//		 System.out.println(cart);
		
		Connection con=new Connection();
		
		 OrderService orderService=new OrderService();
		 priceService pService=new priceService();
		LoginService login=new LoginService();
		System.out.println(login.validateDate("30/1/2023"));
		
	
		}
		
		
	}


