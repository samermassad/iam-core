package fr.epita.iam.exceptions;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.datamodel.User;

/**
 * <h3>Description</h3>
 * <p>This exception class is thrown when an exception occurs during deletion of identites and users</p>
 * 
 * <h5>extends</h5> fr.epita.iam.exceptions.DataException
 * 
 * <h3>Usage</h3>
 * <p>
 *   <pre><code>throw new DeleteException(${exception},${faultyIdentity});</code></pre>
 *   <pre><code>throw new DeleteException(${exception},${faultyUser});</code></pre>
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class DeleteException extends DataException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param cause
	 * @param faultyIdentity
	 */
	public DeleteException(Exception cause, Identity faultyIdentity) {
		super(cause, faultyIdentity);
	}
	/**
	 * @param cause
	 * @param faultyUser
	 */
	public DeleteException(Exception cause, User faultyUser) {
		super(cause, faultyUser);
	}

	@Override
	public String getMessage() {
		return "A problem occurred while deleting: " + faultyIdentity;
	}

}
