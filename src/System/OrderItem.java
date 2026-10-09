package System;

public class OrderItem {
	private int order_id;
	private int product_id;
	private int quantity;
	private String productName;
	private double price;
	private double subTotal;
	
	public OrderItem(int order_id, int quantity, double price,double subTotal,String name) {
		this.order_id = order_id;
		this.quantity = quantity;
		this.price = price;
		this.subTotal = subTotal;
		this.productName = name;
	}
	
	public int getOrderId() {
		return order_id;
	}
	
	public int product_id() {
		return product_id;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public double getPrice() {
		return price;
	}
	
	public double getSubTotal() {
		return subTotal;
	}
}
