package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminManager {

	public void viewProduct() {
		CustomerManager.viewProduct();
	}

	public void addProduct() {
		String query = "INSERT INTO product (product_name, category_id, price) VALUES (?,?,?)";

		String select = "SELECT product_name FROM product WHERE product_name = ?";

		String category = "SELECT * FROM category";

		String categoryExist = "SELECT category_id FROM category WHERE category_id = ? ";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			String name;
			int categoryID;
			try (PreparedStatement exist = con.prepareStatement(select)) {
				while (true) {
					name = Exceptions.StringException("Enter product name: ");
					exist.setString(1, name);
					try (ResultSet rs = exist.executeQuery()) {
						if (rs.next()) {
							System.out.println("Name already exist! ");
							continue;
						}
						break;
					}
				}
			}

			try (PreparedStatement cat = con.prepareStatement(category);
					PreparedStatement catExist = con.prepareStatement(categoryExist)) {

				while (true) {

					try (ResultSet st = cat.executeQuery()) {
						while (st.next()) {
							System.out.println("category_id: " + st.getInt("category_id") + " | " + "category_name: "
									+ st.getString("category_name"));
							System.out.println();
						}
					}
					categoryID = Exceptions.IntegerException("Enter category id: ");
					catExist.setInt(1, categoryID);
					try (ResultSet st = catExist.executeQuery()) {
						if (!st.next()) {
							System.out.println("Category id does not exist! ");
							continue;
						}
					}
					break;
				}
			}

			double price = Exceptions.DoubleException("Enter price: ");
			ps.setString(1, name);
			ps.setInt(2, categoryID);
			ps.setDouble(3, price);
			ps.executeUpdate();
			System.out.println("Successfully added product! ");
		} catch (SQLException e) {
			System.out.println("Cannot add product" + e.getMessage());
		}
	}

	public void updateProduct() {
		String queryView = "SELECT * FROM product ORDER BY product_id ASC;";

		String queryExist = "SELECT * FROM product WHERE product_id = ?";

		try (Connection con = DBConnection.getConnection()) {

			try (PreparedStatement ps = con.prepareStatement(queryView)) {
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						System.out.println("Product_id: " + rs.getInt("product_id") + " | " + "Product_name: "
								+ rs.getString("product_name") + " | " + "category_id: " + rs.getInt("category_id")
								+ " | " + "Price: " + rs.getDouble("price"));
						System.out.println();
					}
				}
			}
			int id;
			try (PreparedStatement exist = con.prepareStatement(queryExist)) {
				id = Exceptions.IntegerException("Enter product ID: ");
				exist.setInt(1, id);
				try (ResultSet result = exist.executeQuery()) {
					if (!result.next()) {
						System.out.println("ID does not exist! ");
						return;
					}
				}
			}

			System.out.println("1. Update Name");
			System.out.println("2. Update Price");
			System.out.println("3. Update Category");
			System.out.println("4. Update All");
			System.out.println("5. Return");
			int choice = Exceptions.IntegerException("choice: ");
			switch (choice) {
			case 1 -> updateProductName(con, id);
			case 2 -> updateProductPrice(con, id);
			case 3 -> updateProductCategory(con, id);
			case 4 -> updateAllProductFields(con, id);
			case 5 -> {
				System.out.println("returning...");
				return;
			}
			default -> System.out.println("Invalid input! ");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateProductName(Connection con, int id) throws SQLException {
		String queryUpdate = "UPDATE product SET product_name = ? WHERE product_id = ? ";
		String select = "SELECT product_name FROM product WHERE product_name = ? AND product_id <> ? ";
		String name;

		try (PreparedStatement nameExist = con.prepareStatement(select)) {
			while (true) {
				name = Exceptions.StringException("Enter new product name: ");
				nameExist.setString(1, name);
				nameExist.setInt(2, id);
				try (ResultSet isNameExist = nameExist.executeQuery()) {
					if (isNameExist.next()) {
						System.out.println("product Name Exist! ");
						continue;
					}
				}
				break;
			}

		}

		try (PreparedStatement ps = con.prepareStatement(queryUpdate)) {
			ps.setString(1, name);
			ps.setInt(2, id);
			ps.executeUpdate();
		}
		
		System.out.println("Succesfully update name! ");
	}

	private void updateProductPrice(Connection con, int id) throws SQLException {
		String query = "UPDATE product SET price = ? WHERE product_id = ?";

		try (PreparedStatement ps = con.prepareStatement(query)) {
			double price = Exceptions.DoubleException("Enter new price: ");

			ps.setDouble(1, price);
			ps.setInt(2, id);
			ps.executeUpdate();
		}

		System.out.println("Succesfully updated price! ");

	}

	private void updateProductCategory(Connection con, int id) throws SQLException {

		String query = "SELECT * FROM category WHERE category_id = ?";
		String updateC = "UPDATE product SET category_id = ? WHERE product_id = ? ";

		int categoryID;
		try (PreparedStatement ps = con.prepareStatement(query)) {
			categoryID = Exceptions.IntegerException("Enter new category id: ");
			ps.setInt(1, categoryID);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					System.out.println("ID does not exist");
					return;
				}
			}
		}

		try (PreparedStatement update = con.prepareStatement(updateC)) {
			update.setInt(1, categoryID);
			update.setInt(2, id);
			update.executeUpdate();
		}

		System.out.println("Succesfully Updated Category!");
	}

	private void updateAllProductFields(Connection con, int id) throws SQLException {
		String select = "SELECT product_name FROM product WHERE product_name = ? AND product_id <> ?";
		String query = "SELECT * FROM category WHERE category_id = ?";
		String updateAll = "UPDATE product SET product_name = ?, " + "category_id = ?," + "price = ?"
				+ " WHERE product_id = ?";
		String name;

		try (PreparedStatement nameExist = con.prepareStatement(select)) {
			while (true) {
				name = Exceptions.StringException("Enter new product name: ");
				nameExist.setString(1, name);
				nameExist.setInt(2, id);
				try (ResultSet isNameExist = nameExist.executeQuery()) {
					if (isNameExist.next()) {
						System.out.println("Product Name Exist! ");
						continue;
					}
				}
				break;
			}
		}

		double price = Exceptions.DoubleException("Enter new price: ");
		int categoryID;
		try (PreparedStatement ps = con.prepareStatement(query)) {
			categoryID = Exceptions.IntegerException("Enter new category id: ");
			ps.setInt(1, categoryID);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					System.out.println("ID does not exist");
					return;
				}
			}
		}

		try (PreparedStatement uptAll = con.prepareStatement(updateAll)) {
			uptAll.setString(1, name);
			uptAll.setInt(2, categoryID);
			uptAll.setDouble(3, price);
			uptAll.setInt(4, id);
			uptAll.executeUpdate();
		}
		
		System.out.println("Succesfully update all fields");

	}

	public void deleteProduct() {
		String queryExist = "SELECT * FROM product WHERE product_id = ?";
		String queryView = "SELECT * FROM product ORDER BY product_id ASC;";
		String deleteProduct = "DELETE FROM product WHERE product_id = ?";
		
		try(Connection con = DBConnection.getConnection(); PreparedStatement dlt = con.prepareStatement(deleteProduct)){
			
			try (PreparedStatement ps = con.prepareStatement(queryView)) {
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						System.out.println("Product_id: " + rs.getInt("product_id") + " | " + "Product_name: "
								+ rs.getString("product_name") + " | " + "category_id: " + rs.getInt("category_id")
								+ " | " + "Price: " + rs.getDouble("price"));
						System.out.println();
					}
				}
			}
			int id;
			try (PreparedStatement exist = con.prepareStatement(queryExist)) {
				id = Exceptions.IntegerException("Enter product ID: ");
				exist.setInt(1, id);
				try (ResultSet result = exist.executeQuery()) {
					if (!result.next()) {
						System.out.println("ID does not exist! ");
						return;
					}
				}
			}
			
			dlt.setInt(1, id);
			dlt.executeUpdate();
			System.out.println("Succesfully deleted product!");
			
		}catch(SQLException e) {
			System.out.println("Cannot delete product: " + e.getMessage());
		}
	}

	public void managerCategory() {
		
		try(Connection con = DBConnection.getConnection()){
			while(true) {
				System.out.println("--- Manage Category ---");
				System.out.println("1. View Categories");
				System.out.println("2. Add Categories");
				System.out.println("3. Update Categories");
				System.out.println("4. Delete Cetegories");
				System.out.println("5. Return");
				int choice = Exceptions.IntegerException("choice: ");
				
				switch(choice) {
					case 1 -> viewCategory(con); 
					case 2 -> addCategory(con);
					case 3 -> updateCategory(con);
					case 4 -> deleteCategory(con);
					case 5 -> {
						System.out.println("Returning...");
						return;
					}
					default -> System.out.println("Invalid Input! ");
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void viewCategory(Connection con) throws SQLException {

		String query = "SELECT * FROM category";
		
		try(PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()){
			
			while(rs.next()) {
				System.out.println("Category ID: " + rs.getInt("category_id") + " | " + "Category Name: " + rs.getString("category_name"));
				System.out.println();
			}
		}
	}

	private void addCategory(Connection con)throws SQLException{
		String query = "INSERT INTO category (category_name) VALUES (?)";
		
		String nameExist = "SELECT category_name FROM category WHERE category_name = ?";
		
		String name;
		while(true) {
			try(PreparedStatement ps = con.prepareStatement(nameExist)){
				name = Exceptions.StringException("Enter Category Name: ");
				ps.setString(1, name);
				try(ResultSet rs = ps.executeQuery()){
					if(rs.next()) {
						System.out.println("Category name already exist! ");
						continue;
					}
				}
			}
			break;
		}
		
		try(PreparedStatement add = con.prepareStatement(query)){
			add.setString(1, name);
			add.executeUpdate();
		}
		System.out.println("Succesfully Added Category");
		
	}

	private void updateCategory(Connection con) throws SQLException {
		String query = "UPDATE category SET category_name = ? WHERE category_id = ?";
		
		String idExist = "SELECT category_id FROM category WHERE category_id = ?";
		
		int id;
		try(PreparedStatement exist = con.prepareStatement(idExist)){
			id = Exceptions.IntegerException("Enter Category ID: ");
			exist.setInt(1, id);
			try(ResultSet rs = exist.executeQuery()){
				if(!rs.next()) {
					System.out.println("ID does not exist! ");
					return;
				}
			}
		}
		String name;
		try(PreparedStatement upt = con.prepareStatement(query)){
			name = Exceptions.StringException("Enter new name: ");
			upt.setString(1, name);
			upt.setInt(2, id);
			upt.executeUpdate();
		}
		
		System.out.println("Succesfully udpated category! ");
		
	}
	
	private void deleteCategory(Connection con) throws SQLException {
		String query = "DELETE FROM category WHERE category_id = ?";
		
		String idExist = "SELECT category_id FROM category WHERE category_id = ?";
		
		int id;
		try(PreparedStatement exist = con.prepareStatement(idExist)){
			id = Exceptions.IntegerException("Enter Category ID: ");
			exist.setInt(1, id);
			try(ResultSet rs = exist.executeQuery()){
				if(!rs.next()) {
					System.out.println("ID does not exist! ");
					return;
				}
			}
		}

		try(PreparedStatement upt = con.prepareStatement(query)){
			upt.setInt(1, id);
			upt.executeUpdate();
		}
		
		System.out.println("Succesfully deleted category! ");
		
	}
	
	public void updateOrderStatus() {
		String query = "SELECT order_id, user_id FROM orders WHERE order_id = ? AND  user_id = ? ";
		
		String update = "UPDATE orders SET status = ? WHERE order_id = ? AND user_id = ?";
		
		try(Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(update)){
			int orderID;
			int userID;
			
			try(PreparedStatement check = con.prepareStatement(query)){
				orderID = Exceptions.IntegerException("Enter orderID: ");
				userID = Exceptions.IntegerException("Enter userID: ");
				check.setInt(1, orderID);
				check.setInt(2, userID);
				
				try(ResultSet rs = check.executeQuery()){
					if(!rs.next()) {
						System.out.println("Order does not exist! ");
						return;
					}
				}
			}
			
			String status = Exceptions.StringException("Enter Updated Order Status: ");
			
			ps.setString(1, status);
			ps.setInt(2, orderID);
			ps.setInt(3, userID);
			ps.executeUpdate();
			
			System.out.println("Successfully Updated Order Status! ");
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void viewAllOrders() {
		String query = "SELECT * FROM orders";
		
		try(Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(query)){
			
			try(ResultSet rs = ps.executeQuery()){
				System.out.println("orderID\tuserID\ttotal amount\tstatus\t\tcreatedAt");
				System.out.println("---------------------------------------------------------");
				while(rs.next()) {
					System.out.println(rs.getInt("order_id") + "\t" + rs.getInt("user_id") + "\t" 
							+ rs.getDouble("total_amount") + "\t\t" + rs.getString("status") + "\t\t" + rs.getTimestamp("created_at"));
					System.out.println();
				}
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void viewAllUsers() {
		String query = "SELECT user_id, username, role FROM users";
		
		try(Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(query)){
			
			try(ResultSet rs = ps.executeQuery()){
				System.out.println("ID\tUSERNAME\t\tROLE");
				System.out.println("---------------------------------------");
				while(rs.next()) {
					System.out.println(rs.getInt("user_id") + "\t" + rs.getString("username") + "\t\t" +  rs.getString("role"));
					System.out.println();
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void promoteUser() {
		String query = "SELECT user_id FROM users WHERE user_id = ?";
		
		String promote = "UPDATE users SET role = ? WHERE user_id = ?";
		try(Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(promote)){
			
			int id;
			try(PreparedStatement check = con.prepareStatement(query)){
				id = Exceptions.IntegerException("Enter user id: ");
				check.setInt(1, id);
				try(ResultSet rs = check.executeQuery()){
					if(!rs.next()) {
						System.out.println("User id does not exist");
						return;
					}
				}
			}
			
			String role = Exceptions.StringException("Enter role to promote: ");
			
			ps.setString(1, role);
			ps.setInt(2, id);
			ps.executeUpdate();
			System.out.println("Succesfully promoted user! ");
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
