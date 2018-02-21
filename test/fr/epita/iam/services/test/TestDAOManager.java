package fr.epita.iam.services.test;

import java.util.List;

import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.DuplicateException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.exceptions.UpdateException;
import fr.epita.iam.services.identity.dao.IdentityDAO;
import fr.epita.iam.services.identity.dao.IdentityDAOManager;

public class TestDAOManager {

	public static void main(String[] args) throws CreationException, SearchException, ReadOnlyException,
			DuplicateException, UpdateException, TransformerException, DeleteException {

		TestCreateAndSearch();

		// testUpdate();

		//testDelete();
	}

	private static void testDelete() throws ReadOnlyException, DeleteException, CreationException, DuplicateException,
			SearchException, TransformerException {
		// given
		final IdentityDAO dao = new IdentityDAOManager();

		// when
		// dao.create(new Identity("samer", "5", "samer@samer.com"));

		// then
		dao.delete(new Identity("Samer2", "5", "samer@samer.com"));
	}

	/**
	 * @throws ReadOnlyException
	 * @throws IdentityUpdateException
	 * @throws IdentitySearchException
	 * @throws TransformerException
	 */
	private static void testUpdate() throws ReadOnlyException, UpdateException, SearchException, TransformerException {
		// given
		final IdentityDAO dao = new IdentityDAOManager();

		// when
		Identity from = new Identity(null, "4", null);
		dao.update(from, new Identity("Samer2", "4", "samer@samer.com"));

		/*
		 * then
		 * 
		 * final List<Identity> list = dao.search(new Identity(null,"8",null));
		 * 
		 * if (list.isEmpty()) { System.out.println("failure"); } else {
		 * System.out.println("success"); } System.out.println(list);
		 */
	}

	/**
	 * @throws IdentityCreationException
	 * @throws ReadOnlyException
	 * @throws IdentityDuplicateException
	 * @throws IdentitySearchException
	 * @throws TransformerException
	 */
	private static void TestCreateAndSearch()
			throws CreationException, ReadOnlyException, DuplicateException, SearchException, TransformerException {
		// given
		final IdentityDAO dao = new IdentityDAOManager();

		// when
		//dao.create(new Identity("Georges W. Bush", "2", "tbr@tbr.com"));

		// then
		final List<Identity> list = dao.search(new Identity("r", null, ""));

		if (list.isEmpty()) {
			System.out.println("failure");
		} else {
			System.out.println("success");
		}
		System.out.println(list);

	}

}
