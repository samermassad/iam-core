package fr.epita.iam.services.identity.dao;

import java.util.List;

import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.DuplicateException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.exceptions.UpdateException;

/**
 * <h3>Description</h3>
 * <p>
 * Interface for all identity DAOs
 * </p>
 *
 * <h3>Usage</h3>
 * 
 * <pre>
 * <code>IdentityDAO dao = new IdentityDAOManager();</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 */
public interface IdentityDAO {

	/**
	 * Creates an identity
	 * 
	 * @param identity
	 *            - indentity to add
	 * @throws CreationException
	 * @throws ReadOnlyException
	 * @throws DuplicateException
	 * @throws SearchException
	 * @throws TransformerException
	 */
	public void create(Identity identity)
			throws CreationException, ReadOnlyException, DuplicateException, SearchException, TransformerException;

	/**
	 * Search for an identity
	 * 
	 * @param criteria
	 *            - criteria of the identity to search for
	 * @return list of matched identities
	 * @throws SearchException
	 */
	public List<Identity> search(Identity criteria) throws SearchException;

	/**
	 * Update information of an identity
	 * 
	 * @param from
	 *            - orginal identity
	 * @param to
	 *            - edited identity
	 * @throws ReadOnlyException
	 * @throws UpdateException
	 * @throws TransformerException
	 */
	public void update(Identity from, Identity to) throws ReadOnlyException, UpdateException, TransformerException;

	/**
	 * Delete an identity
	 * 
	 * @param identity
	 *            - identity to delete
	 * @throws ReadOnlyException
	 * @throws DeleteException
	 * @throws TransformerException
	 */
	public void delete(Identity identity) throws ReadOnlyException, DeleteException, TransformerException;

	/**
	 * 
	 * @param criteria
	 *            - criteria of the identity to search for
	 * @return identity
	 * @throws SearchException
	 */
	public Identity getUserByUid(Identity criteria) throws SearchException;

	

}
