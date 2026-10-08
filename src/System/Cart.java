package System;

public class Cart {
	private int cart_itemID;
	private int productID;
	private String productName;
	private int quantity;
	private double subTotal;
	private double productPrice;

	
	Cart(int cart_itemID, int productID, String productName, int quantity,double productPrice, double subTotal){
		this.cart_itemID = cart_itemID;
		this.productID = productID;
		this.productName = productName;
		this.quantity = quantity;
		this.productPrice = productPrice;
		this.subTotal = subTotal;
	}
	
	public int getCart_itemID() {
		return cart_itemID;
	}


	public int getProductID() {
		return productID;
	}


	public int getQuantity() {
		return quantity;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public String getProductName() {
		return productName;
	}

	public double getProductPrice() {
		return productPrice;
	}


	
}
