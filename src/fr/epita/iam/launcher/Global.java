package fr.epita.iam.launcher;

/**
 * <h3>Description</h3>
 * <p>
 * All the global information are stored and accessed from this class. However,
 * Changing the values of this class is restricted to this package only.
 * </p>
 * 
 * @author Samer Masaad
 */
public class Global {

	private static boolean readonly = false;
	private static boolean db = true;
	private static boolean xml = true;

	// prevent creating an instance for this class
	private Global() {
	}

	/**
	 * 
	 * @return if the program is running in readonly mode
	 */
	public static boolean isReadOnly() {
		return readonly;
	}

	/**
	 * 
	 * @param readOnly
	 */
	protected static void setReadOnly(boolean readOnly) {
		readonly = readOnly;
	}

	/**
	 * 
	 * @return if database connectivity is working properly
	 */
	public static boolean isDBWorking() {
		return db;
	}

	/**
	 * 
	 * @param database
	 */
	protected static void setDBWorking(boolean database) {
		db = database;
	}

	/**
	 * 
	 * @return if XML file is found and parsed successfuly
	 */
	public static boolean isXMLWorking() {
		return xml;
	}

	/**
	 * 
	 * @param xml
	 */
	protected static void setXMLWorking(boolean xmlWorking) {
		xml = xmlWorking;
	}

}
