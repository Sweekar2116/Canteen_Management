package service;

import Data.Cart;
import dao.Connection;
import java.util.List;

public class priceService {
	Connection con=new Connection();
	public int price(String itemName) {
		return con.dosaPrice(itemName);
	}
	
	public void alterQty(List<Cart> orders ) {
		orders.forEach(order->{
			int qty=con.getItemQty(order.getItemNAme());
			int totalQty=0;
			totalQty=qty-order.getItemQtry();
			con.updateQty(order.getItemNAme(), totalQty);
			
		});
		
	}
	
	public int getTotal(List<Cart> items) {
		int total=0;
		for(int i=0;i<items.size();i++) {
			int amount=con.getItemPrice(items.get(i).getItemNAme());
			int calTotal=amount*items.get(i).itemQtry;
			total=total+calTotal;
		}
		return total;
	}
	
	public int addPayment(int orderId,List<Cart> itms) {
		
		List<Integer>itemIds=con.getItemIds(orderId);
		int total=getTotal(itms);
//		List<Integer> itemPrice=itemIds.stream().map(id->con.getPrice(id)).collect(Collectors.toList());
//		int total=itemPrice.stream().mapToInt(Integer::intValue).sum();
		int userId=con.getUserId(orderId);
		int result=con.payment(orderId, total, userId);
		return result;
}
	
	public boolean checkQuantity(String item,int qty) {
		int dbQty=con.getItemQty(item);
		int check=dbQty-qty;
		if(check>0) {
			if(dbQty>=0) {
				return true;
			}else {
				return false;
			}
		}
		
		return false;
		
	}
	
}
