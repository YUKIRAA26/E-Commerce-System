package System;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerManager {
	private static List<Product> product = new ArrayList<>();
	private static List<Cart> cart = new ArrayList<>();
	
	public void viewProduct() {
		product.clear();
		String select = "SELECT p.product_id, p.product_name, c.category_name, p.price FROM product p JOIN category c ON p.category_id = c.category_id ORDER BY p.product_id ASC";
			
		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement pt = con.prepareStatement(select);
			ResultSet rs = pt.executeQuery();
			
		
			boolean found = false;
			while(rs.next()) {
				found = true;
				int id = rs.getInt("product_id");
				String pName = rs.getString("product_name");
				String cName = rs.getString("category_name");
				double price = rs.getDouble("price");
				
				product.add(new Product(id,pName,cName,price));
				
			}
			
			if(!found) {
				System.out.println("No product found! ");
				return;
			}
			System.out.println("--- Products ---");
			System.out.print("No.\tProduct\t\tCategory\tPrice\n");
			for(Product p: product) {
				System.out.println(p.getProductID() + "\t" + p.getProductName() + "\t\t" + p.getCategoryName() + "\t" + "$" + p.getPrice() + "\n" );
			}
			
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void searchProduct() {
		product.clear();
		String search = "SELECT p.product_id, p.product_name, c.category_name, p.price FROM product p JOIN category c ON p.category_id = c.category_id WHERE p.product_name LIKE ?";
		
		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement pt = con.prepareStatement(search);
			
			System.out.println("--- Search Product ---");
			System.out.println();
			String name = Exceptions.StringException("Enter Product Name: ");
			
			pt.setString(1, "%" + name + "%");

			ResultSet rs = pt.executeQuery();
			
	
			boolean found = false;
			while(rs.next()) {
				found = true;
				int id = rs.getInt("product_id");
				String pName = rs.getString("product_name");
				String cName = rs.getString("category_name");
				double price = rs.getDouble("price");
				
				product.add(new Product(id,pName,cName,price));
			}
			
			if(!found) {
				System.out.println("No product found! ");
				return;
			}
			
			System.out.print("No.\tProduct\t\tCategory\tPrice\n");
			for(Product p: product) {
				System.out.println(p.getProductID() + "\t" + p.getProductName() + "\t\t" + p.getCategoryName() + "\t" + "$" + p.getPrice() + "\n" );
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void productCategory() {
		product.clear();
		String statement = "SELECT c.category_id, c.category_name, COUNT(p.product_id) AS product_count FROM category c LEFT JOIN product p ON c.category_id = p.category_id GROUP BY c.category_id, c.category_name ORDER BY c.category_id";
		
		try {
		Connection con = DBConnection.getConnection();
		PreparedStatement pt = con.prepareStatement(statement);
		
		ResultSet rt = pt.executeQuery();
		while(rt.next()) {
			int categoryID = rt.getInt("category_id");
			String categoryName = rt.getString("category_name");
			int count = rt.getInt("product_count");
			
			product.add(new Product(categoryID,categoryName,count));
		}
		System.out.println("--- Product Category ---");
		System.out.println("NO.\tCategory\t\tProducts");
		for(Product p: product) {
			System.out.println(p.getCategoryID() + "\t" + p.getCategoryName() + "\t\t" + p.getCount() + "\n");
			
		}
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void addToCart(Users user) {
		String statement = "INSERT INTO cart_items" +
						   "(user_id,product_id,quantity)" +
						   "VALUES (?,?,?)";
		String select = "SELECT * FROM cart_items WHERE user_id = ? AND product_id = ? ";
		
		String update = "UPDATE cart_items SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
		
		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement pt = con.prepareStatement(statement);
			
			PreparedStatement selct = con.prepareStatement(select);
			//update
			PreparedStatement upt = con.prepareStatement(update);
			viewProduct();
			int pID = Exceptions.IntegerException("Enter product ID: ");
			
			selct.setInt(1, user.getUserID());
			selct.setInt(2, pID);
			ResultSet slt = selct.executeQuery();

			if(slt.next()) {
				int quantity = Exceptions.IntegerException("Enter quantity: ");
				upt.setInt(1, quantity);
				upt.setInt(2, user.getUserID());
				upt.setInt(3, pID);
				upt.executeUpdate();
				System.out.println("Succesfully update quantity");
				return;
			}
			
			
			boolean found = false;
			for(Product p: product) {
				if(p.getProductID() == pID) {
					found = true;
				}
			}
			
			if(!found) {
				System.out.println("Item not found!");
				return;
			}
			
			int quantity = Exceptions.IntegerException("Enter quantity: ");
			pt.setInt(1, user.getUserID());
			pt.setInt(2, pID);
			pt.setInt(3, quantity);
			pt.executeUpdate();
			System.out.println("Item added to cart! ");
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void viewCart(Users user) {
		cart.clear();
		String select = "SELECT c.cart_item_id, p.product_id, p.product_name, c.quantity, p.price, p.price * c.quantity AS SubTotal"
				+ " FROM cart_items c"
				+ " JOIN product p ON c.product_id = p.product_id"
				+" WHERE c.user_id = ?";
		
		String total = "SELECT SUM(p.price * c.quantity) AS Total, SUM(c.quantity) AS total_item"
				+ " FROM cart_items c"
				+ " JOIN product p ON c.product_id = p.product_id"
				+ " WHERE c.user_id = ?";
			
		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement pt = con.prepareStatement(select);
			pt.setInt(1, user.getUserID());
			ResultSet rs = pt.executeQuery();
			
			
			PreparedStatement tot = con.prepareStatement(total);
			
			boolean found = false;
		
			while(rs.next()) {
				found = true;
				int cid = rs.getInt("cart_item_id");
				int pid = rs.getInt("product_id");
				String pName = rs.getString("product_name");
				int quantity = rs.getInt("quantity");
				double subTotal = rs.getDouble("subTotal");
				double price = rs.getDouble("price");
				
				cart.add(new Cart(cid,pid,pName,quantity,price,subTotal));
				
			}
			
			if(!found) {
				System.out.println("No items found in this cart! ");
				return;
			}
			System.out.println("----------- My Cart ----------");
			System.out.print("ID\tProduct\t\tPrice\tqty\tSubTotal\n");
			for(Cart c: cart) {
				System.out.println(c.getProductID() + "\t" + c.getProductName() + "\t\t" + "$" + c.getProductPrice()+ "\t" + c.getQuantity() + "\t" + "$" + c.getSubTotal() + "\n" );
			}
			System.out.println("------------------------------");

			tot.setInt(1, user.getUserID());
			ResultSet totals = tot.executeQuery();
			if(totals.next()) {
				int totalItem = totals.getInt("total_item");
				double totalCost = totals.getDouble("Total");
				
				System.out.println("Total items: " + totalItem);
				System.out.println("Total Amount: " + "$" + totalCost);
			}
			
			
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
