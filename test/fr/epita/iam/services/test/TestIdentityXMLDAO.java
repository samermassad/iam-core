/**
 * Ce fichier est la propriété de Thomas BROUSSARD
 * Code application :
 * Composant :
 */
package fr.epita.iam.services.test;

import java.util.List;

import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DuplicateException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.services.identity.dao.IdentityDAO;
import fr.epita.iam.services.identity.dao.IdentityXMLDAO;

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
public class TestIdentityXMLDAO {

	/**
	 * <h3>Description</h3>
	 * <p>
	 * This methods allows to ...
	 * </p>
	 *
	 * <h3>Usage</h3>
	 * <p>
	 * It should be used as follows :
	 *
	 * <pre>
	 * <code> ${enclosing_type} sample;
	 *
	 * //...
	 *
	 * sample.${enclosing_method}();
	 *</code>
	 * </pre>
	 * </p>
	 *
	 * @since $${version}
	 * @see Voir aussi $${link}
	 * @author ${user}
	 *
	 *         ${tags}
	 * @throws IdentityCreationException
	 * @throws IdentitySearchException
	 * @throws ReadOnlyException 
	 * @throws IdentityDuplicateException 
	 * @throws TransformerException 
	 */
	public static void main(String[] args) throws CreationException, SearchException, ReadOnlyException, DuplicateException, TransformerException {
		// given
		final IdentityDAO dao = new IdentityXMLDAO();

		// when
		dao.create(new Identity("Thomas Broussard", "1234", "tbr@tbr.com"));

		// then
		final List<Identity> list = dao.search(new Identity("Thomas Broussard", null, null));

		if (list.isEmpty()) {
			System.out.println("failure");
		} else {
			System.out.println("success");
		}
		System.out.println(list);

	}

}
