package fr.epita.iam.exceptions;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.datamodel.User;

/**
 * <h3>Description</h3>
 * <p>
 * This exception class is thrown when an exception occurs during updating
 * identites and users
 * </p>
 * 
 * <h5>extends</h5> fr.epita.iam.exceptions.DataException
 * 
 * <h3>Usage</h3>
 * <p>
 * 
 * <pre>
 * <code>throw new UpdateException(${exception},${faultyIdentity});</code>
 * </pre>
 * 
 * <pre>
 * <code>throw new UpdateException(${faultyIdentity});</code>
 * </pre>
 * 
 * <pre>
 * <code>throw new UpdateException(${exception},${faultyUser});</code>
 * </pre>
 * 
 * <pre>
 * <code>throw new UpdateException(${faultyUser});</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class UpdateException extends DataException {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param e
	 * @param faultyIdentity
	 */
	public UpdateException(Exception e, Identity faultyIdentity) {
		super(e, faultyIdentity);
	}

	/**
	 * @param faultyIdentity
	 */
	public UpdateException(Identity faultyIdentity) {
		super(faultyIdentity);
	}

	/**
	 * @param e
	 * @param faultyUser
	 */
	public UpdateException(Exception e, User faultyUser) {
		super(e, faultyUser);
	}

	/**
	 * @param faultyUser
	 */
	public UpdateException(User faultyUser) {
		super(faultyUser);
	}

	@Override
	public String getMessage() {
		return "Error occured while updating: " + faultyIdentity;
	}
}
