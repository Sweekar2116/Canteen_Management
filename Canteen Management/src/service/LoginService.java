package service;

import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Data.Item;
import Data.OrderDetails;
import Data.User;
import dao.Connection;

public class LoginService {
	static String userEmail;
	
	public void saveEmail(String userEmail) {
		this.userEmail=userEmail;
	}
	
	public String email() {
		return userEmail;
	}
	
	Connection con=new Connection();
	public int userSignup(String userName,String userEmail,long phone,String password) throws ClassNotFoundException {
		
		return con.insertUser(userName, userEmail, phone, password);
	}
	
	public int userLogin(String email,String password) throws ClassNotFoundException, SQLException {
		
		if(con.login(email, password)>0) {
			saveEmail(email);
		}
		return con.login(email, password);
	}
	
	public int checkEmail(String email) throws ClassNotFoundException
	{
		return con.check(email);
	}
	
	public int checkName(String itemName) throws ClassNotFoundException
	{
		return con.checkName(itemName);
	}
	public ArrayList<Item> itemDetails() throws ClassNotFoundException
	{
		return con.itemList();
	}
	
	public ArrayList<User> userDetails() throws ClassNotFoundException
	{
		return con.userList();
	}
	
	public int removeuser(String email) throws ClassNotFoundException
	{
		return con.removeuser(email);
		
	}
	
	public int userCount() throws ClassNotFoundException
	{
		return con.userCount();
		
	}
	public int itemCount() throws ClassNotFoundException
	{
		return con.itemCount();
		
	}
	public int orderCount() throws ClassNotFoundException
	{
		return con.orderCount();
		
	}
	
	
	
	
	public int forgotpassword(String email,String password) throws ClassNotFoundException
	{
		return con.forgotpassword(email, password);
		
	}
	
	public int priceupdate(String itemName,String itemPRICE,String itemQUANTITY) throws ClassNotFoundException
	{
		return con.priceupdate(itemName,itemPRICE,itemQUANTITY);
		
	}

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
	
	 public int getUserId(String userEmail) {
		 return con.getUserId(userEmail);
	 }
	

	 public boolean validateLogin(String email,String password) {
		 if(email.equals("")||(password.equals(""))) {
			 if(password.equals("")) {
				 return false;
			 }
		 }
		 return true;
	 }
	 
	public boolean validateSignUp(String userName,String userEmail,String phone,String pass) {
	
		if(userName.equals("")) {
			return false;
		}
		if(userEmail.equals("")) {
			return false;
		}
		if(phone.equals("")) {
			return false;
		}
		if(pass.equals("")) {
			return false;
		}
		
		return true;
	}
	
	public boolean validateConfirmPass(String pass,String cPass) {
		if(pass.equals(cPass)) {
			return true;
		}
		return false;
	}
	
	public boolean validateEmail(String useEmail) {
		 String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		 Pattern pattern = Pattern.compile(regex);  
		 Matcher matcher = pattern.matcher(useEmail);  
		 if(matcher.matches()) {
			 return true;
		 }
		 return false;
	}
	
	public boolean validateMobile(String phone) {
		Pattern p = Pattern.compile("^\\d{10}$");
		Matcher m = p.matcher(phone);
		if(m.matches()) {
			return true;
		}
		 
		return false;
	}
	
	public boolean validateForgotPass(String email,String pass) {
		if(email.equals("")) {
			return false;
		}
		if(pass.equals("")) {
			return false;
		}
		
		return true;
	}
	
	public int validateDate(String date) {
		 String regex = "[0-9]{2}[/]{1}[0-9]{2}[/]{1}[0-9]{4}";
		 if(date.matches("[0-9]{2}[/]{1}[0-9]{2}[/]{1}[0-9]{4}"))
		 {
		 SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		 sdf.setLenient(false);
		 try {
		 Date d1=sdf.parse(date);
		 return 1;
		 } catch (ParseException e) {
		 return -1;
		 }
		 }
		 else {
		   return -1;
		 }
	}
	
	
	
}
	

