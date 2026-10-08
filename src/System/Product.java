package System;

public class Product {
	private int productID;
	private int categoryID;
	private String productName;
	private String categoryName;
	private double price;
	private int count;
	
	Product(int productID, String productName, String categoryName, double price){
		this.productID = productID;
		this.productName = productName;
		this.categoryName = categoryName;
		this.price = price;
	}
	
	//view category
	Product(int categoryID , String categoryName, int count){
		this.categoryID = categoryID;
		this.categoryName = categoryName;
		this.count = count;
	}
	
	public int getProductID() {
		return productID;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	
	public double getPrice() {
		return price;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public int getCount() {
		return count;
	}

}
