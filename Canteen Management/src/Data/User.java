package Data;

import java.math.BigInteger;

public class User {
	
		private int userId;
		private String userName;
		private String userEmail;
		private long userPhone;
		private String userPassword;
		public User(int userId,String userName,String userEmail,long userPhone,String userPassword) {
			super();
			this.userId = userId;
			this.userName = userName;
			this.userEmail = userEmail;
			this.userPhone=userPhone;
			this.userPassword=userPassword;
			
		}
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getUserEmail() {
			return userEmail;
		}
		public void setUserEmail1(String userEmail) {
			this.userEmail = userEmail;
		}
		
		public long getUserPhone() {
			return userPhone;
		}
		public void setUserPhone(long userPhone) {
			this.userPhone = userPhone;
		}
		public String getUserPassword() {
			return userPassword;
		}
		public void setUserPassword(String userPassword) {
			this.userPassword = userPassword;
		}
		
		
		@Override
		public String toString() {
			return "User [userId=" + userId + ", userName=" + userName + ",userEmail=" + userEmail + ", userPhone" + userPhone + " userPassword=" + userPassword + "]";
		}
		
		
		
	}



