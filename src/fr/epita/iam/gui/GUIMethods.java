package fr.epita.iam.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
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
import fr.epita.iam.launcher.Global;
import fr.epita.iam.services.identity.dao.IdentityDAOManager;
import fr.epita.iam.services.users.dao.UserDAOManager;
import fr.epita.logger.Logger;

/**
 * @author Samer Masaad
 */
public class GUIMethods {

	private static Logger LOGGER = new Logger(GUIMethods.class);

	protected static boolean login(String usrname, String pwd) {
		UserDAOManager dao = new UserDAOManager();
		boolean success = false;
		try {
			success = dao.login(new User(usrname, pwd, 0));

		} catch (SearchException e) {
			LOGGER.error("Failed to login.", e);
		}
		return success;
	}

	protected static void add(String displayName, String email, String uid)
			throws CreationException, ReadOnlyException, DuplicateException, SearchException, TransformerException {
		IdentityDAOManager dao = new IdentityDAOManager();
		Identity identity = new Identity();
		identity.setDisplayName(displayName);
		identity.setEmail(email);
		identity.setUid(uid);
		try {
			dao.create(identity);
		} catch (CreationException | ReadOnlyException | SearchException | TransformerException e1) {
			LOGGER.error("Error occured while creating identity" + identity, e1);
			throw e1;
		}

	}

	protected static void identityEdit(Identity from, String displayName, String email, String uid)
			throws ReadOnlyException, UpdateException, TransformerException {
		IdentityDAOManager dao = new IdentityDAOManager();
		Identity to = new Identity();
		to.setDisplayName(displayName);
		to.setEmail(email);
		to.setUid(uid);
		try {
			dao.update(from, to);
		} catch (ReadOnlyException | UpdateException | TransformerException e1) {
			LOGGER.error("Error occured while updating identity: " + from + ", to: " + to, e1);
			throw e1;
		}
	}

	protected static List<Identity> identitySearch(String displayName, String email, String uid)
			throws SearchException {
		List<Identity> results = new ArrayList<>();
		Identity identity = new Identity();
		if (!displayName.isEmpty()) {
			identity.setDisplayName(displayName);
		}
		if (!email.isEmpty()) {
			identity.setEmail(email);
		}
		if (!uid.isEmpty()) {
			identity.setUid(uid);
		}
		IdentityDAOManager dao = new IdentityDAOManager();
		try {
			results = dao.search(identity);
		} catch (SearchException e) {
			LOGGER.error("error occured while searching", e);
			throw e;
		}
		return results;
	}

	protected static void identityDelete(Identity identity)
			throws ReadOnlyException, TransformerException, DeleteException {
		IdentityDAOManager dao = new IdentityDAOManager();
		try {
			dao.delete(identity);
		} catch (ReadOnlyException | TransformerException | DeleteException e1) {
			LOGGER.error("error occured while searching", e1);
			throw e1;
		}
	}

	protected static void userAdd(String userName, String password, String uid) throws CreationException,
			ReadOnlyException, SearchException, TransformerException, DuplicateException, NoIdentityFoundException {
		UserDAOManager dao = new UserDAOManager();
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		user.setIdentityID(dao.getId(uid));
		user.setUid(uid);
		try {
			dao.create(user);
		} catch (CreationException | ReadOnlyException | SearchException | TransformerException e1) {
			LOGGER.error("Error occured while creating a user:" + user, e1);
			throw e1;
		} catch (DuplicateException e2) {
			LOGGER.error("Duplicate UID found while creating:" + user, e2);
			throw e2;
		} catch (NoIdentityFoundException e3) {
			LOGGER.error("No corresponding identity found while creating:" + user, e3);
			throw e3;
		}
	}

	protected static boolean checkPassword(int iID, String oldPwd) throws SearchException {
		UserDAOManager dao = new UserDAOManager();
		User old = new User(null, oldPwd, iID);
		try {
			return dao.checkOldPwd(old);
		} catch (SearchException e1) {
			LOGGER.error("Error occured while checking old password of user whose identity ID is " + iID, e1);
			throw e1;
		}
	}

	protected static void userUpdate(User from, String userName, String newPwd, int iID)
			throws ReadOnlyException, UpdateException, TransformerException, SearchException {
		UserDAOManager dao = new UserDAOManager();
		User to = new User();
		to.setUserName(userName);
		to.setPassword(newPwd);
		to.setIdentityID(iID);
		try {
			LOGGER.info("Updating user: " + from + " to: " + to);
			dao.update(from, to);
		} catch (ReadOnlyException | UpdateException | TransformerException | SearchException e1) {
			LOGGER.error("Error occured while updating user: " + from + " to: " + to, e1);
			throw e1;
		}
	}

	protected static List<User> userSearch(String userName, String uid) throws SearchException {
		List<User> results;
		UserDAOManager dao = new UserDAOManager();
		User user = new User();
		if (!userName.isEmpty()) {
			user.setUserName(userName);
		}
		if (!uid.isEmpty()) {
			user.setUid(uid);
			if(Global.isDBWorking()) user.setIdentityID(dao.getId(uid));
		}
		try {
			results = dao.search(user);
		} catch (SearchException e) {
			LOGGER.error("error occured while searching", e);
			throw e;
		}
		return results;
	}
}
