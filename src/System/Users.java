package System;

public class Users {
	private int userID;
	private String username;
	private String role;
	private String fullName;
	private String email;
	

	Users(int id, String username,String role, String fullName, String email){
		this.fullName = fullName;
		this.email = email;
		this.setUserID(id);
		this.setUsername(username);
		this.setRole(role);		
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
