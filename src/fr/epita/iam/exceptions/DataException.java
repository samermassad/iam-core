package fr.epita.iam.exceptions;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.datamodel.User;

/**
 * <h3>Description</h3>
 * <p>
 * Super class for all other custom exceptions in fr.epita.iam.exception
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class DataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Identity faultyIdentity = null;
	protected User faultyUser = null;

	/**
	 * 
	 * @param cause
	 * @param faultyIdentity
	 */
	public DataException(Exception cause, Identity faultyIdentity) {
		initCause(cause);
		this.faultyIdentity = faultyIdentity;
	}

	/**
	 * 
	 * @param faultyIdentity
	 */
	public DataException(Identity faultyIdentity) {
		this.faultyIdentity = faultyIdentity;
	}

	/**
	 * 
	 * @param cause
	 * @param faultyUser
	 */
	public DataException(Exception cause, User faultyUser) {
		initCause(cause);
		this.faultyUser = faultyUser;
	}

	/**
	 * 
	 * @param faultyUser
	 */
	public DataException(User faultyUser) {
		this.faultyUser = faultyUser;
	}

}
