package fr.epita.iam.services.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.epita.iam.services.configuration.ConfigurationService;
import fr.epita.logger.Logger;

/**
 * <h3>Description</h3>
 * <p>
 * This class manages the JDBC connection.
 * </p>
 * 
 * @author Samer Masaad
 */
public class JDBCConnection {

	// prevent creating an instance of this class
	private JDBCConnection() {
	}

	private static final Logger LOGGER = new Logger(JDBCConnection.class);
	private static final String DB_HOST = "db.host";
	private static final String DB_PWD = "db.pwd";
	private static final String DB_USER = "db.user";

	/**
	 * Initialises a connection with the database. Derby is used as a database, so
	 * the derbyclient.jar should be added to the java build path
	 * 
	 * @return The JDBC connection
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException {

		final ConfigurationService confService = ConfigurationService.getInstance();

		final String url = confService.getConfigurationValue(DB_HOST);
		final String password = confService.getConfigurationValue(DB_PWD);
		final String username = confService.getConfigurationValue(DB_USER);

		//get the driver
		Class.forName("org.apache.derby.jdbc.ClientDriver");

		return DriverManager.getConnection(url, username, password);
	}

	/**
	 * Closes the connection
	 * 
	 * @param connection
	 */
	public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		// close connection
		if (connection != null) {
			try {
				connection.close();
			} catch (final SQLException e) {
				LOGGER.error("Error occured while closing the connection with the database", e);
			}
		}
		// close prepared statement
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (final SQLException e) {
				LOGGER.error("Error occured while closing the prepared statement", e);
			}
		}
		// close result set
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (final SQLException e) {
				LOGGER.error("Error occured while closing the result set", e);
			}
		}
	}

}
