package fr.epita.iam.datamodel;

/**
 * <h3>Description</h3>
 * <p>This class creates and manages an Identity</p>
 *
 * <h3>Usage</h3>
 * <p>
 *   <pre><code>Identity id1 = new Identity();</code></pre>
 *   <pre><code>Identity id2 = new Identity(${displayName},${uid},${email});</code></pre>
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class Identity {

	private String displayName;
	private String uid;
	private String email;

	/**
	 *
	 */
	public Identity() {

	}

	/**
	 * @param displayName
	 * @param uid
	 * @param email
	 */
	public Identity(String displayName, String uid, String email) {
		this.displayName = displayName;
		this.uid = uid;
		this.email = email;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/** 
	 * @return String representation of the identity
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Identity [displayName=" + displayName + ", uid=" + uid + ", email=" + email + "]";
	}

}
