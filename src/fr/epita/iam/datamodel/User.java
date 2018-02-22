package fr.epita.iam.datamodel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fr.epita.logger.Logger;

/**
 * <h3>Description</h3>
 * <p>
 * This class creates and manages a user
 * </p>
 *
 * <h3>Usage</h3>
 * <p>
 * 
 * <pre>
 * <code>User user1 = new User();</code>
 * </pre>
 * 
 * <pre>
 * <code>User user2 = new User(${userName},${clear_text_password},${identityID});</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class User {

	private String userName;
	private String hashedPassword;
	private Integer identityID = 0;
	private String uid;

	private static final Logger LOGGER = new Logger(User.class);

	/**
	 *
	 */
	public User() {

	}

	/**
	 * @param userName
	 * @param cleatTextPassword
	 * @param identityID
	 */
	public User(String userName, String cleatTextPassword, int identityID) {
		this.userName = userName;
		this.hashedPassword = md5Hash(cleatTextPassword);
		this.identityID = identityID;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the hashed password
	 */
	public String getHashedPassword() {
		return hashedPassword;
	}

	/**
	 * hashes the password before storing
	 * 
	 * @param clearTextPassword
	 */
	public void setPassword(String clearTextPassword) {
		this.hashedPassword = md5Hash(clearTextPassword);
	}

	/**
	 * store the hashedpassword without performing hash procedure 
	 * @param hashedPassword
	 */
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	
	/**
	 * @param passwordToHash
	 * @return MD5 Hash of the password
	 */
	private String md5Hash(String passwordToHash) {
		if (passwordToHash == null)
			return null;
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			md.update(passwordToHash.getBytes());
			// Get the hash's bytes
			byte[] bytes = md.digest();
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Error occured while hashing the password of the user:" + this, e);
		}
		return generatedPassword;
	}

	/**
	 * @return the identityID
	 */
	public int getIdentityID() {
		return identityID;
	}

	/**
	 * @param identityID
	 *            the identity ID corresponding to this user
	 */
	public void setIdentityID(int identityID) {
		this.identityID = identityID;
	}

	/**
	 * @return String representation of the user
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userName=" + userName + ", hashedPassword=" + hashedPassword + ", identityID=" + identityID + ", uid="+uid+"]";
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

}
