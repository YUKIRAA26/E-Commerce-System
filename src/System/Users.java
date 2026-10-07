package System;

public class Users {
	private int userID;
	private String username;
	private String role;
	
	Users(int id, String username,String role){
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
}
