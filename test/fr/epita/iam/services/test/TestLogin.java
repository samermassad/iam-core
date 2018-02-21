package fr.epita.iam.services.test;

import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.User;
import fr.epita.iam.exceptions.*;
import fr.epita.iam.services.users.dao.UserDAO;
import fr.epita.iam.services.users.dao.UserDAOManager;

public class TestLogin {

	public static void main(String[] args) throws CreationException, ReadOnlyException, DuplicateException, SearchException, NoIdentityFoundException, TransformerException, DeleteException {

		UserDAO dao = new UserDAOManager();
		
		//dao.create(new User("samermassad","password",2));
		
		System.out.println(dao.login(new User("root","root",0)));
		
		//dao.delete(new User(null, null, 2));
		
	}

}
