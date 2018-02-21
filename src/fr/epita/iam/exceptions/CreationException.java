package fr.epita.iam.exceptions;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.datamodel.User;

/**
 * <h3>Description</h3>
 * <p>
 * This exception class is thrown when an exception occurs during creation of
 * identites and users
 * </p>
 * 
 * <h5>extends</h5> fr.epita.iam.exceptions.DataException
 * 
 * <h3>Usage</h3>
 * <p>
 * 
 * <pre>
 * <code>throw new CreationException(${exception},${faultyIdentity});</code>
 * </pre>
 * 
 * <pre>
 * <code>throw new CreationException(${exception},${faultyUser});</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class CreationException extends DataException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param e
	 * @param faultyIdentity
	 */
	public CreationException(Exception e, Identity faultyIdentity) {
		super(e, faultyIdentity);
	}

	/**
	 * @param e
	 * @param faultyUser
	 */
	public CreationException(Exception e, User faultyUser) {
		super(e, faultyUser);
	}

	@Override
	public String getMessage() {
		return "A problem occurred while creating that Identity : " + faultyIdentity;
	}

}
