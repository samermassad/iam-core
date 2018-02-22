package fr.epita.iam.services.users.dao;

import java.util.List;

import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.User;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.DuplicateException;
import fr.epita.iam.exceptions.NoIdentityFoundException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.exceptions.UpdateException;
import fr.epita.iam.launcher.Global;
import fr.epita.logger.Logger;

/**
 * <h3>Description</h3>
 * <p>
 * Manages the JDBC and XML User DAOs.
 * </p>
 *
 * <h3>Usage</h3>
 * 
 * <pre>
 * <code>UserDAOManager dao = new UserDAOManager();</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 */
public class UserDAOManager implements UserDAO {

	private boolean readOnly = true;

	private static Logger logger = new Logger(UserDAOManager.class);

	private UserDAO dbDAO;
	private UserDAO xmlDAO;

	/**
	 * <h3>Constructor</h3>
	 */
	public UserDAOManager() {
		readOnly = Global.isReadOnly();
		dbDAO = new UserJDBCDAO();
		xmlDAO = new UserXMLDAO();
	}

	/**
	 * Creates a user if not in read only mode and IdentityID is not duplicated and
	 * exists in the Identities table. Otherwise throws {@link ReadOnlyException},
	 * {@link DuplicateException} or {@link NoIdentityFoundException}
	 * 
	 * @param user
	 *            - user to add
	 * @throws CreationException
	 * @throws ReadOnlyException
	 * @throws DuplicateException
	 * @throws SearchException
	 * @throws TransformerException
	 */
	public void create(User user) throws CreationException, ReadOnlyException, DuplicateException, SearchException,
			TransformerException, NoIdentityFoundException {
		if (readOnly) {
			logger.error("Running in read-only mode. Can't create this user : " + user);
			throw new ReadOnlyException(user);
		} else {
			if (getUserByIdentityId(user) == null && !usernameExists(user)) {
				if (user.getIdentityID() != 0) {
					// identity id found
					dbDAO.create(user);
					xmlDAO.create(user);
				} else {
					// identity id not found
					logger.error("Cannot create a user who doesn't have an identity: " + user);
					throw new NoIdentityFoundException(user);
				}
			} else {
				logger.error("Duplicate Username or UID found while creating this user: " + user);
				throw new DuplicateException(user);
			}
		}
	}

	/**
	 * @param user
	 * @return
	 * @throws SearchException 
	 */
	public boolean usernameExists(User user) throws SearchException {
		return dbDAO.usernameExists(user);
	}

	/**
	 * Search for a user. Search database if it's working, otherwise search XML
	 * file.
	 * 
	 * @param criteria
	 *            - criteria of user to search for
	 * @return list of matched identities
	 * @throws SearchException
	 */
	public List<User> search(User criteria) throws SearchException {
		if (Global.isDBWorking()) {
			// use database if working
			return dbDAO.search(criteria);
		} else {
			// use XML file
			return xmlDAO.search(criteria);
		}
	}

	/**
	 * Updates the information of a user if not in read only mode. Otherwise throws
	 * {@link ReadOnlyException}
	 * 
	 * @param from
	 *            - original user
	 * @param to
	 *            - edited user
	 * @throws ReadOnlyException
	 * @throws UpdateException
	 * @throws TransformerException
	 * @throws SearchException 
	 */
	public void update(User from, User to) throws ReadOnlyException, UpdateException, TransformerException, SearchException {
		if (readOnly) {
			logger.error("Running in read-only mode. Can't update this user : " + to);
			throw new ReadOnlyException(to);
		} else {
			if(to.getUserName() != null && !usernameExists(to)) {
				// username is not null and new username doesn't exist
				dbDAO.update(from, to);
				xmlDAO.update(from, to);
			} else {
				throw new UpdateException(to);
			}
		}
	}

	/**
	 * Delete a user if not in read only mode. Otherwise throws
	 * {@link ReadOnlyException}
	 * 
	 * @param identity
	 * @throws ReadOnlyException
	 * @throws DeleteException
	 * @throws TransformerException
	 */
	public void delete(User user) throws ReadOnlyException, DeleteException, TransformerException {
		if (readOnly) {
			logger.error("Running in read-only mode. Can't delete this user : " + user);
			throw new ReadOnlyException(user);
		} else {
			dbDAO.delete(user);
			xmlDAO.delete(user);
		}
	}

	/**
	 * @param criteria
	 *            - criteria of the user
	 * @return user
	 */
	@Override
	public User getUserByIdentityId(User criteria) throws SearchException {
		return dbDAO.getUserByIdentityId(criteria);
	}

	/**
	 * Get ID of an identity from UID.
	 * 
	 * @param uid
	 * @return id of the identity
	 */
	@Override
	public int getId(String identityID) {
		return dbDAO.getId(identityID);
	}

	/**
	 * 
	 * @param login
	 *            - login credentials
	 * @return true if login succeeded, false otherwise
	 * @throws SearchException
	 */
	@Override
	public boolean login(User login) throws SearchException {
		if (login.getUserName() != null && login.getHashedPassword() != null) {
			//provided credentials are both not null
			if (Global.isDBWorking()) {
				// use database if working
				return dbDAO.login(login);
			} else {
				// use XML file
				return xmlDAO.login(login);
			}
		}
		return false;
	}

	/**
	 * checks if the new password is equal to the old provided clear text
	 * password
	 * 
	 * @param oldIdentity
	 * @param newIdentity
	 * @return false if old or new password is not set (is/are null) , false if old
	 *         and new password are not identical , true if old and new password are
	 *         identical
	 * @throws SearchException 
	 */
	@Override
	public boolean checkOldPwd(User oldUser) throws SearchException {
		String oldProvidedHashedPassword = oldUser.getHashedPassword();
		if (oldProvidedHashedPassword == null) {
			return false;
		} else {
			// old password is not null
			oldUser = getUserByIdentityId(oldUser);
			return oldUser.getHashedPassword().equals(oldProvidedHashedPassword);
		}
	}

}
