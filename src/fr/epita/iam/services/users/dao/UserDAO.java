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

/**
 * <h3>Description</h3>
 * <p>
 * Interface for all user DAOs
 * </p>
 *
 * <h3>Usage</h3>
 * 
 * <pre>
 * <code>UserDAO dao = new UserDAOManager();</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 */
public interface UserDAO {
	/**
	 * Creates a user
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
			TransformerException, NoIdentityFoundException;

	/**
	 * Search for a user
	 * 
	 * @param criteria
	 *            - criteria of the user to search for
	 * @return list of matched identities
	 * @throws SearchException
	 */
	public List<User> search(User criteria) throws SearchException;

	/**
	 * 
	 * @param criteria
	 * @return identity
	 * @throws SearchException
	 */
	public User getUserByIdentityId(User criteria) throws SearchException;

	/**
	 * Update information of a user
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
	public void update(User from, User to) throws ReadOnlyException, UpdateException, TransformerException, SearchException;

	/**
	 * Delete a user
	 * 
	 * @param user
	 *            - user to delete
	 * @throws ReadOnlyException
	 * @throws DeleteException
	 * @throws TransformerException
	 */
	public void delete(User user) throws ReadOnlyException, DeleteException, TransformerException;

	/**
	 * Get ID of an identity from UID.
	 * 
	 * @param uid
	 * @return id of the identity
	 */
	public int getId(String uid);

	/**
	 * 
	 * @param login
	 *            - login credentials
	 * @return true if login succeeded, false otherwise
	 * @throws SearchException
	 */
	public boolean login(User login) throws SearchException;

	/**
	 * checks if the new password is equal to the old provided clear text password
	 * 
	 * @param oldIdentity
	 * @param newIdentity
	 * @return false if old or new password is not set (is/are null) , false if old
	 *         and new password are not identical , true if old and new password are
	 *         identical
	 * @throws SearchException 
	 */
	public boolean checkOldPwd(User oldUser) throws SearchException;

	/**
	 * @param user
	 * @return
	 * @throws SearchException 
	 */
	public boolean usernameExists(User user) throws SearchException;

}
