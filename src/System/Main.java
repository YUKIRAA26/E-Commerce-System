package System;

public class Main {
	private static final UserManager userManager = new UserManager();

	public static void main(String[] args) {
		Users isLogged = null;
		while (isLogged == null) {
			loginMenu();
			int choice = Exceptions.IntegerException("Enter choice: ");

			switch (choice) {
			case 1 -> isLogged = userManager.login();
			case 2 -> userManager.register();
			}

			if (isLogged != null) {
				if (isLogged.getRole().equals("customer")) {
					CustomerManager cManager = new CustomerManager();
					while (isLogged != null) {
						customerMenu();
						int choice1 = Exceptions.IntegerException("Choice: ");

						switch (choice1) {
						case 1 -> cManager.viewProduct();
						case 2 -> cManager.searchProduct();
						case 3 -> cManager.productCategory();
						case 4 -> cManager.addToCart(isLogged);
						case 5 -> cManager.viewCart(isLogged);
						case 10 -> {
							System.out.println("Logged out...");
							isLogged = null;
						}
						}
					}
				} else if (isLogged.getRole().equals("admin")) {
					adminMenu();
				}
			}

		}

	}

	private static void loginMenu() {
		System.out.println("--- Welcome To My E-Commerce ---");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("3. Exit");
	}

	private static void customerMenu() {
		System.out.println("--- Customer Menu ---");
		System.out.println("1. View Products");
		System.out.println("2. Search Product ");
		System.out.println("3. View Product Categories");
		System.out.println("4. Add Product to Cart");
		System.out.println("5. View Cart");
		System.out.println("6. Remove Product from Cart");
		System.out.println("7. Checkout");
		System.out.println("8. View My Orders");
		System.out.println("9. View My Profile");
		System.out.println("10. Logout");
	}

	private static void adminMenu() {
		System.out.println("--- Admin Menu ---");
		System.out.println("1. View Products");
		System.out.println("2. Add Product ");
		System.out.println("3. Update Product");
		System.out.println("4. Delete Product");
		System.out.println("5. Manage Categories");
		System.out.println("6. View All Orders");
		System.out.println("7. Update Order Status");
		System.out.println("8. View All Users");
		System.out.println("9. Promote User to Admin");
		System.out.println("10. View Sales Report");
		System.out.println("11. Logout");
	}

}
