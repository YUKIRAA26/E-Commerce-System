package System;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class CustomerManager {
	private static List<Product> product = new ArrayList<>();
	private static List<Cart> cart = new ArrayList<>();
	private static List<Orders> orders = new ArrayList<>();
	private static List<OrderItem> orderItem = new ArrayList<>();

	public void viewProduct() {
		product.clear();
		String select = "SELECT p.product_id, p.product_name, c.category_name, p.price FROM product p JOIN category c ON p.category_id = c.category_id ORDER BY p.product_id ASC";

		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement pt = con.prepareStatement(select);
			ResultSet rs = pt.executeQuery();

			boolean found = false;
			while (rs.next()) {
				found = true;
				int id = rs.getInt("product_id");
				String pName = rs.getString("product_name");
				String cName = rs.getString("category_name");
				double price = rs.getDouble("price");

				product.add(new Product(id, pName, cName, price));

			}

			if (!found) {
				System.out.println("No product found! ");
				return;
			}
			System.out.println("--- Products ---");
			System.out.print("No.\tProduct\t\tCategory\tPrice\n");
			for (Product p : product) {
				System.out.println(p.getProductID() + "\t" + p.getProductName() + "\t\t" + p.getCategoryName() + "\t"
						+ "$" + p.getPrice() + "\n");
			}

		} catch (SQLException e) {
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
			while (rs.next()) {
				found = true;
				int id = rs.getInt("product_id");
				String pName = rs.getString("product_name");
				String cName = rs.getString("category_name");
				double price = rs.getDouble("price");

				product.add(new Product(id, pName, cName, price));
			}

			if (!found) {
				System.out.println("No product found! ");
				return;
			}

			System.out.print("No.\tProduct\t\tCategory\tPrice\n");
			for (Product p : product) {
				System.out.println(p.getProductID() + "\t" + p.getProductName() + "\t\t" + p.getCategoryName() + "\t"
						+ "$" + p.getPrice() + "\n");
			}

		} catch (SQLException e) {
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
			while (rt.next()) {
				int categoryID = rt.getInt("category_id");
				String categoryName = rt.getString("category_name");
				int count = rt.getInt("product_count");

				product.add(new Product(categoryID, categoryName, count));
			}
			System.out.println("--- Product Category ---");
			System.out.println("NO.\tCategory\t\tProducts");
			for (Product p : product) {
				System.out.println(p.getCategoryID() + "\t" + p.getCategoryName() + "\t\t" + p.getCount() + "\n");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addToCart(Users user) {
		String statement = "INSERT INTO cart_items" + "(user_id,product_id,quantity)" + "VALUES (?,?,?)";
		String select = "SELECT * FROM cart_items WHERE user_id = ? AND product_id = ? ";

		String update = "UPDATE cart_items SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";

		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement pt = con.prepareStatement(statement);

			PreparedStatement selct = con.prepareStatement(select);
			// update
			PreparedStatement upt = con.prepareStatement(update);
			viewProduct();
			int pID = Exceptions.IntegerException("Enter product ID: ");

			selct.setInt(1, user.getUserID());
			selct.setInt(2, pID);
			ResultSet slt = selct.executeQuery();

			if (slt.next()) {
				int quantity = Exceptions.IntegerException("Enter quantity: ");
				upt.setInt(1, quantity);
				upt.setInt(2, user.getUserID());
				upt.setInt(3, pID);
				upt.executeUpdate();
				System.out.println("Succesfully update quantity");
				return;
			}

			boolean found = false;
			for (Product p : product) {
				if (p.getProductID() == pID) {
					found = true;
				}
			}

			if (!found) {
				System.out.println("Item not found!");
				return;
			}

			int quantity = Exceptions.IntegerException("Enter quantity: ");
			pt.setInt(1, user.getUserID());
			pt.setInt(2, pID);
			pt.setInt(3, quantity);
			pt.executeUpdate();
			System.out.println("Item added to cart! ");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void viewCart(Users user) {
		cart.clear();
		String select = "SELECT c.cart_item_id, p.product_id, p.product_name, c.quantity, p.price, p.price * c.quantity AS SubTotal"
				+ " FROM cart_items c" + " JOIN product p ON c.product_id = p.product_id" + " WHERE c.user_id = ?";

		String total = "SELECT SUM(p.price * c.quantity) AS Total, SUM(c.quantity) AS total_item" + " FROM cart_items c"
				+ " JOIN product p ON c.product_id = p.product_id" + " WHERE c.user_id = ?";

		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement pt = con.prepareStatement(select);
			pt.setInt(1, user.getUserID());
			ResultSet rs = pt.executeQuery();

			PreparedStatement tot = con.prepareStatement(total);

			boolean found = false;

			while (rs.next()) {
				found = true;
				int cid = rs.getInt("cart_item_id");
				int pid = rs.getInt("product_id");
				String pName = rs.getString("product_name");
				int quantity = rs.getInt("quantity");
				double subTotal = rs.getDouble("subTotal");
				double price = rs.getDouble("price");

				cart.add(new Cart(cid, pid, pName, quantity, price, subTotal));

			}

			if (!found) {
				System.out.println("No items found in this cart! ");
				return;
			}
			System.out.println("----------- My Cart ----------");
			System.out.print("ID\tProduct\t\tPrice\tqty\tSubTotal\n");
			for (Cart c : cart) {
				System.out.println(c.getProductID() + "\t" + c.getProductName() + "\t\t" + "$" + c.getProductPrice()
						+ "\t" + c.getQuantity() + "\t" + "$" + c.getSubTotal() + "\n");
			}
			System.out.println("------------------------------");

			tot.setInt(1, user.getUserID());
			ResultSet totals = tot.executeQuery();
			if (totals.next()) {
				int totalItem = totals.getInt("total_item");
				double totalCost = totals.getDouble("Total");

				System.out.println("Total items: " + totalItem);
				System.out.println("Total Amount: " + "$" + totalCost);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void removeProductCart(Users user) {
		String delete = "DELETE FROM cart_items c WHERE c.user_id = ? AND c.product_id = ?";

		String select = "SELECT * FROM cart_items WHERE user_id = ?";

		Connection con = null;
		try {
			con = DBConnection.getConnection();

			try (PreparedStatement pr = con.prepareStatement(select)) {
				pr.setInt(1, user.getUserID());

				try (ResultSet rs = pr.executeQuery()) {
					if (!rs.next()) {
						System.out.println("Cart is empty! ");
						return;
					}
				}

				try (PreparedStatement dlt = con.prepareStatement(delete)) {
					System.out.println("--- Delete Item ---");
					viewCart(user);
					int productId = Exceptions.IntegerException("Enter Product ID: ");
					dlt.setInt(1, user.getUserID());
					dlt.setInt(2, productId);
					
					int row = dlt.executeUpdate();

					if (row > 0) {
						System.out.println("Succesfully delete item from the cart");
					} else {
						System.out.println("Item not found! ");
						return;
					}

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void checkOut(Users user) {
		String statement1 = "SELECT user_id FROM cart_items WHERE user_id = ?";

		String total = "SELECT SUM(p.price * c.quantity) AS Total " + "FROM cart_items c "
				+ "JOIN product p ON c.product_id = p.product_id " + "WHERE c.user_id = ?";

		String confirmed = "INSERT INTO orders(user_id, total_amount, status) " + "VALUES (?, ?, ?)";

		String retCart = "SELECT p.product_id, p.product_name, c.quantity, p.price, "
				+ "p.price * c.quantity AS SubTotal " + "FROM cart_items c "
				+ "JOIN product p ON c.product_id = p.product_id " + "WHERE c.user_id = ?";

		String insertItem = "INSERT INTO order_items " + "(order_id, product_id, quantity, price) "
				+ "VALUES (?, ?, ?, ?)";

		String deleteCart = "DELETE FROM cart_items WHERE user_id = ?";

		Connection con = null;

		try {
			con = DBConnection.getConnection();

			// 1. Check whether the cart has items
			try (PreparedStatement stmt1 = con.prepareStatement(statement1)) {

				stmt1.setInt(1, user.getUserID());

				try (ResultSet rs = stmt1.executeQuery()) {
					if (!rs.next()) {
						System.out.println("Your cart is empty!");
						return;
					}
				}
			}

			// 2. Calculate the cart total
			double totalAmount;

			try (PreparedStatement tot = con.prepareStatement(total)) {
				tot.setInt(1, user.getUserID());

				try (ResultSet resTot = tot.executeQuery()) {
					if (!resTot.next() || resTot.getObject("Total") == null) {
						System.out.println("Could not calculate cart total.");
						return;
					}

					totalAmount = resTot.getDouble("Total");
				}
			}

			// 3. Ask for confirmation
			String answer = Exceptions.StringException("Confirm order? (Y/N): ").trim();

			if (!answer.equalsIgnoreCase("Y")) {
				System.out.println("Returning to menu...");
				return;
			}

			// 4. Start the transaction
			con.setAutoCommit(false);

			// Temporary list: update Java state only after commit succeeds

			try {
				// 5. Insert the order and request its generated ID
				int orderID;

				try (PreparedStatement cfm = con.prepareStatement(confirmed, Statement.RETURN_GENERATED_KEYS)) {

					cfm.setInt(1, user.getUserID());
					cfm.setDouble(2, totalAmount);
					cfm.setString(3, "PENDING");

					cfm.executeUpdate();

					try (ResultSet key = cfm.getGeneratedKeys()) {
						if (!key.next()) {
							throw new SQLException("Failed to retrieve generated order ID.");
						}

						orderID = key.getInt(1);
					}
				}

				// 6. Copy the cart items into order_items
				try (PreparedStatement cart = con.prepareStatement(retCart);
						PreparedStatement insItem = con.prepareStatement(insertItem)) {

					cart.setInt(1, user.getUserID());

					try (ResultSet rtCart = cart.executeQuery()) {
						while (rtCart.next()) {
							int pid = rtCart.getInt("product_id");
							int quantity = rtCart.getInt("quantity");
							double price = rtCart.getDouble("price");

							insItem.setInt(1, orderID);
							insItem.setInt(2, pid);
							insItem.setInt(3, quantity);
							insItem.setDouble(4, price);

							insItem.executeUpdate();

						}
					}
				}

				// 7. Delete the cart rows from MySQL
				try (PreparedStatement dltCart = con.prepareStatement(deleteCart)) {

					dltCart.setInt(1, user.getUserID());
					dltCart.executeUpdate();
				}

				// 8. Save all database changes together
				con.commit();

				// 9. Update Java lists and show success only after commit
				cart.clear();

				System.out.println("Order placed successfully!");
				System.out.println("Order ID: " + orderID);
				System.out.printf("Total: $%.2f%n", totalAmount);

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			System.out.println("Checkout failed. Your database changes were rolled back.");
			e.printStackTrace();

		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void viewOrder(Users user) {

		String retOrder = "SELECT * FROM orders" + " WHERE user_id = ?";

		String retData =     "SELECT oi.quantity, p.product_name, oi.price, "
				  + "oi.price * oi.quantity AS sub_total "
				  + "FROM order_items oi "
				  + "JOIN product p ON oi.product_id = p.product_id "
				  + "JOIN orders o ON oi.order_id = o.order_id "
				  + "WHERE oi.order_id = ? AND o.user_id = ?";
		
		String select = "SELECT * FROM orders WHERE order_id = ? AND user_id = ?";

		String total =  "SELECT SUM(oi.price * oi.quantity) AS Total "
				  + "FROM order_items oi "
				  + "JOIN orders o ON oi.order_id = o.order_id "	
				  + "WHERE oi.order_id = ? AND o.user_id = ?";

		Connection con = null;
		try {
			orders.clear();
		
			con = DBConnection.getConnection();
			int orderId;
			try (PreparedStatement pt = con.prepareStatement(retOrder)) {
				pt.setInt(1, user.getUserID());

				try (ResultSet order = pt.executeQuery()) {

					if (!order.next()) {
						System.out.println("No order yet! ");
						return;
					}
					
					do {
						orderId = order.getInt("order_id");
						double totalAmount = order.getDouble("total_amount");
						String status = order.getString("status");
						LocalDateTime date = order.getTimestamp("created_at").toLocalDateTime();

						orders.add(new Orders(orderId, totalAmount, status, date));
					} while (order.next());

					System.out.println("--- View Orders ---");
					System.out.println("Id\tTotal Amount\tStatus\t\tCreated At");
					for (Orders o : orders) {
						System.out.println(o.getOrder_id() + "\t" + o.getTotalAmount() + "\t\t" + o.getStatus() + "\t\t"
								+ o.getCreatedAt());
					}
				}

				String answer = Exceptions.StringException("Check order item (Y | N): ").trim();

				if (!answer.equalsIgnoreCase("Y")) {
					System.out.println("Returning to menu...");
					return;
				}
				
				orderItem.clear();
				int selectionOrderID = Exceptions.IntegerException("Enter Order ID to view: ");
				
				try(PreparedStatement pr = con.prepareStatement(select)){
					pr.setInt(1, selectionOrderID);
					pr.setInt(2, user.getUserID());
					try(ResultSet rs = pr.executeQuery()){
						if(!rs.next()) {
							System.out.println("Order id does not exist! ");
							return;
						}
					}
				}

				double totalAmount;
				try (PreparedStatement tot = con.prepareStatement(total)) {
					tot.setInt(1, selectionOrderID);
					tot.setInt(2, user.getUserID());

					try (ResultSet rs = tot.executeQuery()) {
						if (!rs.next() || rs.getObject("Total") == null) {
							System.out.println("Cannot calculate total!");
							return;
						}

						totalAmount = rs.getDouble("Total");
					}
				}

				try (PreparedStatement ps = con.prepareStatement(retData)) {

					ps.setInt(1, selectionOrderID);
					ps.setInt(2, user.getUserID());
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							int quantity = rs.getInt("quantity");
							double price = rs.getDouble("price");
							double subTotal = rs.getDouble("sub_total");
							String pName = rs.getString("product_name");

							orderItem.add(new OrderItem(selectionOrderID,quantity, price, subTotal, pName));
						}
					}

						System.out.println("----- Order Item -----");
						System.out.println("Order #" + selectionOrderID);
						System.out.println("Product Name\tQuantity\tPrice\tSubtotal");
						for(OrderItem o: orderItem) {
						
							System.out.println(o.getProductName() + "\t\t" + o.getQuantity() + "\t\t" + o.getPrice() + "\t"
									+ o.getSubTotal());
						}
						System.out.println("---------------------");
						System.out.println("Total: " + totalAmount);
						System.out.println("---------------------\n");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void viewProfile(Users user) {
	    String query = "SELECT user_id, username, full_name, email, role "
	                 + "FROM users WHERE user_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement pt = con.prepareStatement(query)) {

	        pt.setInt(1, user.getUserID());

	        try (ResultSet rs = pt.executeQuery()) {
	            if (rs.next()) {
	                System.out.println("\n========== MY PROFILE ==========");
	                System.out.println("User ID   : " + rs.getInt("user_id"));
	                System.out.println("Username  : " + rs.getString("username"));
	                System.out.println("Full Name : " + rs.getString("full_name"));
	                System.out.println("Email     : " + rs.getString("email"));
	                System.out.println("Role      : " + rs.getString("role"));
	                System.out.println("================================");
	            } else {
	                System.out.println("Profile not found!");
	            }
	        }

	    } catch (SQLException e) {
	        System.out.println("Error retrieving profile.");
	        e.printStackTrace();
	    }
	}
}
