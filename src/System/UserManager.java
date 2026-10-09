package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;
import java.util.*;

public class UserManager {
	private static final Predicate<String> userPassLengthValidation = s -> s.length() >= 7;

	// register
	public void register() {
		String emailExist = "SELECT email FROM users WHERE email = ?";
		String select = "SELECT username FROM users WHERE username = ?";
		String insert = "INSERT INTO users (username,password,role,full_name,email) VALUES (?,?,?,?,?)";
		System.out.println("--- Register Account ---");
		Connection con = null;
		try {
			con = DBConnection.getConnection();

			String username;
			try (PreparedStatement st = con.prepareStatement(select)) {
				while (true) {
					username = Exceptions.StringException("Enter Username: ");
					if (!userPassLengthValidation.test(username)) {
						System.out.println("Character length must be 7 up");
						continue;
					}

					st.setString(1, username);
					try (ResultSet rs = st.executeQuery();) {
						if (rs.next()) {
							System.out.println("Username already exist! ");
							continue;
						}

						System.out.println("Username is valid ");
						break;

					}

				}
			}

			try (PreparedStatement pt = con.prepareStatement(insert)) {
				String password;
				while (true) {
					password = Exceptions.StringException("Enter password: ");
					if (!userPassLengthValidation.test(password)) {
						System.out.println("Character length must be 7 up");
						continue;
					}

					System.out.println("Password is valid");
					break;
				}
				String fullName;

				fullName = Exceptions.StringException("Enter full name: ");

				String email;
				try (PreparedStatement eml = con.prepareStatement(emailExist)) {
					while (true) {
						email = Exceptions.StringException("Enter email: ");

						if (!emailValidation(email)) {
							System.out.println("Invalid email! ");
							continue;
						}
						eml.setString(1, email);
						try (ResultSet check = eml.executeQuery()) {
							if (check.next()) {
								System.out.println("Email already exist! ");
								continue;
							}
						}

						break;
					}
				}

				String role = "customer";

				pt.setString(1, username);
				pt.setString(2, password);
				pt.setString(3, role);
				pt.setString(4, fullName);
				pt.setString(5, email);
				pt.executeUpdate();
				System.out.println("Succesfully registered account! ");
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

	// login
	public Users login() {
		String retrieve = "SELECT * FROM users WHERE username = ? AND password = ?";
		System.out.println("--- login ---");
		Connection con = null;
		try {

			con = DBConnection.getConnection();
			try (PreparedStatement pt = con.prepareStatement(retrieve)) {
				String username = Exceptions.StringException("Username: ");
				pt.setString(1, username);
				String password = Exceptions.StringException("password: ");
				pt.setString(2, password);
				try (ResultSet rs = pt.executeQuery()) {
					if (rs.next()) {
						int id = rs.getInt("user_id");
						String user = rs.getString("username");
						String role = rs.getString("role");
						String fullName = rs.getString("full_name");
						String email = rs.getString("email");

						System.out.println("Succesfully log in!");
						return new Users(id, user, role,fullName,email);
					}

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(con != null) {
				try {
					con.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("account not found! ");
		return null;
	}

	public boolean emailValidation(String email) {
		return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	}
}
