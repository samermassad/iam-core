package fr.epita.iam.exceptions;

import fr.epita.iam.datamodel.User;

/**
 * <h3>Description</h3>
 * <p>
 * This exception class is thrown when trying to create a user with an identity
 * ID that doesn't exists
 * </p>
 * 
 * <h5>extends</h5> fr.epita.iam.exceptions.DataException
 * 
 * <h3>Usage</h3>
 * <p>
 * 
 * <pre>
 * <code>throw new NoIdentityFoundException(${faultyUser});</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class NoIdentityFoundException extends DataException {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param faultyIdentity
	 */
	public NoIdentityFoundException(User faultyUser) {
		super(faultyUser);
	}

	@Override
	public String getMessage() {
		return "Cannot create a user who doesn't have an identity: " + faultyUser;
	}
}
