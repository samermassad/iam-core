package fr.epita.iam.services.test;

import fr.epita.iam.datamodel.User;

public class TestUsers {
	
	public static void main(String[] args) {
		
		User user1 = new User("samer","password",1);
		
		System.out.println(user1);
		
	}
	
}
