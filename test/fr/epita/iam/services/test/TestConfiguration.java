/**
 * Ce fichier est la propriété de Thomas BROUSSARD
 * Code application :
 * Composant :
 */
package fr.epita.iam.services.test;

import java.io.File;

import fr.epita.iam.services.configuration.ConfigurationService;

/**
 * <h3>Description</h3>
 * <p>This class allows to ...</p>
 *
 * <h3>Usage</h3>
 * <p>This class should be used as follows:
 *   <pre><code>${type_name} instance = new ${type_name}();</code></pre>
 * </p>
 *
 * @since $${version}
 * @see See also $${link}
 * @author ${user}
 *
 * ${tags}
 */
public class TestConfiguration {

	public static void main(String[] args) {
		if (System.getProperty("conf") == null) {
			// no argument
			System.out.println("re run the program with a configuration location as the first argument");
			return;
		}
		// When
		final ConfigurationService configService = ConfigurationService.getInstance();
		final String dbHost = configService.getConfigurationValue("db.host");

		// Then
		if (dbHost == null) {
			System.out.println("failure");
		} else {
			System.out.println(dbHost);
			System.out.println("success");
		}
	}

	/**
	 * <h3>Description</h3>
	 * <p>This methods allows to ...</p>
	 *
	 * <h3>Usage</h3>
	 * <p>It should be used as follows :
	 *
	 * <pre><code> ${enclosing_type} sample;
	 *
	 * //...
	 *
	 * sample.${enclosing_method}();
	 *</code></pre>
	 * </p>
	 *
	 * @since $${version}
	 * @see Voir aussi $${link}
	 * @author ${user}
	 *
	 * ${tags}
	 */
	private static void testWithHardcodedValues() {
		// Given one "properties" file
		final String filePath = "test/conf.properties";
		final File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("the configuration file does not exist");
			return;
		}

		// When
		final ConfigurationService configService = ConfigurationService.getInstance();
		final String dbHost = configService.getConfigurationValue("db.host");

		// Then
		if (dbHost == null) {
			System.out.println("failure");
		} else {
			System.out.println(dbHost);
			System.out.println("success");
		}
	}

}
