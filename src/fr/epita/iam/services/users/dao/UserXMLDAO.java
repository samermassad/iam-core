package fr.epita.iam.services.users.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.epita.iam.datamodel.User;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.DuplicateException;
import fr.epita.iam.exceptions.NoIdentityFoundException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.exceptions.UpdateException;
import fr.epita.iam.services.connections.XMLConnection;
import fr.epita.logger.Logger;

/**
 * <h3>Description</h3>
 * <p>
 * Manages the XML User DAO.
 * </p>
 *
 * <h3>Usage</h3>
 * 
 * <pre>
 * <code>UserXMLDAOManager dao = new UserXMLDAOManager();</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 */
public class UserXMLDAO implements UserDAO {

	private static final Logger LOGGER = new Logger(UserXMLDAO.class);
	private Document document;
	private String oldHashedPassword = null;

	private static final String USEREXPRESSION = "/users/user[";
	private static final String PROPERTYNAMEEXPRESSION = "./property[@name='";
	private static final String TEXTEXPRESSION = "']/text()";

	private static final String PROPERTY = "property";
	private static final String USER = "user";
	
	private static final String USERNAME = "username";
	private static final String IDENTITYID = "uid";
	private static final String HASHEDPASSWORD = "hashedPassword";

	private static final String USERNAMEEXPRESSION = PROPERTYNAMEEXPRESSION + USERNAME + TEXTEXPRESSION;
	private static final String IDENTITYIDEXPRESSION = PROPERTYNAMEEXPRESSION + IDENTITYID + TEXTEXPRESSION;
	private static final String HASHEDPASSWORDEXPRESSION = PROPERTYNAMEEXPRESSION + HASHEDPASSWORD + TEXTEXPRESSION;

	/**
	 * <h3>Constuctor</h3>
	 * Parse the XML file and prepare the document
	 */
	public UserXMLDAO() {
		try {
			// get the XML document
			document = XMLConnection.getUsersXML();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOGGER.error("Failed to load local users' XML.", e);
		}

	}

	/**
	 * Adds the user to the XML file if not in read only mode and IdentityID is not duplicated and
	 * exists in the Identities table. Otherwise throws {@link ReadOnlyException},
	 * {@link DuplicateException} or {@link NoIdentityFoundException}
	 * 
	 * @param user
	 *            - user to add
	 * @throws CreationException
	 * @throws ReadOnlyException
	 * @throws DuplicateException
	 * @throws SearchException
	 * @throws TransformerException
	 */
	@Override
	public void create(User user) throws CreationException, TransformerException {
		// get root element
		final Element root = document.getDocumentElement();
		// create a new identity element
		final Element userElement = getNewUserElt();
		// set the properties
		setProperties(user, userElement);
		// append the identity element to the root
		root.appendChild(userElement);
		// save the document
		XMLConnection.saveUserXML(document);
	}

	private void setProperties(User user, final Element userElement) {
		userElement.appendChild(getNewPropertyElmt(USERNAME, user.getUserName()));
		userElement.appendChild(getNewPropertyElmt(IDENTITYID, user.getUid().toLowerCase()));
		String hashedPassword = null;
		if(user.getHashedPassword() == null) {
			// password hasn't been change during user editing
			hashedPassword = this.oldHashedPassword;
		} else {
			// password has been changed
			hashedPassword = user.getHashedPassword();
		}
		userElement.appendChild(getNewPropertyElmt(HASHEDPASSWORD, hashedPassword));
	}

	private Element getNewPropertyElmt(String propertyName, String propertyValue) {
		// create a new property element
		final Element userProperty = getNewPropertyElt();
		// set node values
		userProperty.setNodeValue(propertyName);
		userProperty.setAttribute("name", propertyName);
		userProperty.setTextContent(propertyValue);
		return userProperty;
	}

	private Element getNewUserElt() {
		return document.createElement(USER);
	}

	private Element getNewPropertyElt() {
		return document.createElement(PROPERTY);
	}

	/**
	 * Updates the information of a user in the XML file if not in read only mode.
	 * Otherwise throws {@link ReadOnlyException}
	 * 
	 * @param from
	 *            - original user
	 * @param to
	 *            - edited user
	 * @throws ReadOnlyException
	 * @throws UpdateException
	 * @throws TransformerException
	 */
	@Override
	public void update(User from, User to) throws TransformerException {
		if (from.getIdentityID() == to.getIdentityID()) {
			// safe to go
			// construct the expression
			String expression = USEREXPRESSION + IDENTITYIDEXPRESSION + " = '" + from.getUid().toLowerCase() + "']";
			try {
				final XPathFactory xpathFactory = XPathFactory.newInstance();
				final XPath xpath = xpathFactory.newXPath();
				// compile the expression
				final XPathExpression xPathExpression = xpath.compile(expression);
				// evaluate
				final Node result = (Node) xPathExpression.evaluate(document, XPathConstants.NODE);

				while (result.getFirstChild() != null) {
					// delete the old element
					result.removeChild(result.getFirstChild());
				}
				if (result instanceof Element) {
					// create a new element
					Element el = (Element) result;
					this.oldHashedPassword = from.getHashedPassword();
					to.setUid(from.getUid());
					setProperties(to, el);
					XMLConnection.saveUserXML(document);
				}

			} catch (XPathExpressionException | NullPointerException e) {
				LOGGER.error("An error occured", e);
			}
		}

	}

	/**
	 * Delete a user from the XML file if not in read only mode. Otherwise throws
	 * {@link ReadOnlyException}
	 * 
	 * @param identity
	 * @throws ReadOnlyException
	 * @throws DeleteException
	 * @throws TransformerException
	 */
	@Override
	public void delete(User user) throws TransformerException {
		// construct the expression
		String expression = USEREXPRESSION + IDENTITYIDEXPRESSION + " = '" + user.getUid() + "']";
		try {
			final XPathFactory xpathFactory = XPathFactory.newInstance();
			final XPath xpath = xpathFactory.newXPath();
			// compile
			final XPathExpression xPathExpression = xpath.compile(expression);
			// evaluate
			final Node result = (Node) xPathExpression.evaluate(document, XPathConstants.NODE);
			
			if(result != null) {
				// if node found, delete it
				result.getParentNode().removeChild(result);
				XMLConnection.saveUserXML(document);
			} else {
				LOGGER.warning("Trying to delete an user from XML file that doesn't exist: " + user);
			}
			
		} catch (final XPathExpressionException e) {
			LOGGER.error("An error occured", e);
		}

	}

	/**
	 * Search for a user.
	 * 
	 * @param criteria
	 *            - criteria of user to search for
	 * @return list of matched identities
	 * @throws SearchException
	 */
	@Override
	public List<User> search(User criteria) throws SearchException {
		final List<User> users = new ArrayList<>();

		// construct the expression
		String expression = USEREXPRESSION;

		if (criteria.getUserName() != null) {
			expression += "contains(" + USERNAMEEXPRESSION + ", '" + criteria.getUserName() + "')";
		} else {
			expression += USERNAMEEXPRESSION + " = *";
		}

		expression += " and ";

		expression += IDENTITYIDEXPRESSION + " = ";
		if (criteria.getUid() != null) {
			expression += "'" + criteria.getUid() + "'";
		} else {
			expression += "*";
		}

		expression += "]";

		try {
			final XPathFactory xpathFactory = XPathFactory.newInstance();
			final XPath xpath = xpathFactory.newXPath();
			// compile
			final XPathExpression xPathExpression = xpath.compile(expression);
			// evaluate
			final NodeList results = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

			final int length = results.getLength();
			for (int i = 0; i < length; i++) {
				// deserialise the results
				final Node item = results.item(i);
				final User user = new User();
				user.setUserName((String) xpathFactory.newXPath().compile(USERNAMEEXPRESSION).evaluate(item,
						XPathConstants.STRING));
				user.setPassword((String) xpathFactory.newXPath().compile(HASHEDPASSWORDEXPRESSION).evaluate(item,
						XPathConstants.STRING));
				user.setUid(
						(String) xpathFactory.newXPath().compile(IDENTITYIDEXPRESSION).evaluate(item, XPathConstants.STRING));
				users.add(user);
			}

		} catch (final XPathExpressionException e) {
			LOGGER.error("An error occured", e);
		}

		return users;
	}

	public User getUserByIdentityId(User criteria) throws SearchException {
		// not needed
		return null;
	}

	public int getId(String identityID) {
		// not needed
		return 0;
	}

	/**
	 * 
	 * @param login
	 *            - login credentials
	 * @return true if login succeeded, false otherwise
	 * @throws SearchException
	 */
	@Override
	public boolean login(User login) throws SearchException {
		boolean success = false;
		// construct the expression
		String expression = USEREXPRESSION + USERNAMEEXPRESSION + " = '" + login.getUserName() + "' and ";
		expression += HASHEDPASSWORDEXPRESSION + " = '" + login.getHashedPassword() + "'";
		expression += "]";
		try {
			final XPathFactory xpathFactory = XPathFactory.newInstance();
			final XPath xpath = xpathFactory.newXPath();
			// compile
			final XPathExpression xPathExpression = xpath.compile(expression);
			// evaluate
			final NodeList results = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
			final int length = results.getLength();
			if(length > 0) {
				// at least one user has these credentials
				success = true;
			}
		} catch (final XPathExpressionException e) {
			LOGGER.error("An error occured", e);
		}
		// log the login attemp
		if(success) {
			LOGGER.info("Successful login from XML using username: " + login.getUserName());
		} else {
			LOGGER.warning("Failed login from XML using username: " + login.getUserName());
		}
		return success;
	}

	/* (non-Javadoc)
	 * @see fr.epita.iam.services.users.dao.UserDAO#checkOldPwd(fr.epita.iam.datamodel.User, fr.epita.iam.datamodel.User)
	 */
	@Override
	public boolean checkOldPwd(User oldUser) throws SearchException {
		// not needed
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.epita.iam.services.users.dao.UserDAO#usernameExists(fr.epita.iam.datamodel.User)
	 */
	@Override
	public boolean usernameExists(User user) throws SearchException {
		// not needed
		return false;
	}

}
