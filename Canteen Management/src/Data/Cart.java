package Data;

public class Cart {
 public String itemNAme;
 public int itemPrice;
 public int itemQtry;

public int getItemQtry() {
	return itemQtry;
}
public void setItemQtry(int itemQtry) {
	this.itemQtry = itemQtry;
}


public Cart(String itemNAme,int itemPrice, int itemQtry) {
	super();
	this.itemNAme = itemNAme;
	this.itemQtry = itemQtry;
	this.itemPrice= itemPrice;
}



public Cart(String itemNAme, int itemQtry) {
	super();
	this.itemNAme = itemNAme;
	this.itemQtry = itemQtry;
}
public String getItemNAme() {
	return itemNAme;
}
public void setItemNAme(String itemNAme) {
	this.itemNAme = itemNAme;
}
public int getItemPrice() {
	return itemPrice;
}
public void setItemPrice(int itemPrice) {
	this.itemPrice = itemPrice;
}
 
}
