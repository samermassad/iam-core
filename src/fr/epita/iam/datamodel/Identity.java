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
	/**
	 * @return a hash value of the identity, useful for comparing identities
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (displayName == null ? 0 : displayName.hashCode());
		result = prime * result + (email == null ? 0 : email.hashCode());
		result = prime * result + (uid == null ? 0 : uid.hashCode());
		return result;
	}
	/** 
	 * Tests wether an object equals the current instance
	 * @param obj
	 * @return boolean
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Identity other = (Identity) obj;
		if (displayName == null) {
			if (other.displayName != null) {
				return false;
			}
		} else if (!displayName.equals(other.displayName)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (uid == null) {
			if (other.uid != null) {
				return false;
			}
		} else if (!uid.equals(other.uid)) {
			return false;
		}
		return true;
	}

}
