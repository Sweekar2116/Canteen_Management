package Data;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDetails {
	private int orderId;
	private String userEmail;
	private String orderdDate;
	
	
	public OrderDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderDetails(int orderId, String userEmail,String date) {
		super();
		this.orderId = orderId;
		this.userEmail = userEmail;
		this.orderdDate=date;
	}
	
	
	public String getOrderdDate() {
		return orderdDate;
	}
	public void setOrderdDate(String orderdDate) {
		this.orderdDate = orderdDate;
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
	
	public int calcuatePayment(List<Payment> payment ) {
		
		List<Integer> totalList=payment.stream().map(data->data.getTotal()).collect(Collectors.toList());
		int total=totalList.stream().mapToInt(Integer::intValue).sum();
		
		return total;
}

}
