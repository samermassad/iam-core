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
import fr.epita.iam.launcher.Global;
import fr.epita.logger.Logger;

/**
 * <h3>Description</h3>
 * <p>
 * Manages the JDBC and XML Identity DAOs.
 * </p>
 *
 * <h3>Usage</h3>
 * 
 * <pre>
 * <code>IdentityDAOManager dao = new IdentityDAOManager();</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 */
public class IdentityDAOManager implements IdentityDAO {

	private boolean readOnly = true;

	private static Logger logger = new Logger(IdentityDAOManager.class);

	private IdentityDAO dbDAO;
	private IdentityDAO xmlDAO;

	/**
	 * <h3>Constructor</h3>
	 */
	public IdentityDAOManager() {
		readOnly = Global.isReadOnly();
		dbDAO = new IdentityJDBCDAO();
		xmlDAO = new IdentityXMLDAO();
	}

	/**
	 * Creates an identity if not in read only mode and UID is not duplicated.
	 * Otherwise throws {@link ReadOnlyException} or {@link DuplicateException}
	 * 
	 * @param identity
	 *            - identity to add
	 * @throws CreationException
	 * @throws ReadOnlyException
	 * @throws DuplicateException
	 * @throws SearchException
	 * @throws TransformerException
	 */
	public void create(Identity identity)
			throws CreationException, ReadOnlyException, DuplicateException, SearchException, TransformerException {
		if (readOnly) {
			logger.error("Running in read-only mode. Can't create this identity : " + identity);
			throw new ReadOnlyException(identity);
		} else {
			if (getUserByUid(identity) == null) {
				// no duplicate uid found
				dbDAO.create(identity);
				xmlDAO.create(identity);
			} else {
				// duplicate uid found
				logger.error("Duplicate UID found while creating this identity: " + identity);
				throw new DuplicateException(identity);
			}
		}
	}

	/**
	 * Search for an identity. Search database if it's working, otherwise search XML
	 * file.
	 * 
	 * @param criteria
	 *            - criteria of identity to search for
	 * @return list of matched identities
	 * @throws SearchException
	 */
	public List<Identity> search(Identity criteria) throws SearchException {
		if (Global.isDBWorking()) {
			// use database if working
			return dbDAO.search(criteria);
		} else {
			// use XML
			return xmlDAO.search(criteria);
		}
	}

	/**
	 * Updates the information of an identity if not in read only mode. Otherwise
	 * throws {@link ReadOnlyException}
	 * 
	 * @param from
	 *            - original identity
	 * @param to
	 *            - edited identity
	 * @throws ReadOnlyException
	 * @throws UpdateException
	 * @throws TransformerException
	 */
	public void update(Identity from, Identity to) throws ReadOnlyException, UpdateException, TransformerException {
		if (readOnly) {
			logger.error("Running in read-only mode. Can't update this identity : " + to);
			throw new ReadOnlyException(to);
		} else {
			dbDAO.update(from, to);
			xmlDAO.update(from, to);
		}
	}

	/**
	 * Delete an identity if not in read only mode. Otherwise throws
	 * {@link ReadOnlyException}
	 * 
	 * @param identity
	 * @throws ReadOnlyException
	 * @throws DeleteException
	 * @throws TransformerException
	 */
	public void delete(Identity identity) throws ReadOnlyException, DeleteException, TransformerException {
		if (readOnly) {
			logger.error("Running in read-only mode. Can't delete this identity : " + identity);
			throw new ReadOnlyException(identity);
		} else {
			dbDAO.delete(identity);
			xmlDAO.delete(identity);
		}
	}

	@Override
	public Identity getUserByUid(Identity criteria) throws SearchException {
		return dbDAO.getUserByUid(criteria);
	}

}
