package fr.epita.iam.exceptions;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.datamodel.User;

/**
 * <h3>Description</h3>
 * <p>
 * This exception class is thrown when a duplicate entry is found while creating
 * a user or an identity
 * </p>
 * 
 * <h5>extends</h5> fr.epita.iam.exceptions.DataException
 * 
 * <h3>Usage</h3>
 * <p>
 * 
 * <pre>
 * <code>throw new CreationException(${faultyIdentity});</code>
 * </pre>
 * 
 * <pre>
 * <code>throw new CreationException(${faultyUser});</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class DuplicateException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param faultyIdentity
	 */
	public DuplicateException(Identity faultyIdentity) {
		super(faultyIdentity);
	}

	/**
	 * @param faultyIdentity
	 */
	public DuplicateException(User faultyUser) {
		super(faultyUser);
	}

	@Override
	public String getMessage() {
		return "Duplicate UID found while creating: " + faultyIdentity;
	}
}
