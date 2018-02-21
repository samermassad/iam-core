package fr.epita.iam.services.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import fr.epita.logger.Logger;

/**
 * <h3>Description</h3>
 * <p>
 * This singleton class allows to create only one instance of
 * ConfigurationService, and call it to get the properties from the properties
 * file.
 * </p>
 *
 * <h3>Usage</h3>
 * <p>
 * 
 * <pre>
 * <code>ConfigurationService configService = new ConfigurationService();</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 *
 */
public class ConfigurationService {

	private Properties properties;
	private static ConfigurationService instance;

	private static final Logger LOGGER = new Logger(ConfigurationService.class);

	private ConfigurationService(String filePathToConfiguration) {
		try {
			properties = new Properties();
			properties.load(new FileInputStream(new File(filePathToConfiguration)));
		} catch (final IOException e) {
			LOGGER.error("couldn't open the configuration file", e);
		}
	}

	/**
	 * Creates the instance if hasn't been initialized, then returns it
	 * 
	 * @return the instance
	 */
	public static ConfigurationService getInstance() {
		if (instance == null) {
			if (System.getProperty("conf") == null)
				initDefaultSettings();
			instance = new ConfigurationService(System.getProperty("conf"));
		}
		return instance;
	}

	/**
	 * 
	 * @param propertyKey
	 * @return property associated with the propertyKey
	 */
	public String getConfigurationValue(String propertyKey) {
		return properties.getProperty(propertyKey);
	}

	/**
	 * Checks if the .jar's parent folder has a file called "conf.properties". If
	 * yes, the method sets to the system property "conf" the path of that conf
	 * file.
	 * 
	 * @return true if file found, false if file not found
	 */
	public static boolean initDefaultSettings() {
		// trying to load configuration file from same directory as the jar

		String defaultPath = "c:/tmp/conf.properties";
		File confFile = new File(defaultPath);
		if (confFile.exists()) {
			System.setProperty("conf", defaultPath);
			return true;
		} else
			return false;
	}

}
