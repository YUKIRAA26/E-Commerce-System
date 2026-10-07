package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;
import java.util.*;

public class UserManager {
	private static final Predicate<String> userPassLengthValidation = s -> s.length() >= 7;
	
	//register
	public void register() {
		String select = "SELECT username FROM users WHERE username = ?";
		String insert = "INSERT INTO users (username,password,role) VALUES (?,?,?)";
		System.out.println("--- Register Account ---");
		
		try {
			Connection con = DBConnection.getConnection();
			
			//select
			PreparedStatement st = con.prepareStatement(select);
			//insert
			PreparedStatement pt = con.prepareStatement(insert);
			
			String username;
			while(true) {
				username = Exceptions.StringException("Enter Username: ");
				if(!userPassLengthValidation.test(username)) {
					System.out.println("Character length must be 7 up");
					continue;
				}
				
				st.setString(1, username);
				ResultSet rs = st.executeQuery();
				if(rs.next()) {
					System.out.println("Username already exist! ");
					continue;
				}
				
				pt.setString(1, username);
				
				System.out.println("Username is valid ");
				break;
				
			}
			String password;
			while(true) {
				password = Exceptions.StringException("Enter password: ");
				if(!userPassLengthValidation.test(password)) {
					System.out.println("Character length must be 7 up");
					continue;
				}
				pt.setString(2, password);
				System.out.println("Password is valid");
				break;
			}
			
			String role = "customer";
			pt.setString(3, role);
			pt.executeUpdate();
			System.out.println("Succesfully registered account! ");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//login
	public Users login() {
		String retrieve = "SELECT user_id, username, role FROM users WHERE username = ? AND password = ?";
		System.out.println("--- login ---");
		
		try {
			
			Connection con = DBConnection.getConnection();
			PreparedStatement pt = con.prepareStatement(retrieve);
			
			String username = Exceptions.StringException("Username: ");
			pt.setString(1, username);
			String password = Exceptions.StringException("password: ");
			pt.setString(2, password);
			ResultSet rs = pt.executeQuery();
			if(rs.next()) {
				int id = rs.getInt("user_id");
				String user = rs.getString("username");
				String role = rs.getString("role");
				
				
				System.out.println("Succesfully log in!");
				return new Users(id,user,role);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println("account not found! ");
		return null;
	}
}
