package System;
import java.util.*;

public class Exceptions {
	private static final Scanner scanner = new Scanner(System.in);
	//Integer  Character Input Handling
	public static int IntegerException(String prompt) {
		while(true) {
			try {
				System.out.print(prompt);
				int value = Integer.parseInt(scanner.nextLine().trim());
				if(value <= 0) {
					System.out.println("Value cannot be less than 0! or 0 ");
					continue;
				}
				return value;
			}catch(NumberFormatException e){
				System.out.println("Input should be number! ");
			}
		}
	}
	
	//Double Character Input Handling
	public static double DoubleException(String prompt) {
		while(true) {
			try {
				System.out.print(prompt);
				double value = Double.parseDouble(scanner.nextLine().trim());
				if(value < 0.0) {
					System.out.println("Value cannot be less than 0! ");
					continue;
				}
				return value;
			}catch(NumberFormatException e) {
				System.out.println("Input should be number! ");
			}
		}
	}
	
	//String Empty Input Handling
	public static String StringException(String prompt) {
		while(true) {
			System.out.print(prompt);
			String value = scanner.nextLine().trim();
			if(value.isBlank()) {
				System.out.println("Input cannot be blank! ");
				continue;
			}
			
			return value;
		}
	}
	
	public static String idChecker(Map<String,?> item,String prompt) {
		while(true) {
			String id = StringException(prompt);
			
			if(item.containsKey(id)) {
				System.out.println("ID already exist! ");
				continue;
			}
			return id;
		}
	}
	
	public static String existingId(Map<String,?> item,String prompt) {
		while(true) {
			String id = StringException(prompt);
			
			if(!item.containsKey(id)) {
				System.out.println("ID does not exist! ");
				continue;
			}
			return id;
		}
	}
	
	/*optional
	public static String nameChecker(Set<String> check, String prompt) {
		while(true) {
			String name = StringException(prompt);
			
			if(check.contains(name.toLowerCase())) {
				System.out.println("Name already exist! ");
				continue;
			}
			
			return name;
		}
	}*/
	
}
