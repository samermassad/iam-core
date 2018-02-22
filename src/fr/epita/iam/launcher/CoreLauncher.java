package fr.epita.iam.launcher;

import java.sql.SQLException;

/**
 * <h3>Description</h3>
 * <p>
 * Launcher of iam-core application. This application manages the users and
 * identities and all their necessary information. Data is stored in both
 * database and XML. However, if one of these 2 methods isn't working, the
 * program launches in read only mode.
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class CoreLauncher {

	/**
	 * <h3>Description</h3>
	 * <p>
	 * This method launches the program
	 * </p>
	 *
	 * <h3>Utilisation</h3>
	 * <p>
	 * The first (and only) argument is the file path to the configuration file. If
	 * not provided, the program will search the .jar file's directory for a file
	 * called "conf.properties". If not found, the program will close.
	 * </p>
	 * <p>
	 *
	 * <pre>
	 * <code>
	 * java -Dconf=${pathToConfigFile} -jar ${jarName}
	 *</code>
	 * </pre>
	 * </p>
	 *
	 * @author Samer Masaad
	 *
	 * @throws SQLException
	 */
	public static void main() throws SQLException {

		final PrelaunchTests prelaunchTests = new PrelaunchTests();

		if(prelaunchTests.run()) {
			// 
		}

	}

}
