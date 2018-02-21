package fr.epita.iam.exceptions;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.datamodel.User;

/**
 * <h3>Description</h3>
 * <p>
 * This exception class is thrown when trying to manipulate the data while the program runs in read only mode
 * </p>
 * 
 * <h5>extends</h5> fr.epita.iam.exceptions.DataException
 * 
 * <h3>Usage</h3>
 * <p>
 * 
 * <pre>
 * <code>throw new ReadOnlyException(${faultyIdentity});</code>
 * </pre>
 * 
 * <pre>
 * <code>throw new ReadOnlyException(${faultyUser});</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class ReadOnlyException extends DataException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param identity
	 */
	public ReadOnlyException(Identity identity) {
		super(identity);
	}
	/**
	 * @param user
	 */
	public ReadOnlyException(User user) {
		super(user);
	}

	@Override
	public String getMessage() {
		return "Running in read-only mode. Can't create/update/delete: " + faultyIdentity;
	}

}
