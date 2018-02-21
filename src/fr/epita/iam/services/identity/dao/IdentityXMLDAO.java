package fr.epita.iam.services.identity.dao;

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

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.services.connections.XMLConnection;
import fr.epita.logger.Logger;

/**
 * <h3>Description</h3>
 * <p>
 * Manages the XML DAO.
 * </p>
 *
 * <h3>Usage</h3>
 * 
 * <pre>
 * <code>IdentityXMLDAOManager dao = new IdentityXMLDAOManager();</code>
 * </pre>
 * </p>
 *
 * @author Samer Masaad
 */
public class IdentityXMLDAO implements IdentityDAO {
	
	private Document document;
	private static final Logger LOGGER = new Logger(IdentityXMLDAO.class);

	private static final String TEXTEXPRESSION = "']/text()";
	private static final String PROPERTYEXPRESSION = "./property[@name='";
	private static final String IDENTITYEXPRESSION = "/identities/identity[";
	
	private static final String PROPERTY = "property";
	private static final String IDENTITY = "identity";
	
	private static final String DISPLAYNAME = "displayName";
	private static final String UID = "uid";
	private static final String EMAIL = "email";

	private static final String DISPLAYNAMEEXPRESSION = PROPERTYEXPRESSION + DISPLAYNAME + TEXTEXPRESSION;
	private static final String UIDEXPRESSION = PROPERTYEXPRESSION + UID + TEXTEXPRESSION;
	private static final String EMAILEXPRESSION = PROPERTYEXPRESSION + EMAIL + TEXTEXPRESSION;

	/**
	 * <h3>Constructor</h3>
	 * Parse the XML file and prepare the document
	 */
	public IdentityXMLDAO() {
		try {
			document = XMLConnection.getIdentityXML();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOGGER.error("Failed to load default settings. Exiting program...", e);
		}

	}

	/**
	 * Add an identity to the XML file.
	 * 
	 * @param identity - the identity to add
	 * @throws CreationException
	 * @throws TransformerException
	 */	 
	@Override
	public void create(Identity identity) throws CreationException, TransformerException {
		final Element root = document.getDocumentElement();
		final Element identityElement = getNewIdentityElt();
		setProperties(identity, identityElement);
		root.appendChild(identityElement);
		XMLConnection.saveIdentityXML(document);
	}

	private void setProperties(Identity identity, final Element identityElement) {
		identityElement.appendChild(getNewPropertyElmt(DISPLAYNAME, identity.getDisplayName()));
		identityElement.appendChild(getNewPropertyElmt(UID, identity.getUid()));
		identityElement.appendChild(getNewPropertyElmt(EMAIL, identity.getEmail()));
	}

	private Element getNewPropertyElmt(String propertyName, String propertyValue) {
		final Element identityProperty = getNewPropertyElt();
		identityProperty.setNodeValue(propertyName);
		identityProperty.setAttribute("name", propertyName);
		identityProperty.setTextContent(propertyValue);
		return identityProperty;
	}

	private Element getNewIdentityElt() {
		return document.createElement(IDENTITY);
	}

	private Element getNewPropertyElt() {
		return document.createElement(PROPERTY);
	}

	/**
	 * Update information of an identity in the XML file.
	 * 
	 * @param from - original identity
	 * @param to - edited identity
	 * @throws TransformerException
	 */
	@Override
	public void update(Identity from, Identity to) throws TransformerException {

		if (from.getUid().equals(to.getUid())) {
			String expression = IDENTITYEXPRESSION + UIDEXPRESSION + " = '" + to.getUid() + "']";
			try {
				final XPathFactory xpathFactory = XPathFactory.newInstance();
				final XPath xpath = xpathFactory.newXPath();
				final XPathExpression xPathExpression = xpath.compile(expression);
				final Node result = (Node) xPathExpression.evaluate(document, XPathConstants.NODE);

				while (result.getFirstChild() != null) {
					result.removeChild(result.getFirstChild());
				}
				if (result instanceof Element) {
					Element el = (Element) result;
					setProperties(to, el);
					XMLConnection.saveIdentityXML(document);
				}

			} catch (final XPathExpressionException e) {
				LOGGER.error("An error occured", e);
			}
		}

	}

	/**
	 * Delete an identity from the XML file, also deletes the user associated with it.
	 * 
	 * @param identity - identity to delete
	 * @throws DeleteException
	 */
	@Override
	public void delete(Identity identity) throws TransformerException {

		String expression = IDENTITYEXPRESSION + UIDEXPRESSION + " = '" + identity.getUid() + "']";
		try {
			final XPathFactory xpathFactory = XPathFactory.newInstance();
			final XPath xpath = xpathFactory.newXPath();
			final XPathExpression xPathExpression = xpath.compile(expression);
			final Node result = (Node) xPathExpression.evaluate(document, XPathConstants.NODE);
			
			if(result != null) {
				result.getParentNode().removeChild(result);
				XMLConnection.saveIdentityXML(document);
			} else {
				LOGGER.warning("Trying to delete an identity from XML file that doesn't exist: " + identity);
			}
			
		} catch (final XPathExpressionException e) {
			LOGGER.error("An error occured", e);
		}

	}

	/**
	 * Search the XML file for an identity.
	 * 
	 * @param criteria - criteria of the identity to search for
	 * @return list of matched identities
	 * @throws SearchException
	 */
	@Override
	public List<Identity> search(Identity criteria) throws SearchException {
		final List<Identity> identities = new ArrayList<>();

		// construct the expression
		String expression = IDENTITYEXPRESSION;

		if (criteria.getDisplayName() != null) {
			expression += "contains(./property[@name='displayName']/text(), '" + criteria.getDisplayName() + "')";
		} else {
			expression += DISPLAYNAMEEXPRESSION + " = *";
		}

		expression += " and ";

		expression += UIDEXPRESSION + " = ";
		if (criteria.getUid() != null) {
			expression += "'" + criteria.getUid() + "'";
		} else {
			expression += "*";
		}

		expression += " and ";

		if (criteria.getEmail() != null) {
			expression += "contains(./property[@name='email']/text(), '" + criteria.getEmail() + "')";
		} else {
			expression += EMAILEXPRESSION + " = *";
		}

		expression += "]";

		try {
			final XPathFactory xpathFactory = XPathFactory.newInstance();
			final XPath xpath = xpathFactory.newXPath();
			final XPathExpression xPathExpression = xpath.compile(expression);
			final NodeList results = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

			final int length = results.getLength();
			for (int i = 0; i < length; i++) {
				final Node item = results.item(i);
				final Identity identity = new Identity();
				identity.setDisplayName((String) xpathFactory.newXPath().compile(DISPLAYNAMEEXPRESSION).evaluate(item,
						XPathConstants.STRING));
				identity.setEmail((String) xpathFactory.newXPath().compile(EMAILEXPRESSION).evaluate(item,
						XPathConstants.STRING));
				identity.setUid(
						(String) xpathFactory.newXPath().compile(UIDEXPRESSION).evaluate(item, XPathConstants.STRING));
				identities.add(identity);
			}

		} catch (final XPathExpressionException e) {

			LOGGER.error("An error occured", e);
		}

		return identities;
	}

	@Override
	public Identity getUserByUid(Identity criteria) throws SearchException {
		return null;
	}

}
