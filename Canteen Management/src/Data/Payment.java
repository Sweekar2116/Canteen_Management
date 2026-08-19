package Data;

public class Payment {
	private String billDate;
	private int paymentId;
	private int orderId;
	private String userEmail;
	private int total;
	public Payment(String bDate,int paymentId, int orderId, String userEmail, int total) {
		super();
		this.billDate=bDate;
		this.paymentId = paymentId;
		this.orderId = orderId;
		this.userEmail = userEmail;
		this.total = total;
	}
	
	
	public String getBillDate() {
		return billDate;
	}


	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}


	public int getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
