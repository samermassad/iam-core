package fr.epita.iam.services.test;

import java.util.List;

import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.datamodel.User;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.DuplicateException;
import fr.epita.iam.exceptions.NoIdentityFoundException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.exceptions.UpdateException;
import fr.epita.iam.services.identity.dao.IdentityDAO;
import fr.epita.iam.services.identity.dao.IdentityDAOManager;
import fr.epita.iam.services.users.dao.UserDAO;
import fr.epita.iam.services.users.dao.UserDAOManager;

public class TestUserDAOManager {

	public static void main(String[] args) throws CreationException, SearchException, ReadOnlyException,
			DuplicateException, UpdateException, TransformerException, DeleteException, NoIdentityFoundException {

		//TestCreateAndSearch();

		testUpdate();

		// testDelete();
	}

	private static void testDelete() throws ReadOnlyException, DeleteException, CreationException, DuplicateException,
			SearchException, TransformerException {
		// given
		final UserDAO dao = new UserDAOManager();

		// when
		//dao.create(new User("samer", "21", "samer@samer.com"));

		// then
		dao.delete(new User(null, null, 2));
	}

	/**
	 * @throws ReadOnlyException
	 * @throws IdentityUpdateException
	 * @throws IdentitySearchException
	 * @throws TransformerException
	 */
	private static void testUpdate() throws ReadOnlyException, UpdateException, SearchException, TransformerException {
		// given
		final UserDAO dao = new UserDAOManager();

		// when
		User from = new User(null, null, 2);
		dao.update(from, new User("Samer Massad", null, 2));

		// then
		final List<User> list = dao.search(new User(null, null, 2));

		if (list.isEmpty()) {
			System.out.println("failure");
		} else {
			System.out.println("success");
		}
		System.out.println(list);

	}

	/**
	 * @throws IdentityCreationException
	 * @throws ReadOnlyException
	 * @throws IdentityDuplicateException
	 * @throws IdentitySearchException
	 * @throws TransformerException
	 * @throws NoIdentityFoundException 
	 */
	private static void TestCreateAndSearch()
			throws CreationException, ReadOnlyException, DuplicateException, SearchException, TransformerException, NoIdentityFoundException {
		// given
		final UserDAO dao = new UserDAOManager();

		// when
		dao.create(new User("root", "root", 2));

		// then
		final List<User> list = dao.search(new User("r", null, 2));

		if (list.isEmpty()) {
			System.out.println("failure");
		} else {
			System.out.println("success");
		}
		System.out.println(list);

	}

}
