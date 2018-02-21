package fr.epita.iam.exceptions;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.datamodel.User;

/**
 * <h3>Description</h3>
 * <p>
 * This exception class is thrown when an exception occurs during searching of
 * identites and users
 * </p>
 * 
 * <h5>extends</h5> fr.epita.iam.exceptions.DataException
 * 
 * <h3>Usage</h3>
 * <p>
 * 
 * <pre>
 * <code>throw new SearchException(${exception},${faultyIdentity});</code>
 * </pre>
 * 
 * <pre>
 * <code>throw new SearchException(${exception},${faultyUser});</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class SearchException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param cause
	 * @param faultyIdentity
	 */
	public SearchException(Exception cause, Identity faultyIdentity) {
		super(cause, faultyIdentity);
	}

	/**
	 * @param cause
	 * @param user
	 */
	public SearchException(Exception cause, User faultyUser) {
		super(cause, faultyUser);
	}

	@Override
	public String getMessage() {
		return "a problem occured while searching: " + faultyIdentity;
	}
}
