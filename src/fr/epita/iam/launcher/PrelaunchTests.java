package fr.epita.iam.launcher;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import fr.epita.iam.services.configuration.ConfigurationService;
import fr.epita.iam.services.connections.JDBCConnection;
import fr.epita.iam.services.connections.XMLConnection;
import fr.epita.logger.Logger;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * This class allows to create an instance of PrelaunchTests and perform some
 * tests that define the behaviour of the program before the launching.
 * </p>
 * 
 * <h3>Usage</h3>
 * <p>
 * 
 * <pre>
 * <code>PrelaunchTests pt = new PrelauchTests();</code>
 * </pre>
 * </p>
 * 
 * @author Samer Masaad
 */
public class PrelaunchTests {

	private static Logger LOGGER = new Logger(PrelaunchTests.class);

	/**
	 * If path is not provided from the user while launching the program, the method
	 * will search the .jar's file directory for a file called "conf.properties".
	 * 
	 * @return if configuration file found.
	 */
	public boolean configuration() {
		LOGGER.info("Checking configuration file.");
		if (System.getProperty("conf") == null) {
			// no link to conf file
			// trying the default settings

			LOGGER.info("No link to configuration file provided, trying the default settings.");
			boolean defaultConfig = ConfigurationService.initDefaultSettings();
			if (defaultConfig) {
				// default configuration worked!
				LOGGER.info("Default configuration file found.");
				return true;
			} else {
				// default configuration failed
				LOGGER.error("Failed to load default settings. Exiting program...");
				return false;
			}
		} else {
			// link to conf file provided
			// checking if file exists

			String path = System.getProperty("conf");
			File confFile = new File(path);
			if (!confFile.exists()) {
				LOGGER.error("Provided configuration file doesn't exist. Exiting program...");
				return false;
			}
			LOGGER.info("Configuration file found.");
			return true;
		}
	}

	/**
	 * Tests the JDBC connection with the database
	 * @return if JDBC connection is working correctly
	 * @throws SQLException
	 */
	public boolean jdbc() throws SQLException {
		LOGGER.info("Checking database connection.");
		boolean jdbc = false;
		Connection connection = null;
		try {
			connection = JDBCConnection.getConnection();
			LOGGER.info("Database connection working.");
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("Can't connect to the database", e);
		} finally {
			if (connection != null && !connection.isClosed())
				jdbc = true;
			JDBCConnection.close(connection, null, null);
		}
		return jdbc;
	}

	/**
	 * Test the parsing of the XML file
	 * @return if XML file was parsed successfully.
	 */
	public boolean xml() {
		// checking XML parsing

		LOGGER.info("Checking XML file.");
		try {
			XMLConnection.getIdentityXML();
		} catch (Exception e1) {
			// XML file not found or not working
			// exit program
			LOGGER.error("Can't use XML.", e1);
			return false;
		}
		LOGGER.info("XML file working.");
		return true;
	}

	/**
	 * Runs the prelaunch tests
	 * 
	 * @return boolean - if the program can launch or not
	 * @throws SQLException
	 */
	public boolean run() {

		if (!this.configuration()) {
			// failed to load configuration
			// exiting..
			return false;
		}

		try {
			if (!this.jdbc()) {
				// database connection failed
				Global.setDBWorking(false);
				// trying to use read-only mode from XML
				if (!this.xml()) {
					// XML parsing failed
					// exiting..
					return false;
				} else {
					// using read-only mode
					Global.setXMLWorking(true);
					Global.setReadOnly(true);
				}
			} else {
				// database connection working
				Global.setDBWorking(true);
				// checking XML
				if (!this.xml()) {
					// XML parsing failed
					Global.setXMLWorking(true);
					// using read-only mode
					Global.setReadOnly(true);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
