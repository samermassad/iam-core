package fr.epita.iam.services.connections;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.epita.iam.services.configuration.ConfigurationService;

/**
 * <h3>Description</h3>
 * <p>
 * This class manages the XML parsing and saving.
 * </p>
 * 
 * @author Samer Masaad
 */
public class XMLConnection {
	
	//path to local user's XML file
	private static final String USERSXML = "resources/users_db.xml";

	//prevent creating an instance of this class
	private XMLConnection() {}
	/**
	 * 
	 * @return parsed document of the identities XML file.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	
	public static Document getIdentityXML() throws ParserConfigurationException, SAXException, IOException {
		// open and validate the XML file
		
		final ConfigurationService configuration = ConfigurationService.getInstance();
		String path = configuration.getConfigurationValue("xml.file.path");
		
		return getDocument(path);
		
	}
/**
 * 
 * @return parsed document of the local users XML file.
 * @throws ParserConfigurationException
 * @throws SAXException
 * @throws IOException
 */
	public static Document getUsersXML() throws ParserConfigurationException, SAXException, IOException {
		return getDocument(USERSXML);
	}
	
	/**
	 * @param path
	 * @return parsed document of the XML file defined in path parameter
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static Document getDocument(String path)
			throws ParserConfigurationException, SAXException, IOException {
		// get the file
		final File file = new File(path);
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
		// parse the XML file
		return documentBuilder.parse(new FileInputStream(file));
	}
	
	public static void saveIdentityXML(Document doc) throws TransformerException {
		// get file path
		final ConfigurationService configuration = ConfigurationService.getInstance();
		String path = configuration.getConfigurationValue("xml.file.path");
		// save the file
		save(doc, path);
	}
	
	public static void saveUserXML(Document doc) throws TransformerException {
		save(doc, USERSXML);
	}

	/**
	 * saves a document to an XML file
	 * @param doc
	 * @param path
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	private static void save(Document doc, String path)
			throws TransformerFactoryConfigurationError, TransformerException {
		final File file = new File(path);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult streamResult = new StreamResult(file);
		transformer.transform(source, streamResult);
	}
	
}
